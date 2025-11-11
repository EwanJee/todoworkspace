package com.pwc.todoworksapce.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException ex) {
        Map<String, String> response = Map.of(
                "error", ex.getMessage(),
                "timestamp", Instant.now().toString()
        );
        return ResponseEntity.badRequest().body(response);
    }
}
