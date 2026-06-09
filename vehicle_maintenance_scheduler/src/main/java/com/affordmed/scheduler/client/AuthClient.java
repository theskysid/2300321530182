package com.affordmed.scheduler.client;

import com.affordmed.scheduler.config.AffordMedProperties;
import com.affordmed.scheduler.dto.AuthRequest;
import com.affordmed.scheduler.dto.AuthResponse;
import com.affordmed.logging.service.LoggingService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient restClient;
    private final AffordMedProperties properties;
    private final LoggingService loggingService;

    public AuthClient(RestClient restClient, AffordMedProperties properties, LoggingService loggingService) {
        this.restClient = restClient;
        this.properties = properties;
        this.loggingService = loggingService;
    }

    public AuthResponse authenticate() {
        loggingService.log("backend", "info", "client", "Acquiring new authentication token from AffordMed Auth API");
        AuthRequest request = new AuthRequest(properties.getClientId(), properties.getClientSecret());
        
        try {
            return restClient.post()
                    .uri(properties.getBaseUrl() + properties.getAuthEndpoint())
                    .body(request)
                    .retrieve()
                    .body(AuthResponse.class);
        } catch (Exception e) {
            loggingService.log("backend", "error", "client", "Failed to acquire token: " + e.getMessage());
            throw new RuntimeException("Authentication failed", e);
        }
    }
}
