package main.model.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity class for Pameran (Exhibition)
 */
public class Pameran {
    private Long pameranId;
    private String namaPameran;
    private String deskripsiPameran;
    private LocalDateTime tanggalMulai;
    private LocalDateTime tanggalSelesai;
    private LocalDateTime tanggalDibuat;
    private Boolean isActive;
    private String artefakIds; // Stored as comma-separated string in database

    // Constructors
    public Pameran() {
        this.tanggalDibuat = LocalDateTime.now();
        this.isActive = true;
    }

    public Pameran(String namaPameran, String deskripsiPameran,
            LocalDateTime tanggalMulai, LocalDateTime tanggalSelesai) {
        this();
        this.namaPameran = namaPameran;
        this.deskripsiPameran = deskripsiPameran;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
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

    public String getArtefakIds() {
        return artefakIds;
    }

    public void setArtefakIds(String artefakIds) {
        this.artefakIds = artefakIds;
    }

    // Helper methods for working with artifact IDs
    public List<Long> getArtefakIdsList() {
        if (artefakIds == null || artefakIds.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.stream(artefakIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public void setArtefakIdsList(List<Long> artefakIdsList) {
        if (artefakIdsList == null || artefakIdsList.isEmpty()) {
            this.artefakIds = "";
        } else {
            this.artefakIds = artefakIdsList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
    }

    public void addArtefakId(Long artefakId) {
        List<Long> currentIds = getArtefakIdsList();
        if (!currentIds.contains(artefakId)) {
            currentIds.add(artefakId);
            setArtefakIdsList(currentIds);
            System.out.println("🔧 Added artifact ID " + artefakId + " to pameran. New list: " + this.artefakIds);
        } else {
            System.out.println("⚠️ Artifact ID " + artefakId + " already exists in pameran");
        }
    }

    public void removeArtefakId(Long artefakId) {
        List<Long> currentIds = getArtefakIdsList();
        currentIds.remove(artefakId);
        setArtefakIdsList(currentIds);
    }

    @Override
    public String toString() {
        return "Pameran{" +
                "pameranId=" + pameranId +
                ", namaPameran='" + namaPameran + '\'' +
                ", deskripsiPameran='" + deskripsiPameran + '\'' +
                ", tanggalMulai=" + tanggalMulai +
                ", tanggalSelesai=" + tanggalSelesai +
                ", tanggalDibuat=" + tanggalDibuat +
                ", isActive=" + isActive +
                ", artefakIds='" + artefakIds + '\'' +
                '}';
    }
}
