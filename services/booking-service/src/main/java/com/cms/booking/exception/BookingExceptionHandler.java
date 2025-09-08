package com.cms.booking.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class BookingExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAny(Exception ex) {
        return ResponseEntity.status(500)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(Map.of("type","about:blank","title","Internal Server Error","status",500));
    }
}
