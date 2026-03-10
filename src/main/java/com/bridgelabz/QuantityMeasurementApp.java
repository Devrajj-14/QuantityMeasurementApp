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
        Quantity<TemperatureUnit> temp1 = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> temp2 = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        Quantity<TemperatureUnit> temp3 = new Quantity<>(273.15, TemperatureUnit.KELVIN);

        System.out.println("Temperature Operations:");
        demonstrateEquality(temp1, temp2);
        demonstrateEquality(temp1, temp3);
        demonstrateConversion(new Quantity<>(100.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT);
        demonstrateConversion(new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT), TemperatureUnit.CELSIUS);
        demonstrateConversion(new Quantity<>(0.0, TemperatureUnit.CELSIUS), TemperatureUnit.KELVIN);

        try {
            demonstrateAddition(
                    new Quantity<>(100.0, TemperatureUnit.CELSIUS),
                    new Quantity<>(50.0, TemperatureUnit.CELSIUS)
            );
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        try {
            demonstrateSubtraction(
                    new Quantity<>(100.0, TemperatureUnit.CELSIUS),
                    new Quantity<>(50.0, TemperatureUnit.CELSIUS)
            );
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }

        try {
            demonstrateDivision(
                    new Quantity<>(100.0, TemperatureUnit.CELSIUS),
                    new Quantity<>(50.0, TemperatureUnit.CELSIUS)
            );
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}