package test.model.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import main.model.dto.ArtefakDto;
import java.time.LocalDateTime;

public class ArtefakDtoTest {
    
    private ArtefakDto artefakDto;

    @BeforeEach
    public void setUp() {
        artefakDto = new ArtefakDto();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(artefakDto);
        assertNull(artefakDto.getArtefakId());
        assertNull(artefakDto.getNamaArtefak());
        assertNull(artefakDto.getAsalDaerah());
        assertNull(artefakDto.getDeskripsiArtefak());
        assertNull(artefakDto.getGambar());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String nama = "Keris";
        String asalDaerah = "Jawa";
        String deskripsi = "Senjata tradisional";
        String gambar = "keris.jpg";
        
        ArtefakDto dto = new ArtefakDto(id, nama, asalDaerah, deskripsi, gambar);
        
        assertEquals(id, dto.getArtefakId());
        assertEquals(nama, dto.getNamaArtefak());
        assertEquals(asalDaerah, dto.getAsalDaerah());
        assertEquals(deskripsi, dto.getDeskripsiArtefak());
        assertEquals(gambar, dto.getGambar());
    }

    @Test
    public void testSettersAndGetters() {
        Long id = 1L;
        String nama = "Test Artefak";
        String asalDaerah = "Test Daerah";
        String deskripsi = "Test Deskripsi";
        String gambar = "test.jpg";
        String periode = "Test Periode";
        String status = "TERSEDIA";
        LocalDateTime tanggal = LocalDateTime.now();
        Long curatorId = 100L;
        
        artefakDto.setArtefakId(id);
        artefakDto.setNamaArtefak(nama);
        artefakDto.setAsalDaerah(asalDaerah);
        artefakDto.setDeskripsiArtefak(deskripsi);
        artefakDto.setGambar(gambar);
        artefakDto.setPeriode(periode);
        artefakDto.setStatus(status);
        artefakDto.setTanggalRegistrasi(tanggal);
        artefakDto.setCuratorId(curatorId);
        
        assertEquals(id, artefakDto.getArtefakId());
        assertEquals(nama, artefakDto.getNamaArtefak());
        assertEquals(asalDaerah, artefakDto.getAsalDaerah());
        assertEquals(deskripsi, artefakDto.getDeskripsiArtefak());
        assertEquals(gambar, artefakDto.getGambar());
        assertEquals(periode, artefakDto.getPeriode());
        assertEquals(status, artefakDto.getStatus());
        assertEquals(tanggal, artefakDto.getTanggalRegistrasi());
        assertEquals(curatorId, artefakDto.getCuratorId());
    }
}