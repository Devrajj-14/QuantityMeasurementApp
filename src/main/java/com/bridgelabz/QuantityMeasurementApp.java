package com.bridgelabz;

import java.util.Objects;

public class QuantityMeasurementApp {

    // -------- UC1: Feet --------
    public static final class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    // -------- UC2: Inches (separate, same pattern) --------
    public static final class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    // UC2 asks to reduce dependency on main by creating separate methods
    public static boolean areFeetEqual(double a, double b) {
        return new Feet(a).equals(new Feet(b));
    }

    public static boolean areInchesEqual(double a, double b) {
        return new Inches(a).equals(new Inches(b));
    }

    public static void main(String[] args) {
        System.out.println("Input: 1.0 inch and 1.0 inch");
        System.out.println("Output: Equal (" + areInchesEqual(1.0, 1.0) + ")");

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + areFeetEqual(1.0, 1.0) + ")");
    }
}