package ru.job4j.chat.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.job4j.chat.exceptions.AccessDeniedException;
import ru.job4j.chat.exceptions.OperationNotAcceptableException;
import ru.job4j.chat.dto.ExceptionResponseDto;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

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
        String message = "Операция не применима к таким исходным данным!";
        return
                new ResponseEntity<>(
                    new ExceptionResponseDto(message, new Date()),
                    HttpStatus.NOT_ACCEPTABLE
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        return
                ResponseEntity.badRequest().body(
                    e.getFieldErrors().stream()
                    .map(f -> Map.of(
                            f.getField(),
                            String.format(
                                    "%s. Отклоенно значение: %s",
                                    f.getDefaultMessage(),
                                    f.getRejectedValue()
                            )
                        )
                    ).collect(Collectors.toList())
        );
    }
}
