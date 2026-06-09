package com.affordmed.scheduler.controller;

import com.affordmed.scheduler.dto.OptimizationResult;
import com.affordmed.scheduler.service.SchedulingService;
import com.affordmed.logging.service.LoggingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scheduling")
public class SchedulingController {

    private final SchedulingService schedulingService;
    private final LoggingService loggingService;

    public SchedulingController(SchedulingService schedulingService, LoggingService loggingService) {
        this.schedulingService = schedulingService;
        this.loggingService = loggingService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OptimizationResult>> scheduleAllDepots() {
        loggingService.log("backend", "info", "controller", "Received request to schedule all depots");
        List<OptimizationResult> results = schedulingService.scheduleAllDepots();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/depot/{depotId}")
    public ResponseEntity<OptimizationResult> scheduleSingleDepot(@PathVariable String depotId) {
        loggingService.log("backend", "info", "controller", "Received request to schedule depot " + depotId);
        OptimizationResult result = schedulingService.scheduleSingleDepot(depotId);
        return ResponseEntity.ok(result);
    }
}
