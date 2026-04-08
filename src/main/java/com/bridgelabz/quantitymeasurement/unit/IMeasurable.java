package com.bridgelabz.quantitymeasurement.unit;
public interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();

    /**
     * Returns the measurement category name (e.g., "LENGTH", "WEIGHT", "VOLUME", "TEMPERATURE").
     */
    String getMeasurementType();

    default boolean supportsArithmetic() {
        return true;
    }

    default void validateOperationSupport(String operation) {
        if (!supportsArithmetic()) {
            throw new UnsupportedOperationException(
                    getUnitName() + " does not support " + operation + " operation"
            );
        }
    }

    /**
     * Resolves a unit name string to the corresponding IMeasurable enum constant.
     * Searches across all known unit enums (LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit).
     *
     * @param unitName the name of the unit (case-insensitive)
     * @return the matching IMeasurable instance
     * @throws IllegalArgumentException if no matching unit is found
     */
    static IMeasurable fromUnitName(String unitName) {
        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be null or empty");
        }
        String upper = unitName.toUpperCase().trim();
        // Search LengthUnit
        for (LengthUnit u : LengthUnit.values()) {
            if (u.name().equals(upper)) return u;
        }
        // Search WeightUnit
        for (WeightUnit u : WeightUnit.values()) {
            if (u.name().equals(upper)) return u;
        }
        // Search VolumeUnit
        for (VolumeUnit u : VolumeUnit.values()) {
            if (u.name().equals(upper)) return u;
        }
        // Search TemperatureUnit
        for (TemperatureUnit u : TemperatureUnit.values()) {
            if (u.name().equals(upper)) return u;
        }
        throw new IllegalArgumentException("Unknown unit: " + unitName);
    }
}