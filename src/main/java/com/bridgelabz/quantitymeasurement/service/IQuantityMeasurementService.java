package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.*;

import java.util.List;

/**
 * Service interface for quantity measurement operations.
 * Works with DTOs for controller communication and JPA for persistence.
 */
public interface IQuantityMeasurementService {

    /** Compare two quantities for equality. */
    QuantityResponseDTO compareQuantities(QuantityOperationRequestDTO request);

    /** Convert a quantity to a target unit. */
    QuantityResponseDTO convertQuantity(QuantityConversionRequestDTO request);

    /** Add two quantities. */
    QuantityResponseDTO addQuantities(QuantityOperationRequestDTO request);

    /** Subtract two quantities. */
    QuantityResponseDTO subtractQuantities(QuantityOperationRequestDTO request);

    /** Divide two quantities (returns dimensionless ratio). */
    QuantityResponseDTO divideQuantities(QuantityOperationRequestDTO request);

    /** Get all measurement history. */
    List<MeasurementHistoryDTO> getAllMeasurements();

    /** Get measurements filtered by operation type. */
    List<MeasurementHistoryDTO> getMeasurementsByOperation(String operationType);

    /** Get measurements filtered by measurement type. */
    List<MeasurementHistoryDTO> getMeasurementsByMeasurementType(String measurementType);

    /** Get total measurement count. */
    long getMeasurementCount();

    /** Get count by operation type. */
    long getCountByOperation(String operationType);

    /** Clear all measurement history. */
    void clearHistory();
}
