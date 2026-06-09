package com.affordmed.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationResult {
    private String depotId;
    private int mechanicHours;
    private List<String> selectedTaskIds;
    private int totalDuration;
    private int totalImpact;
}
