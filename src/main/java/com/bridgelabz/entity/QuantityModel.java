package com.bridgelabz.entity;

import com.bridgelabz.IMeasurable;

/**
 * A generic POJO model class for representing a quantity with its associated unit of measurement.
 * Used internally within the service layer for performing operations on quantities,
 * such as conversion, comparison, addition, subtraction, and division.
 *
 * @param <U> the unit type that implements IMeasurable
 */
public class QuantityModel<U extends IMeasurable> {

    private double value;
    private U unit;

    public QuantityModel() {
    }

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public U getUnit() {
        return unit;
    }

    public void setUnit(U unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "QuantityModel(" + value + ", " + (unit != null ? unit.getUnitName() : "null") + ")";
    }
}
