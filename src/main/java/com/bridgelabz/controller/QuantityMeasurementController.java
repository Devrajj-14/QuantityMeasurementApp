package com.bridgelabz.controller;

import com.bridgelabz.entity.QuantityDTO;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.service.IQuantityMeasurementService;

/**
 * Controller layer for the Quantity Measurement Application.
 * Handles user interactions and delegates business logic to the Service Layer.
 *
 * Provides performXXX methods for each operation (comparison, conversion, addition,
 * subtraction, division) and a consolidated runAllDemonstrations() method for
 * end-to-end demonstration across all measurement categories.
 *
 * The controller does not contain any measurement calculation logic.
 * It only catches exceptions, formats output, and delegates to the service.
 *
 * Dependency Injection: receives IQuantityMeasurementService via constructor injection,
 * promoting loose coupling and testability.
 */
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    /**
     * Constructs the controller with a service dependency.
     *
     * @param service the measurement service implementation
     */
    public QuantityMeasurementController(IQuantityMeasurementService service) {
        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.service = service;
    }

    /**
     * Performs an equality comparison between two quantities.
     */
    public void performComparison(QuantityDTO dto1, QuantityDTO dto2) {
        try {
            boolean result = service.compare(dto1, dto2);
            System.out.println("Comparing " + dto1 + " and " + dto2 + " : " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("COMPARISON ERROR: " + e.getMessage());
        }
    }

    /**
     * Performs a unit conversion on a quantity.
     */
    public void performConversion(QuantityDTO sourceDTO, QuantityDTO targetUnitDTO) {
        try {
            QuantityDTO result = service.convert(sourceDTO, targetUnitDTO);
            System.out.println("Converting " + sourceDTO + " to " + targetUnitDTO.getUnit().getUnitName()
                    + " : " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("CONVERSION ERROR: " + e.getMessage());
        }
    }

    /**
     * Performs addition of two quantities.
     */
    public void performAddition(QuantityDTO dto1, QuantityDTO dto2) {
        try {
            QuantityDTO result = service.add(dto1, dto2);
            System.out.println("Adding " + dto1 + " and " + dto2 + " : " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("ADDITION ERROR: " + e.getMessage());
        }
    }

    /**
     * Performs subtraction of one quantity from another.
     */
    public void performSubtraction(QuantityDTO dto1, QuantityDTO dto2) {
        try {
            QuantityDTO result = service.subtract(dto1, dto2);
            System.out.println("Subtracting " + dto2 + " from " + dto1 + " : " + result);
        } catch (QuantityMeasurementException e) {
            System.out.println("SUBTRACTION ERROR: " + e.getMessage());
        }
    }

    /**
     * Performs division of one quantity by another.
     */
    public void performDivision(QuantityDTO dto1, QuantityDTO dto2) {
        try {
            QuantityDTO result = service.divide(dto1, dto2);
            System.out.println("Dividing " + dto1 + " by " + dto2 + " : " + result.getValue());
        } catch (QuantityMeasurementException e) {
            System.out.println("DIVISION ERROR: " + e.getMessage());
        }
    }

    /**
     * Runs all demonstrations across multiple measurement categories,
     * showcasing the full capabilities of the N-Tier architecture.
     */
    public void runAllDemonstrations() {
        System.out.println("==========================================================");
        System.out.println(" UC15: Quantity Measurement App — N-Tier Architecture Demo ");
        System.out.println("==========================================================\n");

        demonstrateLengthOperations();
        demonstrateWeightOperations();
        demonstrateVolumeOperations();
        demonstrateTemperatureOperations();
        demonstrateCrossCategoryPrevention();

        System.out.println("==========================================================");
        System.out.println(" All demonstrations completed successfully.");
        System.out.println("==========================================================");
    }

    // ======================== Private Demonstration Methods ========================

    private void demonstrateLengthOperations() {
        System.out.println("--- Length Operations ---");
        QuantityDTO feet1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO inches12 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        QuantityDTO yard1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.YARDS);
        QuantityDTO feet3 = new QuantityDTO(3.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO feet10 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO feet5 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO targetInch = new QuantityDTO(0, QuantityDTO.LengthUnit.INCH);

        performComparison(feet1, inches12);
        performComparison(yard1, feet3);
        performConversion(feet1, targetInch);
        performAddition(feet10, feet5);
        performSubtraction(feet10, feet5);
        performDivision(feet10, feet5);
        System.out.println();
    }

    private void demonstrateWeightOperations() {
        System.out.println("--- Weight Operations ---");
        QuantityDTO kg1 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO gram1000 = new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM);
        QuantityDTO kg5 = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO kg2 = new QuantityDTO(2.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO targetGram = new QuantityDTO(0, QuantityDTO.WeightUnit.GRAM);

        performComparison(kg1, gram1000);
        performConversion(kg1, targetGram);
        performAddition(kg5, kg2);
        performSubtraction(kg5, kg2);
        performDivision(kg5, kg2);
        System.out.println();
    }

    private void demonstrateVolumeOperations() {
        System.out.println("--- Volume Operations ---");
        QuantityDTO litre1 = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO ml1000 = new QuantityDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE);
        QuantityDTO litre5 = new QuantityDTO(5.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO litre2 = new QuantityDTO(2.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO targetMl = new QuantityDTO(0, QuantityDTO.VolumeUnit.MILLILITRE);

        performComparison(litre1, ml1000);
        performConversion(litre1, targetMl);
        performAddition(litre5, litre2);
        performSubtraction(litre5, litre2);
        performDivision(litre5, litre2);
        System.out.println();
    }

    private void demonstrateTemperatureOperations() {
        System.out.println("--- Temperature Operations ---");
        QuantityDTO celsius0 = new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO fahrenheit32 = new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
        QuantityDTO celsius100 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO celsius50 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO targetFahrenheit = new QuantityDTO(0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
        QuantityDTO targetKelvin = new QuantityDTO(0, QuantityDTO.TemperatureUnit.KELVIN);

        // Equality and conversion work for temperature
        performComparison(celsius0, fahrenheit32);
        performConversion(celsius100, targetFahrenheit);
        performConversion(celsius0, targetKelvin);

        // Arithmetic operations should fail for temperature
        System.out.println("  (Testing unsupported arithmetic for temperature...)");
        performAddition(celsius100, celsius50);
        performSubtraction(celsius100, celsius50);
        performDivision(celsius100, celsius50);
        System.out.println();
    }

    private void demonstrateCrossCategoryPrevention() {
        System.out.println("--- Cross-Category Operation Prevention ---");
        QuantityDTO feet = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO kg = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);

        performComparison(feet, kg);
        performAddition(feet, kg);
        System.out.println();
    }
}
