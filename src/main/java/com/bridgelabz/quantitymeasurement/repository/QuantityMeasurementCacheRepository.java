package com.bridgelabz.quantitymeasurement.repository;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory cache implementation of the measurement repository.
 * Stores records in a simple ArrayList — suitable for testing and lightweight usage.
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);
    private final List<QuantityMeasurementEntity> cache = new ArrayList<>();
    private long idCounter = 0;

    @Override
    public void save(QuantityMeasurementEntity entity) {
        entity.setId(++idCounter);
        cache.add(entity);
        logger.info("Saved measurement to cache: id={}", entity.getId());
    }

    @Override
    public List<QuantityMeasurementEntity> findAll() {
        logger.info("Retrieving all measurements from cache: count={}", cache.size());
        return new ArrayList<>(cache);
    }

    @Override
    public List<QuantityMeasurementEntity> findByOperationType(String operationType) {
        List<QuantityMeasurementEntity> results = cache.stream()
                .filter(e -> e.getOperationType().equalsIgnoreCase(operationType))
                .collect(Collectors.toList());
        logger.info("Found {} measurements for operation type: {}", results.size(), operationType);
        return results;
    }

    @Override
    public List<QuantityMeasurementEntity> findByMeasurementType(String measurementType) {
        List<QuantityMeasurementEntity> results = cache.stream()
                .filter(e -> e.getMeasurementType().equalsIgnoreCase(measurementType))
                .collect(Collectors.toList());
        logger.info("Found {} measurements for measurement type: {}", results.size(), measurementType);
        return results;
    }

    @Override
    public long count() {
        return cache.size();
    }

    @Override
    public void deleteAll() {
        int size = cache.size();
        cache.clear();
        idCounter = 0;
        logger.info("Deleted all {} measurements from cache", size);
    }

    @Override
    public String getPoolStatistics() {
        return "CacheRepository: " + cache.size() + " records in memory";
    }
}
