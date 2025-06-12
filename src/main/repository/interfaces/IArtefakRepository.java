package main.repository.interfaces;

import java.util.List;

import main.model.entities.Artefak;

// Interface untuk data access artefak
public interface IArtefakRepository {
    Artefak save(Artefak artefak);
    Artefak findById(Long id);
    List<Artefak> findAll();
    List<Artefak> findAllOrderById();
    List<Artefak> findByNamaArtefakContaining(String nama);
    List<Artefak> findByNamaArtefakContainingOrderById(String nama);
    void update(Artefak artefak);
    void deleteById(Long id);
    
    // Add missing methods that were referenced in the interface
    List<Artefak> findByAsalDaerah(String asalDaerah);
    boolean existsById(Long id);
    long count();
}