package com.affordmed.logging.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "affordmed.logging")
public class LoggingProperties {
    private String baseUrl = "http://localhost:8080";
    private String logEndpoint = "/api/log";
    private String token = "default-token";

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLogEndpoint() {
        return logEndpoint;
    }

    public void setLogEndpoint(String logEndpoint) {
        this.logEndpoint = logEndpoint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
