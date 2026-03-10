package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private static final double EPS = 1e-6;

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        var result = a.subtract(b);

        assertEquals(5.0, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        var a = new Quantity<>(10.0, VolumeUnit.LITRE);
        var b = new Quantity<>(3.0, VolumeUnit.LITRE);

        var result = a.subtract(b);

        assertEquals(7.0, result.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, result.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(6.0, LengthUnit.INCH);

        var result = a.subtract(b);

        assertEquals(9.5, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        var a = new Quantity<>(120.0, LengthUnit.INCH);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        var result = a.subtract(b);

        assertEquals(60.0, result.getValue(), EPS);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(6.0, LengthUnit.INCH);

        var result = a.subtract(b, LengthUnit.FEET);

        assertEquals(9.5, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(6.0, LengthUnit.INCH);

        var result = a.subtract(b, LengthUnit.INCH);

        assertEquals(114.0, result.getValue(), EPS);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        var a = new Quantity<>(5.0, VolumeUnit.LITRE);
        var b = new Quantity<>(2.0, VolumeUnit.LITRE);

        var result = a.subtract(b, VolumeUnit.MILLILITRE);

        assertEquals(3000.0, result.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        var a = new Quantity<>(5.0, LengthUnit.FEET);
        var b = new Quantity<>(10.0, LengthUnit.FEET);

        var result = a.subtract(b);

        assertEquals(-5.0, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_ResultingInZero() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(120.0, LengthUnit.INCH);

        var result = a.subtract(b);

        assertEquals(0.0, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        var a = new Quantity<>(5.0, LengthUnit.FEET);
        var b = new Quantity<>(0.0, LengthUnit.INCH);

        var result = a.subtract(b);

        assertEquals(5.0, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        var a = new Quantity<>(5.0, LengthUnit.FEET);
        var b = new Quantity<>(-2.0, LengthUnit.FEET);

        var result = a.subtract(b);

        assertEquals(7.0, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtraction_NonCommutative() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        var result1 = a.subtract(b);
        var result2 = b.subtract(a);

        assertEquals(5.0, result1.getValue(), EPS);
        assertEquals(-5.0, result2.getValue(), EPS);
    }

    @Test
    void testSubtraction_WithLargeValues() {
        var a = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        var b = new Quantity<>(5e5, WeightUnit.KILOGRAM);

        var result = a.subtract(b);

        assertEquals(5e5, result.getValue(), EPS);
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
    }

    @Test
    void testSubtraction_NullOperand() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> a.subtract(null));
    }

    @Test
    void testSubtraction_NullTargetUnit() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> a.subtract(b, null));
    }

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(2.0, LengthUnit.FEET);

        assertEquals(5.0, a.divide(b), EPS);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        var a = new Quantity<>(10.0, VolumeUnit.LITRE);
        var b = new Quantity<>(5.0, VolumeUnit.LITRE);

        assertEquals(2.0, a.divide(b), EPS);
    }

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        var a = new Quantity<>(24.0, LengthUnit.INCH);
        var b = new Quantity<>(2.0, LengthUnit.FEET);

        assertEquals(1.0, a.divide(b), EPS);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        var a = new Quantity<>(2.0, WeightUnit.KILOGRAM);
        var b = new Quantity<>(2000.0, WeightUnit.GRAM);

        assertEquals(1.0, a.divide(b), EPS);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(2.0, LengthUnit.FEET);

        assertEquals(5.0, a.divide(b), EPS);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        var a = new Quantity<>(5.0, LengthUnit.FEET);
        var b = new Quantity<>(10.0, LengthUnit.FEET);

        assertEquals(0.5, a.divide(b), EPS);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(10.0, LengthUnit.FEET);

        assertEquals(1.0, a.divide(b), EPS);
    }

    @Test
    void testDivision_NonCommutative() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        assertEquals(2.0, a.divide(b), EPS);
        assertEquals(0.5, b.divide(a), EPS);
    }

    @Test
    void testDivision_ByZero() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(0.0, LengthUnit.FEET);

        assertThrows(ArithmeticException.class, () -> a.divide(b));
    }

    @Test
    void testDivision_WithLargeRatio() {
        var a = new Quantity<>(1e6, WeightUnit.KILOGRAM);
        var b = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertEquals(1e6, a.divide(b), EPS);
    }

    @Test
    void testDivision_WithSmallRatio() {
        var a = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        var b = new Quantity<>(1e6, WeightUnit.KILOGRAM);

        assertEquals(1e-6, a.divide(b), 1e-12);
    }

    @Test
    void testDivision_NullOperand() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> a.divide(null));
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        var length = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(2.0, LengthUnit.FEET));
        var weight = new Quantity<>(10.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM));
        var volume = new Quantity<>(5.0, VolumeUnit.LITRE).subtract(new Quantity<>(2.0, VolumeUnit.LITRE));

        assertEquals(8.0, length.getValue(), EPS);
        assertEquals(5.0, weight.getValue(), EPS);
        assertEquals(3.0, volume.getValue(), EPS);
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        assertEquals(2.0,
                new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(5.0, LengthUnit.FEET)), EPS);

        assertEquals(2.0,
                new Quantity<>(10.0, WeightUnit.KILOGRAM).divide(new Quantity<>(5.0, WeightUnit.KILOGRAM)), EPS);

        assertEquals(2.0,
                new Quantity<>(10.0, VolumeUnit.LITRE).divide(new Quantity<>(5.0, VolumeUnit.LITRE)), EPS);
    }

    @Test
    void testSubtraction_ChainedOperations() {
        var result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(1.0, LengthUnit.FEET));

        assertEquals(7.0, result.getValue(), EPS);
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testSubtractionAddition_Inverse() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(2.0, LengthUnit.FEET);

        var result = a.add(b).subtract(b);

        assertEquals(a.getValue(), result.getValue(), EPS);
        assertEquals(a.getUnit(), result.getUnit());
    }

    @Test
    void testSubtraction_Immutability() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        var result = a.subtract(b);

        assertEquals(10.0, a.getValue(), EPS);
        assertEquals(5.0, b.getValue(), EPS);
        assertEquals(5.0, result.getValue(), EPS);
    }

    @Test
    void testDivision_Immutability() {
        var a = new Quantity<>(10.0, LengthUnit.FEET);
        var b = new Quantity<>(5.0, LengthUnit.FEET);

        double ratio = a.divide(b);

        assertEquals(10.0, a.getValue(), EPS);
        assertEquals(5.0, b.getValue(), EPS);
        assertEquals(2.0, ratio, EPS);
    }
}