package com.bridgelabz.entity;

/**
 * Data Transfer Object (DTO) for holding quantity measurement input data.
 * Contains value and corresponding unit with its measurement type.
 * Uses inner IMeasurableUnit interface and enums for self-contained representation,
 * decoupled from the internal domain units (IMeasurable).
 */
public class QuantityDTO {

    private double value;
    private IMeasurableUnit unit;

    /**
     * Interface representing measurable units for quantity measurements within the DTO layer.
     * This is different from the IMeasurable interface used in the core domain/service layer.
     */
    public interface IMeasurableUnit {
        String getUnitName();
        String getMeasurementType();
    }

    /**
     * Length units for the DTO layer.
     */
    public enum LengthUnit implements IMeasurableUnit {
        FEET, INCH, YARDS, CENTIMETERS;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "LENGTH";
        }
    }

    /**
     * Weight units for the DTO layer.
     */
    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM, GRAM, POUND;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "WEIGHT";
        }
    }

    /**
     * Volume units for the DTO layer.
     */
    public enum VolumeUnit implements IMeasurableUnit {
        LITRE, MILLILITRE, GALLON;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "VOLUME";
        }
    }

    /**
     * Temperature units for the DTO layer.
     */
    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT, KELVIN;

        @Override
        public String getUnitName() {
            return name();
        }

        @Override
        public String getMeasurementType() {
            return "TEMPERATURE";
        }
    }

    public QuantityDTO() {
    }

    public QuantityDTO(double value, IMeasurableUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public IMeasurableUnit getUnit() {
        return unit;
    }

    public void setUnit(IMeasurableUnit unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "QuantityDTO(" + value + ", " + (unit != null ? unit.getUnitName() : "null") + ")";
    }
}
