package ru.job4j.auth.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.job4j.auth.dto.PersonDto;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "tz_persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 1, message = "Id должен быть больше нуля", groups = {Operations.OnUpdate.class})
    private int id;
    @Size(min = 4, max = 60, message = "Длина логина должна быть в пределах [4,60]")
    private String login;
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
    private boolean enabled;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "tr_roles_persons",
            joinColumns = @JoinColumn(name = "id_person"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> roles;

    public Person() {
        roles = new HashSet<>();
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

    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    public void addRole(Role value) {
        roles.add(value);
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> result = new HashSet<>();
        roles.forEach((r) -> result.add(new SimpleGrantedAuthority(r.getAuthority())));
        return result;
    }

    public Set<String> getAuthorityNames() {
        Set<String> result = new HashSet<>();
        roles.forEach(((r) -> result.add(r.getAuthority())));
        return result;
    }

    public Person patch(PersonDto other) {
        if (id == 0) {
            throw new IllegalStateException("Эта операция неприменима к новым объектам!");
        }
        if (other.getLogin() != null) {
            login = other.getLogin();
        }
        if (other.getPassword() != null) {
            password = other.getPassword();
        }
        if (other.isEnabled() != enabled) {
            enabled = other.isEnabled();
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id
               && Objects.equals(login, person.login)
               && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
