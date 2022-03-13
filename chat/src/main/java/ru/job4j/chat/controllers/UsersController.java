package ru.job4j.chat.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domains.Operations;
import ru.job4j.chat.exceptions.AccessDeniedException;
import ru.job4j.chat.exceptions.JwtAuthorizationException;
import ru.job4j.chat.exceptions.ObjectNotFoundException;
import ru.job4j.chat.exceptions.OperationNotAcceptableException;
import ru.job4j.chat.domains.Account;
import ru.job4j.chat.dto.AuthenticationDto;
import ru.job4j.chat.dto.ExceptionResponseDto;
import ru.job4j.chat.services.AccountsApi;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class UsersController extends JwtAuthorizationController {

    public UsersController(AccountsApi service) {
        super(service);
    }

    @PostMapping("/users")
    @Validated(Operations.OnCreate.class)
    public ResponseEntity<Account> createUser(@Valid @RequestBody Account user)
        throws OperationNotAcceptableException {

        Account result = getAccounts().create(user);
        if (result == null) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Account>> getAllUsers(
            @RequestHeader(name = "Authorization", required = false) String token
    ) throws AccessDeniedException {

        authorize(token);
        List<Account> list = getAccounts().findAll(token);
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> signIn(@Valid @RequestBody AuthenticationDto auth)
        throws JwtAuthorizationException {

        String token = getAccounts().signIn(auth);
        HttpHeaders headers = new HttpHeaders();
        headers.add(AccountsApi.AUTH_HEADER_NAME, token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Account> getUserById(
            @PathVariable("id") int userId,
            @RequestHeader(name = "Authorization", required = false) String token
    ) throws AccessDeniedException, ObjectNotFoundException {

        authorize(token);
        Account acc = getAccounts().findById(token, userId);
        if (acc == null) {
            throw new ObjectNotFoundException();
        }
        return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @PutMapping("/users")
    @Validated(Operations.OnUpdate.class)
    public ResponseEntity<Void> updateUser(
            @Valid @RequestBody Account user,
            @RequestHeader(name = "Authorization", required = false) String token
    ) throws AccessDeniedException, OperationNotAcceptableException {

        authorize(token);
        if (!getAccounts().update(token, user)) {
            throw new OperationNotAcceptableException();
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") int userId,
            @RequestHeader(name = "Authorization", required = false) String token
    ) throws AccessDeniedException, ObjectNotFoundException {

        authorize(token);
        if (!getAccounts().deleteById(token, userId)) {
            throw new ObjectNotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(JwtAuthorizationException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(JwtAuthorizationException e) {
        String msg = "Ошибка авторизации: неправильный логин и/или пароль!";
        ExceptionResponseDto resp = new ExceptionResponseDto(msg, new Date());
        return new ResponseEntity<>(resp, HttpStatus.UNAUTHORIZED);
    }
}