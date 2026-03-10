package com.bridgelabz;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;
    private static final double EPSILON = 1e-6;

    private enum ArithmeticOperation {
        ADD {
            @Override
            double compute(double left, double right) {
                return left + right;
            }
        },
        SUBTRACT {
            @Override
            double compute(double left, double right) {
                return left - right;
            }
        },
        DIVIDE {
            @Override
            double compute(double left, double right) {
                if (Math.abs(right) < EPSILON) {
                    throw new ArithmeticException("Cannot divide by zero quantity");
                }
                return left / right;
            }
        };

        abstract double compute(double left, double right);
    }

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
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true, "addition");

        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double convertedResult = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(roundToTwoDecimals(convertedResult), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true, "subtraction");

        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double convertedResult = targetUnit.convertFromBaseUnit(baseResult);

        return new Quantity<>(roundToTwoDecimals(convertedResult), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false, "division");
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired, String operation) {
        if (other == null) {
            throw new IllegalArgumentException("Other quantity cannot be null");
        }

        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category operations are not allowed");
        }

        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Quantity values must be finite");
        }

        this.unit.validateOperationSupport(operation);
        other.unit.validateOperationSupport(operation);

        if (targetUnitRequired) {
            validateTargetUnit(targetUnit);
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

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        double thisBaseValue = this.unit.convertToBaseUnit(this.value);
        double otherBaseValue = other.unit.convertToBaseUnit(other.value);

        return operation.compute(thisBaseValue, otherBaseValue);
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