package ru.job4j.chat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.chat.domains.Account;

import java.util.List;

@Service
public class AccountsApi {

    private static final String ACCOUNTS_URI = "http://localhost:8080/person/";
    private static final String ACCOUNT_URI = "http://localhost:8080/person/{id}";
    private static final Logger LOG = LoggerFactory.getLogger(AccountsApi.class);

    private final RestTemplate rest;

    AccountsApi(RestTemplate temp) {
        rest = temp;
        Account test = findById(1);
        if (test == null) {
            throw new IllegalStateException(
                "Системная ошибка: сервис аккаунтов не найден "
                + "или не отвечает должным образом - нет аккаунта с id=1"
            );
        }
    }

    Account create(Account value) {
        Account result = null;
        try {
            result = rest.postForObject(ACCOUNTS_URI, value, Account.class);
        } catch (Throwable ex) {
            LOG.error("Ошибка создания аккаунта: ", ex);
        }
        return result;
    }

    boolean update(Account value) {
        boolean result = false;
        try {
            rest.put(ACCOUNTS_URI, value);
            result = true;
        } catch (Throwable ex) {
            LOG.error("Ошибка изменения аккаунта: ", ex);
        }
        return result;
    }

    List<Account> findAll() {
        List<Account> result = null;
        try {
            result = rest.exchange(
                    ACCOUNTS_URI, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<Account>>() { }
            ).getBody();
        } catch (Throwable ex) {
            LOG.error("Ошибка получения списка аккаунтов: ", ex);
        }
        return result;
    }

    Account findById(int accountId) {
        Account result = null;
        try {
            result = rest.getForObject(ACCOUNT_URI, Account.class, accountId);
        } catch (Throwable ex) {
            LOG.error("Ошибка получения аккаунта: ", ex);
        }
        return result;
    }

    boolean deleteById(int accountId) {
        boolean result = false;
        try {
            rest.delete(ACCOUNT_URI, accountId);
            result = true;
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления аккаунта: ", ex);
        }
        return result;
    }
}