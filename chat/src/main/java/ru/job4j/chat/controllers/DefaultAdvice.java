package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.job4j.chat.exceptions.AccessDeniedException;
import ru.job4j.chat.exceptions.OperationNotAcceptableException;
import ru.job4j.chat.dto.ExceptionResponseDto;

import java.util.Date;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(AccessDeniedException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(OperationNotAcceptableException.class)
    public ResponseEntity<ExceptionResponseDto> handleException(
            OperationNotAcceptableException e
    ) {
        return
                new ResponseEntity<>(new ExceptionResponseDto(
                    "Операция не применима к таким исходным данным!",
                    new Date()
                ), HttpStatus.NOT_ACCEPTABLE);
    }
}
