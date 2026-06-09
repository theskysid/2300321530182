package com.affordmed.scheduler.client;

import com.affordmed.scheduler.dto.AuthResponse;
import com.affordmed.logging.service.LoggingService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TokenProvider {

    private final AuthClient authClient;
    private final LoggingService loggingService;
    private final Lock lock = new ReentrantLock();

    private String currentToken;
    private Instant expiryTime;


    private static final long REFRESH_BUFFER_SECONDS = 30;

    public TokenProvider(AuthClient authClient, LoggingService loggingService) {
        this.authClient = authClient;
        this.loggingService = loggingService;
    }

    public String getToken() {
        if (isTokenValid()) {
            return currentToken;
        }

        lock.lock();
        try {
            if (!isTokenValid()) {
                if (currentToken != null) {
                    loggingService.log("backend", "info", "client", "Token expired or nearing expiry, refreshing token");
                }
                
                AuthResponse response = authClient.authenticate();
                this.currentToken = response.getToken();
                

                long expiresInSeconds = response.getExpiresIn() > 0 ? response.getExpiresIn() : 3600;
                this.expiryTime = Instant.now().plusSeconds(expiresInSeconds);
            }
            return currentToken;
        } finally {
            lock.unlock();
        }
    }

    private boolean isTokenValid() {
        return currentToken != null && expiryTime != null && 
               Instant.now().plusSeconds(REFRESH_BUFFER_SECONDS).isBefore(expiryTime);
    }
}
