package main.model.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Artefak
 */
public class ArtefakDto {
    private Long artefakId;          // ID
    private String namaArtefak;      // Name
    private String asalDaerah;       // From
    private String deskripsiArtefak; // Description
    private String gambar;           // Images
    
    // Additional fields retained for compatibility
    private String periode;
    private String status;
    private LocalDateTime tanggalRegistrasi;
    private Long curatorId;
    
    // Constructors
    public ArtefakDto() {
    }
    
    public ArtefakDto(Long artefakId, String namaArtefak, String asalDaerah, String deskripsiArtefak, String gambar) {
        this.artefakId = artefakId;
        this.namaArtefak = namaArtefak;
        this.asalDaerah = asalDaerah;
        this.deskripsiArtefak = deskripsiArtefak;
        this.gambar = gambar;
    }
    
    // Complete constructor
    public ArtefakDto(Long artefakId, String namaArtefak, String asalDaerah, String deskripsiArtefak, 
                      String gambar, String periode, String status, LocalDateTime tanggalRegistrasi, Long curatorId) {
        this.artefakId = artefakId;
        this.namaArtefak = namaArtefak;
        this.asalDaerah = asalDaerah;
        this.deskripsiArtefak = deskripsiArtefak;
        this.gambar = gambar;
        this.periode = periode;
        this.status = status;
        this.tanggalRegistrasi = tanggalRegistrasi;
        this.curatorId = curatorId;
    }
    
    // Getters and setters
    public Long getArtefakId() {
        return artefakId;
    }
    
    public void setArtefakId(Long artefakId) {
        this.artefakId = artefakId;
    }
    
    public String getNamaArtefak() {
        return namaArtefak;
    }
    
    public void setNamaArtefak(String namaArtefak) {
        this.namaArtefak = namaArtefak;
    }
    
    public String getAsalDaerah() {
        return asalDaerah;
    }
    
    public void setAsalDaerah(String asalDaerah) {
        this.asalDaerah = asalDaerah;
    }
    
    public String getDeskripsiArtefak() {
        return deskripsiArtefak;
    }
    
    public void setDeskripsiArtefak(String deskripsiArtefak) {
        this.deskripsiArtefak = deskripsiArtefak;
    }
    
    public String getGambar() {
        return gambar;
    }
    
    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
    
    public String getPeriode() {
        return periode;
    }
    
    public void setPeriode(String periode) {
        this.periode = periode;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getTanggalRegistrasi() {
        return tanggalRegistrasi;
    }
    
    public void setTanggalRegistrasi(LocalDateTime tanggalRegistrasi) {
        this.tanggalRegistrasi = tanggalRegistrasi;
    }
    
    public Long getCuratorId() {
        return curatorId;
    }
    
    public void setCuratorId(Long curatorId) {
        this.curatorId = curatorId;
    }
    
    @Override
    public String toString() {
        return "ArtefakDto{" +
                "artefakId=" + artefakId +
                ", namaArtefak='" + namaArtefak + '\'' +
                ", asalDaerah='" + asalDaerah + '\'' +
                ", deskripsiArtefak='" + deskripsiArtefak + '\'' +
                ", gambar='" + gambar + '\'' +
                '}';
    }
}