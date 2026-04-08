package com.bridgelabz.quantitymeasurement.exception;

/**
 * Exception for database-related failures (SQL, connection pool, schema).
 */
public class DatabaseException extends QuantityMeasurementException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
