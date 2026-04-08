package com.bridgelabz.quantitymeasurement.integration;

import com.bridgelabz.quantitymeasurement.controller.QuantityMeasurementController;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.bridgelabz.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.bridgelabz.quantitymeasurement.unit.LengthUnit;
import com.bridgelabz.quantitymeasurement.unit.WeightUnit;
import com.bridgelabz.quantitymeasurement.unit.VolumeUnit;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end integration test: config -> repo -> service -> controller with real H2.
 */
class QuantityMeasurementIntegrationTest {

    private static ConnectionPool connectionPool;
    private QuantityMeasurementController controller;
    private IQuantityMeasurementService service;

    @BeforeAll
    static void setupPool() {
        connectionPool = new ConnectionPool(
                "jdbc:h2:mem:integrationdb;DB_CLOSE_DELAY=-1",
                "sa", "", 3
        );
        connectionPool.initialize();
    }

    @BeforeEach
    void setup() {
        var repository = new QuantityMeasurementDatabaseRepository(connectionPool);
        repository.deleteAll();
        service = new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    @AfterAll
    static void tearDown() {
        connectionPool.closeAll();
    }

    @Test
    void testFullFlow_comparison_persisted() {
        Quantity<LengthUnit> q1 = new Quantity<>(12.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(144.0, LengthUnit.INCH);

        boolean result = controller.performComparison(q1, q2);

        assertTrue(result);
        assertEquals(1, service.getMeasurementCount());

        List<QuantityMeasurementEntity> records = service.getMeasurementsByOperation("COMPARE");
        assertEquals(1, records.size());
        assertEquals("LENGTH", records.get(0).getMeasurementType());
    }

    @Test
    void testFullFlow_conversion_persisted() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = controller.performConversion(q, LengthUnit.INCH);

        assertEquals(12.0, result.getValue(), 1e-6);
        assertEquals(1, service.getMeasurementCount());

        List<QuantityMeasurementEntity> records = service.getMeasurementsByOperation("CONVERT");
        assertEquals(1, records.size());
    }

    @Test
    void testFullFlow_addition_persisted() {
        Quantity<LengthUnit> q1 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);

        Quantity<LengthUnit> result = controller.performAddition(q1, q2, LengthUnit.INCH);

        assertEquals(36.0, result.getValue(), 1e-6);
        assertEquals(1, service.getMeasurementCount());
    }

    @Test
    void testFullFlow_multipleOperations_correctCount() {
        controller.performComparison(
                new Quantity<>(1.0, WeightUnit.KILOGRAM),
                new Quantity<>(1000.0, WeightUnit.GRAM)
        );
        controller.performAddition(
                new Quantity<>(1.0, VolumeUnit.LITRE),
                new Quantity<>(500.0, VolumeUnit.MILLILITRE),
                VolumeUnit.LITRE
        );
        controller.performDivision(
                new Quantity<>(10.0, LengthUnit.FEET),
                new Quantity<>(5.0, LengthUnit.FEET)
        );

        assertEquals(3, service.getMeasurementCount());
    }

    @Test
    void testFullFlow_clearHistory() {
        controller.performComparison(
                new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH)
        );
        assertEquals(1, service.getMeasurementCount());

        controller.clearHistory();

        assertEquals(0, service.getMeasurementCount());
    }

    @Test
    void testCacheRepository_worksIdentically() {
        var cacheRepo = new QuantityMeasurementCacheRepository();
        var cacheService = new QuantityMeasurementServiceImpl(cacheRepo);
        var cacheController = new QuantityMeasurementController(cacheService);

        cacheController.performComparison(
                new Quantity<>(1.0, LengthUnit.FEET),
                new Quantity<>(12.0, LengthUnit.INCH)
        );

        assertEquals(1, cacheService.getMeasurementCount());
        cacheController.clearHistory();
        assertEquals(0, cacheService.getMeasurementCount());
    }

    @Test
    void testOldQuantityLogic_stillWorks() {
        // Verify core model behavior is untouched
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(feet, inches);

        Quantity<LengthUnit> sum = feet.add(inches);
        assertEquals(2.0, sum.getValue(), 1e-6);

        Quantity<WeightUnit> kg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> gram = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertEquals(kg, gram);
    }
}
