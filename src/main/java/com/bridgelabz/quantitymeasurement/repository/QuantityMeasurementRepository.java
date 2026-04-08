package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for quantity measurement persistence.
 * Replaces the old JDBC-based QuantityMeasurementDatabaseRepository.
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    /**
     * Find measurements by operation type (e.g., COMPARE, CONVERT, ADD, SUBTRACT, DIVIDE).
     */
    List<QuantityMeasurementEntity> findByOperationTypeOrderByCreatedAtDesc(String operationType);

    /**
     * Find measurements by measurement type (e.g., LENGTH, WEIGHT, VOLUME, TEMPERATURE).
     */
    List<QuantityMeasurementEntity> findByMeasurementTypeOrderByCreatedAtDesc(String measurementType);

    /**
     * Get all measurements ordered by newest first.
     */
    List<QuantityMeasurementEntity> findAllByOrderByCreatedAtDesc();

    /**
     * Count measurements by operation type.
     */
    long countByOperationType(String operationType);

    /**
     * Count measurements by measurement type.
     */
    long countByMeasurementType(String measurementType);
}
