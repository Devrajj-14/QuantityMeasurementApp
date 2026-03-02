package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    // -------- FEET TESTS (UC1 regression) --------
    @Test
    void givenSameFeetValues_whenCompared_shouldReturnTrue() {
        assertTrue(QuantityMeasurementApp.areFeetEqual(1.0, 1.0),
                "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void givenDifferentFeetValues_whenCompared_shouldReturnFalse() {
        assertFalse(QuantityMeasurementApp.areFeetEqual(1.0, 2.0),
                "1.0 ft should NOT be equal to 2.0 ft");
    }

    @Test
    void givenFeetObject_whenComparedWithNull_shouldReturnFalse() {
        QuantityMeasurementApp.Feet feet = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(feet.equals(null), "Feet should NOT be equal to null");
    }

    @Test
    void givenFeetObject_whenComparedWithItself_shouldReturnTrue() {
        QuantityMeasurementApp.Feet feet = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(feet.equals(feet), "Feet must be equal to itself (reflexive)");
    }

    @Test
    void givenFeetObject_whenComparedWithDifferentType_shouldReturnFalse() {
        QuantityMeasurementApp.Feet feet = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(feet.equals("1.0"), "Feet should NOT equal a different type");
    }

    // -------- INCHES TESTS (UC2) --------
    @Test
    void givenSameInchValues_whenCompared_shouldReturnTrue() {
        assertTrue(QuantityMeasurementApp.areInchesEqual(1.0, 1.0),
                "1.0 inch should be equal to 1.0 inch");
    }

    @Test
    void givenDifferentInchValues_whenCompared_shouldReturnFalse() {
        assertFalse(QuantityMeasurementApp.areInchesEqual(1.0, 2.0),
                "1.0 inch should NOT be equal to 2.0 inch");
    }

    @Test
    void givenInchesObject_whenComparedWithNull_shouldReturnFalse() {
        QuantityMeasurementApp.Inches inch = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(inch.equals(null), "Inches should NOT be equal to null");
    }

    @Test
    void givenInchesObject_whenComparedWithItself_shouldReturnTrue() {
        QuantityMeasurementApp.Inches inch = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(inch.equals(inch), "Inches must be equal to itself (reflexive)");
    }

    @Test
    void givenInchesObject_whenComparedWithDifferentType_shouldReturnFalse() {
        QuantityMeasurementApp.Inches inch = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(inch.equals(123), "Inches should NOT equal a different type");
    }
}