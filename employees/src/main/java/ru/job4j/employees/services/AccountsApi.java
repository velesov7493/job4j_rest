package ru.job4j.employees.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.employees.domains.Account;
import ru.job4j.employees.dto.AuthenticationDto;

import java.util.List;

@Service
public class AccountsApi {

    public static final String AUTH_HEADER_NAME = "Authorization";

    private static final String ACCOUNTS_URI = "http://localhost:8080/person/";
    private static final String ACCOUNT_URI = "http://localhost:8080/person/{id}";
    private static final String LOGIN_URI = "http://localhost:8080/login";
    private static final String VALIDATE_URI = "http://localhost:8080/person/by-token";
    private static final Logger LOG = LoggerFactory.getLogger(AccountsApi.class);

    private final RestTemplate rest;
    private final String serviceToken;

    AccountsApi(RestTemplate temp) {
        rest = temp;
        AuthenticationDto auth = AuthenticationDto.of("employees-service", "service");
        serviceToken = signIn(auth);
    }

    public Account create(Account value) {
        ResponseEntity<Account> resp = null;
        try {
            HttpEntity<Account> request = new HttpEntity<>(value);
            resp = rest.exchange(ACCOUNTS_URI, HttpMethod.POST, request, Account.class);
        } catch (Throwable ex) {
            LOG.error("Ошибка создания аккаунта: ", ex);
        }
        return
                resp == null ? null
                : resp.getBody();
    }

    public boolean update(String token, Account value) {
        boolean result = false;
        ResponseEntity<Account> resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTH_HEADER_NAME, token);
            HttpEntity<Account> request = new HttpEntity<>(value, headers);
            resp = rest.exchange(ACCOUNTS_URI, HttpMethod.PUT, request, Account.class);
            result = resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Throwable ex) {
            LOG.error("Ошибка изменения аккаунта: ", ex);
        }
        return result;
    }

    public List<Account> findAll(String token) {
        ResponseEntity<List<Account>> resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTH_HEADER_NAME, token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            resp = rest.exchange(
                    ACCOUNTS_URI, HttpMethod.GET, request,
                    new ParameterizedTypeReference<>() { }
            );
        } catch (Throwable ex) {
            LOG.error("Ошибка получения списка аккаунтов: ", ex);
        }
        return
                resp == null ? null
                : resp.getBody();
    }

    public Account findById(String token, int accountId) {
        ResponseEntity<Account> resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTH_HEADER_NAME, token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            resp = rest.exchange(ACCOUNT_URI, HttpMethod.GET, request, Account.class, accountId);
        } catch (Throwable ex) {
            LOG.error("Ошибка получения аккаунта: ", ex);
        }
        return
                resp == null ? null
                : resp.getBody();
    }

    Account findById(int accountId) {
        return findById(serviceToken, accountId);
    }

    public boolean deleteById(String token, int accountId) {
        boolean result = false;
        ResponseEntity<Void> resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTH_HEADER_NAME, token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            resp = rest.exchange(ACCOUNT_URI, HttpMethod.DELETE, request, Void.class, accountId);
            result = resp != null && resp.getStatusCode().is2xxSuccessful();
        } catch (Throwable ex) {
            LOG.error("Ошибка удаления аккаунта: ", ex);
        }
        return result;
    }

    public Account getAccountByToken(String token) {
        ResponseEntity<Account> resp = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTH_HEADER_NAME, token);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            resp = rest.exchange(VALIDATE_URI, HttpMethod.GET, request, Account.class);
        } catch (Throwable ex) {
            LOG.error("Ошибка при проверке токена: ", ex);
        }
        return
                resp == null ? null
                : resp.getBody();
    }

    public String signIn(AuthenticationDto auth) {
        ResponseEntity<Void> resp = null;
        try {
            HttpEntity<AuthenticationDto> request = new HttpEntity<>(auth);
            resp = rest.exchange(LOGIN_URI, HttpMethod.POST, request, Void.class);
        } catch (Throwable ex) {
            throw new IllegalStateException(ex);
        }
        List<String> headers =
                resp == null ? null
                : resp.getHeaders().get(AUTH_HEADER_NAME);
        return
                headers == null ? null
                : headers.get(0);
    }
}