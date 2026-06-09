package com.affordmed.scheduler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "affordmed.api")
public class AffordMedProperties {
    private String baseUrl = "http://localhost:8080";
    private String authEndpoint = "/api/auth";
    private String depotsEndpoint = "/api/depots";
    private String vehiclesEndpoint = "/api/vehicles/tasks";
    private String clientId = "default-client";
    private String clientSecret = "default-secret";

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }

    public String getAuthEndpoint() { return authEndpoint; }
    public void setAuthEndpoint(String authEndpoint) { this.authEndpoint = authEndpoint; }

    public String getDepotsEndpoint() { return depotsEndpoint; }
    public void setDepotsEndpoint(String depotsEndpoint) { this.depotsEndpoint = depotsEndpoint; }

    public String getVehiclesEndpoint() { return vehiclesEndpoint; }
    public void setVehiclesEndpoint(String vehiclesEndpoint) { this.vehiclesEndpoint = vehiclesEndpoint; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientSecret() { return clientSecret; }
    public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
}
