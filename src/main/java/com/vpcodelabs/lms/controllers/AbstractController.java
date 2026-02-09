package com.vpcodelabs.lms.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.vpcodelabs.lms.dtos.ErrorResponse;
import com.vpcodelabs.lms.exceptions.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractController {
    
    // Reusable Methods and Logic
    protected <T> ResponseEntity<T> sendOkResponse(T body) {
        log.info("Sending OK response with body: {}", body);
        return ResponseEntity.ok(body);
    }

    protected <T> ResponseEntity<T> sendCreatedResponse(T body) {
        log.info("Sending Created response with body: {}", body);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    protected <T> ResponseEntity<T> sendNotFoundResponse(T body) {
        log.info("Sending Not Found response with body: {}", body);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    protected <T> ResponseEntity<T> sendNoContentResponse() {
        log.info("Sending No Content response");
        return ResponseEntity.noContent().build();
    }

    // Handle Error Messages
    // Common Exceptions Handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("An Unexpected occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
            .message("An unexpected error occurred")
            .errorCode("INTERNAL SERVER ERROR")
            .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handles Custom LMS Exceptions
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
            CustomException ex) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(ex.getMessage())
                .errorCode(ex.getStatus().toString())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    // Validation Exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Validation Failed")
                .errorCode("BAD REQUEST")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .validationErrors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
