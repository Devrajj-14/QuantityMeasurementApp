package com.bridgelabz.quantitymeasurement.integration;

import com.bridgelabz.quantitymeasurement.dto.*;
import com.bridgelabz.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Full integration tests using @SpringBootTest.
 * Tests the complete flow: REST -> Service -> JPA -> H2.
 */
@SpringBootTest
@AutoConfigureMockMvc
class QuantityMeasurementIntegrationSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private QuantityMeasurementRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    // ===================== Compare Integration =====================

    @Test
    void testCompare_equalQuantities_fullFlow() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                12.0, "FEET", 144.0, "INCH", null);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(1.0))
                .andExpect(jsonPath("$.message").value("Quantities are equal"));

        // Verify persisted
        assert repository.count() == 1;
        assert repository.findByOperationTypeOrderByCreatedAtDesc("COMPARE").size() == 1;
    }

    // ===================== Convert Integration =====================

    @Test
    void testConvert_feetToInch_fullFlow() throws Exception {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                1.0, "FEET", "INCH");

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(12.0))
                .andExpect(jsonPath("$.resultUnit").value("INCH"));

        assert repository.count() == 1;
    }

    @Test
    void testConvert_celsiusToKelvin_fullFlow() throws Exception {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                0.0, "CELSIUS", "KELVIN");

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(273.15));
    }

    // ===================== Add Integration =====================

    @Test
    void testAdd_feetAndInch_fullFlow() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                2.0, "FEET", 12.0, "INCH", "INCH");

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(36.0))
                .andExpect(jsonPath("$.resultUnit").value("INCH"));
    }

    @Test
    void testAdd_weight_fullFlow() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                1.0, "KILOGRAM", 500.0, "GRAM", "GRAM");

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(1500.0));
    }

    // ===================== Subtract Integration =====================

    @Test
    void testSubtract_fullFlow() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 24.0, "INCH", "FEET");

        mockMvc.perform(post("/api/v1/quantities/subtract")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(8.0))
                .andExpect(jsonPath("$.resultUnit").value("FEET"));
    }

    // ===================== Divide Integration =====================

    @Test
    void testDivide_fullFlow() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 5.0, "FEET", null);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultValue").value(2.0))
                .andExpect(jsonPath("$.resultUnit").value("RATIO"));
    }

    // ===================== Error Handling Integration =====================

    @Test
    void testCompare_crossCategory_shouldReturn400() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                1.0, "FEET", 1.0, "KILOGRAM", null);

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Quantity Measurement Error"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testAdd_temperature_shouldReturn400() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                100.0, "CELSIUS", 50.0, "CELSIUS", null);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Unsupported Operation"));
    }

    @Test
    void testConvert_unknownUnit_shouldReturn400() throws Exception {
        QuantityConversionRequestDTO request = new QuantityConversionRequestDTO(
                1.0, "UNKNOWN", "FEET");

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testDivide_byZero_shouldReturn400() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "FEET", 0.0, "FEET", null);

        mockMvc.perform(post("/api/v1/quantities/divide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Arithmetic Error"));
    }

    @Test
    void testValidation_emptyUnit_shouldReturn400() throws Exception {
        QuantityOperationRequestDTO request = new QuantityOperationRequestDTO(
                10.0, "", 5.0, "FEET", null);

        mockMvc.perform(post("/api/v1/quantities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ===================== History Integration =====================

    @Test
    void testHistory_multipleOperations_fullFlow() throws Exception {
        // Perform 3 operations
        mockMvc.perform(post("/api/v1/quantities/compare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new QuantityOperationRequestDTO(1.0, "FEET", 12.0, "INCH", null))));

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new QuantityOperationRequestDTO(10.0, "FEET", 5.0, "FEET", "FEET"))));

        mockMvc.perform(post("/api/v1/quantities/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new QuantityConversionRequestDTO(1.0, "KILOGRAM", "GRAM"))));

        // Verify history
        mockMvc.perform(get("/api/v1/quantities/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        // Verify count
        mockMvc.perform(get("/api/v1/quantities/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(3));

        // Verify count by operation
        mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1));

        // Clear history
        mockMvc.perform(delete("/api/v1/quantities/history"))
                .andExpect(status().isOk());

        // Verify empty
        mockMvc.perform(get("/api/v1/quantities/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(0));
    }

    @Test
    void testHistoryByMeasurementType_fullFlow() throws Exception {
        // Add length and weight operations
        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new QuantityOperationRequestDTO(10.0, "FEET", 5.0, "FEET", "FEET"))));

        mockMvc.perform(post("/api/v1/quantities/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new QuantityOperationRequestDTO(1.0, "KILOGRAM", 500.0, "GRAM", "GRAM"))));

        // Verify by measurement type
        mockMvc.perform(get("/api/v1/quantities/history/measurement/LENGTH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/api/v1/quantities/history/measurement/WEIGHT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
