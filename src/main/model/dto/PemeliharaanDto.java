package main.model.dto;

import java.time.LocalDateTime;

public class PemeliharaanDto {
    private Long pemeliharaanId;
    private Long artefakId;
    private String namaArtefak; // Added for UI display
    private Long petugasId;
    private String namaPetugas; // Added for UI display
    private String jenisPemeliharaan;
    private String deskripsiPemeliharaan;
    private LocalDateTime tanggalMulai;
    private LocalDateTime tanggalSelesai;
    private String status;
    private String catatan;
    
    // Default constructor
    public PemeliharaanDto() {}
    
    // Constructor with required fields
    public PemeliharaanDto(Long artefakId, Long petugasId, String jenisPemeliharaan, 
                          String deskripsiPemeliharaan, LocalDateTime tanggalMulai) {
        this.artefakId = artefakId;
        this.petugasId = petugasId;
        this.jenisPemeliharaan = jenisPemeliharaan;
        this.deskripsiPemeliharaan = deskripsiPemeliharaan;
        this.tanggalMulai = tanggalMulai;
    }
    
    // Getters and Setters
    public Long getPemeliharaanId() { return pemeliharaanId; }
    public void setPemeliharaanId(Long pemeliharaanId) { this.pemeliharaanId = pemeliharaanId; }
    
    public Long getArtefakId() { return artefakId; }
    public void setArtefakId(Long artefakId) { this.artefakId = artefakId; }
    
    public String getNamaArtefak() { return namaArtefak; }
    public void setNamaArtefak(String namaArtefak) { this.namaArtefak = namaArtefak; }
    
    public Long getPetugasId() { return petugasId; }
    public void setPetugasId(Long petugasId) { this.petugasId = petugasId; }
    
    public String getNamaPetugas() { return namaPetugas; }
    public void setNamaPetugas(String namaPetugas) { this.namaPetugas = namaPetugas; }
    
    public String getJenisPemeliharaan() { return jenisPemeliharaan; }
    public void setJenisPemeliharaan(String jenisPemeliharaan) { 
        this.jenisPemeliharaan = jenisPemeliharaan; 
    }
    
    public String getDeskripsiPemeliharaan() { return deskripsiPemeliharaan; }
    public void setDeskripsiPemeliharaan(String deskripsiPemeliharaan) { 
        this.deskripsiPemeliharaan = deskripsiPemeliharaan; 
    }
    
    public LocalDateTime getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDateTime tanggalMulai) { this.tanggalMulai = tanggalMulai; }
    
    public LocalDateTime getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDateTime tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}