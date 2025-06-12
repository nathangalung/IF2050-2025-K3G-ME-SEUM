package main.controller;

import java.util.List;
import main.model.dto.ArtefakDto;
import main.model.enums.StatusArtefak;
import main.service.interfaces.IArtefakService;

// Controller untuk mengelola operasi artefak
public class ManajemenArtefakController {
    private final IArtefakService artefakService;
    
    public ManajemenArtefakController(IArtefakService artefakService) {
        this.artefakService = artefakService;
    }
    
    // Create new artefak
    public ArtefakDto createArtefak(ArtefakDto artefakDto) {
        try {
            return artefakService.createArtefak(artefakDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create artefak: " + e.getMessage());
        }
    }
    
    // Update existing artefak
    public ArtefakDto updateArtefak(Long id, ArtefakDto artefakDto) {
        try {
            return artefakService.updateArtefak(id, artefakDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update artefak: " + e.getMessage());
        }
    }
    
    // Delete artefak by ID
    public void deleteArtefak(Long id) {
        try {
            artefakService.deleteArtefak(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete artefak: " + e.getMessage());
        }
    }
    
    // Get artefak by ID
    public ArtefakDto getArtefakById(Long id) {
        try {
            return artefakService.getArtefakById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get artefak: " + e.getMessage());
        }
    }
    
    // Get all artefaks
    public List<ArtefakDto> getAllArtefaks() {
        try {
            return artefakService.getAllArtefaks();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all artefaks: " + e.getMessage());
        }
    }
    
    // Get all artefaks ordered by ID
    public List<ArtefakDto> getAllArtefaksOrderById() {
        try {
            return artefakService.getAllArtefaksOrderById();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all artefaks ordered by ID: " + e.getMessage());
        }
    }
    
    // Search artefaks by name
    public List<ArtefakDto> searchArtefaks(String name) {
        try {
            return artefakService.searchArtefakByName(name);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search artefaks: " + e.getMessage());
        }
    }
    
    // Search artefaks by name ordered by ID
    public List<ArtefakDto> searchArtefaksOrderById(String name) {
        try {
            return artefakService.searchArtefakByNameOrderById(name);
        } catch (Exception e) {
            throw new RuntimeException("Failed to search artefaks: " + e.getMessage());
        }
    }
    
    // Get artefaks by asal daerah
    public List<ArtefakDto> getArtefaksByAsalDaerah(String asalDaerah) {
        try {
            return artefakService.getArtefaksByAsalDaerah(asalDaerah);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get artefaks by asal daerah: " + e.getMessage());
        }
    }
    
    // Update artefak status
    public ArtefakDto updateArtefakStatus(Long id, StatusArtefak status) {
        try {
            return artefakService.updateArtefakStatus(id, status);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update artefak status: " + e.getMessage());
        }
    }
    
    // Validate artefak data
    public boolean validateArtefakData(ArtefakDto artefakDto) {
        return artefakService.validateArtefakData(artefakDto);
    }
    
    // Get total artefaks count
    public long getTotalArtefaks() {
        try {
            return artefakService.getTotalArtefaks();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get total artefaks: " + e.getMessage());
        }
    }
}