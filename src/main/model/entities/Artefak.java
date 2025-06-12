package main.model.entities;

import main.model.enums.StatusArtefak;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Entity untuk data artefak
public class Artefak {
    private Long artefakId;
    private String namaArtefak;
    private String deskripsiArtefak;
    private StatusArtefak status;
    private String gambar;
    private String asalDaerah;
    private String periode;
    private LocalDateTime tanggalRegistrasi;
    private List<String> historyPemeliharaan;
    private Long curatorId; // Add this field
    
    // Default constructor
    public Artefak() {
        this.historyPemeliharaan = new ArrayList<>();
        this.tanggalRegistrasi = LocalDateTime.now();
        this.status = StatusArtefak.TERSEDIA;
    }
    
    // Constructor with parameters
    public Artefak(String namaArtefak, String deskripsiArtefak, String asalDaerah, String periode) {
        this();
        this.namaArtefak = namaArtefak;
        this.deskripsiArtefak = deskripsiArtefak;
        this.asalDaerah = asalDaerah;
        this.periode = periode;
    }
    
    // Business method: ubah status
    public void ubahStatus(StatusArtefak statusBaru) {
        this.status = statusBaru;
    }
    
    // Business method: get info lengkap
    public String getInfo() {
        return String.format("%s: %s (Status: %s)", 
            namaArtefak, deskripsiArtefak, status.getDisplayName());
    }
    
    // Business method: set gambar
    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
    
    // Business method: tambah history pemeliharaan
    public void addHistoryPemeliharaan(String record) {
        this.historyPemeliharaan.add(record);
    }
    
    // Getters and Setters
    public Long getArtefakId() { return artefakId; }
    public void setArtefakId(Long artefakId) { this.artefakId = artefakId; }
    
    public String getNamaArtefak() { return namaArtefak; }
    public void setNamaArtefak(String namaArtefak) { this.namaArtefak = namaArtefak; }
    
    public String getDeskripsiArtefak() { return deskripsiArtefak; }
    public void setDeskripsiArtefak(String deskripsiArtefak) { this.deskripsiArtefak = deskripsiArtefak; }
    
    public StatusArtefak getStatus() { return status; }
    public void setStatus(StatusArtefak status) { this.status = status; }
    
    public String getGambar() { return gambar; }
    
    public String getAsalDaerah() { return asalDaerah; }
    public void setAsalDaerah(String asalDaerah) { this.asalDaerah = asalDaerah; }
    
    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }
    
    public LocalDateTime getTanggalRegistrasi() { return tanggalRegistrasi; }
    public void setTanggalRegistrasi(LocalDateTime tanggalRegistrasi) { this.tanggalRegistrasi = tanggalRegistrasi; }
    
    public List<String> getHistoryPemeliharaan() { return new ArrayList<>(historyPemeliharaan); }
    public void setHistoryPemeliharaan(List<String> historyPemeliharaan) { 
        this.historyPemeliharaan = new ArrayList<>(historyPemeliharaan); 
    }
    
    // Add curator ID getter and setter
    public Long getCuratorId() { return curatorId; }
    public void setCuratorId(Long curatorId) { this.curatorId = curatorId; }
    
    @Override
    public String toString() {
        return String.format("Artefak{id=%d, nama='%s', status=%s, asalDaerah='%s', periode='%s', curatorId=%d}", 
            artefakId, namaArtefak, status, asalDaerah, periode, curatorId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Artefak artefak = (Artefak) obj;
        return artefakId != null && artefakId.equals(artefak.artefakId);
    }
    
    @Override
    public int hashCode() {
        return artefakId != null ? artefakId.hashCode() : 0;
    }
}