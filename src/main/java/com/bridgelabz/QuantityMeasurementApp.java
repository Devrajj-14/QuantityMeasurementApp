package com.bridgelabz;

import java.util.Objects;

public class QuantityMeasurementApp {

    /**
     * UC1: Represents a measurement in feet.
     * Immutable value object.
     */
    public static final class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;                  // same reference
            if (obj == null || getClass() != obj.getClass()) return false; // null/type check
            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0; // safe double compare
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return value + " ft";
        }
    }

    // Optional: demo run
    public static void main(String[] args) {
        Feet a = new Feet(1.0);
        Feet b = new Feet(1.0);

        System.out.println("Input: " + a + " and " + b);
        System.out.println("Output: Equal (" + a.equals(b) + ")");
    }
}