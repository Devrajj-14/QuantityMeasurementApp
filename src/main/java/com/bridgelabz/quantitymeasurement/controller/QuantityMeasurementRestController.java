package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.*;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Quantity Measurement operations.
 * Exposes all core quantity operations and history management via HTTP endpoints.
 */
@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurement", description = "REST API for quantity comparison, conversion, and arithmetic")
public class QuantityMeasurementRestController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementRestController.class);
    private final IQuantityMeasurementService service;

    public QuantityMeasurementRestController(IQuantityMeasurementService service) {
        this.service = service;
    }

    // ===================== Core Operations =====================

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities for equality")
    public ResponseEntity<QuantityResponseDTO> compare(@Valid @RequestBody QuantityOperationRequestDTO request) {
        logger.info("REST: Compare request - {}", request);
        QuantityResponseDTO response = service.compareQuantities(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity from one unit to another")
    public ResponseEntity<QuantityResponseDTO> convert(@Valid @RequestBody QuantityConversionRequestDTO request) {
        logger.info("REST: Convert request - {}", request);
        QuantityResponseDTO response = service.convertQuantity(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityResponseDTO> add(@Valid @RequestBody QuantityOperationRequestDTO request) {
        logger.info("REST: Add request - {}", request);
        QuantityResponseDTO response = service.addQuantities(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract two quantities")
    public ResponseEntity<QuantityResponseDTO> subtract(@Valid @RequestBody QuantityOperationRequestDTO request) {
        logger.info("REST: Subtract request - {}", request);
        QuantityResponseDTO response = service.subtractQuantities(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide two quantities (returns dimensionless ratio)")
    public ResponseEntity<QuantityResponseDTO> divide(@Valid @RequestBody QuantityOperationRequestDTO request) {
        logger.info("REST: Divide request - {}", request);
        QuantityResponseDTO response = service.divideQuantities(request);
        return ResponseEntity.ok(response);
    }

    // ===================== History / Query Endpoints =====================

    @GetMapping("/history")
    @Operation(summary = "Get all measurement history")
    public ResponseEntity<List<MeasurementHistoryDTO>> getAllHistory() {
        logger.info("REST: Get all history");
        List<MeasurementHistoryDTO> history = service.getAllMeasurements();
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/operation/{type}")
    @Operation(summary = "Get measurement history by operation type")
    public ResponseEntity<List<MeasurementHistoryDTO>> getHistoryByOperation(@PathVariable String type) {
        logger.info("REST: Get history by operation type: {}", type);
        List<MeasurementHistoryDTO> history = service.getMeasurementsByOperation(type);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/history/measurement/{type}")
    @Operation(summary = "Get measurement history by measurement type")
    public ResponseEntity<List<MeasurementHistoryDTO>> getHistoryByMeasurementType(@PathVariable String type) {
        logger.info("REST: Get history by measurement type: {}", type);
        List<MeasurementHistoryDTO> history = service.getMeasurementsByMeasurementType(type);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/count")
    @Operation(summary = "Get total measurement count")
    public ResponseEntity<Map<String, Long>> getTotalCount() {
        logger.info("REST: Get total count");
        long count = service.getMeasurementCount();
        return ResponseEntity.ok(Map.of("totalCount", count));
    }

    @GetMapping("/count/{operationType}")
    @Operation(summary = "Get measurement count by operation type")
    public ResponseEntity<Map<String, Object>> getCountByOperation(@PathVariable String operationType) {
        logger.info("REST: Get count by operation: {}", operationType);
        long count = service.getCountByOperation(operationType);
        return ResponseEntity.ok(Map.of("operationType", operationType.toUpperCase(), "count", count));
    }

    @DeleteMapping("/history")
    @Operation(summary = "Clear all measurement history")
    public ResponseEntity<Map<String, String>> clearHistory() {
        logger.info("REST: Clear history");
        service.clearHistory();
        return ResponseEntity.ok(Map.of("message", "Measurement history cleared successfully"));
    }
}
