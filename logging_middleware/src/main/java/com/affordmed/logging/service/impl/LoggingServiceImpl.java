package com.affordmed.logging.service.impl;

import com.affordmed.logging.config.LoggingProperties;
import com.affordmed.logging.dto.LogRequest;
import com.affordmed.logging.service.LoggingService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class LoggingServiceImpl implements LoggingService {

    private final LoggingProperties properties;
    private final RestClient restClient;
    private final Validator validator;
    private final ExecutorService executorService;

    public LoggingServiceImpl(LoggingProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
        

        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void log(String stack, String level, String packageName, String message) {
        LogRequest request = new LogRequest(stack, level, packageName, message);
        
        Set<ConstraintViolation<LogRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {

            System.err.println("Logging Middleware Validation Failed:");
            for (ConstraintViolation<LogRequest> violation : violations) {
                System.err.println("- " + violation.getMessage());
            }
            return;
        }

        executorService.submit(() -> {
            try {
                restClient.post()
                        .uri(properties.getLogEndpoint())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getToken())
                        .body(request)
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {

                System.err.println("Logging Middleware Failed to send log: " + e.getMessage());
            }
        });
    }
}
