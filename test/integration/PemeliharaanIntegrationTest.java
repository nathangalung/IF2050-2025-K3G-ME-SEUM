package test.integration;

import main.controller.PemeliharaanController;
import main.model.dto.PemeliharaanDto;
import main.model.entities.Pemeliharaan;
import main.model.enums.StatusPemeliharaan;
import main.service.impl.PemeliharaanServiceImpl;
import main.repository.impl.PemeliharaanRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the complete Pemeliharaan (maintenance) workflow
 * Tests the interaction between Controller, Service, and Repository layers
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Pemeliharaan Integration Tests")
class PemeliharaanIntegrationTest {

    private PemeliharaanController controller;
    private PemeliharaanServiceImpl service;
    private PemeliharaanRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        // Note: In a real integration test, you'd use a test database
        // For now, we'll test the component integration without actual DB calls
        repository = new PemeliharaanRepositoryImpl();
        service = new PemeliharaanServiceImpl(repository);
        controller = new PemeliharaanController(service);
    }

    @Test
    @DisplayName("Should perform complete maintenance workflow integration")
    void testCompleteMaintenanceWorkflow() {
        // Given - Create a new maintenance task
        PemeliharaanDto newTaskDto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
        
        // When & Then - Test full workflow without DB dependency
        // This demonstrates the integration between layers
        
        // Step 1: Verify DTO creation
        assertNotNull(newTaskDto);
        assertEquals("DIJADWALKAN", newTaskDto.getStatus());
        assertEquals("RUTIN", newTaskDto.getJenisPemeliharaan());
        
        // Step 2: Test entity conversion logic
        Pemeliharaan entity = service.convertToEntity(newTaskDto);
        assertNotNull(entity);
        assertEquals(StatusPemeliharaan.DIJADWALKAN, entity.getStatus());
        
        // Step 3: Test status transitions
        entity.mulaiPemeliharaan();
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, entity.getStatus());
        
        entity.selesaikanPemeliharaan("Task completed successfully");
        assertEquals(StatusPemeliharaan.SELESAI, entity.getStatus());
        assertNotNull(entity.getTanggalSelesai());
        
        // Step 4: Test DTO conversion back
        PemeliharaanDto completedDto = service.convertToDto(entity);
        assertEquals("SELESAI", completedDto.getStatus());
        assertEquals("Task completed successfully", completedDto.getCatatan());
    }

    @Test
    @DisplayName("Should validate maintenance data across layers")
    void testMaintenanceDataValidation() {
        // Test invalid data handling
        PemeliharaanDto invalidDto = new PemeliharaanDto();
        // Missing required fields
        
        assertThrows(IllegalArgumentException.class, () -> {
            service.validatePemeliharaanData(invalidDto);
        });
        
        // Test valid data
        PemeliharaanDto validDto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
        assertDoesNotThrow(() -> {
            service.validatePemeliharaanData(validDto);
        });
    }

    @Test
    @DisplayName("Should handle different maintenance types")
    void testMaintenanceTypes() {
        // Test different maintenance types
        String[] maintenanceTypes = {"RUTIN", "PREVENTIF", "KOREKTIF", "DARURAT"};
        
        for (String type : maintenanceTypes) {
            PemeliharaanDto dto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
            dto.setJenisPemeliharaan(type);
            
            // Verify type is preserved through conversion
            Pemeliharaan entity = service.convertToEntity(dto);
            assertEquals(type, entity.getJenisPemeliharaan());
            
            PemeliharaanDto convertedBack = service.convertToDto(entity);
            assertEquals(type, convertedBack.getJenisPemeliharaan());
        }
    }

    @Test
    @DisplayName("Should handle status transitions correctly across layers")
    void testStatusTransitionsIntegration() {
        // Test each status transition
        PemeliharaanDto dto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
        Pemeliharaan entity = service.convertToEntity(dto);
        
        // Initial state
        assertEquals(StatusPemeliharaan.DIJADWALKAN, entity.getStatus());
        
        // Start maintenance
        entity.mulaiPemeliharaan();
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, entity.getStatus());
        
        // Complete maintenance
        entity.selesaikanPemeliharaan("Completed");
        assertEquals(StatusPemeliharaan.SELESAI, entity.getStatus());
        
        // Verify final state in DTO
        PemeliharaanDto finalDto = service.convertToDto(entity);
        assertEquals("SELESAI", finalDto.getStatus());
        assertEquals("Completed", finalDto.getCatatan());
        assertNotNull(finalDto.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should handle maintenance cancellation workflow")
    void testMaintenanceCancellation() {
        PemeliharaanDto dto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
        Pemeliharaan entity = service.convertToEntity(dto);
        
        // Cancel maintenance
        entity.batalkanPemeliharaan("Equipment not available");
        
        // Verify cancellation
        assertEquals(StatusPemeliharaan.SELESAI, entity.getStatus());
        assertTrue(entity.getCatatan().contains("CANCELLED:"));
        assertTrue(entity.getCatatan().contains("Equipment not available"));
        assertNotNull(entity.getTanggalSelesai());
        
        // Verify in DTO
        PemeliharaanDto cancelledDto = service.convertToDto(entity);
        assertEquals("SELESAI", cancelledDto.getStatus());
        assertTrue(cancelledDto.getCatatan().contains("CANCELLED:"));
    }

    @Test
    @DisplayName("Should preserve datetime information across conversions")
    void testDateTimePreservation() {
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 15, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 15, 17, 0);
        
        PemeliharaanDto dto = createTestMaintenanceDto(StatusPemeliharaan.SELESAI);
        dto.setTanggalMulai(startTime);
        dto.setTanggalSelesai(endTime);
        
        // Convert to entity and back
        Pemeliharaan entity = service.convertToEntity(dto);
        PemeliharaanDto convertedDto = service.convertToDto(entity);
        
        // Verify datetime preservation
        assertEquals(startTime, convertedDto.getTanggalMulai());
        assertEquals(endTime, convertedDto.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should handle long maintenance descriptions")
    void testLongDescriptions() {
        String longDescription = "This is a very detailed maintenance description that includes " +
                "multiple steps, special instructions, safety considerations, and detailed " +
                "procedures that must be followed during the maintenance process. " +
                "It may contain special characters and formatting requirements.";
        
        PemeliharaanDto dto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
        dto.setDeskripsiPemeliharaan(longDescription);
        
        // Test preservation through conversions
        Pemeliharaan entity = service.convertToEntity(dto);
        assertEquals(longDescription, entity.getDeskripsiPemeliharaan());
        
        PemeliharaanDto convertedDto = service.convertToDto(entity);
        assertEquals(longDescription, convertedDto.getDeskripsiPemeliharaan());
    }

    @Test
    @DisplayName("Should validate business rules integration")
    void testBusinessRulesIntegration() {
        PemeliharaanDto dto = createTestMaintenanceDto(StatusPemeliharaan.DIJADWALKAN);
        
        // Test required field validation
        dto.setArtefakId(null);
        assertThrows(IllegalArgumentException.class, () -> {
            service.validatePemeliharaanData(dto);
        });
        
        // Test empty description validation
        dto.setArtefakId(1L);
        dto.setDeskripsiPemeliharaan("");
        assertThrows(IllegalArgumentException.class, () -> {
            service.validatePemeliharaanData(dto);
        });
        
        // Test null description validation
        dto.setDeskripsiPemeliharaan(null);
        assertThrows(IllegalArgumentException.class, () -> {
            service.validatePemeliharaanData(dto);
        });
    }

    private PemeliharaanDto createTestMaintenanceDto(StatusPemeliharaan status) {
        PemeliharaanDto dto = new PemeliharaanDto();
        dto.setArtefakId(1L);
        dto.setPetugasId(1L);
        dto.setJenisPemeliharaan("RUTIN");
        dto.setDeskripsiPemeliharaan("Test maintenance for " + status.name());
        dto.setTanggalMulai(LocalDateTime.now());
        dto.setStatus(status.name());
        return dto;
    }
}
