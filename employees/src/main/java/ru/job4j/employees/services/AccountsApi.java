package ru.job4j.employees.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.employees.domains.Account;

import java.util.List;

@Service
public class AccountsApi {

    private static final String ACCOUNTS_URI = "http://localhost:8080/person/";
    private static final String ACCOUNT_URI = "http://localhost:8080/person/{id}";

    private final RestTemplate rest;

    public AccountsApi(RestTemplate temp) {
        rest = temp;
    }

    public Account create(Account value) {
        return rest.postForObject(ACCOUNTS_URI, value, Account.class);
    }

    public void update(Account value) {
        rest.put(ACCOUNTS_URI, value);
    }

    public List<Account> findAll() {
        return
                rest.exchange(
                    ACCOUNTS_URI, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Account>>() { }
                ).getBody();
    }

    public Account findById(int accountId) {
        return rest.getForObject(ACCOUNT_URI, Account.class, accountId);
    }

    public void deleteById(int accountId) {
        rest.delete(ACCOUNT_URI, accountId);
    }
}
