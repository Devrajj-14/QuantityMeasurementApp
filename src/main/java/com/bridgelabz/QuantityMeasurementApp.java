package com.bridgelabz;

public class QuantityMeasurementApp {

    public static <U extends IMeasurable> void demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {
        System.out.println("Comparing " + q1 + " and " + q2 + " : " + q1.equals(q2));
    }

    public static <U extends IMeasurable> void demonstrateConversion(Quantity<U> quantity, U targetUnit) {
        System.out.println("Converting " + quantity + " to " + targetUnit.getUnitName() + " : " + quantity.convertTo(targetUnit));
    }

    public static <U extends IMeasurable> void demonstrateAddition(Quantity<U> q1, Quantity<U> q2) {
        System.out.println("Adding " + q1 + " and " + q2 + " : " + q1.add(q2));
    }

    public static <U extends IMeasurable> void demonstrateAddition(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        System.out.println("Adding " + q1 + " and " + q2 + " in " + targetUnit.getUnitName() + " : " + q1.add(q2, targetUnit));
    }

    public static void main(String[] args) {

        Quantity<LengthUnit> length1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> length2 = new Quantity<>(12.0, LengthUnit.INCH);

        Quantity<WeightUnit> weight1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> weight2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        Quantity<VolumeUnit> volume1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> volume2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> volume3 = new Quantity<>(1.0, VolumeUnit.GALLON);

        System.out.println("Length Operations:");
        demonstrateEquality(length1, length2);
        demonstrateConversion(length1, LengthUnit.INCH);
        demonstrateAddition(length1, length2, LengthUnit.FEET);

        System.out.println();

        System.out.println("Weight Operations:");
        demonstrateEquality(weight1, weight2);
        demonstrateConversion(weight1, WeightUnit.GRAM);
        demonstrateAddition(weight1, weight2, WeightUnit.KILOGRAM);

        System.out.println();

        System.out.println("Volume Operations:");
        demonstrateEquality(volume1, volume2);
        demonstrateEquality(volume3, new Quantity<>(3.78541, VolumeUnit.LITRE));
        demonstrateConversion(volume1, VolumeUnit.MILLILITRE);
        demonstrateConversion(volume3, VolumeUnit.LITRE);
        demonstrateAddition(volume1, volume2, VolumeUnit.LITRE);
        demonstrateAddition(volume1, volume3, VolumeUnit.GALLON);
    }
}