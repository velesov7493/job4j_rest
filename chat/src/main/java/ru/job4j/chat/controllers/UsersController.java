package ru.job4j.chat.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domains.Account;
import ru.job4j.chat.dto.AuthenticationDto;
import ru.job4j.chat.services.AccountsApi;

import java.util.List;

@RestController
public class UsersController {

    private final AccountsApi accounts;

    public UsersController(AccountsApi service) {
        accounts = service;
    }

    @PostMapping("/users")
    public ResponseEntity<Account> createUser(@RequestBody Account user) {
        Account result = accounts.create(user);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Account>> getAllUsers(
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Account> list = accounts.findAll(token);
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> signIn(@RequestBody AuthenticationDto auth) {
        String token = accounts.signIn(auth);
        ResponseEntity<Void> result = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (token != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(AccountsApi.AUTH_HEADER_NAME, token);
            result = new ResponseEntity<>(headers, HttpStatus.OK);
        }
        return result;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Account> getUserById(
            @PathVariable("id") int userId,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        Account usr = accounts.getAccountByToken(token);
        if (usr == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Account acc = accounts.findById(token, userId);
        return
                acc == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<Void> updateUser(
            @RequestBody Account user,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        boolean result = accounts.update(token, user);
        return
                result
                ? new ResponseEntity<>(HttpStatus.ACCEPTED)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") int userId,
            @RequestHeader(name = "Authorization", required = false) String token
    ) {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return
                accounts.deleteById(token, userId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}