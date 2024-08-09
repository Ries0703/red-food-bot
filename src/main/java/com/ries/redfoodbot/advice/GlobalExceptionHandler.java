package com.ries.redfoodbot.advice;

import com.ries.redfoodbot.exceptions.WebHookException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .build();
    }
}
