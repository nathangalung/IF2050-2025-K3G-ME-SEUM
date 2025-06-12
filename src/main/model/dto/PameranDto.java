package main.model.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Pameran (Exhibition)
 */
public class PameranDto {
    private Long pameranId;
    private String namaPameran;
    private String deskripsiPameran;
    private LocalDateTime tanggalMulai;
    private LocalDateTime tanggalSelesai;
    private LocalDateTime tanggalDibuat;
    private Boolean isActive;
    private List<Long> artefakIds;

    // Constructors
    public PameranDto() {
    }

    public PameranDto(String namaPameran, String deskripsiPameran,
            LocalDateTime tanggalMulai, LocalDateTime tanggalSelesai) {
        this.namaPameran = namaPameran;
        this.deskripsiPameran = deskripsiPameran;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.tanggalDibuat = LocalDateTime.now();
        this.isActive = true;
    }

    // Complete constructor
    public PameranDto(Long pameranId, String namaPameran, String deskripsiPameran,
            LocalDateTime tanggalMulai, LocalDateTime tanggalSelesai,
            LocalDateTime tanggalDibuat, Boolean isActive, List<Long> artefakIds) {
        this.pameranId = pameranId;
        this.namaPameran = namaPameran;
        this.deskripsiPameran = deskripsiPameran;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.tanggalDibuat = tanggalDibuat;
        this.isActive = isActive;
        this.artefakIds = artefakIds;
    }

    // Getters and Setters
    public Long getPameranId() {
        return pameranId;
    }

    public void setPameranId(Long pameranId) {
        this.pameranId = pameranId;
    }

    public String getNamaPameran() {
        return namaPameran;
    }

    public void setNamaPameran(String namaPameran) {
        this.namaPameran = namaPameran;
    }

    public String getDeskripsiPameran() {
        return deskripsiPameran;
    }

    public void setDeskripsiPameran(String deskripsiPameran) {
        this.deskripsiPameran = deskripsiPameran;
    }

    public LocalDateTime getTanggalMulai() {
        return tanggalMulai;
    }

    public void setTanggalMulai(LocalDateTime tanggalMulai) {
        this.tanggalMulai = tanggalMulai;
    }

    public LocalDateTime getTanggalSelesai() {
        return tanggalSelesai;
    }

    public void setTanggalSelesai(LocalDateTime tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public LocalDateTime getTanggalDibuat() {
        return tanggalDibuat;
    }

    public void setTanggalDibuat(LocalDateTime tanggalDibuat) {
        this.tanggalDibuat = tanggalDibuat;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public List<Long> getArtefakIds() {
        return artefakIds;
    }

    public void setArtefakIds(List<Long> artefakIds) {
        this.artefakIds = artefakIds;
    }

    @Override
    public String toString() {
        return "PameranDto{" +
                "pameranId=" + pameranId +
                ", namaPameran='" + namaPameran + '\'' +
                ", deskripsiPameran='" + deskripsiPameran + '\'' +
                ", tanggalMulai=" + tanggalMulai +
                ", tanggalSelesai=" + tanggalSelesai +
                ", tanggalDibuat=" + tanggalDibuat +
                ", isActive=" + isActive +
                ", artefakIds=" + artefakIds +
                '}';
    }
}
