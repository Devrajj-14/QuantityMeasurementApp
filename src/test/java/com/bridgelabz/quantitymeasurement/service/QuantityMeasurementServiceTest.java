package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.model.Quantity;
import com.bridgelabz.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.bridgelabz.quantitymeasurement.unit.LengthUnit;
import com.bridgelabz.quantitymeasurement.unit.WeightUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for QuantityMeasurementServiceImpl with mocked repository.
 */
@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceTest {

    @Mock
    private IQuantityMeasurementRepository repository;

    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setup() {
        service = new QuantityMeasurementServiceImpl(repository);
    }

    @Test
    void testCompare_equalQuantities_shouldReturnTrue() {
        Quantity<LengthUnit> q1 = new Quantity<>(12.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(144.0, LengthUnit.INCH);

        boolean result = service.compareQuantities(q1, q2);

        assertTrue(result);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testCompare_unequalQuantities_shouldReturnFalse() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        boolean result = service.compareQuantities(q1, q2);

        assertFalse(result);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testConvert_shouldReturnConvertedQuantity() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = service.convertQuantity(q, LengthUnit.INCH);

        assertEquals(12.0, result.getValue(), 1e-6);
        assertEquals(LengthUnit.INCH, result.getUnit());
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testAdd_shouldReturnSumAndPersist() {
        Quantity<LengthUnit> q1 = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);

        Quantity<LengthUnit> result = service.addQuantities(q1, q2, LengthUnit.INCH);

        assertEquals(36.0, result.getValue(), 1e-6);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testSubtract_shouldReturnDifferenceAndPersist() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        Quantity<LengthUnit> result = service.subtractQuantities(q1, q2, LengthUnit.FEET);

        assertEquals(5.0, result.getValue(), 1e-6);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testDivide_shouldReturnRatioAndPersist() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);

        double result = service.divideQuantities(q1, q2);

        assertEquals(2.0, result, 1e-6);
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testGetAllMeasurements_shouldDelegateToRepository() {
        QuantityMeasurementEntity e1 = new QuantityMeasurementEntity(
                10.0, "FEET", 5.0, "FEET", "ADD", "LENGTH", 15.0, "FEET");
        when(repository.findAll()).thenReturn(Arrays.asList(e1));

        List<QuantityMeasurementEntity> results = service.getAllMeasurements();

        assertEquals(1, results.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetMeasurementCount_shouldDelegateToRepository() {
        when(repository.count()).thenReturn(5L);

        long count = service.getMeasurementCount();

        assertEquals(5L, count);
        verify(repository, times(1)).count();
    }

    @Test
    void testClearHistory_shouldDelegateToRepository() {
        service.clearHistory();

        verify(repository, times(1)).deleteAll();
    }
}
