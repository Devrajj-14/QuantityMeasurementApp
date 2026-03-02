package com.bridgelabz;

import java.util.Objects;

/**
 * Immutable value object representing a weight quantity (UC9).
 * Mirrors QuantityLength design from UC8, but uses WeightUnit.
 */
public final class QuantityWeight {
    private static final double EPS = 1e-6;

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite (not NaN/Infinity)");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit must not be null");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public WeightUnit getUnit() {
        return unit;
    }

    /** Convert this quantity to the target unit and return a NEW object. */
    public QuantityWeight convertTo(WeightUnit targetUnit) {
        Objects.requireNonNull(targetUnit, "Target unit must not be null");
        double baseKg = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseKg);
        return new QuantityWeight(converted, targetUnit);
    }

    /** UC9 conversion API (static). */
    public static double convert(double value, WeightUnit source, WeightUnit target) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
        if (source == null) throw new IllegalArgumentException("Source unit must not be null");
        if (target == null) throw new IllegalArgumentException("Target unit must not be null");

        double baseKg = source.convertToBaseUnit(value);
        return target.convertFromBaseUnit(baseKg);
    }

    /** Add and return in FIRST operand unit (implicit target). */
    public QuantityWeight add(QuantityWeight other) {
        Objects.requireNonNull(other, "Other quantity must not be null");
        return add(other, this.unit);
    }

    /** Add and return in EXPLICIT target unit. */
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        Objects.requireNonNull(other, "Other quantity must not be null");
        Objects.requireNonNull(targetUnit, "Target unit must not be null");

        double aKg = this.unit.convertToBaseUnit(this.value);
        double bKg = other.unit.convertToBaseUnit(other.value);

        double sumKg = aKg + bKg;
        double sumInTarget = targetUnit.convertFromBaseUnit(sumKg);

        return new QuantityWeight(sumInTarget, targetUnit);
    }

    /** Equality is based on physical weight (converted to base kilograms). */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuantityWeight other)) return false;

        double thisKg = this.unit.convertToBaseUnit(this.value);
        double otherKg = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisKg - otherKg) <= EPS;
    }

    @Override
    public int hashCode() {
        double kg = unit.convertToBaseUnit(value);
        long rounded = Math.round(kg / EPS);
        return Long.hashCode(rounded);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}