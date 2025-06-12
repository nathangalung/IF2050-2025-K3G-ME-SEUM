package main.repository.interfaces;

import main.model.entities.Pameran;
import java.util.List;

/**
 * Repository interface for Pameran (Exhibition) data access
 */
public interface IPameranRepository {

    // Basic CRUD operations
    Pameran save(Pameran pameran);

    Pameran findById(Long id);

    List<Pameran> findAll();

    List<Pameran> findAllOrderById();

    void update(Pameran pameran);

    void deleteById(Long id);

    boolean existsById(Long id);

    long count();

    // Search operations
    List<Pameran> findByNamaPameranContaining(String nama);

    List<Pameran> findByNamaPameranContainingOrderById(String nama);

    List<Pameran> findByIsActive(Boolean isActive);

    // Artifact relationship operations
    boolean isArtefakInPameran(Long pameranId, Long artefakId);

    void addArtefakToPameran(Long pameranId, Long artefakId);

    void removeArtefakFromPameran(Long pameranId, Long artefakId);
}
