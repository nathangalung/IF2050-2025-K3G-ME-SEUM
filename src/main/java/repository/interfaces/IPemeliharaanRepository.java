package main.repository.interfaces;

import main.model.entities.Pemeliharaan;
import main.model.enums.StatusPemeliharaan;
import java.time.LocalDateTime;
import java.util.List;

public interface IPemeliharaanRepository {
    // Basic CRUD operations
    Pemeliharaan save(Pemeliharaan pemeliharaan);
    Pemeliharaan findById(Long id);
    List<Pemeliharaan> findAll();
    void update(Pemeliharaan pemeliharaan);
    void deleteById(Long id);
    
    // Custom queries for maintenance management
    List<Pemeliharaan> findByStatus(StatusPemeliharaan status);
    List<Pemeliharaan> findByPetugasId(Long petugasId);
    List<Pemeliharaan> findByArtefakId(Long artefakId);
    
    // Date-based queries
    List<Pemeliharaan> findByTanggalMulaiBetween(LocalDateTime start, LocalDateTime end);
    List<Pemeliharaan> findUpcomingMaintenance();
    
    // Statistics and counts
    long countByStatus(StatusPemeliharaan status);
    long countByPetugasId(Long petugasId);
    long countByArtefakId(Long artefakId);
}
