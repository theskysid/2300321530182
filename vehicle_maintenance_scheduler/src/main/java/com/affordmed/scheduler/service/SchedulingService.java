package com.affordmed.scheduler.service;

import com.affordmed.scheduler.client.DepotClient;
import com.affordmed.scheduler.client.VehicleClient;
import com.affordmed.scheduler.dto.Depot;
import com.affordmed.scheduler.dto.MaintenanceTask;
import com.affordmed.scheduler.dto.OptimizationResult;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulingService {

    private final DepotClient depotClient;
    private final VehicleClient vehicleClient;
    private final KnapsackOptimizationService optimizationService;
    public SchedulingService(DepotClient depotClient, VehicleClient vehicleClient, 
                             KnapsackOptimizationService optimizationService) {
        this.depotClient = depotClient;
        this.vehicleClient = vehicleClient;
        this.optimizationService = optimizationService;
    }

    public List<OptimizationResult> scheduleAllDepots() {

        List<Depot> depots = depotClient.getAllDepots();
        List<OptimizationResult> results = new ArrayList<>();
        
        for (Depot depot : depots) {
            results.add(scheduleForDepot(depot));
        }
        

        return results;
    }

    public OptimizationResult scheduleSingleDepot(String depotId) {

        Depot depot = depotClient.getDepot(depotId);
        OptimizationResult result = scheduleForDepot(depot);

        return result;
    }

    private OptimizationResult scheduleForDepot(Depot depot) {
        List<MaintenanceTask> tasks = vehicleClient.getTasksForDepot(depot.getId());
        return optimizationService.optimize(depot.getId(), depot.getMechanicHours(), tasks);
    }
}
