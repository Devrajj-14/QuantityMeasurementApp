package com.bridgelabz;

/**
 * Standalone enum for length units (UC8).
 * Responsible for converting to/from base unit (FEET).
 */
public enum LengthUnit {
    FEET(1.0),                 // base
    INCH(1.0 / 12.0),          // 1 inch = 1/12 feet
    YARDS(3.0),                // 1 yard = 3 feet
    CENTIMETERS(1.0 / 30.48);  // 1 cm = 1/30.48 feet

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    public double getToFeetFactor() {
        return toFeetFactor;
    }

    /** Convert a value in this unit to base unit (feet). */
    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        return value * toFeetFactor;
    }

    /** Convert a value in base unit (feet) to this unit. */
    public double convertFromBaseUnit(double feetValue) {
        if (!Double.isFinite(feetValue)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        return feetValue / toFeetFactor;
    }
}