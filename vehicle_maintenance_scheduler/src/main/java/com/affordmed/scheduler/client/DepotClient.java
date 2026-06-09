package com.affordmed.scheduler.client;

import com.affordmed.scheduler.config.AffordMedProperties;
import com.affordmed.scheduler.dto.Depot;
import com.affordmed.logging.service.LoggingService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class DepotClient {

    private final RestClient restClient;
    private final AffordMedProperties properties;
    private final TokenProvider tokenProvider;
    private final LoggingService loggingService;

    public DepotClient(RestClient restClient, AffordMedProperties properties, 
                       TokenProvider tokenProvider, LoggingService loggingService) {
        this.restClient = restClient;
        this.properties = properties;
        this.tokenProvider = tokenProvider;
        this.loggingService = loggingService;
    }

    public List<Depot> getAllDepots() {
        loggingService.log("backend", "info", "client", "Fetching all depots from AffordMed API");
        try {
            return restClient.get()
                    .uri(properties.getBaseUrl() + properties.getDepotsEndpoint())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Depot>>() {});
        } catch (Exception e) {
            loggingService.log("backend", "error", "client", "Failed to fetch depots: " + e.getMessage());
            throw new RuntimeException("Failed to fetch depots", e);
        }
    }
    
    public Depot getDepot(String depotId) {
        loggingService.log("backend", "info", "client", "Fetching depot " + depotId + " from AffordMed API");
        try {
            return restClient.get()
                    .uri(properties.getBaseUrl() + properties.getDepotsEndpoint() + "/" + depotId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken())
                    .retrieve()
                    .body(Depot.class);
        } catch (Exception e) {
            loggingService.log("backend", "error", "client", "Failed to fetch depot " + depotId + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch depot", e);
        }
    }
}
