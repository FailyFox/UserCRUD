package com.usercrud.controller;

import com.usercrud.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> userUnder18(UserUnder18Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage(exception.getMessage()));
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> propertiesNotFound(PropertiesNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ExceptionMessage(exception.getMessage()));
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> validException(
            MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage(
                        fieldErrors.stream()
                                .filter(Objects::nonNull)
                                .map(msg -> msg.getDefaultMessage().concat("; "))
                                .reduce("", String::concat)));
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> noSuchUser(UserNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionMessage(exception.getMessage()));
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> wrongField(WrongFieldException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage(exception.getMessage()));
    }
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> invalidDateRange(InvalidDateRangeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionMessage(exception.getMessage()));
    }
}