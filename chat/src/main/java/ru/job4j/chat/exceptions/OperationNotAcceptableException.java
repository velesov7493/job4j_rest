package ru.job4j.chat.exceptions;

public class OperationNotAcceptableException extends Exception {

    public OperationNotAcceptableException() { }

    public OperationNotAcceptableException(String message) {
        super(message);
    }

    public OperationNotAcceptableException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationNotAcceptableException(Throwable cause) {
        super(cause);
    }
}
