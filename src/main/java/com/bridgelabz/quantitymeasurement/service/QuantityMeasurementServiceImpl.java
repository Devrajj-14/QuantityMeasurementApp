package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.*;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.exception.QuantityMeasurementException;
import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.bridgelabz.quantitymeasurement.unit.IMeasurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring service implementation for quantity measurement operations.
 * Preserves all original Quantity model business logic.
 * Uses Spring Data JPA repository for persistence.
 */
@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementServiceImpl.class);
    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    // ===================== Core Operations =====================

    @Override
    @Transactional
    public QuantityResponseDTO compareQuantities(QuantityOperationRequestDTO request) {
        logger.info("Comparing: {} {} and {} {}", request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit());

        IMeasurable firstUnit = resolveUnit(request.getFirstUnit());
        IMeasurable secondUnit = resolveUnit(request.getSecondUnit());
        validateSameCategory(firstUnit, secondUnit);

        Quantity<IMeasurable> q1 = new Quantity<>(request.getFirstValue(), firstUnit);
        Quantity<IMeasurable> q2 = new Quantity<>(request.getSecondValue(), secondUnit);
        boolean result = q1.equals(q2);

        // Persist
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit(),
                "COMPARE", firstUnit.getMeasurementType(),
                result ? 1.0 : 0.0, "BOOLEAN"
        );
        repository.save(entity);

        logger.info("Comparison result: {}", result);
        return new QuantityResponseDTO(
                result ? 1.0 : 0.0, "BOOLEAN", "COMPARE",
                result ? "Quantities are equal" : "Quantities are not equal"
        );
    }

    @Override
    @Transactional
    public QuantityResponseDTO convertQuantity(QuantityConversionRequestDTO request) {
        logger.info("Converting: {} {} to {}", request.getValue(), request.getSourceUnit(), request.getTargetUnit());

        IMeasurable sourceUnit = resolveUnit(request.getSourceUnit());
        IMeasurable targetUnit = resolveUnit(request.getTargetUnit());
        validateSameCategory(sourceUnit, targetUnit);

        Quantity<IMeasurable> quantity = new Quantity<>(request.getValue(), sourceUnit);
        Quantity<IMeasurable> result = quantity.convertTo(targetUnit);

        // Persist
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                request.getValue(), request.getSourceUnit(),
                0.0, request.getTargetUnit(),
                "CONVERT", sourceUnit.getMeasurementType(),
                result.getValue(), result.getUnit().getUnitName()
        );
        repository.save(entity);

        logger.info("Conversion result: {}", result);
        return new QuantityResponseDTO(
                result.getValue(), result.getUnit().getUnitName(), "CONVERT",
                request.getValue() + " " + request.getSourceUnit() + " = " +
                        result.getValue() + " " + result.getUnit().getUnitName()
        );
    }

    @Override
    @Transactional
    public QuantityResponseDTO addQuantities(QuantityOperationRequestDTO request) {
        logger.info("Adding: {} {} + {} {}", request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit());

        IMeasurable firstUnit = resolveUnit(request.getFirstUnit());
        IMeasurable secondUnit = resolveUnit(request.getSecondUnit());
        validateSameCategory(firstUnit, secondUnit);

        IMeasurable targetUnit = request.getTargetUnit() != null && !request.getTargetUnit().isBlank()
                ? resolveUnit(request.getTargetUnit()) : firstUnit;

        Quantity<IMeasurable> q1 = new Quantity<>(request.getFirstValue(), firstUnit);
        Quantity<IMeasurable> q2 = new Quantity<>(request.getSecondValue(), secondUnit);
        Quantity<IMeasurable> result = q1.add(q2, targetUnit);

        // Persist
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit(),
                "ADD", firstUnit.getMeasurementType(),
                result.getValue(), result.getUnit().getUnitName()
        );
        repository.save(entity);

        logger.info("Addition result: {}", result);
        return new QuantityResponseDTO(
                result.getValue(), result.getUnit().getUnitName(), "ADD",
                request.getFirstValue() + " " + request.getFirstUnit() + " + " +
                        request.getSecondValue() + " " + request.getSecondUnit() + " = " +
                        result.getValue() + " " + result.getUnit().getUnitName()
        );
    }

    @Override
    @Transactional
    public QuantityResponseDTO subtractQuantities(QuantityOperationRequestDTO request) {
        logger.info("Subtracting: {} {} - {} {}", request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit());

        IMeasurable firstUnit = resolveUnit(request.getFirstUnit());
        IMeasurable secondUnit = resolveUnit(request.getSecondUnit());
        validateSameCategory(firstUnit, secondUnit);

        IMeasurable targetUnit = request.getTargetUnit() != null && !request.getTargetUnit().isBlank()
                ? resolveUnit(request.getTargetUnit()) : firstUnit;

        Quantity<IMeasurable> q1 = new Quantity<>(request.getFirstValue(), firstUnit);
        Quantity<IMeasurable> q2 = new Quantity<>(request.getSecondValue(), secondUnit);
        Quantity<IMeasurable> result = q1.subtract(q2, targetUnit);

        // Persist
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit(),
                "SUBTRACT", firstUnit.getMeasurementType(),
                result.getValue(), result.getUnit().getUnitName()
        );
        repository.save(entity);

        logger.info("Subtraction result: {}", result);
        return new QuantityResponseDTO(
                result.getValue(), result.getUnit().getUnitName(), "SUBTRACT",
                request.getFirstValue() + " " + request.getFirstUnit() + " - " +
                        request.getSecondValue() + " " + request.getSecondUnit() + " = " +
                        result.getValue() + " " + result.getUnit().getUnitName()
        );
    }

    @Override
    @Transactional
    public QuantityResponseDTO divideQuantities(QuantityOperationRequestDTO request) {
        logger.info("Dividing: {} {} / {} {}", request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit());

        IMeasurable firstUnit = resolveUnit(request.getFirstUnit());
        IMeasurable secondUnit = resolveUnit(request.getSecondUnit());
        validateSameCategory(firstUnit, secondUnit);

        Quantity<IMeasurable> q1 = new Quantity<>(request.getFirstValue(), firstUnit);
        Quantity<IMeasurable> q2 = new Quantity<>(request.getSecondValue(), secondUnit);
        double result = q1.divide(q2);

        // Persist
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                request.getFirstValue(), request.getFirstUnit(),
                request.getSecondValue(), request.getSecondUnit(),
                "DIVIDE", firstUnit.getMeasurementType(),
                result, "RATIO"
        );
        repository.save(entity);

        logger.info("Division result: {}", result);
        return new QuantityResponseDTO(
                result, "RATIO", "DIVIDE",
                request.getFirstValue() + " " + request.getFirstUnit() + " / " +
                        request.getSecondValue() + " " + request.getSecondUnit() + " = " + result
        );
    }

    // ===================== History / Query Operations =====================

    @Override
    public List<MeasurementHistoryDTO> getAllMeasurements() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MeasurementHistoryDTO> getMeasurementsByOperation(String operationType) {
        return repository.findByOperationTypeOrderByCreatedAtDesc(operationType.toUpperCase()).stream()
                .map(this::toHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MeasurementHistoryDTO> getMeasurementsByMeasurementType(String measurementType) {
        return repository.findByMeasurementTypeOrderByCreatedAtDesc(measurementType.toUpperCase()).stream()
                .map(this::toHistoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long getMeasurementCount() {
        return repository.count();
    }

    @Override
    public long getCountByOperation(String operationType) {
        return repository.countByOperationType(operationType.toUpperCase());
    }

    @Override
    @Transactional
    public void clearHistory() {
        logger.info("Clearing measurement history");
        repository.deleteAll();
    }

    // ===================== Helper Methods =====================

    /**
     * Resolve a unit name string to an IMeasurable enum constant.
     * Uses the existing IMeasurable.fromUnitName() static method.
     */
    private IMeasurable resolveUnit(String unitName) {
        try {
            return IMeasurable.fromUnitName(unitName);
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException("Unknown unit: " + unitName);
        }
    }

    /**
     * Validate that two units belong to the same measurement category.
     */
    private void validateSameCategory(IMeasurable unit1, IMeasurable unit2) {
        if (unit1.getClass() != unit2.getClass()) {
            throw new QuantityMeasurementException(
                    "Incompatible measurement types: " + unit1.getMeasurementType() +
                            " and " + unit2.getMeasurementType()
            );
        }
    }

    /**
     * Convert entity to history DTO.
     */
    private MeasurementHistoryDTO toHistoryDTO(QuantityMeasurementEntity entity) {
        return new MeasurementHistoryDTO(
                entity.getId(),
                entity.getFirstValue(), entity.getFirstUnit(),
                entity.getSecondValue(), entity.getSecondUnit(),
                entity.getOperationType(), entity.getMeasurementType(),
                entity.getResultValue(), entity.getResultUnit(),
                entity.getCreatedAt()
        );
    }
}
