package com.bridgelabz.repository;

import com.bridgelabz.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * Repository interface for data access operations related to QuantityMeasurementEntity.
 * Abstracts the persistence layer implementation (in-memory cache, database, etc.)
 * and provides a clean interface for managing quantity measurement data.
 *
 * Designed following the Interface Segregation Principle, allowing different
 * repository implementations to be substituted without affecting the service layer.
 */
public interface IQuantityMeasurementRepository {

    /**
     * Saves a QuantityMeasurementEntity to the repository.
     *
     * @param entity the entity to save
     */
    void save(QuantityMeasurementEntity entity);

    /**
     * Retrieves all stored measurement entities.
     *
     * @return an unmodifiable list of all measurement entities
     */
    List<QuantityMeasurementEntity> getAllMeasurements();

    /**
     * Clears the entire measurement history.
     */
    void clearHistory();
}
