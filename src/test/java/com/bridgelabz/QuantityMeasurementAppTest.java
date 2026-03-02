package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    private static final double EPS = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        double out = QuantityMeasurementApp.convert(1.0,
                QuantityMeasurementApp.LengthUnit.FEET,
                QuantityMeasurementApp.LengthUnit.INCH);
        assertEquals(12.0, out, EPS);
    }

    @Test
    void testConversion_InchesToFeet() {
        double out = QuantityMeasurementApp.convert(24.0,
                QuantityMeasurementApp.LengthUnit.INCH,
                QuantityMeasurementApp.LengthUnit.FEET);
        assertEquals(2.0, out, EPS);
    }

    @Test
    void testConversion_YardsToFeet() {
        double out = QuantityMeasurementApp.convert(3.0,
                QuantityMeasurementApp.LengthUnit.YARDS,
                QuantityMeasurementApp.LengthUnit.FEET);
        assertEquals(9.0, out, EPS);
    }

    @Test
    void testConversion_YardsToInches() {
        double out = QuantityMeasurementApp.convert(1.0,
                QuantityMeasurementApp.LengthUnit.YARDS,
                QuantityMeasurementApp.LengthUnit.INCH);
        assertEquals(36.0, out, EPS);
    }

    @Test
    void testConversion_InchesToYards() {
        double out = QuantityMeasurementApp.convert(72.0,
                QuantityMeasurementApp.LengthUnit.INCH,
                QuantityMeasurementApp.LengthUnit.YARDS);
        assertEquals(2.0, out, EPS);
    }

    @Test
    void testConversion_CentimetersToInches() {
        double out = QuantityMeasurementApp.convert(2.54,
                QuantityMeasurementApp.LengthUnit.CENTIMETERS,
                QuantityMeasurementApp.LengthUnit.INCH);
        assertEquals(1.0, out, 1e-5); // slightly looser due to factor rounding
    }

    @Test
    void testConversion_FeetToYards() {
        double out = QuantityMeasurementApp.convert(6.0,
                QuantityMeasurementApp.LengthUnit.FEET,
                QuantityMeasurementApp.LengthUnit.YARDS);
        assertEquals(2.0, out, EPS);
    }

    @Test
    void testConversion_SameUnit_ReturnsSameValue() {
        double out = QuantityMeasurementApp.convert(5.0,
                QuantityMeasurementApp.LengthUnit.FEET,
                QuantityMeasurementApp.LengthUnit.FEET);
        assertEquals(5.0, out, EPS);
    }

    @Test
    void testConversion_ZeroValue() {
        double out = QuantityMeasurementApp.convert(0.0,
                QuantityMeasurementApp.LengthUnit.FEET,
                QuantityMeasurementApp.LengthUnit.INCH);
        assertEquals(0.0, out, EPS);
    }

    @Test
    void testConversion_NegativeValue() {
        double out = QuantityMeasurementApp.convert(-1.0,
                QuantityMeasurementApp.LengthUnit.FEET,
                QuantityMeasurementApp.LengthUnit.INCH);
        assertEquals(-12.0, out, EPS);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double v = 7.25;
        double toInch = QuantityMeasurementApp.convert(v,
                QuantityMeasurementApp.LengthUnit.FEET,
                QuantityMeasurementApp.LengthUnit.INCH);
        double backToFeet = QuantityMeasurementApp.convert(toInch,
                QuantityMeasurementApp.LengthUnit.INCH,
                QuantityMeasurementApp.LengthUnit.FEET);

        assertEquals(v, backToFeet, EPS);
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(1.0, null, QuantityMeasurementApp.LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(1.0, QuantityMeasurementApp.LengthUnit.FEET, null));
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(Double.NaN,
                        QuantityMeasurementApp.LengthUnit.FEET,
                        QuantityMeasurementApp.LengthUnit.INCH));

        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(Double.POSITIVE_INFINITY,
                        QuantityMeasurementApp.LengthUnit.FEET,
                        QuantityMeasurementApp.LengthUnit.INCH));

        assertThrows(IllegalArgumentException.class,
                () -> QuantityMeasurementApp.convert(Double.NEGATIVE_INFINITY,
                        QuantityMeasurementApp.LengthUnit.FEET,
                        QuantityMeasurementApp.LengthUnit.INCH));
    }

    @Test
    void testInstanceConvertTo_ReturnsNewQuantity() {
        QuantityMeasurementApp.QuantityLength feet =
                new QuantityMeasurementApp.QuantityLength(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.QuantityLength inch = feet.convertTo(QuantityMeasurementApp.LengthUnit.INCH);

        // 1 ft -> 12 inch
        assertEquals(12.0,
                QuantityMeasurementApp.convert(1.0,
                        QuantityMeasurementApp.LengthUnit.FEET,
                        QuantityMeasurementApp.LengthUnit.INCH),
                EPS);

        // object equality across units should still hold (UC3/UC4)
        assertTrue(feet.equals(inch));
    }
}