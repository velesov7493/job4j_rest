package ru.job4j.employees.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.employees.domains.Account;
import ru.job4j.employees.domains.Employee;
import ru.job4j.employees.services.AccountsApi;
import ru.job4j.employees.services.EmployeesService;

import java.util.List;

@RestController
public class EmployeesController {

    private final EmployeesService service;
    private final AccountsApi accounts;

    public EmployeesController(EmployeesService service, AccountsApi accounts) {
        this.service = service;
        this.accounts = accounts;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> findAll(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Employee> list = service.findAllEmployees();
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return
                service.saveEmployee(employee)
                ? new ResponseEntity<>(employee, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> findById(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable(name = "id") int employeeId
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Employee employee = service.findEmployeeById(employeeId);
        return
                employee == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/employees")
    public ResponseEntity<Void> update(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @RequestBody Employee employee
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return
                service.saveEmployee(employee)
                ? new ResponseEntity<>(HttpStatus.ACCEPTED)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable(name = "id") int employeeId
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return
                service.deleteEmployeeById(employeeId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/employee/{id}/accounts")
    public ResponseEntity<Account> createAccount(
            @PathVariable(name = "id") int employeeId, @RequestBody Account account
    ) {
        Account acc = service.createAccount(employeeId, account);
        return
                acc != null
                ? new ResponseEntity<>(acc, HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/accounts")
    public ResponseEntity<Account> updateAccount(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @RequestBody Account account
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.updateAccount(token, account);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteAccount(
            @RequestHeader(name = AccountsApi.AUTH_HEADER_NAME, required = false) String token,
            @PathVariable(name = "id") int accountId
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        service.deleteAccountById(token, accountId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
