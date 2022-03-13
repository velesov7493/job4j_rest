package ru.job4j.chat.domains;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Account {

    @Min(value = 1, message = "Id должен быть больше нуля", groups = {Operations.OnUpdate.class})
    private int id;
    @Size(min = 4, max = 60, message = "Длина логина должна быть в пределах [4,60]")
    private String login;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
    private boolean enabled;
    private Set<String> authorityNames;

    public Account() {
        authorityNames = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getAuthorityNames() {
        return authorityNames;
    }

    public void setAuthorityNames(Set<String> authorityNames) {
        this.authorityNames = authorityNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return
                id == account.id
                && Objects.equals(login, account.login)
                && Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}