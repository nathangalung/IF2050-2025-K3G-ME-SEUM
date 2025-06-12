package main.controller;

import java.util.List;
import main.model.dto.ArtefakDto; // Add this import
import main.model.dto.PameranDto;
import main.service.interfaces.IArtefakService; // Add this import
import main.service.interfaces.IPameranService;

/**
 * Controller for managing Pameran (Exhibition) operations
 */
public class PameranController {
    private final IPameranService pameranService;
    private final IArtefakService artefakService; // Add this field

    // Update constructor to accept both services
    public PameranController(IPameranService pameranService, IArtefakService artefakService) {
        this.pameranService = pameranService;
        this.artefakService = artefakService; // Initialize this
    }

    // Create new pameran
    public PameranDto createPameran(PameranDto pameranDto) {
        try {
            return pameranService.createPameran(pameranDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create exhibition: " + e.getMessage());
        }
    }

    // Update existing pameran
    public PameranDto updatePameran(Long id, PameranDto pameranDto) {
        try {
            return pameranService.updatePameran(id, pameranDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update exhibition: " + e.getMessage());
        }
    }

    // Delete pameran by ID
    public void deletePameran(Long id) {
        try {
            pameranService.deletePameran(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete exhibition: " + e.getMessage());
        }
    }

    // Get pameran by ID
    public PameranDto getPameranById(Long id) {
        try {
            return pameranService.getPameranById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get exhibition: " + e.getMessage());
        }
    }

    // Get all pamerans
    public List<PameranDto> getAllPamerans() {
        try {
            return pameranService.getAllPamerans();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get exhibitions: " + e.getMessage());
        }
    }

    // Get all pamerans ordered by ID
    public List<PameranDto> getAllPameransOrderById() {
        try {
            return pameranService.getAllPameransOrderById();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get exhibitions: " + e.getMessage());
        }
    }

    // Search pamerans by name
    public List<PameranDto> searchPamerans(String searchTerm) {
        try {
            return pameranService.searchPameransByName(searchTerm);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search exhibitions: " + e.getMessage());
        }
    }

    // Search pamerans by name ordered by ID
    public List<PameranDto> searchPameransOrderById(String searchTerm) {
        try {
            return pameranService.searchPameransByNameOrderById(searchTerm);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search exhibitions: " + e.getMessage());
        }
    }

    // Add artifact to pameran
    public void addArtefakToPameran(Long pameranId, Long artefakId) {
        try {
            System.out.println("🔧 PameranController: Adding artifact " + artefakId + " to pameran " + pameranId);

            // First, verify that both pameran and artifact exist
            PameranDto pameran = pameranService.getPameranById(pameranId);
            if (pameran == null) {
                throw new RuntimeException("Exhibition not found with ID: " + pameranId);
            }

            ArtefakDto artifact = artefakService.getArtefakById(artefakId);
            if (artifact == null) {
                throw new RuntimeException("Artifact not found with ID: " + artefakId);
            }

            // Add the artifact to pameran
            pameranService.addArtefakToPameran(pameranId, artefakId);

            System.out.println("✅ Successfully added artifact " + artefakId + " to pameran " + pameranId);

        } catch (Exception e) {
            System.err.println("❌ Error in addArtefakToPameran: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add artifact to exhibition: " + e.getMessage(), e);
        }
    }

    // Remove artifact from pameran
    public void removeArtefakFromPameran(Long pameranId, Long artefakId) {
        try {
            pameranService.removeArtefakFromPameran(pameranId, artefakId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove artifact from exhibition: " + e.getMessage());
        }
    }

    // Get active pamerans
    public List<PameranDto> getActivePamerans() {
        try {
            return pameranService.getActivePamerans();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get active exhibitions: " + e.getMessage());
        }
    }

    // Update pameran status
    public PameranDto updatePameranStatus(Long id, Boolean isActive) {
        try {
            return pameranService.updatePameranStatus(id, isActive);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update exhibition status: " + e.getMessage());
        }
    }

    // Get artifact name by ID
    public String getArtefakNameById(Long artefakId) {
        try {
            ArtefakDto artifact = artefakService.getArtefakById(artefakId);
            return artifact != null ? artifact.getNamaArtefak() : "Unknown Artifact";
        } catch (Exception e) {
            System.err.println("Error getting artifact name for ID " + artefakId + ": " + e.getMessage());
            return "Unknown Artifact";
        }
    }
}
