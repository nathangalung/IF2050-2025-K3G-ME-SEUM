package main.controller;

import main.model.dto.ArtefakDto;
import main.service.interfaces.IArtefakService;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ManajemenArtefakController {
    private final IArtefakService artefakService;
    
    public ManajemenArtefakController(IArtefakService artefakService) {
        this.artefakService = artefakService;
    }
    
    /**
     * Get all artifacts ordered by ID
     * @return List of ArtefakDto ordered by ID
     */
    public List<ArtefakDto> getAllArtefaksOrderById() {
        try {
            // For now, return an empty list since ArtefakService is not implemented
            // TODO: Implement this method when ArtefakService is available
            return new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all artifacts: " + e.getMessage());
        }
    }
}
