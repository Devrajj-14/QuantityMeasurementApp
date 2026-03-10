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

    public static <U extends IMeasurable> void demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2) {
        System.out.println("Subtracting " + q2 + " from " + q1 + " : " + q1.subtract(q2));
    }

    public static <U extends IMeasurable> void demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        System.out.println("Subtracting " + q2 + " from " + q1 + " in " + targetUnit.getUnitName() + " : " + q1.subtract(q2, targetUnit));
    }

    public static <U extends IMeasurable> void demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
        System.out.println("Dividing " + q1 + " by " + q2 + " : " + q1.divide(q2));
    }

    public static void main(String[] args) {
        Quantity<LengthUnit> length1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> length2 = new Quantity<>(6.0, LengthUnit.INCH);

        Quantity<WeightUnit> weight1 = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> weight2 = new Quantity<>(5000.0, WeightUnit.GRAM);

        Quantity<VolumeUnit> volume1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> volume2 = new Quantity<>(500.0, VolumeUnit.MILLILITRE);

        System.out.println("Length Operations:");
        demonstrateSubtraction(length1, length2);
        demonstrateSubtraction(length1, length2, LengthUnit.INCH);
        demonstrateDivision(new Quantity<>(24.0, LengthUnit.INCH), new Quantity<>(2.0, LengthUnit.FEET));

        System.out.println();

        System.out.println("Weight Operations:");
        demonstrateSubtraction(weight1, weight2);
        demonstrateSubtraction(weight1, weight2, WeightUnit.GRAM);
        demonstrateDivision(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5.0, WeightUnit.KILOGRAM));

        System.out.println();

        System.out.println("Volume Operations:");
        demonstrateSubtraction(volume1, volume2);
        demonstrateSubtraction(volume1, new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        demonstrateDivision(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
    }
}