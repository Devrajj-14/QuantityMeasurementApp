package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.bridgelabz.quantitymeasurement.unit.LengthUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuantityMeasurementController with mocked service.
 */
@ExtendWith(MockitoExtension.class)
class QuantityMeasurementControllerTest {

    @Mock
    private IQuantityMeasurementService service;

    private QuantityMeasurementController controller;

    @BeforeEach
    void setup() {
        controller = new QuantityMeasurementController(service);
    }

    @Test
    void testPerformComparison_shouldDelegateToServiceAndReturnResult() {
        Quantity<LengthUnit> q1 = new Quantity<>(12.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(144.0, LengthUnit.INCH);

        when(service.compareQuantities(q1, q2)).thenReturn(true);

        boolean result = controller.performComparison(q1, q2);

        assertTrue(result);
        verify(service, times(1)).compareQuantities(q1, q2);
    }

    @Test
    void testPerformConversion_shouldDelegateToServiceAndReturnResult() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> expected = new Quantity<>(12.0, LengthUnit.INCH);

        when(service.convertQuantity(q, LengthUnit.INCH)).thenReturn(expected);

        Quantity<LengthUnit> result = controller.performConversion(q, LengthUnit.INCH);

        assertEquals(12.0, result.getValue(), 1e-6);
        verify(service, times(1)).convertQuantity(q, LengthUnit.INCH);
    }

    @Test
    void testPerformAddition_shouldDelegateToService() {
        Quantity<LengthUnit> q1 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> expected = new Quantity<>(5.0, LengthUnit.FEET);

        when(service.addQuantities(q1, q2, LengthUnit.FEET)).thenReturn(expected);

        Quantity<LengthUnit> result = controller.performAddition(q1, q2, LengthUnit.FEET);

        assertEquals(5.0, result.getValue(), 1e-6);
        verify(service, times(1)).addQuantities(q1, q2, LengthUnit.FEET);
    }

    @Test
    void testPerformSubtraction_shouldDelegateToService() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> expected = new Quantity<>(5.0, LengthUnit.FEET);

        when(service.subtractQuantities(q1, q2, LengthUnit.FEET)).thenReturn(expected);

        Quantity<LengthUnit> result = controller.performSubtraction(q1, q2, LengthUnit.FEET);

        assertEquals(5.0, result.getValue(), 1e-6);
        verify(service, times(1)).subtractQuantities(q1, q2, LengthUnit.FEET);
    }

    @Test
    void testPerformDivision_shouldDelegateToService() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        when(service.divideQuantities(q1, q2)).thenReturn(2.0);

        double result = controller.performDivision(q1, q2);

        assertEquals(2.0, result, 1e-6);
        verify(service, times(1)).divideQuantities(q1, q2);
    }

    @Test
    void testShowCount_shouldDelegateToService() {
        when(service.getMeasurementCount()).thenReturn(10L);

        long count = controller.showCount();

        assertEquals(10L, count);
        verify(service, times(1)).getMeasurementCount();
    }

    @Test
    void testClearHistory_shouldDelegateToService() {
        controller.clearHistory();

        verify(service, times(1)).clearHistory();
    }
}
