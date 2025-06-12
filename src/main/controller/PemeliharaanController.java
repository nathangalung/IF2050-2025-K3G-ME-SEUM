package main.controller;

import main.model.dto.PemeliharaanDto;
import main.model.enums.StatusPemeliharaan;
import main.service.interfaces.IPemeliharaanService;

import java.time.LocalDateTime;
import java.util.List;

public class PemeliharaanController {
    private final IPemeliharaanService pemeliharaanService;
    
    public PemeliharaanController(IPemeliharaanService pemeliharaanService) {
        this.pemeliharaanService = pemeliharaanService;
    }
    
    // CRUD Operations
    public PemeliharaanDto createPemeliharaan(PemeliharaanDto dto) {
        try {
            return pemeliharaanService.createPemeliharaan(dto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create maintenance record: " + e.getMessage());
        }
    }
    
    public PemeliharaanDto updatePemeliharaan(Long id, PemeliharaanDto dto) {
        try {
            return pemeliharaanService.updatePemeliharaan(id, dto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update maintenance record: " + e.getMessage());
        }
    }
    
    public void deletePemeliharaan(Long id) {
        try {
            pemeliharaanService.deletePemeliharaan(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete maintenance record: " + e.getMessage());
        }
    }
    
    public PemeliharaanDto getPemeliharaanById(Long id) {
        try {
            return pemeliharaanService.getPemeliharaanById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get maintenance record: " + e.getMessage());
        }
    }
    
    public List<PemeliharaanDto> getAllPemeliharaan() {
        try {
            return pemeliharaanService.getAllPemeliharaan();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all maintenance records: " + e.getMessage());
        }
    }
    
    // Status Management
    public PemeliharaanDto mulaiPemeliharaan(Long id) {
        try {
            return pemeliharaanService.mulaiPemeliharaan(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to start maintenance: " + e.getMessage());
        }
    }
    
    public PemeliharaanDto selesaikanPemeliharaan(Long id, String catatan) {
        try {
            return pemeliharaanService.selesaikanPemeliharaan(id, catatan);
        } catch (Exception e) {
            throw new RuntimeException("Failed to complete maintenance: " + e.getMessage());
        }
    }
    
    // Search and Filter Operations
    public List<PemeliharaanDto> getPemeliharaanByStatus(StatusPemeliharaan status) {
        try {
            return pemeliharaanService.getPemeliharaanByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get maintenance by status: " + e.getMessage());
        }
    }
    
    public List<PemeliharaanDto> getPemeliharaanByPetugas(Long petugasId) {
        try {
            return pemeliharaanService.getPemeliharaanByPetugas(petugasId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get maintenance by cleaner: " + e.getMessage());
        }
    }
    
    public List<PemeliharaanDto> getPemeliharaanByArtefak(Long artefakId) {
        try {
            return pemeliharaanService.getPemeliharaanByArtefak(artefakId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get maintenance by artifact: " + e.getMessage());
        }
    }
    
    public List<PemeliharaanDto> getUpcomingPemeliharaan() {
        try {
            return pemeliharaanService.getUpcomingPemeliharaan();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get upcoming maintenance: " + e.getMessage());
        }
    }
    
    public List<PemeliharaanDto> getPemeliharaanByDateRange(LocalDateTime start, LocalDateTime end) {
        try {
            return pemeliharaanService.getPemeliharaanByDateRange(start, end);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get maintenance by date range: " + e.getMessage());
        }
    }
    
    // Statistics
    public long countPemeliharaanByStatus(StatusPemeliharaan status) {
        try {
            return pemeliharaanService.countPemeliharaanByStatus(status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to count maintenance by status: " + e.getMessage());
        }
    }
    
    public long countPemeliharaanByPetugas(Long petugasId) {
        try {
            return pemeliharaanService.countPemeliharaanByPetugas(petugasId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to count maintenance by cleaner: " + e.getMessage());
        }
    }
    
    // New methods as per requirements
    public void ajukanPemeliharaan(Long artefakId, String deskripsi, Long petugasId) {
        try {
            PemeliharaanDto dto = new PemeliharaanDto();
            dto.setArtefakId(artefakId);
            dto.setPetugasId(petugasId);
            dto.setDeskripsiPemeliharaan(deskripsi);
            dto.setTanggalMulai(LocalDateTime.now());
            dto.setStatus(StatusPemeliharaan.DIJADWALKAN.name());
            
            pemeliharaanService.createPemeliharaan(dto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create maintenance request: " + e.getMessage());
        }
    }
      public void updateStatusPemeliharaan(Long pemeliharaanId, String status) {
        try {
            PemeliharaanDto existing = pemeliharaanService.getPemeliharaanById(pemeliharaanId);
            if (existing == null) {
                throw new RuntimeException("Maintenance record not found");
            }
            
            existing.setStatus(status);
            
            // If marking as completed, set completion date to the deadline date
            if ("SELESAI".equals(status)) {
                // Use the deadline (tanggal_mulai) as the completion date (tanggal_selesai)
                if (existing.getTanggalMulai() != null) {
                    existing.setTanggalSelesai(existing.getTanggalMulai());
                    System.out.println("📅 Setting completion date to deadline: " + existing.getTanggalMulai().toLocalDate());
                } else {
                    // Fallback to current time if no deadline was set
                    existing.setTanggalSelesai(LocalDateTime.now());
                    System.out.println("📅 No deadline found, using current time as completion date");
                }
            }
            
            pemeliharaanService.updatePemeliharaan(pemeliharaanId, existing);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update maintenance status: " + e.getMessage());
        }
    }
    
    public void catatTindakanPemeliharaan(Long pemeliharaanId, String tindakan) {
        try {
            PemeliharaanDto existing = pemeliharaanService.getPemeliharaanById(pemeliharaanId);
            if (existing == null) {
                throw new RuntimeException("Maintenance record not found");
            }
            
            String currentCatatan = existing.getCatatan();
            String newCatatan = currentCatatan != null ? 
                currentCatatan + "\n" + tindakan : tindakan;
            
            existing.setCatatan(newCatatan);
            pemeliharaanService.updatePemeliharaan(pemeliharaanId, existing);
        } catch (Exception e) {
            throw new RuntimeException("Failed to record maintenance action: " + e.getMessage());
        }
    }
}
