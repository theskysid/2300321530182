package com.affordmed.scheduler.service;

import com.affordmed.scheduler.dto.MaintenanceTask;
import com.affordmed.scheduler.dto.OptimizationResult;
import com.affordmed.logging.service.LoggingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KnapsackOptimizationServiceTest {

    private KnapsackOptimizationService optimizationService;
    private LoggingService loggingService;

    @BeforeEach
    public void setUp() {
        loggingService = Mockito.mock(LoggingService.class);
        optimizationService = new KnapsackOptimizationService(loggingService);
    }

    @Test
    public void testOptimize_EmptyTasks() {
        OptimizationResult result = optimizationService.optimize("depot1", 10, Collections.emptyList());
        assertEquals("depot1", result.getDepotId());
        assertEquals(0, result.getTotalDuration());
        assertEquals(0, result.getTotalImpact());
        assertTrue(result.getSelectedTaskIds().isEmpty());
    }

    @Test
    public void testOptimize_ZeroCapacity() {
        List<MaintenanceTask> tasks = Arrays.asList(new MaintenanceTask("t1", 5, 10));
        OptimizationResult result = optimizationService.optimize("depot1", 0, tasks);
        assertEquals(0, result.getTotalDuration());
        assertEquals(0, result.getTotalImpact());
        assertTrue(result.getSelectedTaskIds().isEmpty());
    }

    @Test
    public void testOptimize_NormalCase() {

        List<MaintenanceTask> tasks = Arrays.asList(
                new MaintenanceTask("t1", 2, 3),
                new MaintenanceTask("t2", 3, 4),
                new MaintenanceTask("t3", 4, 5),
                new MaintenanceTask("t4", 5, 6)
        );

        OptimizationResult result = optimizationService.optimize("depot1", 5, tasks);

        assertEquals(5, result.getTotalDuration());
        assertEquals(7, result.getTotalImpact());
        assertEquals(2, result.getSelectedTaskIds().size());
        assertTrue(result.getSelectedTaskIds().containsAll(Arrays.asList("t1", "t2")));
    }
}
