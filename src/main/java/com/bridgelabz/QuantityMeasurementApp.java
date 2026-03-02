package com.bridgelabz;

import java.util.Objects;

/**
 * Quantity Measurement App
 * UC1-UC6 combined (Length): equality, unit support, conversion, addition.
 * Base unit used for internal normalization: FEET.
 */
public class QuantityMeasurementApp {

    /**
     * Supported length units with conversion factors relative to FEET (base unit).
     */
    public enum LengthUnit {
        FEET(1.0),                 // 1 ft = 1 ft
        INCH(1.0 / 12.0),          // 1 inch = 1/12 ft
        YARDS(3.0),                // 1 yard = 3 ft
        CENTIMETERS(0.393701 / 12.0); // 1 cm = 0.393701 inch = 0.393701/12 ft

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        /** Converts a value in this unit to feet. */
        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    /**
     * Immutable Value Object representing a length quantity with a unit.
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
            return unit.toFeet(value);
        }

        /**
         * UC5: Convert this QuantityLength to another unit (returns a new object).
         */
        public QuantityLength convertTo(LengthUnit target) {
            double converted = QuantityMeasurementApp.convert(this.value, this.unit, target);
            return new QuantityLength(converted, target);
        }

        /**
         * UC6: Add another QuantityLength to this one (result unit = this.unit).
         */
        public QuantityLength add(QuantityLength other) {
            return QuantityMeasurementApp.add(this, other);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            QuantityLength other = (QuantityLength) obj;
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

    // ---------------- UC1 / UC2 helpers (same-unit equality) ----------------

    public static boolean areFeetEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.FEET).equals(new QuantityLength(b, LengthUnit.FEET));
    }

    public static boolean areInchesEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.INCH).equals(new QuantityLength(b, LengthUnit.INCH));
    }

    // ---------------- UC5: Conversion API ----------------

    /**
     * Converts a numeric value from source unit to target unit using base normalization.
     * Formula: result = value * (source.toFeetFactor / target.toFeetFactor)
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be finite (not NaN/Infinity)");
        }
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target units must not be null");
        }

        double inFeet = source.toFeet(value);          // normalize to base
        return inFeet / target.toFeet(1.0);            // convert base -> target
    }

    // ---------------- UC6: Addition API ----------------

    /**
     * Adds two lengths (any units) and returns result in unit of the first operand.
     * Result unit = a.unit
     */
    public static QuantityLength add(QuantityLength a, QuantityLength b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Operands must not be null");
        }
        if (!Double.isFinite(a.getValue()) || !Double.isFinite(b.getValue())) {
            throw new IllegalArgumentException("Values must be finite (not NaN/Infinity)");
        }

        double sumInFeet = a.getUnit().toFeet(a.getValue()) + b.getUnit().toFeet(b.getValue());
        double sumInAUnit = sumInFeet / a.getUnit().toFeet(1.0);

        return new QuantityLength(sumInAUnit, a.getUnit());
    }

    // ---------------- Demo main ----------------

    public static void main(String[] args) {
        // UC3/UC4 equality demo
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);
        System.out.println("Equality: " + q1 + " == " + q2 + " ? " + q1.equals(q2));

        // UC5 conversion demo
        System.out.println("convert(1.0, FEET, INCH) = " + convert(1.0, LengthUnit.FEET, LengthUnit.INCH));

        // UC6 addition demo
        QuantityLength sum = add(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCH));
        System.out.println("Add: Quantity(1.0, FEET) + Quantity(12.0, INCH) = " + sum);
    }
}