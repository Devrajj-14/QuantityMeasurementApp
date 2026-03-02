package com.bridgelabz;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // --- Same-unit equality ---
    @Test
    void testEquality_FeetToFeet_SameValue() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(a.equals(b), "Quantity(1.0, feet) should equal Quantity(1.0, feet)");
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH);
        var b = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH);
        assertTrue(a.equals(b), "Quantity(1.0, inch) should equal Quantity(1.0, inch)");
    }

    // --- Cross-unit equality ---
    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        var feet = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var inch = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);
        assertTrue(feet.equals(inch), "1 foot should equal 12 inches");
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue_symmetry() {
        var inch = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);
        var feet = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(inch.equals(feet), "12 inches should equal 1 foot (symmetry)");
    }

    // --- Different values ---
    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(a.equals(b), "1 foot should NOT equal 2 feet");
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH);
        var b = new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.INCH);
        assertFalse(a.equals(b), "1 inch should NOT equal 2 inches");
    }

    // --- Null & type safety ---
    @Test
    void testEquality_SameReference() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(a.equals(a), "Object must equal itself (reflexive)");
    }

    @Test
    void testEquality_NullComparison() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(a.equals(null), "Object must not equal null");
    }

    @Test
    void testEquality_DifferentTypeComparison() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(a.equals("Quantity(1.0, feet)"), "Must not equal a different type");
    }

    // --- Invalid / null unit ---
    @Test
    void testEquality_NullUnit_shouldThrow() {
        assertThrows(NullPointerException.class,
                () -> new QuantityMeasurementApp.QuantityLength(1.0, null),
                "Null unit must be rejected");
    }

    // --- Backward compatibility helpers (UC1/UC2 style calls) ---
    @Test
    void testHelper_areFeetEqual() {
        assertTrue(QuantityMeasurementApp.areFeetEqual(1.0, 1.0));
        assertFalse(QuantityMeasurementApp.areFeetEqual(1.0, 2.0));
    }

    @Test
    void testHelper_areInchesEqual() {
        assertTrue(QuantityMeasurementApp.areInchesEqual(1.0, 1.0));
        assertFalse(QuantityMeasurementApp.areInchesEqual(1.0, 2.0));
    }
}