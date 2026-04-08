package com.bridgelabz.quantitymeasurement.controller;

import com.bridgelabz.quantitymeasurement.dto.*;
import com.bridgelabz.quantitymeasurement.service.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MockMvc tests for the REST controller.
 * Tests all endpoints with mocked service layer.
 */
@WebMvcTest(QuantityMeasurementRestController.class)
class QuantityMeasurementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IQuantityMeasurementService service;

    @Autowired
    private ObjectMapper objectMapper;

    // ===================== Compare =====================

    @Test
    void testCompare_equalQuantities_shouldReturn200() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                12.0, "FEET", 144.0, "INCH", null);
        QuantityResponseDTO response = new QuantityResponseDTO(
                1.0, "BOOLEAN", "COMPARE", "Quantities are equal");

        when(service.compareQuantities(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(1.0))
                .andExpect(jsonPath("$.operationType").value("COMPARE"))
                .andExpect(jsonPath("$.message").value("Quantities are equal"));

        verify(service, times(1)).compareQuantities(any());
    }

    @Test
    void testCompare_unequalQuantities_shouldReturn200() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "FEET", null);
        QuantityResponseDTO response = new QuantityResponseDTO(
                0.0, "BOOLEAN", "COMPARE", "Quantities are not equal");

        when(service.compareQuantities(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(0.0))
                .andExpect(jsonPath("$.message").value("Quantities are not equal"));
    }

    // ===================== Convert =====================

    @Test
    void testConvert_feetToInch_shouldReturn200() throws Exception {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                1.0, "FEET", "INCH");
        QuantityResponseDTO response = new QuantityResponseDTO(
                12.0, "INCH", "CONVERT", "1.0 FEET = 12.0 INCH");

        when(service.convertQuantity(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(12.0))
                .andExpect(jsonPath("$.resultUnit").value("INCH"));

        verify(service, times(1)).convertQuantity(any());
    }

    // ===================== Add =====================

    @Test
    void testAdd_shouldReturn200() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                2.0, "FEET", 12.0, "INCH", "INCH");
        QuantityResponseDTO response = new QuantityResponseDTO(
                36.0, "INCH", "ADD", "2.0 FEET + 12.0 INCH = 36.0 INCH");

        when(service.addQuantities(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(36.0))
                .andExpect(jsonPath("$.operationType").value("ADD"));
    }

    // ===================== Subtract =====================

    @Test
    void testSubtract_shouldReturn200() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 24.0, "INCH", "FEET");
        QuantityResponseDTO response = new QuantityResponseDTO(
                8.0, "FEET", "SUBTRACT", "10.0 FEET - 24.0 INCH = 8.0 FEET");

        when(service.subtractQuantities(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/subtract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(8.0))
                .andExpect(jsonPath("$.operationType").value("SUBTRACT"));
    }

    // ===================== Divide =====================

    @Test
    void testDivide_shouldReturn200() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "FEET", null);
        QuantityResponseDTO response = new QuantityResponseDTO(
                2.0, "RATIO", "DIVIDE", "10.0 FEET / 5.0 FEET = 2.0");

        when(service.divideQuantities(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("RATIO"));
    }

    // ===================== History =====================

    @Test
    void testGetAllHistory_shouldReturn200() throws Exception {
        MeasurementHistoryDTO dto = new MeasurementHistoryDTO(
                1L, 10.0, "FEET", 5.0, "FEET",
                "ADD", "LENGTH", 15.0, "FEET", LocalDateTime.now());

        when(service.getAllMeasurements()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/v1/quantities/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].operationType").value("ADD"));
    }

    @Test
    void testGetHistoryByOperation_shouldReturn200() throws Exception {
        when(service.getMeasurementsByOperation("ADD")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/operation/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetHistoryByMeasurementType_shouldReturn200() throws Exception {
        when(service.getMeasurementsByMeasurementType("LENGTH")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/quantities/history/measurement/LENGTH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ===================== Count =====================

    @Test
    void testGetTotalCount_shouldReturn200() throws Exception {
        when(service.getMeasurementCount()).thenReturn(10L);

        mockMvc.perform(get("/api/v1/quantities/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(10));
    }

    @Test
    void testGetCountByOperation_shouldReturn200() throws Exception {
        when(service.getCountByOperation("ADD")).thenReturn(5L);

        mockMvc.perform(get("/api/v1/quantities/count/ADD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));
    }

    // ===================== Delete =====================

    @Test
    void testClearHistory_shouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/v1/quantities/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Measurement history cleared successfully"));

        verify(service, times(1)).clearHistory();
    }

    // ===================== Validation =====================

    @Test
    void testCompare_invalidUnit_shouldReturn400() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "", 5.0, "FEET", null);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testConvert_missingSourceUnit_shouldReturn400() throws Exception {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                1.0, "", "INCH");

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
