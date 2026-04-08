package com.bridgelabz.quantitymeasurement.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for quantity conversion operations.
 */
public class QuantityConversionRequestDTO {

    private double value;

    @NotBlank(message = "Source unit is required")
    private String sourceUnit;

    @NotBlank(message = "Target unit is required")
    private String targetUnit;

    public QuantityConversionRequestDTO() {
    }

    public QuantityConversionRequestDTO(double value, String sourceUnit, String targetUnit) {
        this.value = value;
        this.sourceUnit = sourceUnit;
        this.targetUnit = targetUnit;
    }

    // --- Getters and Setters ---

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getSourceUnit() {
        return sourceUnit;
    }

    public void setSourceUnit(String sourceUnit) {
        this.sourceUnit = sourceUnit;
    }

    public String getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }

    @Override
    public String toString() {
        return "QuantityConversionRequestDTO{" +
                value + " " + sourceUnit + " -> " + targetUnit +
                '}';
    }
}
