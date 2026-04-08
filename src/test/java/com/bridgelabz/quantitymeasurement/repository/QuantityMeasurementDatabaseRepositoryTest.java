package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for QuantityMeasurementDatabaseRepository using in-memory H2.
 */
class QuantityMeasurementDatabaseRepositoryTest {

    private static ConnectionPool connectionPool;
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeAll
    static void setupPool() {
        connectionPool = new ConnectionPool(
                "jdbc:h2:mem:testrepo;DB_CLOSE_DELAY=-1",
                "sa", "", 3
        );
        connectionPool.initialize();
    }

    @BeforeEach
    void setup() {
        repository = new QuantityMeasurementDatabaseRepository(connectionPool);
        repository.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        connectionPool.closeAll();
    }

    @Test
    void testSave_shouldPersistEntityAndAssignId() {
        QuantityMeasurementEntity entity = createEntity("ADD", "LENGTH", 10.0, "FEET", 5.0, "FEET", 15.0, "FEET");

        repository.save(entity);

        assertTrue(entity.getId() > 0, "Entity should have a generated ID");
    }

    @Test
    void testFindAll_shouldReturnAllSavedEntities() {
        repository.save(createEntity("ADD", "LENGTH", 10.0, "FEET", 5.0, "FEET", 15.0, "FEET"));
        repository.save(createEntity("SUBTRACT", "WEIGHT", 10.0, "KG", 5.0, "KG", 5.0, "KG"));

        List<QuantityMeasurementEntity> results = repository.findAll();

        assertEquals(2, results.size());
    }

    @Test
    void testFindByOperationType_shouldFilterCorrectly() {
        repository.save(createEntity("ADD", "LENGTH", 10.0, "FEET", 5.0, "FEET", 15.0, "FEET"));
        repository.save(createEntity("SUBTRACT", "LENGTH", 10.0, "FEET", 5.0, "FEET", 5.0, "FEET"));
        repository.save(createEntity("ADD", "WEIGHT", 5.0, "KG", 3.0, "KG", 8.0, "KG"));

        List<QuantityMeasurementEntity> addResults = repository.findByOperationType("ADD");
        List<QuantityMeasurementEntity> subResults = repository.findByOperationType("SUBTRACT");

        assertEquals(2, addResults.size());
        assertEquals(1, subResults.size());
    }

    @Test
    void testFindByMeasurementType_shouldFilterCorrectly() {
        repository.save(createEntity("ADD", "LENGTH", 10.0, "FEET", 5.0, "FEET", 15.0, "FEET"));
        repository.save(createEntity("ADD", "WEIGHT", 5.0, "KG", 3.0, "KG", 8.0, "KG"));

        List<QuantityMeasurementEntity> lengthResults = repository.findByMeasurementType("LENGTH");
        List<QuantityMeasurementEntity> weightResults = repository.findByMeasurementType("WEIGHT");

        assertEquals(1, lengthResults.size());
        assertEquals(1, weightResults.size());
    }

    @Test
    void testCount_shouldReturnCorrectCount() {
        assertEquals(0, repository.count());

        repository.save(createEntity("ADD", "LENGTH", 10.0, "FEET", 5.0, "FEET", 15.0, "FEET"));
        repository.save(createEntity("SUBTRACT", "LENGTH", 10.0, "FEET", 5.0, "FEET", 5.0, "FEET"));

        assertEquals(2, repository.count());
    }

    @Test
    void testDeleteAll_shouldRemoveAllRecords() {
        repository.save(createEntity("ADD", "LENGTH", 10.0, "FEET", 5.0, "FEET", 15.0, "FEET"));
        repository.save(createEntity("SUBTRACT", "LENGTH", 10.0, "FEET", 5.0, "FEET", 5.0, "FEET"));
        assertEquals(2, repository.count());

        repository.deleteAll();

        assertEquals(0, repository.count());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testGetPoolStatistics_shouldReturnNonNull() {
        String stats = repository.getPoolStatistics();
        assertNotNull(stats);
        assertFalse(stats.isEmpty());
    }

    private QuantityMeasurementEntity createEntity(String operation, String measurement,
                                                    double v1, String u1, double v2, String u2,
                                                    double rv, String ru) {
        return new QuantityMeasurementEntity(v1, u1, v2, u2, operation, measurement, rv, ru);
    }
}
