package com.bridgelabz;

import java.util.Objects;

/**
 * Quantity Measurement App (UC1–UC8 for Length)
 * UC8 refactor: LengthUnit is standalone and owns conversion logic.
 */
public class QuantityMeasurementApp {

    /**
     * Immutable value object representing a length quantity with a LengthUnit.
     * Delegates conversion to LengthUnit (UC8).
     */
    public static final class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite (not NaN/Infinity)");
            }
            this.unit = Objects.requireNonNull(unit, "Unit must not be null");
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        private double valueInFeet() {
            return unit.convertToBaseUnit(value);
        }

        // UC5: instance conversion
        public QuantityLength convertTo(LengthUnit targetUnit) {
            Objects.requireNonNull(targetUnit, "Target unit must not be null");
            double convertedValue = unit.convert(value, targetUnit);
            return new QuantityLength(convertedValue, targetUnit);
        }

        // UC6: instance add (result = this.unit)
        public QuantityLength add(QuantityLength other) {
            return QuantityMeasurementApp.add(this, other);
        }

        // UC7: instance add with explicit target
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            return QuantityMeasurementApp.add(this, other, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            QuantityLength other = (QuantityLength) obj;

            // Compare using base unit normalization (feet)
            return Double.compare(this.valueInFeet(), other.valueInFeet()) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueInFeet());
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit.name() + ")";
        }
    }

    // ---------------- UC1/UC2 helpers ----------------
    public static boolean areFeetEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.FEET).equals(new QuantityLength(b, LengthUnit.FEET));
    }

    public static boolean areInchesEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.INCH).equals(new QuantityLength(b, LengthUnit.INCH));
    }

    // ---------------- UC5: conversion API ----------------
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units must not be null");
        }
        return source.convert(value, target);
    }

    // ---------------- UC6: addition (result unit = first operand unit) ----------------
    public static QuantityLength add(QuantityLength a, QuantityLength b) {
        if (a == null) throw new IllegalArgumentException("First operand must not be null");
        return add(a, b, a.getUnit()); // delegate to UC7 overload (DRY)
    }

    // ---------------- UC7: addition with explicit target unit ----------------
    public static QuantityLength add(QuantityLength a, QuantityLength b, LengthUnit targetUnit) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands must not be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit must not be null");
        }
        if (!Double.isFinite(a.getValue()) || !Double.isFinite(b.getValue())) {
            throw new IllegalArgumentException("Values must be finite (not NaN/Infinity)");
        }

        // Convert both to base (feet), add, then convert to target using LengthUnit
        double sumFeet =
                a.getUnit().convertToBaseUnit(a.getValue())
                        + b.getUnit().convertToBaseUnit(b.getValue());

        double sumInTarget = targetUnit.convertFromBaseUnit(sumFeet);
        return new QuantityLength(sumInTarget, targetUnit);
    }

    // ---------------- Demo main ----------------
    public static void main(String[] args) {
        QuantityLength qFeet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength qInch = new QuantityLength(12.0, LengthUnit.INCH);

        System.out.println("Input: " + qFeet + " and " + qInch);
        System.out.println("Equals: " + qFeet.equals(qInch)); // true

        System.out.println("Convert: " + qFeet.convertTo(LengthUnit.INCH)); // Quantity(12.0, INCH)

        System.out.println("Add (target FEET): " + qFeet.add(qInch, LengthUnit.FEET)); // Quantity(2.0, FEET)
        System.out.println("Add (target YARDS): " + qFeet.add(qInch, LengthUnit.YARDS)); // ~0.6667 yards

        System.out.println("LengthUnit.INCH.convertToBaseUnit(12.0): " + LengthUnit.INCH.convertToBaseUnit(12.0)); // 1.0
    }
}