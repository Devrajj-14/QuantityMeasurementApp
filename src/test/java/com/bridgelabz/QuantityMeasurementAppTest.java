package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPS = 1e-6;

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(2.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches_ResultFeet() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet_ResultInches() {
        var a = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);
        var b = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(24.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.INCH, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet_ResultYards() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        var b = new QuantityMeasurementApp.QuantityLength(3.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.YARDS, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_InchesPlusYard_ResultInches() {
        var a = new QuantityMeasurementApp.QuantityLength(36.0, QuantityMeasurementApp.LengthUnit.INCH);
        var b = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.YARDS);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(72.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.INCH, sum.getUnit());
    }

    @Test
    void testAddition_CentimeterPlusInch_ResultCentimeters() {
        var a = new QuantityMeasurementApp.QuantityLength(2.54, QuantityMeasurementApp.LengthUnit.CENTIMETERS);
        var b = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b);

        // 2.54 cm + 1 inch (=2.54 cm) = 5.08 cm
        assertEquals(5.08, sum.getValue(), 1e-3);
        assertEquals(QuantityMeasurementApp.LengthUnit.CENTIMETERS, sum.getUnit());
    }

    @Test
    void testAddition_WithZero() {
        var a = new QuantityMeasurementApp.QuantityLength(5.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(0.0, QuantityMeasurementApp.LengthUnit.INCH);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(5.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_NegativeValues() {
        var a = new QuantityMeasurementApp.QuantityLength(5.0, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(-2.0, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_Commutativity_InFeetBase() {
        var feet = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        var inch = new QuantityMeasurementApp.QuantityLength(12.0, QuantityMeasurementApp.LengthUnit.INCH);

        // A+B result unit = A's unit, so compare after converting to feet
        var sum1 = QuantityMeasurementApp.add(feet, inch); // in FEET
        var sum2 = QuantityMeasurementApp.add(inch, feet); // in INCH

        double sum1Feet = QuantityMeasurementApp.convert(sum1.getValue(), sum1.getUnit(), QuantityMeasurementApp.LengthUnit.FEET);
        double sum2Feet = QuantityMeasurementApp.convert(sum2.getValue(), sum2.getUnit(), QuantityMeasurementApp.LengthUnit.FEET);

        assertEquals(sum1Feet, sum2Feet, EPS);
    }

    @Test
    void testAddition_NullSecondOperand_Throws() {
        var a = new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.add(a, null));
    }

    @Test
    void testAddition_LargeValues() {
        var a = new QuantityMeasurementApp.QuantityLength(1e6, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(1e6, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(2e6, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }

    @Test
    void testAddition_SmallValues() {
        var a = new QuantityMeasurementApp.QuantityLength(0.001, QuantityMeasurementApp.LengthUnit.FEET);
        var b = new QuantityMeasurementApp.QuantityLength(0.002, QuantityMeasurementApp.LengthUnit.FEET);

        var sum = QuantityMeasurementApp.add(a, b);

        assertEquals(0.003, sum.getValue(), EPS);
        assertEquals(QuantityMeasurementApp.LengthUnit.FEET, sum.getUnit());
    }
}