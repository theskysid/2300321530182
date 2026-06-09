package com.affordmed.scheduler.service;

import com.affordmed.scheduler.dto.MaintenanceTask;
import com.affordmed.scheduler.dto.OptimizationResult;
import com.affordmed.logging.service.LoggingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class KnapsackOptimizationService {

    private final LoggingService loggingService;

    public KnapsackOptimizationService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    public OptimizationResult optimize(String depotId, int mechanicHours, List<MaintenanceTask> tasks) {
        loggingService.log("backend", "info", "service", "Starting knapsack optimization for depot " + depotId);

        int n = tasks.size();
        int capacity = mechanicHours;
        

        if (n == 0 || capacity <= 0) {
            loggingService.log("backend", "warn", "service", "Empty tasks or zero capacity for depot " + depotId);
            return new OptimizationResult(depotId, mechanicHours, Collections.emptyList(), 0, 0);
        }


        int[][] dp = new int[n + 1][capacity + 1];


        for (int i = 1; i <= n; i++) {
            MaintenanceTask task = tasks.get(i - 1);
            int duration = task.getDuration();
            int impact = task.getImpact();

            for (int w = 0; w <= capacity; w++) {
                if (duration <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - duration] + impact);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        int totalImpact = dp[n][capacity];
        

        List<String> selectedTaskIds = new ArrayList<>();
        int w = capacity;
        int totalDuration = 0;
        
        for (int i = n; i > 0 && totalImpact > 0; i--) {
            if (totalImpact != dp[i - 1][w]) {
                MaintenanceTask task = tasks.get(i - 1);
                selectedTaskIds.add(task.getTaskId());
                totalImpact -= task.getImpact();
                w -= task.getDuration();
                totalDuration += task.getDuration();
            }
        }


        Collections.reverse(selectedTaskIds);

        loggingService.log("backend", "info", "service", "Completed knapsack optimization for depot " + depotId);

        return new OptimizationResult(depotId, mechanicHours, selectedTaskIds, totalDuration, dp[n][capacity]);
    }
}
