package com.bridgelabz;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;
    private static final double EPSILON = 1e-6;

    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    public Quantity<U> convertTo(U targetUnit) {
        validateTargetUnit(targetUnit);

        double baseValue = unit.convertToBaseUnit(value);
        double convertedValue = targetUnit.convertFromBaseUnit(baseValue);

        return new Quantity<>(roundToTwoDecimals(convertedValue), targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        validateOther(other);
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateOther(other);
        validateTargetUnit(targetUnit);

        double thisBaseValue = this.unit.convertToBaseUnit(this.value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);
        double sumBaseValue = thisBaseValue + otherBaseValue;
        double resultValue = targetUnit.convertFromBaseUnit(sumBaseValue);

        return new Quantity<>(roundToTwoDecimals(resultValue), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        validateOther(other);
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateOther(other);
        validateTargetUnit(targetUnit);

        double thisBaseValue = this.unit.convertToBaseUnit(this.value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);
        double differenceBaseValue = thisBaseValue - otherBaseValue;
        double resultValue = targetUnit.convertFromBaseUnit(differenceBaseValue);

        return new Quantity<>(roundToTwoDecimals(resultValue), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateOther(other);

        double divisorBaseValue = other.unit.convertToBaseUnit(other.value);
        if (Math.abs(divisorBaseValue) < EPSILON) {
            throw new ArithmeticException("Cannot divide by zero quantity");
        }

        double thisBaseValue = this.unit.convertToBaseUnit(this.value);
        return thisBaseValue / divisorBaseValue;
    }

    private void validateOther(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category operations are not allowed");
        }
        if (!Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Other quantity value must be finite");
        }
    }

    private void validateTargetUnit(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (this.unit.getClass() != targetUnit.getClass()) {
            throw new IllegalArgumentException("Target unit must belong to same measurement category");
        }
    }

    private double roundToTwoDecimals(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;

        if (this.unit.getClass() != other.unit.getClass()) {
            return false;
        }

        double thisBaseValue = this.unit.convertToBaseUnit(this.value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);

        return Math.abs(thisBaseValue - otherBaseValue) < EPSILON;
    }

    @Override
    public int hashCode() {
        double baseValue = unit.convertToBaseUnit(value);
        long rounded = Math.round(baseValue / EPSILON);
        return Objects.hash(rounded, unit.getClass());
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}