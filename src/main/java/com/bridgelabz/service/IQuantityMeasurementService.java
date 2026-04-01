package com.bridgelabz.service;

import com.bridgelabz.entity.QuantityDTO;

/**
 * Service interface defining the contract for quantity measurement operations.
 * Accepts input as QuantityDTO objects and returns results as QuantityDTO objects,
 * providing a standardized API for the controller layer.
 *
 * Supported operations:
 * - Comparison: compare two quantities for equality
 * - Conversion: convert a quantity from one unit to another
 * - Addition: add two quantities
 * - Subtraction: subtract one quantity from another
 * - Division: divide one quantity by another (returns scalar ratio)
 */
public interface IQuantityMeasurementService {

    /**
     * Compares two quantities for equality (after converting to a common base unit).
     *
     * @param dto1 first quantity
     * @param dto2 second quantity
     * @return true if the quantities are equal in value
     */
    boolean compare(QuantityDTO dto1, QuantityDTO dto2);

    /**
     * Converts a quantity to the specified target unit.
     *
     * @param sourceDTO  the quantity to convert
     * @param targetUnit the target unit DTO (only unit field is used)
     * @return a QuantityDTO with the converted value and target unit
     */
    QuantityDTO convert(QuantityDTO sourceDTO, QuantityDTO targetUnit);

    /**
     * Adds two quantities and returns the result in the first quantity's unit.
     *
     * @param dto1 the first quantity
     * @param dto2 the second quantity
     * @return a QuantityDTO with the sum
     */
    QuantityDTO add(QuantityDTO dto1, QuantityDTO dto2);

    /**
     * Subtracts the second quantity from the first and returns the result.
     *
     * @param dto1 the quantity to subtract from
     * @param dto2 the quantity to subtract
     * @return a QuantityDTO with the difference
     */
    QuantityDTO subtract(QuantityDTO dto1, QuantityDTO dto2);

    /**
     * Divides the first quantity by the second and returns the dimensionless ratio.
     *
     * @param dto1 the dividend quantity
     * @param dto2 the divisor quantity
     * @return a QuantityDTO with the ratio as value (unit is null)
     */
    QuantityDTO divide(QuantityDTO dto1, QuantityDTO dto2);
}
