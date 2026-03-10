package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private static final double EPS = 1e-6;

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        var a = new Quantity<>(1.0, LengthUnit.FEET);
        var b = new Quantity<>(12.0, LengthUnit.INCH);

        var sum = a.add(b, LengthUnit.FEET);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        var a = new Quantity<>(1.0, LengthUnit.FEET);
        var b = new Quantity<>(12.0, LengthUnit.INCH);

        var sum = a.add(b, LengthUnit.INCH);

        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCH, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        var a = new Quantity<>(1.0, LengthUnit.FEET);
        var b = new Quantity<>(12.0, LengthUnit.INCH);

        var sum = a.add(b, LengthUnit.YARDS);

        assertEquals(2.0 / 3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        var a = new Quantity<>(1.0, LengthUnit.YARDS);
        var b = new Quantity<>(3.0, LengthUnit.FEET);

        var sum = a.add(b, LengthUnit.YARDS);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        var a = new Quantity<>(1.0, LengthUnit.YARDS);
        var b = new Quantity<>(3.0, LengthUnit.FEET);

        var sum = a.add(b, LengthUnit.FEET);

        assertEquals(6.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        var a = new Quantity<>(1.0, LengthUnit.INCH);
        var b = new Quantity<>(1.0, LengthUnit.INCH);

        var sum = a.add(b, LengthUnit.CENTIMETERS);

        assertEquals(5.08, sum.getValue(), 1e-3);
        assertEquals(LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        var a = new Quantity<>(1.0, LengthUnit.FEET);
        var b = new Quantity<>(12.0, LengthUnit.INCH);

        var sum1 = a.add(b, LengthUnit.YARDS);
        var sum2 = b.add(a, LengthUnit.YARDS);

        assertEquals(sum1.getValue(), sum2.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum1.getUnit());
        assertEquals(LengthUnit.YARDS, sum2.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        var a = new Quantity<>(5.0, LengthUnit.FEET);
        var b = new Quantity<>(0.0, LengthUnit.INCH);

        var sum = a.add(b, LengthUnit.YARDS);

        assertEquals(5.0 / 3.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        var a = new Quantity<>(5.0, LengthUnit.FEET);
        var b = new Quantity<>(-2.0, LengthUnit.FEET);

        var sum = a.add(b, LengthUnit.INCH);

        assertEquals(36.0, sum.getValue(), EPS);
        assertEquals(LengthUnit.INCH, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit_Throws() {
        var a = new Quantity<>(1.0, LengthUnit.FEET);
        var b = new Quantity<>(12.0, LengthUnit.INCH);

        assertThrows(IllegalArgumentException.class, () -> a.add(b, null));
    }
}