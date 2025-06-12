package main.model.entities;

import main.model.enums.StatusPemeliharaan;
import java.time.LocalDateTime;

public class Pemeliharaan {
    private Long pemeliharaanId;
    private Long artefakId;
    private Long petugasId;
    private String jenisPemeliharaan;
    private String deskripsiPemeliharaan;
    private LocalDateTime tanggalMulai;
    private LocalDateTime tanggalSelesai;
    private StatusPemeliharaan status;
    private String catatan;
    
    // Default constructor
    public Pemeliharaan() {
        this.status = StatusPemeliharaan.DIJADWALKAN;
        this.tanggalMulai = LocalDateTime.now();
    }
      // Constructor with required fields
    public Pemeliharaan(Long artefakId, Long petugasId, String jenisPemeliharaan,
                       String deskripsiPemeliharaan, LocalDateTime tanggalMulai) {
        this();
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
    public void setArtefakId(Long artefakId) { this.artefakId = artefakId; }    public Long getPetugasId() { return petugasId; }
    public void setPetugasId(Long petugasId) { this.petugasId = petugasId; }

    public String getJenisPemeliharaan() { return jenisPemeliharaan; }
    public void setJenisPemeliharaan(String jenisPemeliharaan) { this.jenisPemeliharaan = jenisPemeliharaan; }

    public String getDeskripsiPemeliharaan() { return deskripsiPemeliharaan; }
    public void setDeskripsiPemeliharaan(String deskripsiPemeliharaan) { 
        this.deskripsiPemeliharaan = deskripsiPemeliharaan; 
    }

    public LocalDateTime getTanggalMulai() { return tanggalMulai; }
    public void setTanggalMulai(LocalDateTime tanggalMulai) { this.tanggalMulai = tanggalMulai; }

    public LocalDateTime getTanggalSelesai() { return tanggalSelesai; }
    public void setTanggalSelesai(LocalDateTime tanggalSelesai) { this.tanggalSelesai = tanggalSelesai; }

    public StatusPemeliharaan getStatus() { return status; }
    public void setStatus(StatusPemeliharaan status) { this.status = status; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    
    // Business logic methods
    public void mulaiPemeliharaan() {
        this.status = StatusPemeliharaan.SEDANG_BERLANGSUNG;
    }
    
    public void selesaikanPemeliharaan(String catatan) {
        this.status = StatusPemeliharaan.SELESAI;
        this.tanggalSelesai = LocalDateTime.now();
        this.catatan = catatan;
    }

    public void batalkanPemeliharaan(String catatan) {
        this.status = StatusPemeliharaan.SELESAI; // Since we don't have DIBATALKAN status
        this.tanggalSelesai = LocalDateTime.now();
        this.catatan = "CANCELLED: " + catatan; // Add prefix to indicate cancellation
    }
}
