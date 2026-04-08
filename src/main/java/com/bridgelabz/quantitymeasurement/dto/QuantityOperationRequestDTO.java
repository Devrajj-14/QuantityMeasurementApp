package com.bridgelabz.quantitymeasurement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for binary quantity operations (compare, add, subtract, divide).
 */
public class QuantityOperationRequestDTO {

    private double firstValue;

    @NotBlank(message = "First unit is required")
    private String firstUnit;

    private double secondValue;

    @NotBlank(message = "Second unit is required")
    private String secondUnit;

    /** Optional: target unit for addition/subtraction result. Defaults to firstUnit if absent. */
    private String targetUnit;

    public QuantityOperationRequestDTO() {
    }

    public QuantityOperationRequestDTO(double firstValue, String firstUnit,
                                        double secondValue, String secondUnit,
                                        String targetUnit) {
        this.firstValue = firstValue;
        this.firstUnit = firstUnit;
        this.secondValue = secondValue;
        this.secondUnit = secondUnit;
        this.targetUnit = targetUnit;
    }

    // --- Getters and Setters ---

    public double getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(double firstValue) {
        this.firstValue = firstValue;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = firstUnit;
    }

    public double getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(double secondValue) {
        this.secondValue = secondValue;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    @Override
    public String toString() {
        return "QuantityOperationRequestDTO{" +
                firstValue + " " + firstUnit +
                " <op> " + secondValue + " " + secondUnit +
                (targetUnit != null ? " -> " + targetUnit : "") +
                '}';
    }
}
