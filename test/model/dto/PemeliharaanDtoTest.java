package test.model.dto;

import main.model.dto.PemeliharaanDto;
import main.model.enums.StatusPemeliharaan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PemeliharaanDto
 * Tests data transfer object functionality
 */
@DisplayName("PemeliharaanDto Tests")
class PemeliharaanDtoTest {

    private PemeliharaanDto dto;

    @BeforeEach
    void setUp() {
        dto = new PemeliharaanDto();
    }

    @Test
    @DisplayName("Should create empty DTO with default constructor")
    void testDefaultConstructor() {
        // When
        PemeliharaanDto newDto = new PemeliharaanDto();

        // Then
        assertNull(newDto.getPemeliharaanId());
        assertNull(newDto.getArtefakId());
        assertNull(newDto.getPetugasId());
        assertNull(newDto.getJenisPemeliharaan());
        assertNull(newDto.getDeskripsiPemeliharaan());
        assertNull(newDto.getTanggalMulai());
        assertNull(newDto.getTanggalSelesai());
        assertNull(newDto.getStatus());
        assertNull(newDto.getCatatan());
    }

    @Test
    @DisplayName("Should create DTO with constructor parameters")
    void testParameterizedConstructor() {
        // Given
        Long artefakId = 1L;
        Long petugasId = 2L;
        String jenis = "RUTIN";
        String deskripsi = "Daily cleaning";
        LocalDateTime tanggalMulai = LocalDateTime.now();

        // When
        PemeliharaanDto newDto = new PemeliharaanDto(artefakId, petugasId, jenis, deskripsi, tanggalMulai);

        // Then
        assertEquals(artefakId, newDto.getArtefakId());
        assertEquals(petugasId, newDto.getPetugasId());
        assertEquals(jenis, newDto.getJenisPemeliharaan());
        assertEquals(deskripsi, newDto.getDeskripsiPemeliharaan());
        assertEquals(tanggalMulai, newDto.getTanggalMulai());
    }

    @Test
    @DisplayName("Should set and get pemeliharaanId correctly")
    void testPemeliharaanId() {
        // Given
        Long expectedId = 123L;

        // When
        dto.setPemeliharaanId(expectedId);

        // Then
        assertEquals(expectedId, dto.getPemeliharaanId());
    }

    @Test
    @DisplayName("Should set and get artefakId correctly")
    void testArtefakId() {
        // Given
        Long expectedId = 456L;

        // When
        dto.setArtefakId(expectedId);

        // Then
        assertEquals(expectedId, dto.getArtefakId());
    }

    @Test
    @DisplayName("Should set and get namaArtefak correctly")
    void testNamaArtefak() {
        // Given
        String expectedName = "Ancient Vase";

        // When
        dto.setNamaArtefak(expectedName);

        // Then
        assertEquals(expectedName, dto.getNamaArtefak());
    }

    @Test
    @DisplayName("Should set and get petugasId correctly")
    void testPetugasId() {
        // Given
        Long expectedId = 789L;

        // When
        dto.setPetugasId(expectedId);

        // Then
        assertEquals(expectedId, dto.getPetugasId());
    }

    @Test
    @DisplayName("Should set and get namaPetugas correctly")
    void testNamaPetugas() {
        // Given
        String expectedName = "John Doe";

        // When
        dto.setNamaPetugas(expectedName);

        // Then
        assertEquals(expectedName, dto.getNamaPetugas());
    }

    @Test
    @DisplayName("Should set and get jenisPemeliharaan correctly")
    void testJenisPemeliharaan() {
        // Given
        String expectedType = "PREVENTIF";

        // When
        dto.setJenisPemeliharaan(expectedType);

        // Then
        assertEquals(expectedType, dto.getJenisPemeliharaan());
    }

    @Test
    @DisplayName("Should set and get deskripsiPemeliharaan correctly")
    void testDeskripsiPemeliharaan() {
        // Given
        String expectedDescription = "Regular cleaning and inspection";

        // When
        dto.setDeskripsiPemeliharaan(expectedDescription);

        // Then
        assertEquals(expectedDescription, dto.getDeskripsiPemeliharaan());
    }

    @Test
    @DisplayName("Should set and get tanggalMulai correctly")
    void testTanggalMulai() {
        // Given
        LocalDateTime testDateTime = LocalDateTime.of(2025, 1, 15, 10, 30);

        // When
        dto.setTanggalMulai(testDateTime);

        // Then
        assertEquals(testDateTime, dto.getTanggalMulai());
    }

    @Test
    @DisplayName("Should set and get tanggalSelesai correctly")
    void testTanggalSelesai() {
        // Given
        LocalDateTime testDateTime = LocalDateTime.of(2025, 1, 15, 15, 45);

        // When
        dto.setTanggalSelesai(testDateTime);

        // Then
        assertEquals(testDateTime, dto.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should set and get status correctly")
    void testStatus() {
        // Given
        String expectedStatus = StatusPemeliharaan.DIJADWALKAN.name();

        // When
        dto.setStatus(expectedStatus);

        // Then
        assertEquals(expectedStatus, dto.getStatus());
    }

    @Test
    @DisplayName("Should set and get catatan correctly")
    void testCatatan() {
        // Given
        String expectedNote = "Special handling required";

        // When
        dto.setCatatan(expectedNote);

        // Then
        assertEquals(expectedNote, dto.getCatatan());
    }

    @Test
    @DisplayName("Should handle all properties in a complete maintenance workflow")
    void testCompleteMaintenanceWorkflow() {
        // Given - Setup complete maintenance data
        Long pemeliharaanId = 1L;
        Long artefakId = 100L;
        String namaArtefak = "Roman Statue";
        Long petugasId = 50L;
        String namaPetugas = "Jane Smith";
        String jenisPemeliharaan = "KOREKTIF";
        String deskripsiPemeliharaan = "Repair damaged base";
        LocalDateTime tanggalMulai = LocalDateTime.now().minusDays(2);
        LocalDateTime tanggalSelesai = LocalDateTime.now();
        String status = StatusPemeliharaan.SELESAI.name();
        String catatan = "Base repaired and reinforced";

        // When - Set all properties
        dto.setPemeliharaanId(pemeliharaanId);
        dto.setArtefakId(artefakId);
        dto.setNamaArtefak(namaArtefak);
        dto.setPetugasId(petugasId);
        dto.setNamaPetugas(namaPetugas);
        dto.setJenisPemeliharaan(jenisPemeliharaan);
        dto.setDeskripsiPemeliharaan(deskripsiPemeliharaan);
        dto.setTanggalMulai(tanggalMulai);
        dto.setTanggalSelesai(tanggalSelesai);
        dto.setStatus(status);
        dto.setCatatan(catatan);

        // Then - Verify all properties
        assertEquals(pemeliharaanId, dto.getPemeliharaanId());
        assertEquals(artefakId, dto.getArtefakId());
        assertEquals(namaArtefak, dto.getNamaArtefak());
        assertEquals(petugasId, dto.getPetugasId());
        assertEquals(namaPetugas, dto.getNamaPetugas());
        assertEquals(jenisPemeliharaan, dto.getJenisPemeliharaan());
        assertEquals(deskripsiPemeliharaan, dto.getDeskripsiPemeliharaan());
        assertEquals(tanggalMulai, dto.getTanggalMulai());
        assertEquals(tanggalSelesai, dto.getTanggalSelesai());
        assertEquals(status, dto.getStatus());
        assertEquals(catatan, dto.getCatatan());
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void testNullValues() {
        // When - Set null values
        dto.setPemeliharaanId(null);
        dto.setArtefakId(null);
        dto.setNamaArtefak(null);
        dto.setPetugasId(null);
        dto.setNamaPetugas(null);
        dto.setJenisPemeliharaan(null);
        dto.setDeskripsiPemeliharaan(null);

        // Then - Should handle nulls gracefully
        assertNull(dto.getPemeliharaanId());
        assertNull(dto.getArtefakId());
        assertNull(dto.getNamaArtefak());
        assertNull(dto.getPetugasId());
        assertNull(dto.getNamaPetugas());
        assertNull(dto.getJenisPemeliharaan());
        assertNull(dto.getDeskripsiPemeliharaan());
    }

    @Test
    @DisplayName("Should support maintenance status transitions in DTO")
    void testMaintenanceStatusTransitions() {
        // Test DIJADWALKAN status
        dto.setStatus(StatusPemeliharaan.DIJADWALKAN.name());
        assertEquals(StatusPemeliharaan.DIJADWALKAN.name(), dto.getStatus());

        // Test SEDANG_BERLANGSUNG status  
        dto.setStatus(StatusPemeliharaan.SEDANG_BERLANGSUNG.name());
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG.name(), dto.getStatus());

        // Test SELESAI status
        dto.setStatus(StatusPemeliharaan.SELESAI.name());
        assertEquals(StatusPemeliharaan.SELESAI.name(), dto.getStatus());
    }
}
