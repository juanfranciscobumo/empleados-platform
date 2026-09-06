package com.globant.empleados.payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class NameAlreadyExistsAdvice {

    @ExceptionHandler(NameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    String employeeNotFoundHandler(NameAlreadyExistsException ex) {
        return ex.getMessage();
    }
}
