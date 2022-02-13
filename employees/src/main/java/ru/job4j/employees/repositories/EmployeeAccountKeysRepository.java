package ru.job4j.employees.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.employees.domains.KeyEmployeeAccount;

import java.util.List;

@Repository
public interface EmployeeAccountKeysRepository extends JpaRepository<KeyEmployeeAccount, Integer> {

    List<KeyEmployeeAccount> findAllByEmployeeId(int employeeId);

    List<KeyEmployeeAccount> findAllByAccountId(int accountId);

    void deleteAllByAccountId(int accountId);
}
