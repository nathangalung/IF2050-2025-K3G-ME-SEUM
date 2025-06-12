package main.service.interfaces;

import main.model.dto.PemeliharaanDto;
import main.model.entities.Pemeliharaan;
import main.model.enums.StatusPemeliharaan;
import java.time.LocalDateTime;
import java.util.List;

public interface IPemeliharaanService {
    // Basic CRUD operations
    PemeliharaanDto createPemeliharaan(PemeliharaanDto pemeliharaanDto);
    PemeliharaanDto updatePemeliharaan(Long id, PemeliharaanDto pemeliharaanDto);
    void deletePemeliharaan(Long id);
    PemeliharaanDto getPemeliharaanById(Long id);
    List<PemeliharaanDto> getAllPemeliharaan();
    
    // Status management
    PemeliharaanDto mulaiPemeliharaan(Long id);
    PemeliharaanDto selesaikanPemeliharaan(Long id, String catatan);
    
    // Search and filter operations
    List<PemeliharaanDto> getPemeliharaanByStatus(StatusPemeliharaan status);
    List<PemeliharaanDto> getPemeliharaanByPetugas(Long petugasId);
    List<PemeliharaanDto> getPemeliharaanByArtefak(Long artefakId);
    List<PemeliharaanDto> getUpcomingPemeliharaan();
    List<PemeliharaanDto> getPemeliharaanByDateRange(LocalDateTime start, LocalDateTime end);
    
    // Statistics
    long countPemeliharaanByStatus(StatusPemeliharaan status);
    long countPemeliharaanByPetugas(Long petugasId);
    long countPemeliharaanByArtefak(Long artefakId);
    
    // Validation and conversion
    boolean validatePemeliharaanData(PemeliharaanDto pemeliharaanDto);
    PemeliharaanDto convertToDto(Pemeliharaan pemeliharaan);
    Pemeliharaan convertToEntity(PemeliharaanDto dto);

    // Additional methods
    PemeliharaanDto recordMaintenanceAction(Long id, String action);
    PemeliharaanDto scheduleNewMaintenance(Long artefakId, Long petugasId, String description);
}
