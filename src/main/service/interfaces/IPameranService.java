package main.service.interfaces;

import java.util.List;
import main.model.dto.PameranDto;
import main.model.entities.Pameran;

/**
 * Service interface for Pameran (Exhibition) operations
 */
public interface IPameranService {
    PameranDto createPameran(PameranDto pameranDto);

    PameranDto updatePameran(Long id, PameranDto pameranDto);

    void deletePameran(Long id);

    PameranDto getPameranById(Long id);

    List<PameranDto> getAllPamerans();

    List<PameranDto> getAllPameransOrderById();

    List<PameranDto> searchPameransByName(String name);

    List<PameranDto> searchPameransByNameOrderById(String name);

    void addArtefakToPameran(Long pameranId, Long artefakId);

    void removeArtefakFromPameran(Long pameranId, Long artefakId);

    List<PameranDto> getActivePamerans();

    PameranDto updatePameranStatus(Long id, Boolean isActive);

    // Conversion methods
    PameranDto convertToDto(Pameran pameran);

    Pameran convertToEntity(PameranDto dto);
}
