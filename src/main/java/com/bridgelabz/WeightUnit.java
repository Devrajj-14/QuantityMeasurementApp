package com.bridgelabz;

/**
 * Standalone enum for weight units (UC9).
 * Responsible for converting to/from base unit (KILOGRAM).
 */
public enum WeightUnit {
    KILOGRAM(1.0),       // base
    GRAM(0.001),         // 1 g = 0.001 kg
    POUND(0.453592);     // 1 lb ≈ 0.453592 kg

    private final double toKilogramFactor;

    WeightUnit(double toKilogramFactor) {
        this.toKilogramFactor = toKilogramFactor;
    }

    public double getToKilogramFactor() {
        return toKilogramFactor;
    }

    /** Convert a value in this unit to base unit (kilogram). */
    public double convertToBaseUnit(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        return value * toKilogramFactor;
    }

    /** Convert a value in base unit (kilogram) to this unit. */
    public double convertFromBaseUnit(double kgValue) {
        if (!Double.isFinite(kgValue)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        return kgValue / toKilogramFactor;
    }
}