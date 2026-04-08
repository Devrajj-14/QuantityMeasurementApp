package com.bridgelabz.quantitymeasurement;

import com.bridgelabz.quantitymeasurement.controller.QuantityMeasurementController;
import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.bridgelabz.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.bridgelabz.quantitymeasurement.unit.*;
import com.bridgelabz.quantitymeasurement.util.ApplicationConfig;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for the Quantity Measurement Application.
 * Wires repository -> service -> controller based on configuration.
 */
public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private final QuantityMeasurementController controller;
    private ConnectionPool connectionPool;

    public QuantityMeasurementApp() {
        ApplicationConfig config = new ApplicationConfig();
        String repoType = config.getRepositoryType();
        logger.info("Repository type: {}", repoType);

        // Wire: repository -> service -> controller
        IQuantityMeasurementRepository repository;
        if ("database".equalsIgnoreCase(repoType)) {
            connectionPool = new ConnectionPool(
                    config.getDbUrl(), config.getDbUsername(),
                    config.getDbPassword(), config.getPoolSize()
            );
            connectionPool.initialize();
            repository = new QuantityMeasurementDatabaseRepository(connectionPool);
            logger.info("Using DATABASE repository (H2 JDBC)");
        } else {
            repository = new QuantityMeasurementCacheRepository();
            logger.info("Using CACHE repository (in-memory)");
        }

        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
        this.controller = new QuantityMeasurementController(service);
    }

    /**
     * Run demo operations showcasing all measurement features.
     */
    public void runDemo() {
        System.out.println("============================================");
        System.out.println("   Quantity Measurement App - UC16 Demo");
        System.out.println("============================================\n");

        // --- Length Operations ---
        System.out.println("--- Length Operations ---");
        Quantity<LengthUnit> feet12 = new Quantity<>(12.0, LengthUnit.FEET);
        Quantity<LengthUnit> inch144 = new Quantity<>(144.0, LengthUnit.INCH);
        controller.performComparison(feet12, inch144);

        controller.performConversion(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.INCH);
        controller.performConversion(new Quantity<>(1.0, LengthUnit.YARDS), LengthUnit.FEET);

        controller.performAddition(
                new Quantity<>(2.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH),
                LengthUnit.INCH
        );

        controller.performSubtraction(
                new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(24.0, LengthUnit.INCH),
                LengthUnit.FEET
        );

        controller.performDivision(
                new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(5.0, LengthUnit.FEET)
        );

        // --- Weight Operations ---
        System.out.println("\n--- Weight Operations ---");
        controller.performComparison(
                new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM)
        );

        controller.performAddition(
                new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(500.0, WeightUnit.GRAM),
                WeightUnit.GRAM
        );

        // --- Volume Operations ---
        System.out.println("\n--- Volume Operations ---");
        controller.performConversion(new Quantity<>(1.0, VolumeUnit.GALLON), VolumeUnit.LITRE);
        controller.performAddition(
                new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(500.0, VolumeUnit.MILLILITRE),
                VolumeUnit.MILLILITRE
        );

        // --- Temperature Operations ---
        System.out.println("\n--- Temperature Operations ---");
        controller.performComparison(
                new Quantity<>(0.0, TemperatureUnit.CELSIUS),
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)
        );
        controller.performConversion(
                new Quantity<>(100.0, TemperatureUnit.CELSIUS),
                TemperatureUnit.FAHRENHEIT
        );
        controller.performConversion(
                new Quantity<>(0.0, TemperatureUnit.CELSIUS),
                TemperatureUnit.KELVIN
        );

        // --- Show history & count ---
        controller.showCount();
        controller.showHistory();
    }

    /**
     * Cleanup: release database connections and resources.
     */
    public void shutdown() {
        logger.info("Shutting down application...");
        controller.releaseResources();
        logger.info("Application shutdown complete.");
    }

    // ---------------------------------------------------------------
    // Legacy static demo methods (kept for backward compatibility)
    // ---------------------------------------------------------------

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

    // ---------------------------------------------------------------
    // Main entry point
    // ---------------------------------------------------------------

    public static void main(String[] args) {
        QuantityMeasurementApp app = new QuantityMeasurementApp();
        try {
            app.runDemo();
        } finally {
            app.shutdown();
        }
    }
}