package ru.job4j.auth.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.job4j.auth.domains.Person;
import ru.job4j.auth.services.PersonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private PersonService persons;
    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expired}")
    private long expirationTime;
    @Value("${jwt.token.prefix}")
    private String prefix;
    @Value("${jwt.token.header}")
    private String headerName;

    private String getUsername(String token) {
        return
                token == null ? null
                : JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build().verify(token)
                .getSubject();
    }

    String createToken(String username) {
        return
                prefix
                + JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    boolean validateToken(String token) {
        return
                token != null
                && !JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build().verify(token)
                .getExpiresAt().before(new Date());
    }

    Authentication getAuthentication(String token) {
        Person usr = getUser(token);
        return usr == null ? null
               : new UsernamePasswordAuthenticationToken(
                     usr.getLogin(), usr.getPassword(), usr.getAuthorities()
                 );
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(headerName);
        return
                bearerToken != null && bearerToken.startsWith(prefix)
                ? bearerToken.replace(prefix, "") : null;
    }

    public Person getUser(String token) {
        String username = getUsername(token);
        return
                username == null ? null
                : persons.findByLogin(username);
    }

    public void setPersonsService(PersonService value) {
        persons = value;
    }

    public String getAuthHeaderName() {
        return headerName;
    }
}