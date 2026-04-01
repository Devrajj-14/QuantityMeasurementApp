package com.bridgelabz.exception;

/**
 * Custom exception class for handling errors related to quantity measurement operations.
 * Extends RuntimeException (unchecked) to allow throwing without mandatory try-catch,
 * while still providing the option to catch when desired.
 *
 * Used for: null inputs, invalid units, incompatible measurement types,
 * unsupported arithmetic operations, invalid conversions, and repository failures.
 */
public class QuantityMeasurementException extends RuntimeException {

    /**
     * Constructs exception with a custom error message.
     *
     * @param message the detail message
     */
    public QuantityMeasurementException(String message) {
        super(message);
    }

    /**
     * Constructs exception with a custom message and root cause.
     *
     * @param message the detail message
     * @param cause   the root cause exception
     */
    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}
