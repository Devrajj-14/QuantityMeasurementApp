package com.bridgelabz.quantitymeasurement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point for the Quantity Measurement REST API.
 * Replaces the old standalone QuantityMeasurementApp (now in legacy package).
 */
@SpringBootApplication
public class QuantityMeasurementApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApplication.class, args);
    }
}
