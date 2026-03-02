package com.bridgelabz;

import java.util.Objects;

public class QuantityMeasurementApp {

    // UC3 Step 1: Enum for supported units (base = FEET)
    public enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }
    // UC3 Step 2: Single DRY class for any length + unit
    public static final class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            this.value = value;
            this.unit = Objects.requireNonNull(unit, "Unit must not be null");
        }

        private double valueInFeet() {
            return unit.toFeet(value);
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
            return "Quantity(" + value + ", " + unit.name().toLowerCase() + ")";
        }
    }

    // Backward-compat style helpers (so UC1/UC2 behavior still easy to call)
    public static boolean areFeetEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.FEET)
                .equals(new QuantityLength(b, LengthUnit.FEET));
    }

    public static boolean areInchesEqual(double a, double b) {
        return new QuantityLength(a, LengthUnit.INCH)
                .equals(new QuantityLength(b, LengthUnit.INCH));
    }

    public static void main(String[] args) {
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        System.out.println("Input: " + q1 + " and " + q2);
        System.out.println("Output: Equal (" + q1.equals(q2) + ")");

        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.INCH);

        System.out.println("Input: " + q3 + " and " + q4);
        System.out.println("Output: Equal (" + q3.equals(q4) + ")");
        System.out.println(new QuantityLength(1.0, LengthUnit.YARDS)
                .equals(new QuantityLength(3.0, LengthUnit.FEET))); // true
    }
}