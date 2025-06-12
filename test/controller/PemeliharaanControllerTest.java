package test.controller;

import main.controller.PemeliharaanController;
import main.model.dto.PemeliharaanDto;
import main.model.enums.StatusPemeliharaan;
import main.service.interfaces.IPemeliharaanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PemeliharaanController
 * These tests focus on business logic and controller behavior
 * Testing the maintenance management functionality
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PemeliharaanController Unit Tests")
class PemeliharaanControllerTest {

    @Mock
    private IPemeliharaanService pemeliharaanService;

    private PemeliharaanController controller;
    private PemeliharaanDto testDto;

    @BeforeEach
    void setUp() {
        controller = new PemeliharaanController(pemeliharaanService);
        
        testDto = new PemeliharaanDto();
        testDto.setPemeliharaanId(1L);
        testDto.setArtefakId(1L);
        testDto.setPetugasId(1L);
        testDto.setJenisPemeliharaan("RUTIN");
        testDto.setDeskripsiPemeliharaan("Test maintenance");
        testDto.setTanggalMulai(LocalDateTime.now());
        testDto.setStatus(StatusPemeliharaan.DIJADWALKAN.name());
        testDto.setCatatan("Initial notes");
    }

    @Test
    @DisplayName("Should create maintenance record successfully")
    void testCreatePemeliharaan_Success() {
        // Given
        when(pemeliharaanService.createPemeliharaan(testDto)).thenReturn(testDto);

        // When
        PemeliharaanDto result = controller.createPemeliharaan(testDto);

        // Then
        assertNotNull(result);
        assertEquals(testDto.getPemeliharaanId(), result.getPemeliharaanId());
        assertEquals(testDto.getArtefakId(), result.getArtefakId());
        verify(pemeliharaanService, times(1)).createPemeliharaan(testDto);
    }

    @Test
    @DisplayName("Should update maintenance record successfully")
    void testUpdatePemeliharaan_Success() {
        // Given
        Long maintenanceId = 1L;
        testDto.setDeskripsiPemeliharaan("Updated description");
        when(pemeliharaanService.updatePemeliharaan(maintenanceId, testDto)).thenReturn(testDto);

        // When
        PemeliharaanDto result = controller.updatePemeliharaan(maintenanceId, testDto);

        // Then
        assertNotNull(result);
        assertEquals("Updated description", result.getDeskripsiPemeliharaan());
        verify(pemeliharaanService, times(1)).updatePemeliharaan(maintenanceId, testDto);
    }

    @Test
    @DisplayName("Should get maintenance by ID successfully")
    void testGetPemeliharaanById_Success() {
        // Given
        Long maintenanceId = 1L;
        when(pemeliharaanService.getPemeliharaanById(maintenanceId)).thenReturn(testDto);

        // When
        PemeliharaanDto result = controller.getPemeliharaanById(maintenanceId);

        // Then
        assertNotNull(result);
        assertEquals(testDto.getPemeliharaanId(), result.getPemeliharaanId());
        verify(pemeliharaanService, times(1)).getPemeliharaanById(maintenanceId);
    }

    @Test
    @DisplayName("Should get all maintenance records successfully")
    void testGetAllPemeliharaan_Success() {
        // Given
        PemeliharaanDto dto2 = new PemeliharaanDto();
        dto2.setPemeliharaanId(2L);
        dto2.setArtefakId(2L);
        dto2.setDeskripsiPemeliharaan("Second maintenance");
        
        List<PemeliharaanDto> expectedList = Arrays.asList(testDto, dto2);
        when(pemeliharaanService.getAllPemeliharaan()).thenReturn(expectedList);

        // When
        List<PemeliharaanDto> result = controller.getAllPemeliharaan();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testDto.getPemeliharaanId(), result.get(0).getPemeliharaanId());
        assertEquals(dto2.getPemeliharaanId(), result.get(1).getPemeliharaanId());
        verify(pemeliharaanService, times(1)).getAllPemeliharaan();
    }

    @Test
    @DisplayName("Should start maintenance successfully")
    void testMulaiPemeliharaan_Success() {
        // Given
        Long maintenanceId = 1L;
        PemeliharaanDto updatedDto = new PemeliharaanDto();
        updatedDto.setPemeliharaanId(maintenanceId);
        updatedDto.setStatus(StatusPemeliharaan.SEDANG_BERLANGSUNG.name());
        
        when(pemeliharaanService.mulaiPemeliharaan(maintenanceId)).thenReturn(updatedDto);

        // When
        PemeliharaanDto result = controller.mulaiPemeliharaan(maintenanceId);

        // Then
        assertNotNull(result);
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG.name(), result.getStatus());
        verify(pemeliharaanService, times(1)).mulaiPemeliharaan(maintenanceId);
    }

    @Test
    @DisplayName("Should complete maintenance successfully")
    void testSelesaikanPemeliharaan_Success() {
        // Given
        Long maintenanceId = 1L;
        String completionNotes = "Maintenance completed successfully";
        PemeliharaanDto completedDto = new PemeliharaanDto();
        completedDto.setPemeliharaanId(maintenanceId);
        completedDto.setStatus(StatusPemeliharaan.SELESAI.name());
        completedDto.setCatatan(completionNotes);
        completedDto.setTanggalSelesai(LocalDateTime.now());
        
        when(pemeliharaanService.selesaikanPemeliharaan(maintenanceId, completionNotes)).thenReturn(completedDto);

        // When
        PemeliharaanDto result = controller.selesaikanPemeliharaan(maintenanceId, completionNotes);

        // Then
        assertNotNull(result);
        assertEquals(StatusPemeliharaan.SELESAI.name(), result.getStatus());
        assertEquals(completionNotes, result.getCatatan());
        assertNotNull(result.getTanggalSelesai());
        verify(pemeliharaanService, times(1)).selesaikanPemeliharaan(maintenanceId, completionNotes);
    }

    @Test
    @DisplayName("Should record maintenance action successfully")
    void testCatatTindakanPemeliharaan_Success() {
        // Given
        Long maintenanceId = 1L;
        String tindakan = "Cleaning completed";
        testDto.setCatatan("Previous note");
        
        when(pemeliharaanService.getPemeliharaanById(maintenanceId)).thenReturn(testDto);
        when(pemeliharaanService.updatePemeliharaan(eq(maintenanceId), any(PemeliharaanDto.class)))
            .thenReturn(testDto);

        // When
        assertDoesNotThrow(() -> {
            controller.catatTindakanPemeliharaan(maintenanceId, tindakan);
        });

        // Then
        verify(pemeliharaanService, times(1)).getPemeliharaanById(maintenanceId);
        verify(pemeliharaanService, times(1)).updatePemeliharaan(eq(maintenanceId), any(PemeliharaanDto.class));
    }

    @Test
    @DisplayName("Should get maintenance by status successfully")
    void testGetPemeliharaanByStatus_Success() {
        // Given
        StatusPemeliharaan status = StatusPemeliharaan.DIJADWALKAN;
        List<PemeliharaanDto> expectedList = Arrays.asList(testDto);
        when(pemeliharaanService.getPemeliharaanByStatus(status)).thenReturn(expectedList);

        // When
        List<PemeliharaanDto> result = controller.getPemeliharaanByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDto.getStatus(), result.get(0).getStatus());
        verify(pemeliharaanService, times(1)).getPemeliharaanByStatus(status);
    }

    @Test
    @DisplayName("Should get upcoming maintenance successfully")
    void testGetUpcomingPemeliharaan_Success() {
        // Given
        List<PemeliharaanDto> upcomingList = Arrays.asList(testDto);
        when(pemeliharaanService.getUpcomingPemeliharaan()).thenReturn(upcomingList);

        // When
        List<PemeliharaanDto> result = controller.getUpcomingPemeliharaan();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(pemeliharaanService, times(1)).getUpcomingPemeliharaan();
    }

    @Test
    @DisplayName("Should count maintenance by status successfully")
    void testCountPemeliharaanByStatus_Success() {
        // Given
        StatusPemeliharaan status = StatusPemeliharaan.SELESAI;
        long expectedCount = 5L;
        when(pemeliharaanService.countPemeliharaanByStatus(status)).thenReturn(expectedCount);

        // When
        long result = controller.countPemeliharaanByStatus(status);

        // Then
        assertEquals(expectedCount, result);
        verify(pemeliharaanService, times(1)).countPemeliharaanByStatus(status);
    }

    @Test
    @DisplayName("Should validate maintenance data creation")
    void testValidateMaintenanceCreation() {
        // Given
        PemeliharaanDto validDto = new PemeliharaanDto();
        validDto.setArtefakId(1L);
        validDto.setPetugasId(1L);
        validDto.setJenisPemeliharaan("PREVENTIF");
        validDto.setDeskripsiPemeliharaan("Regular cleaning");
        validDto.setTanggalMulai(LocalDateTime.now().plusDays(1));
        
        when(pemeliharaanService.createPemeliharaan(validDto)).thenReturn(validDto);

        // When
        PemeliharaanDto result = controller.createPemeliharaan(validDto);

        // Then
        assertNotNull(result);
        assertEquals("PREVENTIF", result.getJenisPemeliharaan());
        assertEquals("Regular cleaning", result.getDeskripsiPemeliharaan());
        verify(pemeliharaanService, times(1)).createPemeliharaan(validDto);
    }

    @Test
    @DisplayName("Should update maintenance status successfully")
    void testUpdateStatusPemeliharaan_Success() {
        // Given
        Long maintenanceId = 1L;
        String newStatus = "SELESAI";
        
        when(pemeliharaanService.getPemeliharaanById(maintenanceId)).thenReturn(testDto);
        when(pemeliharaanService.updatePemeliharaan(eq(maintenanceId), any(PemeliharaanDto.class)))
            .thenReturn(testDto);

        // When
        assertDoesNotThrow(() -> {
            controller.updateStatusPemeliharaan(maintenanceId, newStatus);
        });

        // Then
        verify(pemeliharaanService, times(1)).getPemeliharaanById(maintenanceId);
        verify(pemeliharaanService, times(1)).updatePemeliharaan(eq(maintenanceId), any(PemeliharaanDto.class));
    }
}