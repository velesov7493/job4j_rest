package ru.job4j.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.job4j.auth.dto.AuthenticationDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager auth;
    private final JwtTokenProvider provider;

    public JwtAuthenticationFilter(
            AuthenticationManager auth,
            JwtTokenProvider provider
    ) {
        this.auth = auth;
        this.provider = provider;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response
    ) throws AuthenticationException {

        Authentication result;
        try {
            AuthenticationDto creds =
                    new ObjectMapper()
                    .readValue(request.getInputStream(), AuthenticationDto.class);
            result = auth.authenticate(
                        new UsernamePasswordAuthenticationToken(
                            creds.getUsername(),
                            creds.getPassword(),
                            new ArrayList<>()
                        )
            );
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult
    ) throws IOException, ServletException {

        String token = provider.createToken(((User) authResult.getPrincipal()).getUsername());
        response.addHeader(provider.getAuthHeaderName(), token);
    }
}
