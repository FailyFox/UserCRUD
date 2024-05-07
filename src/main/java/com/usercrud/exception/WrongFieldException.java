package com.usercrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongFieldException extends RuntimeException {
    public WrongFieldException() {
        super("Please check that the fields are correct!");
    }
}