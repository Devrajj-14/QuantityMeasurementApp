package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.bridgelabz.quantitymeasurement.unit.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller for quantity measurement operations.
 * Console-style — talks only to the service layer, prints results.
 */
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);
    private final IQuantityMeasurementService service;

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    /**
     * Compare two quantities and print the result.
     */
    public <U extends IMeasurable> boolean performComparison(Quantity<U> q1, Quantity<U> q2) {
        boolean result = service.compareQuantities(q1, q2);
        System.out.println("Comparing " + q1 + " and " + q2 + " : " + result);
        return result;
    }

    /**
     * Convert a quantity to a target unit and print the result.
     */
    public <U extends IMeasurable> Quantity<U> performConversion(Quantity<U> quantity, U targetUnit) {
        Quantity<U> result = service.convertQuantity(quantity, targetUnit);
        System.out.println("Converting " + quantity + " to " + targetUnit.getUnitName() + " : " + result);
        return result;
    }

    /**
     * Add two quantities and print the result.
     */
    public <U extends IMeasurable> Quantity<U> performAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> result = service.addQuantities(q1, q2, targetUnit);
        System.out.println("Adding " + q1 + " + " + q2 + " in " + targetUnit.getUnitName() + " : " + result);
        return result;
    }

    /**
     * Subtract two quantities and print the result.
     */
    public <U extends IMeasurable> Quantity<U> performSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        Quantity<U> result = service.subtractQuantities(q1, q2, targetUnit);
        System.out.println("Subtracting " + q2 + " from " + q1 + " in " + targetUnit.getUnitName() + " : " + result);
        return result;
    }

    /**
     * Divide two quantities and print the ratio.
     */
    public <U extends IMeasurable> double performDivision(Quantity<U> q1, Quantity<U> q2) {
        double result = service.divideQuantities(q1, q2);
        System.out.println("Dividing " + q1 + " by " + q2 + " : " + result);
        return result;
    }

    /**
     * Show all measurement history.
     */
    public void showHistory() {
        List<QuantityMeasurementEntity> records = service.getAllMeasurements();
        System.out.println("\n=== Measurement History (" + records.size() + " records) ===");
        if (records.isEmpty()) {
            System.out.println("No measurements recorded yet.");
        } else {
            for (QuantityMeasurementEntity record : records) {
                System.out.println("  " + record);
            }
        }
        System.out.println("===========================================\n");
    }

    /**
     * Show the total measurement count.
     */
    public long showCount() {
        long count = service.getMeasurementCount();
        System.out.println("Total measurements recorded: " + count);
        return count;
    }

    /**
     * Clear all measurement history.
     */
    public void clearHistory() {
        service.clearHistory();
        System.out.println("Measurement history cleared.");
    }

    /**
     * Release resources held by the service.
     */
    public void releaseResources() {
        service.releaseResources();
        logger.info("Controller resources released");
    }
}
