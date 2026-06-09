package com.affordmed.scheduler.client;

import com.affordmed.scheduler.config.AffordMedProperties;
import com.affordmed.scheduler.dto.MaintenanceTask;
import com.affordmed.logging.service.LoggingService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class VehicleClient {

    private final RestClient restClient;
    private final AffordMedProperties properties;
    private final TokenProvider tokenProvider;
    private final LoggingService loggingService;

    public VehicleClient(RestClient restClient, AffordMedProperties properties, 
                         TokenProvider tokenProvider, LoggingService loggingService) {
        this.restClient = restClient;
        this.properties = properties;
        this.tokenProvider = tokenProvider;
        this.loggingService = loggingService;
    }

    public List<MaintenanceTask> getTasksForDepot(String depotId) {
        loggingService.log("backend", "info", "client", "Fetching tasks for depot " + depotId + " from AffordMed API");
        try {
            return restClient.get()
                    .uri(properties.getBaseUrl() + properties.getVehiclesEndpoint() + "?depotId=" + depotId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<MaintenanceTask>>() {});
        } catch (Exception e) {
            loggingService.log("backend", "error", "client", "Failed to fetch tasks for depot " + depotId + ": " + e.getMessage());
            throw new RuntimeException("Failed to fetch tasks for depot", e);
        }
    }
}
