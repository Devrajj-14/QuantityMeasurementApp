package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPS = 1e-6;

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.FEET);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.INCH);

        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.INCH, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.YARDS);

        // 2 feet = 2/3 yards = 0.666666...
        assertEquals(2.0 / 3.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        var b = new QuantityMeasurementApp.QuantityLength(3.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.YARDS);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        var b = new QuantityMeasurementApp.QuantityLength(3.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.FEET);

        assertEquals(6.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH);
        var b = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.CENTIMETERS);

        // 2 inches = 5.08 cm
        assertEquals(5.08, sum.getValue(), 1e-3);
        assertEquals(QuantityMeasurementApp.LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum1 = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.YARDS);
        var sum2 = QuantityMeasurementApp.add(b, a, QuantityMeasurementApp.LengthUnit.YARDS);

        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, sum1.getUnit());
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, sum2.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        var a = new QuantityMeasurementApp.QuantityLength(5.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(0.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.YARDS);

        // 5 ft = 5/3 yards
        assertEquals(5.0 / 3.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        var a = new QuantityMeasurementApp.QuantityLength(5.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(-2.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b, QuantityMeasurementApp.LengthUnit.INCH);

        // 3 ft = 36 inches
        assertEquals(36.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.INCH, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit_Throws() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.add(a, b, null));
    }
}