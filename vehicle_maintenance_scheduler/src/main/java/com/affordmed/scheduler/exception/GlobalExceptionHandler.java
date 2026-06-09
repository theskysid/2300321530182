package com.affordmed.scheduler.exception;

import com.affordmed.scheduler.dto.ErrorResponse;
import com.affordmed.logging.service.LoggingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final LoggingService loggingService;

    public GlobalExceptionHandler(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        loggingService.log("backend", "warn", "handler", "Validation failure: " + details);
        
        ErrorResponse response = new ErrorResponse("VALIDATION_ERROR", "Invalid request parameters", details);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> handleAuthException(HttpClientErrorException.Unauthorized ex) {
        loggingService.log("backend", "error", "handler", "Authentication failure with external API");
        
        ErrorResponse response = new ErrorResponse("AUTH_ERROR", "Failed to authenticate with AffordMed API", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({HttpClientErrorException.class, HttpServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleApiException(Exception ex) {
        loggingService.log("backend", "error", "handler", "External API failure: " + ex.getMessage());
        
        ErrorResponse response = new ErrorResponse("API_ERROR", "Error communicating with external service", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        loggingService.log("backend", "error", "handler", "Unexpected runtime exception: " + ex.getMessage());
        
        ErrorResponse response = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        loggingService.log("backend", "fatal", "handler", "Fatal unexpected exception: " + ex.getMessage());
        
        ErrorResponse response = new ErrorResponse("FATAL_ERROR", "A fatal error occurred", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
