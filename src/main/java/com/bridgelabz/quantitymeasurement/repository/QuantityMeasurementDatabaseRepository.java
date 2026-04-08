package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.exception.DatabaseException;
import com.bridgelabz.quantitymeasurement.util.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JDBC-based repository implementation using H2 database.
 * Uses ConnectionPool for connection management and parameterized queries for safety.
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);
    private static final String SCHEMA_FILE = "db/schema.sql";

    private static final String INSERT_SQL =
            "INSERT INTO quantity_measurements (first_value, first_unit, second_value, second_unit, " +
            "operation_type, measurement_type, result_value, result_unit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM quantity_measurements ORDER BY created_at DESC";

    private static final String SELECT_BY_OPERATION_SQL =
            "SELECT * FROM quantity_measurements WHERE operation_type = ? ORDER BY created_at DESC";

    private static final String SELECT_BY_MEASUREMENT_SQL =
            "SELECT * FROM quantity_measurements WHERE measurement_type = ? ORDER BY created_at DESC";

    private static final String COUNT_SQL =
            "SELECT COUNT(*) FROM quantity_measurements";

    private static final String DELETE_ALL_SQL =
            "DELETE FROM quantity_measurements";

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        initializeSchema();
    }

    /**
     * Initialize the database schema by running schema.sql from classpath.
     */
    private void initializeSchema() {
        logger.info("Initializing database schema...");
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            String schemaSql = loadSchemaFile();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(schemaSql);
            }
            logger.info("Database schema initialized successfully");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to initialize database schema", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    private String loadSchemaFile() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(SCHEMA_FILE)) {
            if (input == null) {
                throw new DatabaseException("Schema file '" + SCHEMA_FILE + "' not found on classpath");
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to load schema file", e);
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, entity.getFirstValue());
                ps.setString(2, entity.getFirstUnit());
                ps.setDouble(3, entity.getSecondValue());
                ps.setString(4, entity.getSecondUnit());
                ps.setString(5, entity.getOperationType());
                ps.setString(6, entity.getMeasurementType());
                ps.setDouble(7, entity.getResultValue());
                ps.setString(8, entity.getResultUnit());
                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                    }
                }
            }
            logger.info("Saved measurement to database: id={}", entity.getId());
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save measurement", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
                 ResultSet rs = ps.executeQuery()) {
                List<QuantityMeasurementEntity> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs));
                }
                logger.info("Retrieved {} measurements from database", results.size());
                return results;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve measurements", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> findByOperationType(String operationType) {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_OPERATION_SQL)) {
                ps.setString(1, operationType);
                try (ResultSet rs = ps.executeQuery()) {
                    List<QuantityMeasurementEntity> results = new ArrayList<>();
                    while (rs.next()) {
                        results.add(mapResultSetToEntity(rs));
                    }
                    logger.info("Found {} measurements for operation: {}", results.size(), operationType);
                    return results;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query by operation type", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_MEASUREMENT_SQL)) {
                ps.setString(1, measurementType);
                try (ResultSet rs = ps.executeQuery()) {
                    List<QuantityMeasurementEntity> results = new ArrayList<>();
                    while (rs.next()) {
                        results.add(mapResultSetToEntity(rs));
                    }
                    logger.info("Found {} measurements for type: {}", results.size(), measurementType);
                    return results;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query by measurement type", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public long count() {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(COUNT_SQL);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count measurements", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public void deleteAll() {
        Connection conn = null;
        try {
            conn = connectionPool.acquireConnection();
            try (PreparedStatement ps = conn.prepareStatement(DELETE_ALL_SQL)) {
                int deleted = ps.executeUpdate();
                logger.info("Deleted {} measurements from database", deleted);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete measurements", e);
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    @Override
    public void releaseResources() {
        logger.info("Releasing database repository resources...");
        connectionPool.closeAll();
    }

    /**
     * Map a ResultSet row to a QuantityMeasurementEntity.
     */
    private QuantityMeasurementEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setId(rs.getLong("id"));
        entity.setFirstValue(rs.getDouble("first_value"));
        entity.setFirstUnit(rs.getString("first_unit"));
        entity.setSecondValue(rs.getDouble("second_value"));
        entity.setSecondUnit(rs.getString("second_unit"));
        entity.setOperationType(rs.getString("operation_type"));
        entity.setMeasurementType(rs.getString("measurement_type"));
        entity.setResultValue(rs.getDouble("result_value"));
        entity.setResultUnit(rs.getString("result_unit"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            entity.setCreatedAt(ts.toLocalDateTime());
        }
        return entity;
    }
}
