package main.model.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Artefak (Artifact)
 * Used to transfer artifact data between layers
 */
public class ArtefakDto {
    
    private Long artefakId;
    private String namaArtefak;
    private String deskripsi;
    private String asal;
    private String kategori;
    private String periode;
    private String kondisi;
    private String lokasi;
    private String fotoUrl;
    private String qrCodeUrl;
    private LocalDateTime tanggalMasuk;
    private LocalDateTime tanggalDibuat;
    private LocalDateTime tanggalDiperbarui;
    private String status;
    
    /**
     * Default constructor
     */
    public ArtefakDto() {}
    
    /**
     * Constructor with essential fields
     */
    public ArtefakDto(String namaArtefak, String deskripsi, String asal, String kategori) {
        this.namaArtefak = namaArtefak;
        this.deskripsi = deskripsi;
        this.asal = asal;
        this.kategori = kategori;
        this.tanggalDibuat = LocalDateTime.now();
        this.status = "AKTIF";
    }
    
    /**
     * Constructor with all fields
     */
    public ArtefakDto(Long artefakId, String namaArtefak, String deskripsi, String asal, 
                     String kategori, String periode, String kondisi, String lokasi, 
                     String fotoUrl, LocalDateTime tanggalMasuk) {
        this.artefakId = artefakId;
        this.namaArtefak = namaArtefak;
        this.deskripsi = deskripsi;
        this.asal = asal;
        this.kategori = kategori;
        this.periode = periode;
        this.kondisi = kondisi;
        this.lokasi = lokasi;
        this.fotoUrl = fotoUrl;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalDibuat = LocalDateTime.now();
        this.status = "AKTIF";
    }
    
    // Getters and Setters
    
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
    
    public String getDeskripsi() {
        return deskripsi;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    
    public String getAsal() {
        return asal;
    }
    
    public void setAsal(String asal) {
        this.asal = asal;
    }
    
    public String getKategori() {
        return kategori;
    }
    
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    
    public String getPeriode() {
        return periode;
    }
    
    public void setPeriode(String periode) {
        this.periode = periode;
    }
    
    public String getKondisi() {
        return kondisi;
    }
    
    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }
    
    public String getLokasi() {
        return lokasi;
    }
    
    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
    
    public String getFotoUrl() {
        return fotoUrl;
    }
    
    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
    
    public String getQrCodeUrl() {
        return qrCodeUrl;
    }
    
    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }
    
    public LocalDateTime getTanggalMasuk() {
        return tanggalMasuk;
    }
    
    public void setTanggalMasuk(LocalDateTime tanggalMasuk) {
        this.tanggalMasuk = tanggalMasuk;
    }
    
    public LocalDateTime getTanggalDibuat() {
        return tanggalDibuat;
    }
    
    public void setTanggalDibuat(LocalDateTime tanggalDibuat) {
        this.tanggalDibuat = tanggalDibuat;
    }
    
    public LocalDateTime getTanggalDiperbarui() {
        return tanggalDiperbarui;
    }
    
    public void setTanggalDiperbarui(LocalDateTime tanggalDiperbarui) {
        this.tanggalDiperbarui = tanggalDiperbarui;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    // Utility methods
    
    /**
     * Check if the artifact is active
     * @return true if status is AKTIF, false otherwise
     */
    public boolean isActive() {
        return "AKTIF".equals(status);
    }
    
    /**
     * Mark artifact as active
     */
    public void activate() {
        this.status = "AKTIF";
        this.tanggalDiperbarui = LocalDateTime.now();
    }
    
    /**
     * Mark artifact as inactive
     */
    public void deactivate() {
        this.status = "NONAKTIF";
        this.tanggalDiperbarui = LocalDateTime.now();
    }
    
    /**
     * Update the last modified timestamp
     */
    public void touch() {
        this.tanggalDiperbarui = LocalDateTime.now();
    }
    
    /**
     * Get display name for the artifact
     * @return formatted display name
     */
    public String getDisplayName() {
        if (namaArtefak == null || namaArtefak.trim().isEmpty()) {
            return "Unknown Artifact";
        }
        return namaArtefak + (kategori != null ? " (" + kategori + ")" : "");
    }
    
    /**
     * Get short description for display
     * @param maxLength maximum length of description
     * @return truncated description
     */
    public String getShortDescription(int maxLength) {
        if (deskripsi == null || deskripsi.trim().isEmpty()) {
            return "No description available";
        }
        
        if (deskripsi.length() <= maxLength) {
            return deskripsi;
        }
        
        return deskripsi.substring(0, maxLength - 3) + "...";
    }
    
    @Override
    public String toString() {
        return "ArtefakDto{" +
                "artefakId=" + artefakId +
                ", namaArtefak='" + namaArtefak + '\'' +
                ", kategori='" + kategori + '\'' +
                ", asal='" + asal + '\'' +
                ", kondisi='" + kondisi + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ArtefakDto that = (ArtefakDto) obj;
        return artefakId != null ? artefakId.equals(that.artefakId) : that.artefakId == null;
    }
    
    @Override
    public int hashCode() {
        return artefakId != null ? artefakId.hashCode() : 0;
    }
}