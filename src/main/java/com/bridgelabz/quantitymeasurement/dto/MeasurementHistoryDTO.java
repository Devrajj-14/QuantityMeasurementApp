package com.bridgelabz.quantitymeasurement.dto;

import java.time.LocalDateTime;

/**
 * DTO for returning measurement history records via the REST API.
 * Decouples entity internals from API response.
 */
public class MeasurementHistoryDTO {

    private Long id;
    private double firstValue;
    private String firstUnit;
    private double secondValue;
    private String secondUnit;
    private String operationType;
    private String measurementType;
    private double resultValue;
    private String resultUnit;
    private LocalDateTime createdAt;

    public MeasurementHistoryDTO() {
    }

    public MeasurementHistoryDTO(Long id, double firstValue, String firstUnit,
                                  double secondValue, String secondUnit,
                                  String operationType, String measurementType,
                                  double resultValue, String resultUnit,
                                  LocalDateTime createdAt) {
        this.id = id;
        this.firstValue = firstValue;
        this.firstUnit = firstUnit;
        this.secondValue = secondValue;
        this.secondUnit = secondUnit;
        this.operationType = operationType;
        this.measurementType = measurementType;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
