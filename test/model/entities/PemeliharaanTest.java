package test.model.entities;

import main.model.entities.Pemeliharaan;
import main.model.enums.StatusPemeliharaan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Pemeliharaan entity
 * Tests the business logic within the entity
 */
@DisplayName("Pemeliharaan Entity Tests")
class PemeliharaanTest {

    private Pemeliharaan pemeliharaan;

    @BeforeEach
    void setUp() {
        pemeliharaan = new Pemeliharaan();
        pemeliharaan.setPemeliharaanId(1L);
        pemeliharaan.setArtefakId(1L);
        pemeliharaan.setPetugasId(1L);
        pemeliharaan.setJenisPemeliharaan("RUTIN");
        pemeliharaan.setDeskripsiPemeliharaan("Test maintenance");
        pemeliharaan.setTanggalMulai(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create Pemeliharaan with default values")
    void testDefaultConstructor() {
        // When
        Pemeliharaan newPemeliharaan = new Pemeliharaan();

        // Then
        assertEquals(StatusPemeliharaan.DIJADWALKAN, newPemeliharaan.getStatus());
        assertNotNull(newPemeliharaan.getTanggalMulai());
        assertNull(newPemeliharaan.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should create Pemeliharaan with constructor parameters")
    void testParameterizedConstructor() {
        // Given
        Long artefakId = 2L;
        Long petugasId = 3L;
        String jenis = "PREVENTIF";
        String deskripsi = "Preventive maintenance";
        LocalDateTime tanggalMulai = LocalDateTime.now().plusDays(1);

        // When
        Pemeliharaan newPemeliharaan = new Pemeliharaan(artefakId, petugasId, jenis, deskripsi, tanggalMulai);

        // Then
        assertEquals(artefakId, newPemeliharaan.getArtefakId());
        assertEquals(petugasId, newPemeliharaan.getPetugasId());
        assertEquals(jenis, newPemeliharaan.getJenisPemeliharaan());
        assertEquals(deskripsi, newPemeliharaan.getDeskripsiPemeliharaan());
        assertEquals(tanggalMulai, newPemeliharaan.getTanggalMulai());
        assertEquals(StatusPemeliharaan.DIJADWALKAN, newPemeliharaan.getStatus());
    }

    @Test
    @DisplayName("Should start maintenance correctly")
    void testMulaiPemeliharaan() {
        // Given - Initially scheduled
        assertEquals(StatusPemeliharaan.DIJADWALKAN, pemeliharaan.getStatus());

        // When
        pemeliharaan.mulaiPemeliharaan();

        // Then
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, pemeliharaan.getStatus());
    }

    @Test
    @DisplayName("Should complete maintenance correctly")
    void testSelesaikanPemeliharaan() {
        // Given
        String completionNote = "Maintenance completed successfully";
        pemeliharaan.mulaiPemeliharaan(); // Start first

        // When
        pemeliharaan.selesaikanPemeliharaan(completionNote);

        // Then
        assertEquals(StatusPemeliharaan.SELESAI, pemeliharaan.getStatus());
        assertEquals(completionNote, pemeliharaan.getCatatan());
        assertNotNull(pemeliharaan.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should cancel maintenance correctly")
    void testBatalkanPemeliharaan() {
        // Given
        String cancelNote = "Equipment unavailable";

        // When
        pemeliharaan.batalkanPemeliharaan(cancelNote);

        // Then
        assertEquals(StatusPemeliharaan.SELESAI, pemeliharaan.getStatus());
        assertTrue(pemeliharaan.getCatatan().contains("CANCELLED:"));
        assertTrue(pemeliharaan.getCatatan().contains(cancelNote));
        assertNotNull(pemeliharaan.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void testGettersAndSetters() {
        // Given
        Long id = 10L;
        Long artefakId = 20L;
        Long petugasId = 30L;
        String jenis = "KOREKTIF";
        String deskripsi = "Repair damage";
        LocalDateTime tanggalMulai = LocalDateTime.now();
        LocalDateTime tanggalSelesai = LocalDateTime.now().plusHours(2);
        StatusPemeliharaan status = StatusPemeliharaan.SELESAI;
        String catatan = "Work completed";

        // When
        pemeliharaan.setPemeliharaanId(id);
        pemeliharaan.setArtefakId(artefakId);
        pemeliharaan.setPetugasId(petugasId);
        pemeliharaan.setJenisPemeliharaan(jenis);
        pemeliharaan.setDeskripsiPemeliharaan(deskripsi);
        pemeliharaan.setTanggalMulai(tanggalMulai);
        pemeliharaan.setTanggalSelesai(tanggalSelesai);
        pemeliharaan.setStatus(status);
        pemeliharaan.setCatatan(catatan);

        // Then
        assertEquals(id, pemeliharaan.getPemeliharaanId());
        assertEquals(artefakId, pemeliharaan.getArtefakId());
        assertEquals(petugasId, pemeliharaan.getPetugasId());
        assertEquals(jenis, pemeliharaan.getJenisPemeliharaan());
        assertEquals(deskripsi, pemeliharaan.getDeskripsiPemeliharaan());
        assertEquals(tanggalMulai, pemeliharaan.getTanggalMulai());
        assertEquals(tanggalSelesai, pemeliharaan.getTanggalSelesai());
        assertEquals(status, pemeliharaan.getStatus());
        assertEquals(catatan, pemeliharaan.getCatatan());
    }

    @Test
    @DisplayName("Should handle status transitions correctly")
    void testStatusTransitions() {
        // Initially DIJADWALKAN
        assertEquals(StatusPemeliharaan.DIJADWALKAN, pemeliharaan.getStatus());

        // Start maintenance
        pemeliharaan.mulaiPemeliharaan();
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, pemeliharaan.getStatus());

        // Complete maintenance
        pemeliharaan.selesaikanPemeliharaan("Work completed");
        assertEquals(StatusPemeliharaan.SELESAI, pemeliharaan.getStatus());
        assertNotNull(pemeliharaan.getTanggalSelesai());
    }

    @Test
    @DisplayName("Should handle maintenance workflow from scheduled to completed")
    void testCompleteMaintenanceWorkflow() {
        // Given - New maintenance is scheduled
        Pemeliharaan newMaintenance = new Pemeliharaan(1L, 1L, "RUTIN", "Daily cleaning", LocalDateTime.now());
        
        // Then - Should start as scheduled
        assertEquals(StatusPemeliharaan.DIJADWALKAN, newMaintenance.getStatus());
        assertNull(newMaintenance.getTanggalSelesai());

        // When - Start maintenance
        newMaintenance.mulaiPemeliharaan();

        // Then - Should be in progress
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, newMaintenance.getStatus());

        // When - Complete maintenance
        String completionNotes = "Cleaning completed, all artifacts dusted";
        newMaintenance.selesaikanPemeliharaan(completionNotes);

        // Then - Should be completed
        assertEquals(StatusPemeliharaan.SELESAI, newMaintenance.getStatus());
        assertEquals(completionNotes, newMaintenance.getCatatan());
        assertNotNull(newMaintenance.getTanggalSelesai());
    }
}
