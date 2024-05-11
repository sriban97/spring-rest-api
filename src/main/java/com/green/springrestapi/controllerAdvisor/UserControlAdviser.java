package com.green.springrestapi.controllerAdvisor;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class UserControlAdviser {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private Map<String, String> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            error.put(e.getField(), e.getDefaultMessage());
        });
        return error;
    }

    @ExceptionHandler(value = JDBCException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ResponseEntity<String> jDBCException(JDBCException ex) {
        if (ex.getErrorCode() == 23505) {
            return ResponseEntity.badRequest().body("The user exists hence couldn't create the same new user!");
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}