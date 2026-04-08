package com.bridgelabz.quantitymeasurement.service;

import com.bridgelabz.quantitymeasurement.dto.*;
import com.bridgelabz.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.bridgelabz.quantitymeasurement.exception.QuantityMeasurementException;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementRepository;
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
 * Unit tests for QuantityMeasurementServiceImpl with mocked JPA repository.
 */
@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceSpringTest {

    @Mock
    private QuantityMeasurementRepository repository;

    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setup() {
        service = new QuantityMeasurementServiceImpl(repository);
    }

    // ===================== Compare =====================

    @Test
    void testCompare_equalQuantities_shouldReturnEqual() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                12.0, "FEET", 144.0, "INCH", null);

        QuantityResponseDTO result = service.compareQuantities(request);

        assertEquals(1.0, result.getResultValue());
        assertEquals("COMPARE", result.getOperationType());
        assertTrue(result.getMessage().contains("equal"));
        verify(repository, times(1)).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testCompare_unequalQuantities_shouldReturnNotEqual() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "FEET", null);

        QuantityResponseDTO result = service.compareQuantities(request);

        assertEquals(0.0, result.getResultValue());
        assertTrue(result.getMessage().contains("not equal"));
    }

    @Test
    void testCompare_crossCategory_shouldThrow() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                1.0, "FEET", 1.0, "KILOGRAM", null);

        assertThrows(QuantityMeasurementException.class,
                () -> service.compareQuantities(request));
    }

    // ===================== Convert =====================

    @Test
    void testConvert_feetToInch_shouldReturn12() {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                1.0, "FEET", "INCH");

        QuantityResponseDTO result = service.convertQuantity(request);

        assertEquals(12.0, result.getResultValue(), 1e-6);
        assertEquals("INCH", result.getResultUnit());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testConvert_celsiusToFahrenheit_shouldReturn212() {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                100.0, "CELSIUS", "FAHRENHEIT");

        QuantityResponseDTO result = service.convertQuantity(request);

        assertEquals(212.0, result.getResultValue(), 1e-6);
    }

    @Test
    void testConvert_unknownUnit_shouldThrow() {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                1.0, "UNKNOWN", "INCH");

        assertThrows(QuantityMeasurementException.class,
                () -> service.convertQuantity(request));
    }

    // ===================== Add =====================

    @Test
    void testAdd_feetPlusInch_shouldReturnCorrectSum() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                2.0, "FEET", 12.0, "INCH", "INCH");

        QuantityResponseDTO result = service.addQuantities(request);

        assertEquals(36.0, result.getResultValue(), 1e-6);
        assertEquals("INCH", result.getResultUnit());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testAdd_temperature_shouldThrow() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                100.0, "CELSIUS", 50.0, "CELSIUS", null);

        assertThrows(UnsupportedOperationException.class,
                () -> service.addQuantities(request));
    }

    @Test
    void testAdd_crossCategory_shouldThrow() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "KILOGRAM", null);

        assertThrows(QuantityMeasurementException.class,
                () -> service.addQuantities(request));
    }

    // ===================== Subtract =====================

    @Test
    void testSubtract_shouldReturnDifference() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "FEET", "FEET");

        QuantityResponseDTO result = service.subtractQuantities(request);

        assertEquals(5.0, result.getResultValue(), 1e-6);
        assertEquals("FEET", result.getResultUnit());
    }

    // ===================== Divide =====================

    @Test
    void testDivide_shouldReturnRatio() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "FEET", null);

        QuantityResponseDTO result = service.divideQuantities(request);

        assertEquals(2.0, result.getResultValue(), 1e-6);
        assertEquals("RATIO", result.getResultUnit());
    }

    @Test
    void testDivide_byZero_shouldThrow() {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 0.0, "FEET", null);

        assertThrows(ArithmeticException.class,
                () -> service.divideQuantities(request));
    }

    // ===================== History =====================

    @Test
    void testGetAllMeasurements_shouldReturnDTOList() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                10.0, "FEET", 5.0, "FEET", "ADD", "LENGTH", 15.0, "FEET");
        entity.setId(1L);
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(entity));

        List<MeasurementHistoryDTO> results = service.getAllMeasurements();

        assertEquals(1, results.size());
        assertEquals("ADD", results.get(0).getOperationType());
    }

    @Test
    void testGetMeasurementCount_shouldDelegateToRepository() {
        when(repository.count()).thenReturn(5L);

        assertEquals(5L, service.getMeasurementCount());
    }

    @Test
    void testGetCountByOperation_shouldDelegateToRepository() {
        when(repository.countByOperationType("ADD")).thenReturn(3L);

        assertEquals(3L, service.getCountByOperation("ADD"));
    }

    @Test
    void testClearHistory_shouldCallDeleteAll() {
        service.clearHistory();

        verify(repository, times(1)).deleteAll();
    }
}
