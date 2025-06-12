package main.ui.interfaces;

import main.model.dto.PemeliharaanDto;
import java.util.List;

public interface PemeliharaanInterface {
    // Form display methods
    void showAddPemeliharaanForm();
    void showEditPemeliharaanForm(Long pemeliharaanId);
    void showPemeliharaanDetails(PemeliharaanDto pemeliharaan);
    
    // List display methods
    void displayPemeliharaanList(List<PemeliharaanDto> pemeliharaanList);
    void displayUpcomingPemeliharaan(List<PemeliharaanDto> upcomingList);
    
    // Action handlers
    void handleStartPemeliharaan(Long pemeliharaanId);
    void handleCompletePemeliharaan(Long pemeliharaanId);
    void handleAjukanPemeliharaan(Long artefakId);
    void handleUpdateStatus(Long pemeliharaanId, String status);
    void handleCatatTindakan(Long pemeliharaanId);
    
    // Filter handlers
    void applyStatusFilter(String status);
    void applyDateFilter(String startDate, String endDate);
    void applyPetugasFilter(Long petugasId);
    void applyArtefakFilter(Long artefakId);
    
    // UI update methods
    void refreshPemeliharaanList();
    void updateStatistics();
    
    // Message display methods
    void showSuccessMessage(String message);
    void showErrorMessage(String message);
    void showConfirmationDialog(String message, Runnable onConfirm);
    
    // Navigation methods
    void navigateToMaintenanceList();
    void navigateToMaintenanceCalendar();
    void navigateToMaintenanceStats();
}
