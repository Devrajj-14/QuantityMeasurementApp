package com.bridgelabz;

import com.bridgelabz.controller.QuantityMeasurementController;
import com.bridgelabz.entity.QuantityDTO;
import com.bridgelabz.entity.QuantityMeasurementEntity;
import com.bridgelabz.entity.QuantityModel;
import com.bridgelabz.exception.QuantityMeasurementException;
import com.bridgelabz.repository.IQuantityMeasurementRepository;
import com.bridgelabz.repository.QuantityMeasurementCacheRepository;
import com.bridgelabz.service.IQuantityMeasurementService;
import com.bridgelabz.service.QuantityMeasurementServiceImpl;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive JUnit 5 test suite for UC15: N-Tier Architecture.
 * Covers entity construction, service operations, temperature rejection,
 * cross-category rejection, null handling, repository behavior,
 * controller integration, and backward compatibility.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuantityMeasurementUC15Test {

    private static final double EPS = 1e-2;

    private IQuantityMeasurementRepository repository;
    private IQuantityMeasurementService service;

    @BeforeEach
    void setUp() {
        // Reset the singleton and remove persisted data for test isolation
        QuantityMeasurementCacheRepository.resetInstance();
        File dataFile = new File("quantity_measurements.dat");
        if (dataFile.exists()) dataFile.delete();

        repository = QuantityMeasurementCacheRepository.getInstance();
        service = new QuantityMeasurementServiceImpl(repository);
    }

    @AfterEach
    void tearDown() {
        QuantityMeasurementCacheRepository.resetInstance();
        File dataFile = new File("quantity_measurements.dat");
        if (dataFile.exists()) dataFile.delete();
    }

    // ====================== 1. Entity / DTO / Model Tests ======================

    @Test
    @Order(1)
    void testQuantityDTO_Construction() {
        QuantityDTO dto = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        assertEquals(10.0, dto.getValue(), EPS);
        assertEquals(QuantityDTO.LengthUnit.FEET, dto.getUnit());
    }

    @Test
    @Order(2)
    void testQuantityDTO_ToString() {
        QuantityDTO dto = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);
        assertEquals("QuantityDTO(5.0, KILOGRAM)", dto.toString());
    }

    @Test
    @Order(3)
    void testQuantityDTO_SettersGetters() {
        QuantityDTO dto = new QuantityDTO();
        dto.setValue(3.14);
        dto.setUnit(QuantityDTO.VolumeUnit.LITRE);
        assertEquals(3.14, dto.getValue(), EPS);
        assertEquals(QuantityDTO.VolumeUnit.LITRE, dto.getUnit());
    }

    @Test
    @Order(4)
    void testQuantityModel_Construction() {
        QuantityModel<LengthUnit> model = new QuantityModel<>(10.0, LengthUnit.FEET);
        assertEquals(10.0, model.getValue(), EPS);
        assertEquals(LengthUnit.FEET, model.getUnit());
    }

    @Test
    @Order(5)
    void testQuantityModel_ToString() {
        QuantityModel<WeightUnit> model = new QuantityModel<>(2.5, WeightUnit.KILOGRAM);
        assertEquals("QuantityModel(2.5, KILOGRAM)", model.toString());
    }

    @Test
    @Order(6)
    void testQuantityMeasurementEntity_SingleOperandConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "QuantityDTO(1.0, FEET)", "CONVERSION", "QuantityDTO(12.0, INCH)");
        assertEquals("QuantityDTO(1.0, FEET)", entity.getOperand1());
        assertNull(entity.getOperand2());
        assertEquals("CONVERSION", entity.getOperationType());
        assertEquals("QuantityDTO(12.0, INCH)", entity.getResult());
        assertFalse(entity.isHasError());
        assertNull(entity.getErrorMessage());
        assertNotNull(entity.getTimestamp());
    }

    @Test
    @Order(7)
    void testQuantityMeasurementEntity_BinaryOperandConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "QuantityDTO(10.0, FEET)", "QuantityDTO(5.0, FEET)",
                "ADDITION", "QuantityDTO(15.0, FEET)");
        assertEquals("QuantityDTO(10.0, FEET)", entity.getOperand1());
        assertEquals("QuantityDTO(5.0, FEET)", entity.getOperand2());
        assertEquals("ADDITION", entity.getOperationType());
        assertEquals("QuantityDTO(15.0, FEET)", entity.getResult());
        assertFalse(entity.isHasError());
    }

    @Test
    @Order(8)
    void testQuantityMeasurementEntity_ErrorConstruction() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "op1", "op2", "ADDITION", "Temperature does not support addition", true);
        assertTrue(entity.isHasError());
        assertEquals("Temperature does not support addition", entity.getErrorMessage());
        assertNull(entity.getResult());
    }

    @Test
    @Order(9)
    void testQuantityMeasurementEntity_ToString_Success() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "10.0 FEET", "5.0 FEET", "ADDITION", "15.0 FEET");
        String str = entity.toString();
        assertTrue(str.contains("ADDITION"));
        assertTrue(str.contains("15.0 FEET"));
        assertFalse(str.contains("ERROR"));
    }

    @Test
    @Order(10)
    void testQuantityMeasurementEntity_ToString_Error() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "op1", "op2", "DIVISION", "Cannot divide by zero", true);
        String str = entity.toString();
        assertTrue(str.contains("ERROR"));
        assertTrue(str.contains("Cannot divide by zero"));
    }

    // ====================== 2. IMeasurableUnit Enum Tests ======================

    @Test
    @Order(11)
    void testIMeasurableUnit_LengthUnit_MeasurementType() {
        assertEquals("LENGTH", QuantityDTO.LengthUnit.FEET.getMeasurementType());
        assertEquals("FEET", QuantityDTO.LengthUnit.FEET.getUnitName());
    }

    @Test
    @Order(12)
    void testIMeasurableUnit_WeightUnit_MeasurementType() {
        assertEquals("WEIGHT", QuantityDTO.WeightUnit.KILOGRAM.getMeasurementType());
    }

    @Test
    @Order(13)
    void testIMeasurableUnit_VolumeUnit_MeasurementType() {
        assertEquals("VOLUME", QuantityDTO.VolumeUnit.LITRE.getMeasurementType());
    }

    @Test
    @Order(14)
    void testIMeasurableUnit_TemperatureUnit_MeasurementType() {
        assertEquals("TEMPERATURE", QuantityDTO.TemperatureUnit.CELSIUS.getMeasurementType());
    }

    // ====================== 3. Service — Comparison Tests ======================

    @Test
    @Order(15)
    void testService_CompareEquality_SameUnit_Success() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertTrue(service.compare(dto1, dto2));
    }

    @Test
    @Order(16)
    void testService_CompareEquality_DifferentUnit_Success() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        assertTrue(service.compare(dto1, dto2));
    }

    @Test
    @Order(17)
    void testService_CompareEquality_NotEqual() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.INCH);
        assertFalse(service.compare(dto1, dto2));
    }

    @Test
    @Order(18)
    void testService_CompareEquality_CrossCategory_Error() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        assertThrows(QuantityMeasurementException.class, () -> service.compare(dto1, dto2));
    }

    // ====================== 4. Service — Conversion Tests ======================

    @Test
    @Order(19)
    void testService_Convert_FeetToInches() {
        QuantityDTO source = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.INCH);
        QuantityDTO result = service.convert(source, target);
        assertEquals(12.0, result.getValue(), EPS);
        assertEquals(QuantityDTO.LengthUnit.INCH, result.getUnit());
    }

    @Test
    @Order(20)
    void testService_Convert_KilogramToGram() {
        QuantityDTO source = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.WeightUnit.GRAM);
        QuantityDTO result = service.convert(source, target);
        assertEquals(1000.0, result.getValue(), EPS);
    }

    @Test
    @Order(21)
    void testService_Convert_LitreToMillilitre() {
        QuantityDTO source = new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.VolumeUnit.MILLILITRE);
        QuantityDTO result = service.convert(source, target);
        assertEquals(1000.0, result.getValue(), EPS);
    }

    @Test
    @Order(22)
    void testService_Convert_CelsiusToFahrenheit() {
        QuantityDTO source = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
        QuantityDTO result = service.convert(source, target);
        assertEquals(212.0, result.getValue(), EPS);
    }

    @Test
    @Order(23)
    void testService_Convert_FahrenheitToCelsius() {
        QuantityDTO source = new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO result = service.convert(source, target);
        assertEquals(0.0, result.getValue(), EPS);
    }

    @Test
    @Order(24)
    void testService_Convert_CelsiusToKelvin() {
        QuantityDTO source = new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.TemperatureUnit.KELVIN);
        QuantityDTO result = service.convert(source, target);
        assertEquals(273.15, result.getValue(), EPS);
    }

    // ====================== 5. Service — Addition Tests ======================

    @Test
    @Order(25)
    void testService_Add_SameUnit_Success() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = service.add(dto1, dto2);
        assertEquals(15.0, result.getValue(), EPS);
    }

    @Test
    @Order(26)
    void testService_Add_DifferentUnits_Success() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        QuantityDTO result = service.add(dto1, dto2);
        assertEquals(2.0, result.getValue(), EPS);
    }

    @Test
    @Order(27)
    void testService_Add_WeightSuccess() {
        QuantityDTO dto1 = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO dto2 = new QuantityDTO(2.0, QuantityDTO.WeightUnit.KILOGRAM);
        QuantityDTO result = service.add(dto1, dto2);
        assertEquals(7.0, result.getValue(), EPS);
    }

    @Test
    @Order(28)
    void testService_Add_VolumeSuccess() {
        QuantityDTO dto1 = new QuantityDTO(5.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO dto2 = new QuantityDTO(2.0, QuantityDTO.VolumeUnit.LITRE);
        QuantityDTO result = service.add(dto1, dto2);
        assertEquals(7.0, result.getValue(), EPS);
    }

    @Test
    @Order(29)
    void testService_Add_UnsupportedOperation_Temperature_Error() {
        QuantityDTO dto1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO dto2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);
        assertThrows(QuantityMeasurementException.class, () -> service.add(dto1, dto2));
    }

    @Test
    @Order(30)
    void testService_Add_CrossCategory_Error() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.WeightUnit.KILOGRAM);
        assertThrows(QuantityMeasurementException.class, () -> service.add(dto1, dto2));
    }

    // ====================== 6. Service — Subtraction Tests ======================

    @Test
    @Order(31)
    void testService_Subtract_SameUnit_Success() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = service.subtract(dto1, dto2);
        assertEquals(5.0, result.getValue(), EPS);
    }

    @Test
    @Order(32)
    void testService_Subtract_CrossUnit_Success() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(6.0, QuantityDTO.LengthUnit.INCH);
        QuantityDTO result = service.subtract(dto1, dto2);
        assertEquals(9.5, result.getValue(), EPS);
    }

    @Test
    @Order(33)
    void testService_Subtract_UnsupportedOperation_Temperature_Error() {
        QuantityDTO dto1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO dto2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);
        assertThrows(QuantityMeasurementException.class, () -> service.subtract(dto1, dto2));
    }

    // ====================== 7. Service — Division Tests ======================

    @Test
    @Order(34)
    void testService_Divide_SameUnit_Success() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = service.divide(dto1, dto2);
        assertEquals(5.0, result.getValue(), EPS);
    }

    @Test
    @Order(35)
    void testService_Divide_ByZero_Error() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET);
        assertThrows(QuantityMeasurementException.class, () -> service.divide(dto1, dto2));
    }

    @Test
    @Order(36)
    void testService_Divide_UnsupportedOperation_Temperature_Error() {
        QuantityDTO dto1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO dto2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);
        assertThrows(QuantityMeasurementException.class, () -> service.divide(dto1, dto2));
    }

    // ====================== 8. Service — Null Input Validation Tests ======================

    @Test
    @Order(37)
    void testService_Compare_NullInput_Rejection() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertThrows(QuantityMeasurementException.class, () -> service.compare(dto1, null));
        assertThrows(QuantityMeasurementException.class, () -> service.compare(null, dto1));
    }

    @Test
    @Order(38)
    void testService_Add_NullInput_Rejection() {
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertThrows(QuantityMeasurementException.class, () -> service.add(dto1, null));
        assertThrows(QuantityMeasurementException.class, () -> service.add(null, dto1));
    }

    @Test
    @Order(39)
    void testService_Convert_NullTarget_Rejection() {
        QuantityDTO source = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertThrows(QuantityMeasurementException.class, () -> service.convert(source, null));
    }

    @Test
    @Order(40)
    void testService_NullUnit_Rejection() {
        QuantityDTO dto1 = new QuantityDTO(1.0, null);
        QuantityDTO dto2 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertThrows(QuantityMeasurementException.class, () -> service.compare(dto1, dto2));
    }

    // ====================== 9. Repository Tests ======================

    @Test
    @Order(41)
    void testRepository_SaveAndRetrieve() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "10 FEET", "5 FEET", "ADDITION", "15 FEET");
        repository.save(entity);
        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertEquals(1, all.size());
        assertEquals("ADDITION", all.get(0).getOperationType());
    }

    @Test
    @Order(42)
    void testRepository_GetAllMeasurements_ReadOnly() {
        repository.save(new QuantityMeasurementEntity("a", "b", "COMP", "true"));
        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertThrows(UnsupportedOperationException.class, () -> all.add(
                new QuantityMeasurementEntity("x", "y", "z", "w")));
    }

    @Test
    @Order(43)
    void testRepository_ClearHistory() {
        repository.save(new QuantityMeasurementEntity("a", "b", "COMP", "true"));
        repository.save(new QuantityMeasurementEntity("c", "d", "ADD", "15"));
        assertEquals(2, repository.getAllMeasurements().size());
        repository.clearHistory();
        assertEquals(0, repository.getAllMeasurements().size());
    }

    @Test
    @Order(44)
    void testRepository_SaveNull_Rejection() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    @Order(45)
    void testRepository_ServiceOperationSavesHistory() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        service.add(dto1, dto2);
        service.subtract(dto1, dto2);
        service.compare(dto1, dto2);
        assertEquals(3, repository.getAllMeasurements().size());
    }

    @Test
    @Order(46)
    void testRepository_FilePersistence() {
        // Save some data
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        service.add(dto1, dto2);

        // Reset singleton and reload from disk
        QuantityMeasurementCacheRepository.resetInstance();
        IQuantityMeasurementRepository newRepo = QuantityMeasurementCacheRepository.getInstance();
        List<QuantityMeasurementEntity> loaded = newRepo.getAllMeasurements();
        assertEquals(1, loaded.size());
        assertEquals("ADDITION", loaded.get(0).getOperationType());
    }

    // ====================== 10. Controller Integration Tests ======================

    @Test
    @Order(47)
    void testController_NotNullService() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityMeasurementController(null));
    }

    @Test
    @Order(48)
    void testController_PerformComparison_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        // Should not throw — output goes to System.out
        assertDoesNotThrow(() -> controller.performComparison(dto1, dto2));
    }

    @Test
    @Order(49)
    void testController_PerformAddition_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        assertDoesNotThrow(() -> controller.performAddition(dto1, dto2));
    }

    @Test
    @Order(50)
    void testController_PerformAddition_TemperatureError() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO dto1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO dto2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);
        // Controller should catch the error and print it, not throw
        assertDoesNotThrow(() -> controller.performAddition(dto1, dto2));
    }

    @Test
    @Order(51)
    void testController_PerformConversion_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO source = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.INCH);
        assertDoesNotThrow(() -> controller.performConversion(source, target));
    }

    @Test
    @Order(52)
    void testController_PerformSubtraction_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        assertDoesNotThrow(() -> controller.performSubtraction(dto1, dto2));
    }

    @Test
    @Order(53)
    void testController_PerformDivision_Success() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        assertDoesNotThrow(() -> controller.performDivision(dto1, dto2));
    }

    @Test
    @Order(54)
    void testController_RunAllDemonstrations_NoException() {
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        assertDoesNotThrow(controller::runAllDemonstrations);
    }

    // ====================== 11. Layer Separation & Design Tests ======================

    @Test
    @Order(55)
    void testLayerSeparation_ServiceIndependence() {
        // Service can be tested independently without controller
        QuantityDTO dto1 = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCH);
        assertTrue(service.compare(dto1, dto2));
    }

    @Test
    @Order(56)
    void testService_ConstructorRejectsNullRepository() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityMeasurementServiceImpl(null));
    }

    @Test
    @Order(57)
    void testEntity_Immutability() {
        // QuantityMeasurementEntity does not expose setters — initialized via constructors only
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
                "10 FEET", "5 FEET", "ADDITION", "15 FEET");
        assertEquals("10 FEET", entity.getOperand1());
        assertEquals("15 FEET", entity.getResult());
        // No setters to call — entity is effectively immutable
    }

    // ====================== 12. IMeasurable.fromUnitName Tests ======================

    @Test
    @Order(58)
    void testIMeasurable_FromUnitName_Length() {
        IMeasurable unit = IMeasurable.fromUnitName("FEET");
        assertEquals("LENGTH", unit.getMeasurementType());
        assertEquals("FEET", unit.getUnitName());
    }

    @Test
    @Order(59)
    void testIMeasurable_FromUnitName_Weight() {
        IMeasurable unit = IMeasurable.fromUnitName("KILOGRAM");
        assertEquals("WEIGHT", unit.getMeasurementType());
    }

    @Test
    @Order(60)
    void testIMeasurable_FromUnitName_Volume() {
        IMeasurable unit = IMeasurable.fromUnitName("LITRE");
        assertEquals("VOLUME", unit.getMeasurementType());
    }

    @Test
    @Order(61)
    void testIMeasurable_FromUnitName_Temperature() {
        IMeasurable unit = IMeasurable.fromUnitName("CELSIUS");
        assertEquals("TEMPERATURE", unit.getMeasurementType());
    }

    @Test
    @Order(62)
    void testIMeasurable_FromUnitName_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> IMeasurable.fromUnitName("UNKNOWN"));
    }

    @Test
    @Order(63)
    void testIMeasurable_FromUnitName_Null() {
        assertThrows(IllegalArgumentException.class, () -> IMeasurable.fromUnitName(null));
    }

    // ====================== 13. Backward Compatibility (UC1–UC14) ======================

    @Test
    @Order(64)
    void testBackwardCompatibility_LengthEquality() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCH);
        assertEquals(q1, q2);
    }

    @Test
    @Order(65)
    void testBackwardCompatibility_LengthConversion() {
        Quantity<LengthUnit> q = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> converted = q.convertTo(LengthUnit.FEET);
        assertEquals(3.0, converted.getValue(), EPS);
    }

    @Test
    @Order(66)
    void testBackwardCompatibility_LengthAddition() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> sum = q1.add(q2);
        assertEquals(15.0, sum.getValue(), EPS);
    }

    @Test
    @Order(67)
    void testBackwardCompatibility_LengthSubtraction() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> diff = q1.subtract(q2);
        assertEquals(5.0, diff.getValue(), EPS);
    }

    @Test
    @Order(68)
    void testBackwardCompatibility_LengthDivision() {
        Quantity<LengthUnit> q1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2.0, LengthUnit.FEET);
        assertEquals(5.0, q1.divide(q2), EPS);
    }

    @Test
    @Order(69)
    void testBackwardCompatibility_WeightEquality() {
        Quantity<WeightUnit> q1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> q2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertEquals(q1, q2);
    }

    @Test
    @Order(70)
    void testBackwardCompatibility_VolumeEquality() {
        Quantity<VolumeUnit> q1 = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> q2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertEquals(q1, q2);
    }

    @Test
    @Order(71)
    void testBackwardCompatibility_TemperatureEquality() {
        Quantity<TemperatureUnit> q1 = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> q2 = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);
        assertEquals(q1, q2);
    }

    @Test
    @Order(72)
    void testBackwardCompatibility_TemperatureConversion() {
        Quantity<TemperatureUnit> q = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> converted = q.convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(212.0, converted.getValue(), EPS);
    }

    @Test
    @Order(73)
    void testBackwardCompatibility_TemperatureArithmetic_Rejected() {
        Quantity<TemperatureUnit> q1 = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> q2 = new Quantity<>(50.0, TemperatureUnit.CELSIUS);
        assertThrows(UnsupportedOperationException.class, () -> q1.add(q2));
        assertThrows(UnsupportedOperationException.class, () -> q1.subtract(q2));
        assertThrows(UnsupportedOperationException.class, () -> q1.divide(q2));
    }

    @Test
    @Order(74)
    void testBackwardCompatibility_NullUnit_Rejected() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, null));
    }

    @Test
    @Order(75)
    void testBackwardCompatibility_NonFiniteValue_Rejected() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    // ====================== 14. Integration / End-to-End Tests ======================

    @Test
    @Order(76)
    void testIntegration_EndToEnd_LengthAddition() {
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO result = service.add(dto1, dto2);
        assertEquals(15.0, result.getValue(), EPS);
        assertEquals(1, repository.getAllMeasurements().size());
    }

    @Test
    @Order(77)
    void testIntegration_EndToEnd_TemperatureUnsupported() {
        QuantityDTO dto1 = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO dto2 = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);
        assertThrows(QuantityMeasurementException.class, () -> service.add(dto1, dto2));
        // Error should still be stored in repository
        assertEquals(1, repository.getAllMeasurements().size());
        assertTrue(repository.getAllMeasurements().get(0).isHasError());
    }

    @Test
    @Order(78)
    void testService_AllMeasurementCategories() {
        // Length
        service.add(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET));
        // Weight
        service.add(new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM));
        // Volume
        service.add(new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE),
                new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE));
        assertEquals(3, repository.getAllMeasurements().size());
    }

    @Test
    @Order(79)
    void testDataFlow_ControllerToService() {
        // Controller calls service, which stores in repository
        QuantityMeasurementController controller = new QuantityMeasurementController(service);
        QuantityDTO dto1 = new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO dto2 = new QuantityDTO(5.0, QuantityDTO.LengthUnit.FEET);
        controller.performAddition(dto1, dto2);
        assertEquals(1, repository.getAllMeasurements().size());
    }

    @Test
    @Order(80)
    void testService_ExceptionHandling_AllOperations() {
        QuantityDTO tempC = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityDTO tempF = new QuantityDTO(50.0, QuantityDTO.TemperatureUnit.CELSIUS);

        // All arithmetic ops should throw for temperature
        assertThrows(QuantityMeasurementException.class, () -> service.add(tempC, tempF));
        assertThrows(QuantityMeasurementException.class, () -> service.subtract(tempC, tempF));
        assertThrows(QuantityMeasurementException.class, () -> service.divide(tempC, tempF));

        // But comparison and conversion should work
        assertDoesNotThrow(() -> service.compare(tempC, tempF));
        assertDoesNotThrow(() -> service.convert(tempC,
                new QuantityDTO(0, QuantityDTO.TemperatureUnit.FAHRENHEIT)));
    }
}
