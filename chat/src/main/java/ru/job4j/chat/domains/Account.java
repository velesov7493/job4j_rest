package ru.job4j.chat.domains;

import java.util.Objects;

public class Account {

    private int id;
    private String login;
    private String password;

    public static Account of(String login, String password) {
        Account result = new Account();
        result.setLogin(login);
        result.setPassword(password);
        return result;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account person = (Account) o;
        return id == person.id
               && Objects.equals(login, person.login)
               && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}