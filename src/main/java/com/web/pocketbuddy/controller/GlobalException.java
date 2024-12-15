package com.web.pocketbuddy.controller;

import com.web.pocketbuddy.exceptions.ClientException;
import com.web.pocketbuddy.exceptions.JwtTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<String> handelClientException(ClientException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<String> handelJwtException(JwtTokenException exception) {
        return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus());
    }

}
