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
import ru.job4j.auth.repositories.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository persons;
    private final BCryptPasswordEncoder encoder;

    public PersonService(PersonRepository persons, BCryptPasswordEncoder encoder) {
        this.persons = persons;
        this.encoder = encoder;
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