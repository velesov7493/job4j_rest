package ru.job4j.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.auth.domains.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query(value = "FROM Person p JOIN FETCH p.roles WHERE p.login = ?1")
    Optional<Person> findByLogin(String login);
}
