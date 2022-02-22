package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.domains.Role;
import ru.job4j.chat.domains.User;
import ru.job4j.chat.services.UsersService;

import java.util.List;

@RestController
public class UsersController {

    private final UsersService users;

    public UsersController(UsersService service) {
        users = service;
    }

    @PostMapping("/users")
    public ResponseEntity<User.Model> createUser(@RequestBody User user) {
        User.Model result = users.saveUser(user);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User.Model>> getAllUsers() {
        List<User.Model> list = users.findAllUsers();
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User.Model> getUserById(@PathVariable("id") int userId) {
        User.Model user = users.findUserById(userId);
        return
                user == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        User.Model result = users.saveUser(user);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int userId) {
        return
                users.deleteUserById(userId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role result = users.saveRole(role);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> list = users.findAllRoles();
        return
                list == null || list.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") int roleId) {
        Role result = users.findRoleById(roleId);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/roles")
    public ResponseEntity<Void> updateRole(@RequestBody Role role) {
        Role result = users.saveRole(role);
        return
                result == null
                ? new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE)
                : new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") int roleId) {
        return
                users.deleteRoleById(roleId)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}