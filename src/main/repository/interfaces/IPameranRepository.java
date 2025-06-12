package main.repository.interfaces;

import java.util.List;
import main.model.entities.Pameran;

/**
 * Repository interface for Pameran (Exhibition) data access
 */
public interface IPameranRepository {
    Pameran save(Pameran pameran);

    Pameran findById(Long id);

    List<Pameran> findAll();

    List<Pameran> findAllOrderById();

    List<Pameran> findByNamaPameranContaining(String nama);

    List<Pameran> findByNamaPameranContainingOrderById(String nama);

    List<Pameran> findByIsActive(Boolean isActive);

    void update(Pameran pameran);

    void deleteById(Long id);

    boolean existsById(Long id);

    long count();
}
