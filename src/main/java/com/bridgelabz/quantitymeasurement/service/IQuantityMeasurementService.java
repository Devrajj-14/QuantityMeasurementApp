package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.unit.IMeasurable;
import com.bridgelabz.quantitymeasurement.model.Quantity;

import java.util.List;

/**
 * Service interface for quantity measurement operations.
 * Combines business logic with persistence.
 */
public interface IQuantityMeasurementService {

    /**
     * Compare two quantities and persist the comparison record.
     * Returns true if they are equal (in base-unit terms).
     */
    <U extends IMeasurable> boolean compareQuantities(Quantity<U> q1, Quantity<U> q2);

    /**
     * Convert a quantity to a target unit and persist the conversion record.
     */
    <U extends IMeasurable> Quantity<U> convertQuantity(Quantity<U> quantity, U targetUnit);

    /**
     * Add two quantities, persist the record, and return the result.
     */
    <U extends IMeasurable> Quantity<U> addQuantities(Quantity<U> q1, Quantity<U> q2, U targetUnit);

    /**
     * Subtract two quantities, persist the record, and return the result.
     */
    <U extends IMeasurable> Quantity<U> subtractQuantities(Quantity<U> q1, Quantity<U> q2, U targetUnit);

    /**
     * Divide two quantities, persist the record, and return the ratio.
     */
    <U extends IMeasurable> double divideQuantities(Quantity<U> q1, Quantity<U> q2);

    /**
     * Get all measurement history.
     */
    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Get measurements filtered by operation type.
     */
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType);

    /**
     * Get measurements filtered by measurement type.
     */
    List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType);

    /**
     * Get total number of measurement records.
     */
    long getMeasurementCount();

    /**
     * Clear all measurement history.
     */
    void clearHistory();

    /**
     * Release service resources (delegates to repository).
     */
    void releaseResources();
}
