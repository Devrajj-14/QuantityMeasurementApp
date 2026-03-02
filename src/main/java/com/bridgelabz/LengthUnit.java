package com.bridgelabz;

import java.util.Objects;

/**
 * UC8: Standalone LengthUnit enum with full responsibility for conversions
 * to/from the base unit (FEET).
 */
public enum LengthUnit {
    FEET(1.0),
    INCH(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(0.393701 / 12.0);

    private final double toFeetFactor;

    LengthUnit(double toFeetFactor) {
        this.toFeetFactor = toFeetFactor;
    }

    /** Conversion factor to base unit (FEET). */
    public double getToFeetFactor() {
        return toFeetFactor;
    }

    /** Convert a value in this unit to base unit (feet). */
    public double convertToBaseUnit(double value) {
        validateFinite(value);
        return value * toFeetFactor;
    }

    /** Convert a value in base unit (feet) to this unit. */
    public double convertFromBaseUnit(double baseFeetValue) {
        validateFinite(baseFeetValue);
        return baseFeetValue / toFeetFactor;
    }

    /** Convert a value from this unit to a target unit via base (feet). */
    public double convert(double value, LengthUnit targetUnit) {
        Objects.requireNonNull(targetUnit, "Target unit must not be null");
        double inFeet = convertToBaseUnit(value);
        return targetUnit.convertFromBaseUnit(inFeet);
    }

    private static void validateFinite(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite (not NaN/Infinity)");
        }
    }
}