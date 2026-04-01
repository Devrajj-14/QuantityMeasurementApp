package com.bridgelabz.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Persistence entity storing the details of a quantity measurement operation.
 * Implements Serializable for file-based persistence in the cache repository.
 *
 * Designed to be effectively immutable — fields are initialized through constructors only.
 * Fields are not declared final to support Java Object Serialization.
 */
public class QuantityMeasurementEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String operand1;
    private String operand2;
    private String operationType;
    private String result;
    private boolean hasError;
    private String errorMessage;
    private String timestamp;

    /**
     * No-arg constructor for serialization support.
     */
    public QuantityMeasurementEntity() {
    }

    /**
     * Constructor for single-operand operations (e.g., conversion).
     */
    public QuantityMeasurementEntity(String operand1, String operationType, String result) {
        this.operand1 = operand1;
        this.operand2 = null;
        this.operationType = operationType;
        this.result = result;
        this.hasError = false;
        this.errorMessage = null;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Constructor for binary operations (e.g., comparison, addition, subtraction, division).
     */
    public QuantityMeasurementEntity(String operand1, String operand2, String operationType, String result) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operationType = operationType;
        this.result = result;
        this.hasError = false;
        this.errorMessage = null;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Constructor for error scenarios during operations.
     */
    public QuantityMeasurementEntity(String operand1, String operand2, String operationType, String errorMessage, boolean hasError) {
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.operationType = operationType;
        this.result = null;
        this.hasError = hasError;
        this.errorMessage = errorMessage;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters

    public String getOperand1() {
        return operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getResult() {
        return result;
    }

    public boolean isHasError() {
        return hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        if (hasError) {
            return "[" + timestamp + "] " + operationType + " — ERROR: " + errorMessage;
        }
        if (operand2 == null) {
            return "[" + timestamp + "] " + operationType + ": " + operand1 + " => " + result;
        }
        return "[" + timestamp + "] " + operationType + ": " + operand1 + " & " + operand2 + " => " + result;
    }
}
