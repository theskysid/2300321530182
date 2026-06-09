package com.affordmed.scheduler.controller;

import com.affordmed.scheduler.dto.OptimizationResult;
import com.affordmed.scheduler.service.SchedulingService;

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
    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<OptimizationResult>> scheduleAllDepots() {

        List<OptimizationResult> results = schedulingService.scheduleAllDepots();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/depot/{depotId}")
    public ResponseEntity<OptimizationResult> scheduleSingleDepot(@PathVariable String depotId) {

        OptimizationResult result = schedulingService.scheduleSingleDepot(depotId);
        return ResponseEntity.ok(result);
    }
}
