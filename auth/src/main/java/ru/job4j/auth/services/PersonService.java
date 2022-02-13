package ru.job4j.auth.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.auth.domains.Person;
import ru.job4j.auth.repositories.PersonRepository;

import java.util.List;

@Service
public class PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository persons;

    public PersonService(PersonRepository repo) {
        persons = repo;
    }

    public List<Person> findAll() {
        return persons.findAll();
    }

    public Person save(Person entity) {
        Person result = null;
        try {
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