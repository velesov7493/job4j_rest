package ru.job4j.employees.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.employees.domains.Account;
import ru.job4j.employees.domains.Employee;
import ru.job4j.employees.domains.KeyEmployeeAccount;
import ru.job4j.employees.repositories.EmployeeAccountKeysRepository;
import ru.job4j.employees.repositories.EmployeeRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeesService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeesService.class);

    private final AccountsApi accounts;
    private final EmployeeRepository employees;
    private final EmployeeAccountKeysRepository keys;

    public EmployeesService(
            AccountsApi s1, EmployeeRepository r1,
            EmployeeAccountKeysRepository r2
    ) {
        accounts = s1;
        employees = r1;
        keys = r2;
    }

    private Set<KeyEmployeeAccount> getAccountsKeys(Employee value) {
        Set<KeyEmployeeAccount> result = new HashSet<>();
        for (Account acc : value.getAccounts()) {
            result.add(KeyEmployeeAccount.of(value.getId(), acc.getId()));
        }
        return result;
    }

    private void fillEmployeeAccounts(Employee value) {
        List<KeyEmployeeAccount> eKeys = keys.findAllByEmployeeId(value.getId());
        eKeys.forEach((k) -> value.addAccount(accounts.findById(k.getAccountId())));
    }

    private void saveAllEmployeeAccounts(Employee value) {
        try {
            Set<KeyEmployeeAccount> old =
                    new HashSet<>(keys.findAllByEmployeeId(value.getId()));
            Set<KeyEmployeeAccount> added = getAccountsKeys(value);
            added.removeAll(old);
            old.removeAll(getAccountsKeys(value));
            if (!added.isEmpty()) {
                keys.saveAll(added);
            }
            if (!old.isEmpty()) {
                keys.deleteAll(old);
            }
        } catch (Exception ex) {
            LOG.error("Ошибка сохранения аккаунтов сотрудника : ", ex);
        }
    }

    public List<Employee> findAllEmployees() {
        List<Employee> result = employees.findAll();
        result.forEach(this::fillEmployeeAccounts);
        return result;
    }

    public Employee findEmployeeById(int employeeId) {
        Optional<Employee> result = employees.findById(employeeId);
        result.ifPresent(this::fillEmployeeAccounts);
        return result.orElse(null);
    }

    public boolean saveEmployee(Employee value) {
        boolean result = false;
        try {
            result = employees.save(value) != null;
            if (result) {
                saveAllEmployeeAccounts(value);
            }
        } catch (Exception ex) {
            LOG.error("Ошибка записи сотрудника: ", ex);
        }
        return result;
    }

    public boolean deleteEmployeeById(int employeeId) {
        boolean result = false;
        try {
            employees.deleteById(employeeId);
            result = true;
        } catch (Exception ex) {
            LOG.error("Ошибка удаления сотрудника: ", ex);
        }
        return result;
    }

    public Account createAccount(int employeeId, Account value) {
        Optional<Employee> e = employees.findById(employeeId);
        if (e.isEmpty()) {
            return null;
        }
        Account result = accounts.create(value);
        KeyEmployeeAccount key = KeyEmployeeAccount.of(employeeId, result.getId());
        keys.save(key);
        return result;
    }

    public boolean updateAccount(Account value) {
        return accounts.update(value);
    }

    public List<Account> findAllAccounts() {
        return accounts.findAll();
    }

    public Account findAccountById(int accountId) {
        return accounts.findById(accountId);
    }

    public boolean deleteAccountById(int accountId) {
        keys.deleteAllByAccountId(accountId);
        return accounts.deleteById(accountId);
    }
}