package ru.job4j.chat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.chat.domains.Role;
import ru.job4j.chat.domains.User;
import ru.job4j.chat.repositories.RolesRepository;
import ru.job4j.chat.repositories.UsersRepository;

import java.util.*;

@Service
public class UsersService {

    private static final Logger LOG = LoggerFactory.getLogger(UsersService.class);

    private final RolesRepository roles;
    private final UsersRepository users;
    private final AccountsApi accounts;

    public UsersService(RolesRepository repo1, UsersRepository repo2, AccountsApi api) {
        roles = repo1;
        users = repo2;
        accounts = api;
    }

    private User.Model fillModel(User value) {
        User.Model result = new User.Model(value);
        result.setAccount(accounts.findById(value.getAccountId()));
        result.setRole(findRoleById(value.getRoleId()));
        return result;
    }

    public List<Role> findAllRoles() {
        return roles.findAll();
    }

    public Role saveRole(Role entity) {
        Role result = null;
        try {
            result = roles.save(entity);
        } catch (Throwable ex) {
            LOG.error("Ошибка сохранения роли: ", ex);
        }
        return result;
    }

    public Role findRoleById(Integer roleId) {
        return roles.findById(roleId).orElse(null);
    }

    public boolean deleteRoleById(Integer roleId) {
        boolean result = false;
        try {
            roles.deleteById(roleId);
            result = true;
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления роли: ", ex);
        }
        return result;
    }

    public List<User.Model> findAllUsers() {
        List<User> list = users.findAll();
        List<User.Model> result = new ArrayList<>();
        list.forEach((u) -> result.add(fillModel(u)));
        return result;
    }

    public User.Model saveUser(User value) {
        User.Model result = null;
        try {
            result = fillModel(users.save(value));
        } catch (Throwable ex) {
           LOG.error("Ошибка сохранения пользователя: ", ex);
        }
        return result;
    }

    public User.Model findUserById(Integer userId) {
        Optional<User> u = users.findById(userId);
        return u.isPresent() ? fillModel(u.get()) : null;
    }

    public boolean deleteUserById(Integer userId) {
        boolean result = false;
        try {
            users.deleteById(userId);
            result = true;
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления пользователя: ", ex);
        }
        return result;
    }
}