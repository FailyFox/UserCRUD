package com.usercrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PropertiesNotFoundException extends RuntimeException {
    public PropertiesNotFoundException() {
        super("There is no such file!");
    }
}