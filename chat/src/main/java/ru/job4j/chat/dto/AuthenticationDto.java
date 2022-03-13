package ru.job4j.chat.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AuthenticationDto {

    @NotNull @NotBlank
    private String username;
    @NotNull @NotBlank
    private String password;

    public static AuthenticationDto of(String username, String password) {
        AuthenticationDto dto = new AuthenticationDto();
        dto.username = username;
        dto.password = password;
        return dto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
