package test.model.entities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import main.model.entities.Artefak;
import main.model.enums.StatusArtefak;
import java.time.LocalDateTime;

public class ArtefakTest {
    
    private Artefak artefak;

    @BeforeEach
    public void setUp() {
        artefak = new Artefak();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(artefak.getHistoryPemeliharaan());
        assertNotNull(artefak.getTanggalRegistrasi());
        assertEquals(StatusArtefak.TERSEDIA, artefak.getStatus());
        assertTrue(artefak.getHistoryPemeliharaan().isEmpty());
    }

    @Test
    public void testParameterizedConstructor() {
        Artefak artefak = new Artefak("Keris", "Senjata tradisional", "Jawa", "Abad 15");
        
        assertEquals("Keris", artefak.getNamaArtefak());
        assertEquals("Senjata tradisional", artefak.getDeskripsiArtefak());
        assertEquals("Jawa", artefak.getAsalDaerah());
        assertEquals("Abad 15", artefak.getPeriode());
        assertEquals(StatusArtefak.TERSEDIA, artefak.getStatus());
    }

    @Test
    public void testSettersAndGetters() {
        Long id = 1L;
        String nama = "Test Artefak";
        String deskripsi = "Test Deskripsi";
        String asalDaerah = "Test Daerah";
        String periode = "Test Periode";
        Long curatorId = 100L;
        LocalDateTime tanggal = LocalDateTime.now();
        
        artefak.setArtefakId(id);
        artefak.setNamaArtefak(nama);
        artefak.setDeskripsiArtefak(deskripsi);
        artefak.setAsalDaerah(asalDaerah);
        artefak.setPeriode(periode);
        artefak.setCuratorId(curatorId);
        artefak.setTanggalRegistrasi(tanggal);
        
        assertEquals(id, artefak.getArtefakId());
        assertEquals(nama, artefak.getNamaArtefak());
        assertEquals(deskripsi, artefak.getDeskripsiArtefak());
        assertEquals(asalDaerah, artefak.getAsalDaerah());
        assertEquals(periode, artefak.getPeriode());
        assertEquals(curatorId, artefak.getCuratorId());
        assertEquals(tanggal, artefak.getTanggalRegistrasi());
    }
}