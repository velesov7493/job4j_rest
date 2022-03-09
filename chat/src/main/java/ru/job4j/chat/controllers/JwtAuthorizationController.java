package ru.job4j.chat.controllers;

import ru.job4j.chat.exceptions.AccessDeniedException;
import ru.job4j.chat.domains.Account;
import ru.job4j.chat.services.AccountsApi;

import java.util.function.Predicate;

public class JwtAuthorizationController {

    private final AccountsApi accounts;

    public JwtAuthorizationController(AccountsApi service) {
        accounts = service;
    }

    AccountsApi getAccounts() {
        return accounts;
    }

    Account authorize(String token) throws AccessDeniedException {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null) {
            throw new AccessDeniedException();
        }
        return acc;
    }

    Account authorizeIf(String token, Predicate<Account> condition)
        throws AccessDeniedException {
        Account acc = accounts.getAccountByToken(token);
        if (acc == null || !condition.test(acc)) {
            throw new AccessDeniedException();
        }
        return acc;
    }
}