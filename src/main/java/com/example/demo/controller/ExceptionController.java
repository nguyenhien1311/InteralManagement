package com.example.demo.controller;


import com.example.demo.exception.CustomException;
import com.example.demo.response.ResponseObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseObject emailNotValidatedHandle(MethodArgumentNotValidException ex, WebRequest request) {
        System.out.println(ex.getBindingResult().getAllErrors().get(0));
        return new ResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(objectError -> objectError.getDefaultMessage())
                        .collect(Collectors.joining(", ")),
                null);
    }

    @ExceptionHandler(value = {DuplicateKeyException.class, MongoWriteException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseObject emailDuplicateHandle(MongoWriteException ex, WebRequest request) {
        return new ResponseObject(
                HttpStatus.BAD_REQUEST.value(),
                "Email has already taken!",
                null);
    }

    @ExceptionHandler(value = {CustomException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> idNullHandle(CustomException ex, WebRequest request) {
        return ResponseEntity.status(ex.getCode())
                .body(new ResponseObject(
                        ex.getCode(),
                        ex.getMessage(),
                        null));
    }
    @ExceptionHandler(value = {NullPointerException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> nullPointerHandle(NullPointerException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseObject(
                        HttpStatus.NO_CONTENT.value(),
                        ex.getMessage(),
                        null));
    }
}
