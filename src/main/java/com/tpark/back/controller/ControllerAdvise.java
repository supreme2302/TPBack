package com.tpark.back.controller;

import com.tpark.back.model.Message;
import com.tpark.back.model.exception.ConflictException;
import com.tpark.back.model.exception.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdvise extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Message> handleNotFoundException() {
        return new ResponseEntity<>(new Message("There is no such user"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Message> handleDuplicateKeyException() {
        return new ResponseEntity<>(new Message("Conflict"), HttpStatus.CONFLICT);
    }
}
