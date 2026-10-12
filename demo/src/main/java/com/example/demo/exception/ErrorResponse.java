package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String error,
        String message,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp) {

    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, null, LocalDateTime.now());
    }
}