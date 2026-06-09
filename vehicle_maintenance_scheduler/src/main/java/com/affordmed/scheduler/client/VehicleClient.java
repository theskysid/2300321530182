package com.affordmed.scheduler.client;

import com.affordmed.scheduler.config.AffordMedProperties;
import com.affordmed.scheduler.dto.MaintenanceTask;

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


    public VehicleClient(RestClient restClient, AffordMedProperties properties, 
                         TokenProvider tokenProvider) {
        this.restClient = restClient;
        this.properties = properties;
        this.tokenProvider = tokenProvider;
    }

    public List<MaintenanceTask> getTasksForDepot(String depotId) {

        try {
            return restClient.get()
                    .uri(properties.getBaseUrl() + properties.getVehiclesEndpoint() + "?depotId=" + depotId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<MaintenanceTask>>() {});
        } catch (Exception e) {

            throw new RuntimeException("Failed to fetch tasks for depot", e);
        }
    }
}
