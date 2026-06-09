package com.affordmed.scheduler.client;

import com.affordmed.scheduler.config.AffordMedProperties;
import com.affordmed.scheduler.dto.AuthRequest;
import com.affordmed.scheduler.dto.AuthResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient restClient;
    private final AffordMedProperties properties;


    public AuthClient(RestClient restClient, AffordMedProperties properties) {
        this.restClient = restClient;
        this.properties = properties;
    }

    public AuthResponse authenticate() {

        AuthRequest request = new AuthRequest(properties.getClientId(), properties.getClientSecret());
        
        try {
            return restClient.post()
                    .uri(properties.getBaseUrl() + properties.getAuthEndpoint())
                    .body(request)
                    .retrieve()
                    .body(AuthResponse.class);
        } catch (Exception e) {

            throw new RuntimeException("Authentication failed", e);
        }
    }
}
