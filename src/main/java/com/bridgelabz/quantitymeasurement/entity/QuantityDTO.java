package com.bridgelabz.quantitymeasurement.entity;

/**
 * Data Transfer Object for quantity input/output.
 * Used to pass quantity data between layers without exposing internal model details.
 */
public class QuantityDTO {

    private double value;
    private String unitName;
    private String measurementType;

    public QuantityDTO() {
    }

    public QuantityDTO(double value, String unitName, String measurementType) {
        this.value = value;
        this.unitName = unitName;
        this.measurementType = measurementType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public void setMeasurementType(String measurementType) {
        this.measurementType = measurementType;
    }

    @Override
    public String toString() {
        return "QuantityDTO{" +
                "value=" + value +
                ", unitName='" + unitName + '\'' +
                ", measurementType='" + measurementType + '\'' +
                '}';
    }
}
