package main.ui.interfaces;

import main.model.dto.ArtefakDto;
import java.util.List;

// Interface untuk UI manajemen artefak
public interface ManajemenArtefakInterface {
    void showAddArtefakForm();
    void showEditArtefakForm(Long artefakId);
    void displayArtefakList(List<ArtefakDto> artefaks);
    void showArtefakDetails(ArtefakDto artefak);
    List<ArtefakDto> searchArtefak(String criteria);
    void showSearchResults(List<ArtefakDto> results);
    void refreshArtefakList();
    void showErrorMessage(String message);
    void showSuccessMessage(String message);
}