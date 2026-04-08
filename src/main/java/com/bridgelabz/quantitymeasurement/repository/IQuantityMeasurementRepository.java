package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * Repository abstraction for quantity measurement persistence.
 * Supports both in-memory cache and database implementations.
 */
public interface IQuantityMeasurementRepository {

    /**
     * Save a measurement record.
     */
    void save(QuantityMeasurementEntity entity);

    /**
     * Retrieve all measurement records.
     */
    List<QuantityMeasurementEntity> findAll();

    /**
     * Find measurements by operation type (e.g., COMPARE, CONVERT, ADD, SUBTRACT, DIVIDE).
     */
    List<QuantityMeasurementEntity> findByOperationType(String operationType);

    /**
     * Find measurements by measurement type (e.g., LENGTH, WEIGHT, VOLUME, TEMPERATURE).
     */
    List<QuantityMeasurementEntity> findByMeasurementType(String measurementType);

    /**
     * Get total count of stored measurements.
     */
    long count();

    /**
     * Delete all measurement records.
     */
    void deleteAll();

    /**
     * Get pool/storage statistics (optional — default returns info string).
     */
    default String getPoolStatistics() {
        return "No pool statistics available";
    }

    /**
     * Release resources (connections, pools, etc.). Default is no-op.
     */
    default void releaseResources() {
        // no-op for in-memory implementations
    }
}
