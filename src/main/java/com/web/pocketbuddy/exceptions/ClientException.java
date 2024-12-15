package com.web.pocketbuddy.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public ClientException(String message, HttpStatus httpStatus) {
        super(message);

        this.message = message;
        this.httpStatus = httpStatus;
    }
}
