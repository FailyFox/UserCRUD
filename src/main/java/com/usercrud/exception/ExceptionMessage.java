package com.usercrud.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionMessage {
    private String message;

    public ExceptionMessage(String message) {
        this.message = message;
    }
}