package com.affordmed.scheduler.client;

import com.affordmed.scheduler.config.AffordMedProperties;
import com.affordmed.scheduler.dto.Depot;

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


    public DepotClient(RestClient restClient, AffordMedProperties properties, 
                       TokenProvider tokenProvider) {
        this.restClient = restClient;
        this.properties = properties;
        this.tokenProvider = tokenProvider;
    }

    public List<Depot> getAllDepots() {

        try {
            return restClient.get()
                    .uri(properties.getBaseUrl() + properties.getDepotsEndpoint())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Depot>>() {});
        } catch (Exception e) {

            throw new RuntimeException("Failed to fetch depots", e);
        }
    }
    
    public Depot getDepot(String depotId) {

        try {
            return restClient.get()
                    .uri(properties.getBaseUrl() + properties.getDepotsEndpoint() + "/" + depotId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.getToken())
                    .retrieve()
                    .body(Depot.class);
        } catch (Exception e) {

            throw new RuntimeException("Failed to fetch depot", e);
        }
    }
}
