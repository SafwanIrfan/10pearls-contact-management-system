package com._pearls.contactms.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class) // Email,etc are not valid
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex){

        log.warn("Email address already exist {}", ex.getMessage()); //for debugging

        Map<String, String> errors = new HashMap<>();
        errors.put("email", ex.getMessage());
        return ResponseEntity.badRequest().body(errors); // for end-users
    }

    @ExceptionHandler(PhoneNoAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handlePhoneNoAlreadyExistsException(EmailAlreadyExistsException ex) {

        log.warn("Phone no already exist {}", ex.getMessage()); //for developers
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Phone no already exists");
        return ResponseEntity.badRequest().body(errors); // for users
    }

    @ExceptionHandler(ContactNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleContactNotFoundException(
            ContactNotFoundException ex){

        log.warn("Contact not found {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Contact not found");
        return ResponseEntity.badRequest().body(errors);
    }




}
