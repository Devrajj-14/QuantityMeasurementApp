package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.bridgelabz.quantitymeasurement.unit.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Service implementation that delegates business logic to the Quantity model
 * and persistence to the injected repository.
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);
    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public <U extends IMeasurable> boolean compareQuantities(Quantity<U> q1, Quantity<U> q2) {
        logger.info("Comparing: {} and {}", q1, q2);
        boolean result = q1.equals(q2);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                q1.getValue(), q1.getUnit().getUnitName(),
                q2.getValue(), q2.getUnit().getUnitName(),
                "COMPARE", getMeasurementType(q1.getUnit()),
                result ? 1.0 : 0.0, "BOOLEAN"
        );
        repository.save(entity);

        logger.info("Comparison result: {}", result);
        return result;
    }

    @Override
    public <U extends IMeasurable> Quantity<U> convertQuantity(Quantity<U> quantity, U targetUnit) {
        logger.info("Converting: {} to {}", quantity, targetUnit.getUnitName());
        Quantity<U> result = quantity.convertTo(targetUnit);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                quantity.getValue(), quantity.getUnit().getUnitName(),
                0.0, targetUnit.getUnitName(),
                "CONVERT", getMeasurementType(quantity.getUnit()),
                result.getValue(), result.getUnit().getUnitName()
        );
        repository.save(entity);

        logger.info("Conversion result: {}", result);
        return result;
    }

    @Override
    public <U extends IMeasurable> Quantity<U> addQuantities(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        logger.info("Adding: {} + {} in {}", q1, q2, targetUnit.getUnitName());
        Quantity<U> result = q1.add(q2, targetUnit);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                q1.getValue(), q1.getUnit().getUnitName(),
                q2.getValue(), q2.getUnit().getUnitName(),
                "ADD", getMeasurementType(q1.getUnit()),
                result.getValue(), result.getUnit().getUnitName()
        );
        repository.save(entity);

        logger.info("Addition result: {}", result);
        return result;
    }

    @Override
    public <U extends IMeasurable> Quantity<U> subtractQuantities(Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        logger.info("Subtracting: {} - {} in {}", q1, q2, targetUnit.getUnitName());
        Quantity<U> result = q1.subtract(q2, targetUnit);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                q1.getValue(), q1.getUnit().getUnitName(),
                q2.getValue(), q2.getUnit().getUnitName(),
                "SUBTRACT", getMeasurementType(q1.getUnit()),
                result.getValue(), result.getUnit().getUnitName()
        );
        repository.save(entity);

        logger.info("Subtraction result: {}", result);
        return result;
    }

    @Override
    public <U extends IMeasurable> double divideQuantities(Quantity<U> q1, Quantity<U> q2) {
        logger.info("Dividing: {} / {}", q1, q2);
        double result = q1.divide(q2);

        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                q1.getValue(), q1.getUnit().getUnitName(),
                q2.getValue(), q2.getUnit().getUnitName(),
                "DIVIDE", getMeasurementType(q1.getUnit()),
                result, "RATIO"
        );
        repository.save(entity);

        logger.info("Division result: {}", result);
        return result;
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return repository.findAll();
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operationType) {
        return repository.findByOperationType(operationType);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType) {
        return repository.findByMeasurementType(measurementType);
    }

    @Override
    public long getMeasurementCount() {
        return repository.count();
    }

    @Override
    public void clearHistory() {
        logger.info("Clearing measurement history");
        repository.deleteAll();
    }

    @Override
    public void releaseResources() {
        logger.info("Releasing service resources");
        repository.releaseResources();
    }

    /**
     * Determine the measurement type from the unit's class name.
     * E.g., LengthUnit -> LENGTH, WeightUnit -> WEIGHT
     */
    private String getMeasurementType(IMeasurable unit) {
        String className = unit.getClass().getSimpleName();
        return className.replace("Unit", "").toUpperCase();
    }
}
