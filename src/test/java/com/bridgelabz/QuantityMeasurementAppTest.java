package com.bridgelabz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

    private static final double EPS = 1e-6;

    @Test
    void testEquality_LitreToLitre_SameValue() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(2.0, VolumeUnit.LITRE);

        assertNotEquals(a, b);
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        var a = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        var b = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        var a = new Quantity<>(1.0, VolumeUnit.GALLON);
        var b = new Quantity<>(3.78541, VolumeUnit.LITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        var volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        var length = new Quantity<>(1.0, LengthUnit.FEET);

        assertNotEquals(volume, length);
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        var volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        var weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);

        assertNotEquals(volume, weight);
    }

    @Test
    void testEquality_NullComparison() {
        var volume = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertNotEquals(null, volume);
    }

    @Test
    void testEquality_SameReference() {
        var volume = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertEquals(volume, volume);
    }

    @Test
    void testEquality_ZeroValue() {
        var a = new Quantity<>(0.0, VolumeUnit.LITRE);
        var b = new Quantity<>(0.0, VolumeUnit.MILLILITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_NegativeVolume() {
        var a = new Quantity<>(-1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(-1000.0, VolumeUnit.MILLILITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_LargeVolumeValue() {
        var a = new Quantity<>(1000000.0, VolumeUnit.MILLILITRE);
        var b = new Quantity<>(1000.0, VolumeUnit.LITRE);

        assertEquals(a, b);
    }

    @Test
    void testEquality_SmallVolumeValue() {
        var a = new Quantity<>(0.001, VolumeUnit.LITRE);
        var b = new Quantity<>(1.0, VolumeUnit.MILLILITRE);

        assertEquals(a, b);
    }

    @Test
    void testConversion_LitreToMillilitre() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);

        var converted = a.convertTo(VolumeUnit.MILLILITRE);

        assertEquals(1000.0, converted.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, converted.getUnit());
    }

    @Test
    void testConversion_MillilitreToLitre() {
        var a = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        var converted = a.convertTo(VolumeUnit.LITRE);

        assertEquals(1.0, converted.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, converted.getUnit());
    }

    @Test
    void testConversion_GallonToLitre() {
        var a = new Quantity<>(1.0, VolumeUnit.GALLON);

        var converted = a.convertTo(VolumeUnit.LITRE);

        assertEquals(3.78541, converted.getValue(), 1e-5);
        assertEquals(VolumeUnit.LITRE, converted.getUnit());
    }

    @Test
    void testConversion_LitreToGallon() {
        var a = new Quantity<>(3.78541, VolumeUnit.LITRE);

        var converted = a.convertTo(VolumeUnit.GALLON);

        assertEquals(1.0, converted.getValue(), 1e-5);
        assertEquals(VolumeUnit.GALLON, converted.getUnit());
    }

    @Test
    void testConversion_MillilitreToGallon() {
        var a = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        var converted = a.convertTo(VolumeUnit.GALLON);

        assertEquals(0.264172, converted.getValue(), 1e-3);
        assertEquals(VolumeUnit.GALLON, converted.getUnit());
    }

    @Test
    void testConversion_SameUnit() {
        var a = new Quantity<>(5.0, VolumeUnit.LITRE);

        var converted = a.convertTo(VolumeUnit.LITRE);

        assertEquals(5.0, converted.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, converted.getUnit());
    }

    @Test
    void testConversion_ZeroValue() {
        var a = new Quantity<>(0.0, VolumeUnit.LITRE);

        var converted = a.convertTo(VolumeUnit.MILLILITRE);

        assertEquals(0.0, converted.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, converted.getUnit());
    }

    @Test
    void testConversion_NegativeValue() {
        var a = new Quantity<>(-1.0, VolumeUnit.LITRE);

        var converted = a.convertTo(VolumeUnit.MILLILITRE);

        assertEquals(-1000.0, converted.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, converted.getUnit());
    }

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(2.0, VolumeUnit.LITRE);

        var sum = a.add(b);

        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        var a = new Quantity<>(500.0, VolumeUnit.MILLILITRE);
        var b = new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        var sum = a.add(b);

        assertEquals(1000.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        var sum = a.add(b);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        var a = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        var b = new Quantity<>(1.0, VolumeUnit.LITRE);

        var sum = a.add(b);

        assertEquals(2000.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, sum.getUnit());
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        var a = new Quantity<>(1.0, VolumeUnit.GALLON);
        var b = new Quantity<>(3.78541, VolumeUnit.LITRE);

        var sum = a.add(b);

        assertEquals(2.0, sum.getValue(), 1e-5);
        assertEquals(VolumeUnit.GALLON, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        var sum = a.add(b, VolumeUnit.LITRE);

        assertEquals(2.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        var sum = a.add(b, VolumeUnit.MILLILITRE);

        assertEquals(2000.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.MILLILITRE, sum.getUnit());
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        var a = new Quantity<>(3.78541, VolumeUnit.LITRE);
        var b = new Quantity<>(3.78541, VolumeUnit.LITRE);

        var sum = a.add(b, VolumeUnit.GALLON);

        assertEquals(2.0, sum.getValue(), 1e-5);
        assertEquals(VolumeUnit.GALLON, sum.getUnit());
    }

    @Test
    void testAddition_Commutativity() {
        var a = new Quantity<>(1.0, VolumeUnit.LITRE);
        var b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);

        var sum1 = a.add(b, VolumeUnit.GALLON);
        var sum2 = b.add(a, VolumeUnit.GALLON);

        assertEquals(sum1.getValue(), sum2.getValue(), 1e-6);
        assertEquals(VolumeUnit.GALLON, sum1.getUnit());
        assertEquals(VolumeUnit.GALLON, sum2.getUnit());
    }

    @Test
    void testAddition_WithZero() {
        var a = new Quantity<>(5.0, VolumeUnit.LITRE);
        var b = new Quantity<>(0.0, VolumeUnit.MILLILITRE);

        var sum = a.add(b);

        assertEquals(5.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testAddition_NegativeValues() {
        var a = new Quantity<>(5.0, VolumeUnit.LITRE);
        var b = new Quantity<>(-2000.0, VolumeUnit.MILLILITRE);

        var sum = a.add(b);

        assertEquals(3.0, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testAddition_LargeValues() {
        var a = new Quantity<>(1e6, VolumeUnit.LITRE);
        var b = new Quantity<>(1e6, VolumeUnit.LITRE);

        var sum = a.add(b);

        assertEquals(2e6, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testAddition_SmallValues() {
        var a = new Quantity<>(0.001, VolumeUnit.LITRE);
        var b = new Quantity<>(0.002, VolumeUnit.LITRE);

        var sum = a.add(b);

        assertEquals(0.003, sum.getValue(), EPS);
        assertEquals(VolumeUnit.LITRE, sum.getUnit());
    }

    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0, VolumeUnit.LITRE.getConversionFactor(), EPS);
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001, VolumeUnit.MILLILITRE.getConversionFactor(), EPS);
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541, VolumeUnit.GALLON.getConversionFactor(), EPS);
    }

    @Test
    void testConvertToBaseUnit_LitreToLitre() {
        assertEquals(5.0, VolumeUnit.LITRE.convertToBaseUnit(5.0), EPS);
    }

    @Test
    void testConvertToBaseUnit_MillilitreToLitre() {
        assertEquals(1.0, VolumeUnit.MILLILITRE.convertToBaseUnit(1000.0), EPS);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541, VolumeUnit.GALLON.convertToBaseUnit(1.0), 1e-5);
    }

    @Test
    void testConvertFromBaseUnit_LitreToLitre() {
        assertEquals(2.0, VolumeUnit.LITRE.convertFromBaseUnit(2.0), EPS);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0, VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0), EPS);
    }

    @Test
    void testConvertFromBaseUnit_LitreToGallon() {
        assertEquals(1.0, VolumeUnit.GALLON.convertFromBaseUnit(3.78541), 1e-5);
    }

    @Test
    void testConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    void testConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, VolumeUnit.LITRE));
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, VolumeUnit.LITRE));
    }
}