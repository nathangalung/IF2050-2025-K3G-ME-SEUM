package main.service.interfaces;

import java.util.List;
import main.model.dto.ArtefakDto;
import main.model.enums.StatusArtefak;

// Interface service untuk artefak
public interface IArtefakService {
    ArtefakDto createArtefak(ArtefakDto artefakDto);
    ArtefakDto updateArtefak(Long id, ArtefakDto artefakDto);
    void deleteArtefak(Long id);
    ArtefakDto getArtefakById(Long id);
    List<ArtefakDto> getAllArtefaks();
    List<ArtefakDto> getAllArtefaksOrderById();
    List<ArtefakDto> searchArtefakByName(String name);
    List<ArtefakDto> searchArtefakByNameOrderById(String name);
    List<ArtefakDto> getArtefaksByAsalDaerah(String asalDaerah);
    ArtefakDto updateArtefakStatus(Long id, StatusArtefak status);
    boolean validateArtefakData(ArtefakDto artefakDto);
    long getTotalArtefaks();
}