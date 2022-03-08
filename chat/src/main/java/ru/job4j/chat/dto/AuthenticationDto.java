package ru.job4j.chat.dto;

public class AuthenticationDto {

    private String username;
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
