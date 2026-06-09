package com.affordmed.scheduler.service;

import com.affordmed.scheduler.client.DepotClient;
import com.affordmed.scheduler.client.VehicleClient;
import com.affordmed.scheduler.dto.Depot;
import com.affordmed.scheduler.dto.MaintenanceTask;
import com.affordmed.scheduler.dto.OptimizationResult;
import com.affordmed.logging.service.LoggingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingService {

    private final DepotClient depotClient;
    private final VehicleClient vehicleClient;
    private final KnapsackOptimizationService optimizationService;
    private final LoggingService loggingService;

    public SchedulingService(DepotClient depotClient, VehicleClient vehicleClient, 
                             KnapsackOptimizationService optimizationService, LoggingService loggingService) {
        this.depotClient = depotClient;
        this.vehicleClient = vehicleClient;
        this.optimizationService = optimizationService;
        this.loggingService = loggingService;
    }

    public List<OptimizationResult> scheduleAllDepots() {
        loggingService.log("backend", "info", "service", "Starting scheduling process for all depots");
        List<Depot> depots = depotClient.getAllDepots();
        List<OptimizationResult> results = new ArrayList<>();
        
        for (Depot depot : depots) {
            results.add(scheduleForDepot(depot));
        }
        
        loggingService.log("backend", "info", "service", "Completed scheduling process for all depots");
        return results;
    }

    public OptimizationResult scheduleSingleDepot(String depotId) {
        loggingService.log("backend", "info", "service", "Starting scheduling process for depot " + depotId);
        Depot depot = depotClient.getDepot(depotId);
        OptimizationResult result = scheduleForDepot(depot);
        loggingService.log("backend", "info", "service", "Completed scheduling process for depot " + depotId);
        return result;
    }

    private OptimizationResult scheduleForDepot(Depot depot) {
        List<MaintenanceTask> tasks = vehicleClient.getTasksForDepot(depot.getId());
        return optimizationService.optimize(depot.getId(), depot.getMechanicHours(), tasks);
    }
}
