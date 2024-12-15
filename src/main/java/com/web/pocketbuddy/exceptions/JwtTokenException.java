package com.web.pocketbuddy.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtTokenException extends RuntimeException {

    private final String message;
    private final HttpStatus httpStatus;

    public JwtTokenException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
