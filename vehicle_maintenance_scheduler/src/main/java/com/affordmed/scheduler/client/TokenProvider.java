package com.affordmed.scheduler.client;

import com.affordmed.scheduler.dto.AuthResponse;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TokenProvider {

    private final AuthClient authClient;
    private String currentToken;
    private Instant expiryTime;

    public TokenProvider(AuthClient authClient) {
        this.authClient = authClient;
    }

    public String getToken() {
        if (!isTokenValid()) {
            AuthResponse response = authClient.authenticate();
            this.currentToken = response.getToken();
            long expiresInSeconds = response.getExpiresIn() > 0 ? response.getExpiresIn() : 3600;
            this.expiryTime = Instant.now().plusSeconds(expiresInSeconds);
        }
        return currentToken;
    }

    private boolean isTokenValid() {
        if (currentToken == null || expiryTime == null) {
            return false;
        }
        return Instant.now().isBefore(expiryTime);
    }
}
