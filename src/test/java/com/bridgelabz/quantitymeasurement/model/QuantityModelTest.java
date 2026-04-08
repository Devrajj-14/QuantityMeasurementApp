package com.bridgelabz.quantitymeasurement.model;

import com.bridgelabz.quantitymeasurement.unit.LengthUnit;
import com.bridgelabz.quantitymeasurement.unit.TemperatureUnit;
import com.bridgelabz.quantitymeasurement.unit.VolumeUnit;
import com.bridgelabz.quantitymeasurement.unit.WeightUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pure unit tests for the Quantity model.
 * Verifies that existing quantity logic (compare, convert, add, subtract, divide)
 * is fully preserved after the Spring Boot migration.
 */
class QuantityModelTest {

    private static final double EPS = 1e-6;

    // ===================== Equality =====================

    @Test
    void testEquality_feetAndInch() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inch = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(feet, inch);
    }

    @Test
    void testEquality_kilogramAndGram() {
        Quantity<WeightUnit> kg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> g = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertEquals(kg, g);
    }

    @Test
    void testEquality_litreAndMillilitre() {
        Quantity<VolumeUnit> l = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertEquals(l, ml);
    }

    @Test
    void testEquality_celsiusAndFahrenheit() {
        Quantity<TemperatureUnit> c = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> f = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(c, f);
    }

    // ===================== Conversion =====================

    @Test
    void testConversion_feetToInch() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> result = feet.convertTo(LengthUnit.INCH);
        assertEquals(12.0, result.getValue(), EPS);
    }

    @Test
    void testConversion_celsiusToFahrenheit() {
        Quantity<TemperatureUnit> celsius = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> result = celsius.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(212.0, result.getValue(), EPS);
    }

    @Test
    void testConversion_celsiusToKelvin() {
        Quantity<TemperatureUnit> celsius = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> result = celsius.convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15, result.getValue(), EPS);
    }

    // ===================== Addition =====================

    @Test
    void testAddition_sameFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> sum = q1.add(q2);
        assertEquals(15.0, sum.getValue(), EPS);
    }

    @Test
    void testAddition_feetAndInch() {
        Quantity<LengthUnit> q1 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        Quantity<LengthUnit> sum = q1.add(q2, LengthUnit.INCH);
        assertEquals(36.0, sum.getValue(), EPS);
    }

    // ===================== Subtraction =====================

    @Test
    void testSubtraction_sameFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> diff = q1.subtract(q2);
        assertEquals(5.0, diff.getValue(), EPS);
    }

    // ===================== Division =====================

    @Test
    void testDivision_sameFeet() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        assertEquals(2.0, q1.divide(q2), EPS);
    }

    @Test
    void testDivision_byZero_shouldThrow() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.0, LengthUnit.FEET);
        assertThrows(ArithmeticException.class, () -> q1.divide(q2));
    }

    // ===================== Temperature Arithmetic Rejected =====================

    @Test
    void testTemperature_arithmeticRejected() {
        Quantity<TemperatureUnit> q1 = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> q2 = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> q1.add(q2));
        assertThrows(UnsupportedOperationException.class, () -> q1.subtract(q2));
        assertThrows(UnsupportedOperationException.class, () -> q1.divide(q2));
    }

    // ===================== Null / Invalid Input =====================

    @Test
    void testNullUnit_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    void testNonFiniteValue_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    // ===================== Immutability =====================

    @Test
    void testImmutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        a.add(b);
        a.subtract(b);
        a.divide(b);
        assertEquals(10.0, a.getValue(), EPS);
        assertEquals(5.0, b.getValue(), EPS);
    }
}
