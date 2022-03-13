package ru.job4j.auth.dto;

import ru.job4j.auth.domains.Operations;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class PersonDto {

    @Min(value = 1, message = "Id должен быть больше нуля", groups = {Operations.OnUpdate.class})
    private int id;
    @Size(min = 4, max = 60, message = "Длина логина должна быть в пределах [4,60]")
    private String login;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
    private boolean enabled;
    private Set<Integer> roleIds;

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

    public Set<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }
}
