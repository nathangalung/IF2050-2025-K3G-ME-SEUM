package main.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import main.model.dto.PameranDto;
import main.model.entities.Pameran;
import main.repository.interfaces.IPameranRepository;
import main.service.interfaces.IPameranService;

/**
 * Implementation of business logic for Pameran (Exhibition)
 */
public class PameranServiceImpl implements IPameranService {
    private final IPameranRepository pameranRepository;

    public PameranServiceImpl(IPameranRepository pameranRepository) {
        this.pameranRepository = pameranRepository;
    }

    @Override
    public PameranDto createPameran(PameranDto pameranDto) {
        // Validate input
        validatePameranData(pameranDto);

        // Convert DTO to entity
        Pameran pameran = convertDtoToEntity(pameranDto);

        // Save entity
        pameran = pameranRepository.save(pameran);

        // Return updated DTO with ID
        return convertEntityToDto(pameran);
    }

    @Override
    public PameranDto updatePameran(Long id, PameranDto pameranDto) {
        // Validate input
        validatePameranData(pameranDto);

        // Find existing pameran
        Pameran existingPameran = pameranRepository.findById(id);
        if (existingPameran == null) {
            throw new RuntimeException("Exhibition not found with ID: " + id);
        }

        // Update fields
        existingPameran.setNamaPameran(pameranDto.getNamaPameran());
        existingPameran.setDeskripsiPameran(pameranDto.getDeskripsiPameran());
        existingPameran.setTanggalMulai(pameranDto.getTanggalMulai());
        existingPameran.setTanggalSelesai(pameranDto.getTanggalSelesai());

        if (pameranDto.getIsActive() != null) {
            existingPameran.setIsActive(pameranDto.getIsActive());
        }

        if (pameranDto.getArtefakIds() != null) {
            existingPameran.setArtefakIdsList(pameranDto.getArtefakIds());
        }

        // Save updated entity
        pameranRepository.update(existingPameran);

        // Return updated DTO
        return convertEntityToDto(existingPameran);
    }

    @Override
    public void deletePameran(Long id) {
        // Check if pameran exists
        if (!pameranRepository.existsById(id)) {
            throw new RuntimeException("Exhibition not found with ID: " + id);
        }

        pameranRepository.deleteById(id);
    }

    @Override
    public PameranDto getPameranById(Long id) {
        Pameran pameran = pameranRepository.findById(id);
        if (pameran == null) {
            throw new RuntimeException("Exhibition not found with ID: " + id);
        }

        return convertEntityToDto(pameran);
    }

    @Override
    public List<PameranDto> getAllPamerans() {
        List<Pameran> pamerans = pameranRepository.findAll();
        return pamerans.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PameranDto> getAllPameransOrderById() {
        List<Pameran> pamerans = pameranRepository.findAllOrderById();
        return pamerans.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PameranDto> searchPameransByName(String name) {
        List<Pameran> pamerans = pameranRepository.findByNamaPameranContaining(name);
        return pamerans.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PameranDto> searchPameransByNameOrderById(String name) {
        List<Pameran> pamerans = pameranRepository.findByNamaPameranContainingOrderById(name);
        return pamerans.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void addArtefakToPameran(Long pameranId, Long artefakId) {
        try {
            System.out.println("🔧 PameranService: Adding artifact " + artefakId + " to pameran " + pameranId);

            // Get the current pameran
            Pameran pameran = pameranRepository.findById(pameranId);
            if (pameran == null) {
                throw new RuntimeException("Pameran not found with ID: " + pameranId);
            }

            // Add the artifact ID to the pameran's artifact list
            pameran.addArtefakId(artefakId);

            // Save the updated pameran
            pameranRepository.save(pameran);

            System.out.println("✅ PameranService: Successfully added artifact to pameran");
            System.out.println("📋 Updated artifact list: " + pameran.getArtefakIds());

        } catch (Exception e) {
            System.err.println("❌ Error in PameranService.addArtefakToPameran: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add artifact to pameran: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeArtefakFromPameran(Long pameranId, Long artefakId) {
        Pameran pameran = pameranRepository.findById(pameranId);
        if (pameran == null) {
            throw new RuntimeException("Exhibition not found with ID: " + pameranId);
        }

        pameran.removeArtefakId(artefakId);
        pameranRepository.update(pameran);
    }

    @Override
    public List<PameranDto> getActivePamerans() {
        List<Pameran> pamerans = pameranRepository.findByIsActive(true);
        return pamerans.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PameranDto updatePameranStatus(Long id, Boolean isActive) {
        Pameran pameran = pameranRepository.findById(id);
        if (pameran == null) {
            throw new RuntimeException("Exhibition not found with ID: " + id);
        }

        pameran.setIsActive(isActive);
        pameranRepository.update(pameran);

        return convertEntityToDto(pameran);
    }

    @Override
    public PameranDto convertToDto(Pameran pameran) {
        return convertEntityToDto(pameran);
    }

    @Override
    public Pameran convertToEntity(PameranDto dto) {
        return convertDtoToEntity(dto);
    }

    // Helper method to convert entity to DTO
    private PameranDto convertEntityToDto(Pameran pameran) {
        if (pameran == null)
            return null;

        PameranDto dto = new PameranDto();
        dto.setPameranId(pameran.getPameranId());
        dto.setNamaPameran(pameran.getNamaPameran());
        dto.setDeskripsiPameran(pameran.getDeskripsiPameran());
        dto.setTanggalMulai(pameran.getTanggalMulai());
        dto.setTanggalSelesai(pameran.getTanggalSelesai());
        dto.setTanggalDibuat(pameran.getTanggalDibuat());
        dto.setIsActive(pameran.getIsActive());
        dto.setArtefakIds(pameran.getArtefakIdsList());

        return dto;
    }

    // Helper method to convert DTO to entity
    private Pameran convertDtoToEntity(PameranDto dto) {
        if (dto == null)
            return null;

        Pameran pameran = new Pameran();

        // Only set ID for existing entities
        if (dto.getPameranId() != null) {
            pameran.setPameranId(dto.getPameranId());
        }

        pameran.setNamaPameran(dto.getNamaPameran());
        pameran.setDeskripsiPameran(dto.getDeskripsiPameran());
        pameran.setTanggalMulai(dto.getTanggalMulai());
        pameran.setTanggalSelesai(dto.getTanggalSelesai());

        // Set timestamp if provided, otherwise will use default (current time)
        if (dto.getTanggalDibuat() != null) {
            pameran.setTanggalDibuat(dto.getTanggalDibuat());
        }

        // Set status if provided, otherwise use default (true)
        if (dto.getIsActive() != null) {
            pameran.setIsActive(dto.getIsActive());
        }

        // Set artifact IDs if provided
        if (dto.getArtefakIds() != null) {
            pameran.setArtefakIdsList(dto.getArtefakIds());
        }

        return pameran;
    }

    // Validation method
    private void validatePameranData(PameranDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Exhibition data cannot be null");
        }

        if (dto.getNamaPameran() == null || dto.getNamaPameran().trim().isEmpty()) {
            throw new IllegalArgumentException("Exhibition name is required");
        }

        if (dto.getTanggalMulai() == null) {
            throw new IllegalArgumentException("Start date is required");
        }

        if (dto.getTanggalSelesai() == null) {
            throw new IllegalArgumentException("End date is required");
        }

        if (dto.getTanggalMulai().isAfter(dto.getTanggalSelesai())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}
