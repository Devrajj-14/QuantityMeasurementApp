package com.bridgelabz;

import java.util.Objects;

/**
 * Quantity Measurement App (UC1–UC8 for Length).
 * UC8 refactor: LengthUnit is standalone and owns conversion logic.
 */
public class QuantityMeasurementApp {

    /** Immutable value object representing a length quantity with a LengthUnit. */
    public static final class QuantityLength {
        private static final double EPS = 1e-6;

        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
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

        public LengthUnit getUnit() {
            return unit;
        }

        /** Converts this quantity into the target unit and returns a NEW object. */
        public QuantityLength convertTo(LengthUnit targetUnit) {
            Objects.requireNonNull(targetUnit, "Target unit must not be null");
            double baseFeet = unit.convertToBaseUnit(value);            // delegate to enum
            double converted = targetUnit.convertFromBaseUnit(baseFeet); // delegate to enum
            return new QuantityLength(converted, targetUnit);
        }

        /** UC5: Static conversion API. */
        public static double convert(double value, LengthUnit source, LengthUnit target) {
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
            if (source == null) throw new IllegalArgumentException("Source unit must not be null");
            if (target == null) throw new IllegalArgumentException("Target unit must not be null");

            double baseFeet = source.convertToBaseUnit(value);
            return target.convertFromBaseUnit(baseFeet);
        }

        /** UC6: Add and return in FIRST operand unit. */
        public QuantityLength add(QuantityLength other) {
            Objects.requireNonNull(other, "Other quantity must not be null");
            return add(other, this.unit);
        }

        /** UC7: Add and return in EXPLICIT target unit. */
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            Objects.requireNonNull(other, "Other quantity must not be null");
            Objects.requireNonNull(targetUnit, "Target unit must not be null");

            double aFeet = this.unit.convertToBaseUnit(this.value);
            double bFeet = other.unit.convertToBaseUnit(other.value);

            double sumFeet = aFeet + bFeet;
            double sumInTarget = targetUnit.convertFromBaseUnit(sumFeet);

            return new QuantityLength(sumInTarget, targetUnit);
        }

        /** Equality is based on physical length (converted to base feet). */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof QuantityLength other)) return false;

            double thisFeet = this.unit.convertToBaseUnit(this.value);
            double otherFeet = other.unit.convertToBaseUnit(other.value);

            return Math.abs(thisFeet - otherFeet) <= EPS;
        }

        @Override
        public int hashCode() {
            // hash based on normalized base-unit value (with rounding to stabilize)
            double feet = unit.convertToBaseUnit(value);
            long rounded = Math.round(feet / EPS); // stable bucket
            return Long.hashCode(rounded);
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // UC1/UC2 helper methods (optional, but match your earlier "reduce main dependency")
    public static boolean areFeetEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.FEET).equals(new QuantityLength(b, LengthUnit.FEET));
    }

    public static boolean areInchesEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.INCH).equals(new QuantityLength(b, LengthUnit.INCH));
    }

    public static void main(String[] args) {
        QuantityLength qFeet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength qInch = new QuantityLength(12.0, LengthUnit.INCH);

        System.out.println("Input: " + qFeet + " and " + qInch);
        System.out.println("Equals: " + qFeet.equals(qInch)); // true

        System.out.println("Convert: " + qFeet.convertTo(LengthUnit.INCH)); // ~Quantity(12.0, INCH)

        System.out.println("Add (target FEET): " + qFeet.add(qInch, LengthUnit.FEET));   // ~Quantity(2.0, FEET)
        System.out.println("Add (target YARDS): " + qFeet.add(qInch, LengthUnit.YARDS)); // ~Quantity(0.666..., YARDS)

        System.out.println("LengthUnit.INCH.convertToBaseUnit(12.0): " + LengthUnit.INCH.convertToBaseUnit(12.0)); // 1.0 feet
    }
}