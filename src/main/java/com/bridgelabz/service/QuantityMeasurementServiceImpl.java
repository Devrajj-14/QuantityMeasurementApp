package com.bridgelabz.service;

import com.bridgelabz.IMeasurable;
import com.bridgelabz.Quantity;
import com.bridgelabz.entity.QuantityDTO;
import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.entity.QuantityModel;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.repository.IQuantityMeasurementRepository;

/**
 * Implementation of IQuantityMeasurementService that contains the core business logic
 * for quantity comparisons, conversions, and arithmetic operations.
 *
 * Follows SRP — solely responsible for measurement operations.
 * Follows OCP — extensible for new units without modifying existing code.
 *
 * Flow for each operation:
 * 1. Accept QuantityDTO input
 * 2. Map DTO units to internal IMeasurable domain units
 * 3. Create Quantity objects for processing
 * 4. Perform business logic
 * 5. Store operation history in repository
 * 6. Return standardized QuantityDTO result
 */
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    /**
     * Constructor with dependency injection for the repository.
     *
     * @param repository the repository to persist operation history
     */
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        this.repository = repository;
    }

    @Override
    public boolean compare(QuantityDTO dto1, QuantityDTO dto2) {
        validateInputs(dto1, dto2, "COMPARISON");
        try {
            IMeasurable unit1 = mapToIMeasurable(dto1);
            IMeasurable unit2 = mapToIMeasurable(dto2);
            validateSameCategory(unit1, unit2, "COMPARISON");

            Quantity<IMeasurable> q1 = createQuantity(dto1.getValue(), unit1);
            Quantity<IMeasurable> q2 = createQuantity(dto2.getValue(), unit2);

            boolean result = q1.equals(q2);

            // Store in repository
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "COMPARISON", String.valueOf(result));
            repository.save(entity);

            return result;
        } catch (QuantityMeasurementException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "COMPARISON", e.getMessage(), true);
            repository.save(errorEntity);
            throw e;
        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "COMPARISON", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Comparison failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO convert(QuantityDTO sourceDTO, QuantityDTO targetUnitDTO) {
        validateInput(sourceDTO, "CONVERSION");
        if (targetUnitDTO == null || targetUnitDTO.getUnit() == null) {
            throw new QuantityMeasurementException("Target unit cannot be null for conversion");
        }
        try {
            IMeasurable sourceUnit = mapToIMeasurable(sourceDTO);
            IMeasurable targetUnit = mapToIMeasurable(targetUnitDTO);
            validateSameCategory(sourceUnit, targetUnit, "CONVERSION");

            Quantity<IMeasurable> sourceQuantity = createQuantity(sourceDTO.getValue(), sourceUnit);
            Quantity<IMeasurable> converted = sourceQuantity.convertTo(targetUnit);

            QuantityDTO result = new QuantityDTO(converted.getValue(), targetUnitDTO.getUnit());

            // Store in repository
            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    sourceDTO.toString(), "CONVERSION", result.toString());
            repository.save(entity);

            return result;
        } catch (QuantityMeasurementException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    sourceDTO.toString(), null, "CONVERSION", e.getMessage(), true);
            repository.save(errorEntity);
            throw e;
        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    sourceDTO.toString(), null, "CONVERSION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Conversion failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO add(QuantityDTO dto1, QuantityDTO dto2) {
        validateInputs(dto1, dto2, "ADDITION");
        try {
            IMeasurable unit1 = mapToIMeasurable(dto1);
            IMeasurable unit2 = mapToIMeasurable(dto2);
            validateSameCategory(unit1, unit2, "ADDITION");

            Quantity<IMeasurable> q1 = createQuantity(dto1.getValue(), unit1);
            Quantity<IMeasurable> q2 = createQuantity(dto2.getValue(), unit2);
            Quantity<IMeasurable> sum = q1.add(q2);

            QuantityDTO result = new QuantityDTO(sum.getValue(), dto1.getUnit());

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "ADDITION", result.toString());
            repository.save(entity);

            return result;
        } catch (UnsupportedOperationException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "ADDITION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Addition not supported: " + e.getMessage(), e);
        } catch (QuantityMeasurementException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "ADDITION", e.getMessage(), true);
            repository.save(errorEntity);
            throw e;
        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "ADDITION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Addition failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO subtract(QuantityDTO dto1, QuantityDTO dto2) {
        validateInputs(dto1, dto2, "SUBTRACTION");
        try {
            IMeasurable unit1 = mapToIMeasurable(dto1);
            IMeasurable unit2 = mapToIMeasurable(dto2);
            validateSameCategory(unit1, unit2, "SUBTRACTION");

            Quantity<IMeasurable> q1 = createQuantity(dto1.getValue(), unit1);
            Quantity<IMeasurable> q2 = createQuantity(dto2.getValue(), unit2);
            Quantity<IMeasurable> difference = q1.subtract(q2);

            QuantityDTO result = new QuantityDTO(difference.getValue(), dto1.getUnit());

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "SUBTRACTION", result.toString());
            repository.save(entity);

            return result;
        } catch (UnsupportedOperationException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "SUBTRACTION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Subtraction not supported: " + e.getMessage(), e);
        } catch (QuantityMeasurementException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "SUBTRACTION", e.getMessage(), true);
            repository.save(errorEntity);
            throw e;
        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "SUBTRACTION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Subtraction failed: " + e.getMessage(), e);
        }
    }

    @Override
    public QuantityDTO divide(QuantityDTO dto1, QuantityDTO dto2) {
        validateInputs(dto1, dto2, "DIVISION");
        try {
            IMeasurable unit1 = mapToIMeasurable(dto1);
            IMeasurable unit2 = mapToIMeasurable(dto2);
            validateSameCategory(unit1, unit2, "DIVISION");

            Quantity<IMeasurable> q1 = createQuantity(dto1.getValue(), unit1);
            Quantity<IMeasurable> q2 = createQuantity(dto2.getValue(), unit2);
            double ratio = q1.divide(q2);

            // Division returns dimensionless ratio — unit is null
            QuantityDTO result = new QuantityDTO(ratio, null);

            QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "DIVISION", String.valueOf(ratio));
            repository.save(entity);

            return result;
        } catch (UnsupportedOperationException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "DIVISION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Division not supported: " + e.getMessage(), e);
        } catch (ArithmeticException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1.toString(), dto2.toString(), "DIVISION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Division failed: " + e.getMessage(), e);
        } catch (QuantityMeasurementException e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "DIVISION", e.getMessage(), true);
            repository.save(errorEntity);
            throw e;
        } catch (Exception e) {
            QuantityMeasurementEntity errorEntity = new QuantityMeasurementEntity(
                    dto1 != null ? dto1.toString() : "null",
                    dto2 != null ? dto2.toString() : "null",
                    "DIVISION", e.getMessage(), true);
            repository.save(errorEntity);
            throw new QuantityMeasurementException("Division failed: " + e.getMessage(), e);
        }
    }

    // ======================== Helper Methods ========================

    /**
     * Validates that a single DTO input is not null and has a valid unit.
     */
    private void validateInput(QuantityDTO dto, String operation) {
        if (dto == null) {
            throw new QuantityMeasurementException("Input quantity cannot be null for " + operation);
        }
        if (dto.getUnit() == null) {
            throw new QuantityMeasurementException("Unit cannot be null for " + operation);
        }
    }

    /**
     * Validates that both DTO inputs are not null and have valid units.
     */
    private void validateInputs(QuantityDTO dto1, QuantityDTO dto2, String operation) {
        if (dto1 == null || dto2 == null) {
            throw new QuantityMeasurementException("Input quantities cannot be null for " + operation);
        }
        if (dto1.getUnit() == null || dto2.getUnit() == null) {
            throw new QuantityMeasurementException("Units cannot be null for " + operation);
        }
    }

    /**
     * Maps a QuantityDTO's unit to an internal IMeasurable domain unit using the unit name.
     */
    private IMeasurable mapToIMeasurable(QuantityDTO dto) {
        try {
            return IMeasurable.fromUnitName(dto.getUnit().getUnitName());
        } catch (IllegalArgumentException e) {
            throw new QuantityMeasurementException("Invalid unit: " + dto.getUnit().getUnitName(), e);
        }
    }

    /**
     * Validates that two IMeasurable units belong to the same measurement category.
     */
    private void validateSameCategory(IMeasurable unit1, IMeasurable unit2, String operation) {
        if (!unit1.getMeasurementType().equals(unit2.getMeasurementType())) {
            throw new QuantityMeasurementException(
                    "Incompatible measurement types for " + operation + ": "
                            + unit1.getMeasurementType() + " and " + unit2.getMeasurementType());
        }
    }

    /**
     * Creates a Quantity object from a value and IMeasurable unit.
     * Uses unchecked cast since we've already validated category compatibility.
     */
    @SuppressWarnings("unchecked")
    private Quantity<IMeasurable> createQuantity(double value, IMeasurable unit) {
        return new Quantity<>(value, unit);
    }
}
