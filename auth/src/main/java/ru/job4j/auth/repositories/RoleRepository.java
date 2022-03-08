package ru.job4j.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.auth.domains.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> { }
