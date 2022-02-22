package ru.job4j.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.domains.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> { }
