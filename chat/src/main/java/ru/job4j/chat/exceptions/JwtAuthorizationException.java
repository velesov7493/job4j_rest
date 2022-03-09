package ru.job4j.chat.exceptions;

public class JwtAuthorizationException extends Exception {

    public JwtAuthorizationException() { }

    public JwtAuthorizationException(String message) {
        super(message);
    }

    public JwtAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtAuthorizationException(Throwable cause) {
        super(cause);
    }
}
