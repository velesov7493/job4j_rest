package ru.job4j.auth.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domains.Person;
import ru.job4j.auth.domains.Role;
import ru.job4j.auth.dto.PersonDto;
import ru.job4j.auth.repositories.PersonRepository;
import ru.job4j.auth.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository persons;
    private final RoleRepository roles;
    private final BCryptPasswordEncoder encoder;

    public PersonService(
            PersonRepository persons,
            RoleRepository roles,
            BCryptPasswordEncoder encoder
    ) {
        this.persons = persons;
        this.roles = roles;
        this.encoder = encoder;
    }

    private void fillRolesByIds(Person value, Set<Integer> roleIds) {
        if (roleIds == null) {
            return;
        }
        value.getRoles().clear();
        for (Integer roleId : roleIds) {
            Optional<Role> r = roles.findById(roleId);
            r.ifPresent(value::addRole);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> usr = persons.findByLogin(username);
        if (usr.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        Person result = usr.get();
        return new User(result.getLogin(), result.getPassword(), result.getAuthorities());
    }

    public List<Person> findAll() {
        return persons.findAll();
    }

    public Person findByLogin(String login) {
        return persons.findByLogin(login).orElse(null);
    }

    public Person save(Person entity) {
        Person result = null;
        try {
            entity.setPassword(encoder.encode(entity.getPassword()));
            result = persons.save(entity);
        } catch (Throwable ex) {
            LOG.error("Ошибка сохранения пользователя: ", ex);
        }
        return result;
    }

    public Person patch(PersonDto data) {
        Optional<Person> old = persons.findById(data.getId());
        if (old.isEmpty()) {
            return null;
        }
        Person p = old.get().patch(data);
        fillRolesByIds(p, data.getRoleIds());
        return save(p);
    }

    public Person findById(Integer integer) {
        return persons.findById(integer).orElse(null);
    }

    public void deleteById(Integer integer) {
        try {
            persons.deleteById(integer);
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления пользователя: ", ex);
        }
    }
}