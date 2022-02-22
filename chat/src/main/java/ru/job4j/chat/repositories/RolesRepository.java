package ru.job4j.chat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.domains.Role;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> { }
