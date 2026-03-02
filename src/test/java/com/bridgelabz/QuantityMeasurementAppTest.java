package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void givenSameFeetValues_whenCompared_shouldReturnTrue() {
        QuantityMeasurementApp.Feet a = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet b = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(a.equals(b), "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void givenDifferentFeetValues_whenCompared_shouldReturnFalse() {
        QuantityMeasurementApp.Feet a = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet b = new QuantityMeasurementApp.Feet(2.0);

        assertFalse(a.equals(b), "1.0 ft should NOT be equal to 2.0 ft");
    }

    @Test
    void givenFeetValue_whenComparedWithNull_shouldReturnFalse() {
        QuantityMeasurementApp.Feet a = new QuantityMeasurementApp.Feet(1.0);

        assertFalse(a.equals(null), "Any Feet value should NOT be equal to null");
    }

    @Test
    void givenSameReference_whenCompared_shouldReturnTrue() {
        QuantityMeasurementApp.Feet a = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(a.equals(a), "Object must be equal to itself (reflexive)");
    }

    @Test
    void givenFeetValue_whenComparedWithDifferentType_shouldReturnFalse() {
        QuantityMeasurementApp.Feet a = new QuantityMeasurementApp.Feet(1.0);

        assertFalse(a.equals("1.0"), "Feet should NOT be equal to a different type");
    }
}