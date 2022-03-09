package ru.job4j.chat.exceptions;

public class AccessDeniedException extends Exception {

    public AccessDeniedException() {
        super("Доступ запрещен! Проверьте Ваш токен в заголовке Authorization");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
}
