package com.bridgelabz;

import java.util.function.Function;

public enum TemperatureUnit implements IMeasurable {
    CELSIUS(
            1.0,
            celsius -> celsius,
            celsius -> celsius,
            () -> false
    ),
    FAHRENHEIT(
            1.0,
            fahrenheit -> (fahrenheit - 32.0) * 5.0 / 9.0,
            celsius -> (celsius * 9.0 / 5.0) + 32.0,
            () -> false
    ),
    KELVIN(
            1.0,
            kelvin -> kelvin - 273.15,
            celsius -> celsius + 273.15,
            () -> false
    );

    private final double conversionFactor;
    private final Function<Double, Double> toBaseConverter;
    private final Function<Double, Double> fromBaseConverter;
    private final SupportsArithmetic supportsArithmetic;

    TemperatureUnit(
            double conversionFactor,
            Function<Double, Double> toBaseConverter,
            Function<Double, Double> fromBaseConverter,
            SupportsArithmetic supportsArithmetic
    ) {
        this.conversionFactor = conversionFactor;
        this.toBaseConverter = toBaseConverter;
        this.fromBaseConverter = fromBaseConverter;
        this.supportsArithmetic = supportsArithmetic;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toBaseConverter.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromBaseConverter.apply(baseValue);
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException(
                "Temperature does not support " + operation + " operation in a meaningful way"
        );
    }
}