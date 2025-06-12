package test.controller;

import main.controller.PameranController;
import main.model.dto.ArtefakDto;
import main.model.dto.PameranDto;
import main.service.interfaces.IArtefakService;
import main.service.interfaces.IPameranService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Pameran Controller Tests")
public class PameranControllerTest {

    @Mock
    private IPameranService pameranService;

    @Mock
    private IArtefakService artefakService;

    private PameranController pameranController;
    private PameranDto testPameranDto;
    private ArtefakDto testArtefakDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pameranController = new PameranController(pameranService, artefakService);

        testPameranDto = new PameranDto();
        testPameranDto.setPameranId(1L);
        testPameranDto.setNamaPameran("Test Exhibition");
        testPameranDto.setDeskripsiPameran("Test Description");
        testPameranDto.setTanggalMulai(LocalDateTime.now());
        testPameranDto.setTanggalSelesai(LocalDateTime.now().plusDays(30));
        testPameranDto.setArtefakIds(Arrays.asList(1L, 2L, 3L));

        // Setup test ArtefakDto
        testArtefakDto = new ArtefakDto();
        testArtefakDto.setArtefakId(1L);
        testArtefakDto.setNamaArtefak("Test Artifact");
        testArtefakDto.setDeskripsiArtefak("Test Artifact Description");
    }

    @Test
    @DisplayName("Should create pameran successfully")
    void testCreatePameran_Success() {
        // Given
        when(pameranService.createPameran(any(PameranDto.class))).thenReturn(testPameranDto);

        // When
        PameranDto result = pameranController.createPameran(testPameranDto);

        // Then
        assertNotNull(result);
        assertEquals(testPameranDto.getNamaPameran(), result.getNamaPameran());
        verify(pameranService, times(1)).createPameran(any(PameranDto.class));

        System.out.println("✅ Controller Test: Create Pameran Success");
    }

    @Test
    @DisplayName("Should get all pamerans successfully")
    void testGetAllPamerans_Success() {
        // Given
        when(pameranService.getAllPamerans()).thenReturn(Arrays.asList(testPameranDto));

        // When
        List<PameranDto> result = pameranController.getAllPamerans();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(pameranService, times(1)).getAllPamerans();

        System.out.println("✅ Controller Test: Get All Pamerans Success");
    }

    @Test
    @DisplayName("Should get pameran by ID successfully")
    void testGetPameranById_Success() {
        // Given
        when(pameranService.getPameranById(1L)).thenReturn(testPameranDto);

        // When
        PameranDto result = pameranController.getPameranById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testPameranDto.getNamaPameran(), result.getNamaPameran());
        verify(pameranService, times(1)).getPameranById(1L);

        System.out.println("✅ Controller Test: Get Pameran By ID Success");
    }

    @Test
    @DisplayName("Should handle controller exceptions gracefully")
    void testGetPameranById_ControllerException() {
        // Given
        when(pameranService.getPameranById(999L))
                .thenThrow(new RuntimeException("Exhibition not found"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pameranController.getPameranById(999L);
        });

        assertTrue(exception.getMessage().contains("Failed to get exhibition") ||
                exception.getMessage().contains("Exhibition not found"));
        verify(pameranService, times(1)).getPameranById(999L);

        System.out.println("✅ Controller Test: Handle Exception Success");
    }

    @Test
    @DisplayName("Should update pameran successfully")
    void testUpdatePameran_Success() {
        // Given
        when(pameranService.updatePameran(eq(1L), any(PameranDto.class))).thenReturn(testPameranDto);

        // When
        PameranDto result = pameranController.updatePameran(1L, testPameranDto);

        // Then
        assertNotNull(result);
        verify(pameranService, times(1)).updatePameran(1L, testPameranDto);

        System.out.println("✅ Controller Test: Update Pameran Success");
    }

    @Test
    @DisplayName("Should delete pameran successfully")
    void testDeletePameran_Success() {
        // Given
        doNothing().when(pameranService).deletePameran(1L);

        // When & Then
        assertDoesNotThrow(() -> pameranController.deletePameran(1L));
        verify(pameranService, times(1)).deletePameran(1L);

        System.out.println("✅ Controller Test: Delete Pameran Success");
    }

    @Test
    @DisplayName("Should search pamerans successfully")
    void testSearchPamerans_Success() {
        // Given
        when(pameranService.searchPameransByName("Test")).thenReturn(Arrays.asList(testPameranDto));

        // When
        List<PameranDto> result = pameranController.searchPamerans("Test");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(pameranService, times(1)).searchPameransByName("Test");

        System.out.println("✅ Controller Test: Search Pamerans Success");
    }

    @Test
    @DisplayName("Should add artifact to pameran successfully")
    void testAddArtefakToPameran_Success() {
        // Given - Mock all the methods that might be called
        when(pameranService.getPameranById(1L)).thenReturn(testPameranDto); // Mock the existence check
        when(artefakService.getArtefakById(4L)).thenReturn(testArtefakDto); // Mock artifact existence check
        doNothing().when(pameranService).addArtefakToPameran(1L, 4L); // Mock the actual add operation

        // When & Then
        assertDoesNotThrow(() -> pameranController.addArtefakToPameran(1L, 4L));

        // Verify that the service method was called
        verify(pameranService, times(1)).addArtefakToPameran(1L, 4L);

        System.out.println("✅ Controller Test: Add Artifact Success");
    }

    @Test
    @DisplayName("Should handle add artifact failure when pameran not found")
    void testAddArtefakToPameran_PameranNotFound() {
        // Given - Mock pameran not found
        when(pameranService.getPameranById(999L)).thenReturn(null);

        // When & Then - Expect exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pameranController.addArtefakToPameran(999L, 4L);
        });

        assertTrue(exception.getMessage().contains("Exhibition not found") ||
                exception.getMessage().contains("Failed to add artifact"));

        System.out.println("✅ Controller Test: Handle Add Artifact - Pameran Not Found Success");
    }

    @Test
    @DisplayName("Should handle add artifact failure when artifact not found")
    void testAddArtefakToPameran_ArtefakNotFound() {
        // Given - Mock pameran exists but artifact doesn't
        when(pameranService.getPameranById(1L)).thenReturn(testPameranDto);
        when(artefakService.getArtefakById(999L)).thenReturn(null);

        // When & Then - Expect exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pameranController.addArtefakToPameran(1L, 999L);
        });

        assertTrue(exception.getMessage().contains("Artifact not found") ||
                exception.getMessage().contains("Failed to add artifact"));

        System.out.println("✅ Controller Test: Handle Add Artifact - Artifact Not Found Success");
    }

    @Test
    @DisplayName("Should get artifact name by ID successfully")
    void testGetArtefakNameById_Success() {
        // Given - Mock the correct method that exists in IArtefakService
        when(artefakService.getArtefakById(1L)).thenReturn(testArtefakDto);

        // When
        String result = pameranController.getArtefakNameById(1L);

        // Then
        assertEquals("Test Artifact", result);
        verify(artefakService, times(1)).getArtefakById(1L);

        System.out.println("✅ Controller Test: Get Artifact Name Success");
    }

    @Test
    @DisplayName("Should handle artifact not found gracefully")
    void testGetArtefakNameById_NotFound() {
        // Given - Mock service to return null
        when(artefakService.getArtefakById(999L)).thenReturn(null);

        // When
        String result = pameranController.getArtefakNameById(999L);

        // Then
        assertEquals("Unknown Artifact", result);
        verify(artefakService, times(1)).getArtefakById(999L);

        System.out.println("✅ Controller Test: Handle Artifact Not Found Success");
    }

    @Test
    @DisplayName("Should handle artifact service exception gracefully")
    void testGetArtefakNameById_ServiceException() {
        // Given - Mock service to throw exception
        when(artefakService.getArtefakById(1L))
                .thenThrow(new RuntimeException("Service error"));

        // When
        String result = pameranController.getArtefakNameById(1L);

        // Then
        assertEquals("Unknown Artifact", result);
        verify(artefakService, times(1)).getArtefakById(1L);

        System.out.println("✅ Controller Test: Handle Service Exception Success");
    }
}
