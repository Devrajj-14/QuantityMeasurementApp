package com.bridgelabz.quantitymeasurement.dto;

/**
 * Response DTO for quantity operation results.
 */
public class QuantityResponseDTO {

    private double resultValue;
    private String resultUnit;
    private String operationType;
    private String message;

    public QuantityResponseDTO() {
    }

    public QuantityResponseDTO(double resultValue, String resultUnit,
                                String operationType, String message) {
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.operationType = operationType;
        this.message = message;
    }

    // --- Getters and Setters ---

    public double getResultValue() {
        return resultValue;
    }

    public void setResultValue(double resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "QuantityResponseDTO{" +
                "result=" + resultValue + " " + resultUnit +
                ", op=" + operationType +
                ", msg='" + message + '\'' +
                '}';
    }
}
