package main.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import main.model.dto.ArtefakDto;
import main.model.entities.Artefak;
import main.model.enums.StatusArtefak;
import main.repository.interfaces.IArtefakRepository;
import main.service.interfaces.IArtefakService;
import main.utils.ValidationUtil;

// Implementasi business logic untuk artefak
public class ArtefakServiceImpl implements IArtefakService {
    private final IArtefakRepository artefakRepository;
    
    public ArtefakServiceImpl(IArtefakRepository artefakRepository) {
        this.artefakRepository = artefakRepository;
    }
    
    @Override
    public ArtefakDto createArtefak(ArtefakDto artefakDto) {
        // Validate input
        if (!ValidationUtil.validateArtefakData(artefakDto)) {
            throw new IllegalArgumentException("Invalid artefak data");
        }
        
        // Convert DTO to entity
        Artefak artefak = convertDtoToEntity(artefakDto);
        
        // Save entity
        artefak = artefakRepository.save(artefak);
        
        // Return updated DTO with ID
        return convertEntityToDto(artefak);
    }
    
    @Override
    public ArtefakDto updateArtefak(Long id, ArtefakDto artefakDto) {
        // Validate input
        if (!ValidationUtil.validateArtefakData(artefakDto)) {
            throw new IllegalArgumentException("Invalid artefak data");
        }
        
        // Find existing artefak
        Artefak existingArtefak = artefakRepository.findById(id);
        if (existingArtefak == null) {
            throw new RuntimeException("Artefak not found with ID: " + id);
        }
        
        // Update fields
        existingArtefak.setNamaArtefak(artefakDto.getNamaArtefak());
        existingArtefak.setDeskripsiArtefak(artefakDto.getDeskripsiArtefak());
        existingArtefak.setAsalDaerah(artefakDto.getAsalDaerah());
        existingArtefak.setPeriode(artefakDto.getPeriode());
        existingArtefak.setGambar(artefakDto.getGambar());
        
        // Only update status if it's provided
        if (artefakDto.getStatus() != null && !artefakDto.getStatus().isEmpty()) {
            existingArtefak.setStatus(StatusArtefak.fromString(artefakDto.getStatus()));
        }
        
        // Update entity
        artefakRepository.update(existingArtefak);
        
        // Return updated DTO
        return convertEntityToDto(existingArtefak);
    }
    
    @Override
    public void deleteArtefak(Long id) {
        // Check if artefak exists
        if (!artefakRepository.existsById(id)) {
            throw new RuntimeException("Artefak not found with ID: " + id);
        }
        
        artefakRepository.deleteById(id);
    }
    
    @Override
    public ArtefakDto getArtefakById(Long id) {
        Artefak artefak = artefakRepository.findById(id);
        if (artefak == null) {
            throw new RuntimeException("Artefak not found with ID: " + id);
        }
        
        return convertEntityToDto(artefak);
    }
    
    @Override
    public List<ArtefakDto> getAllArtefaks() {
        List<Artefak> artefaks = artefakRepository.findAll();
        return artefaks.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ArtefakDto> getAllArtefaksOrderById() {
        List<Artefak> artefaks = artefakRepository.findAllOrderById();
        return artefaks.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ArtefakDto> searchArtefakByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllArtefaksOrderById();
        }
        
        List<Artefak> artefaks = artefakRepository.findByNamaArtefakContaining(name);
        return artefaks.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ArtefakDto> searchArtefakByNameOrderById(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllArtefaksOrderById();
        }
        
        List<Artefak> artefaks = artefakRepository.findByNamaArtefakContainingOrderById(name);
        return artefaks.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ArtefakDto> getArtefaksByAsalDaerah(String asalDaerah) {
        List<Artefak> artefaks = artefakRepository.findByAsalDaerah(asalDaerah);
        return artefaks.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public ArtefakDto updateArtefakStatus(Long id, StatusArtefak status) {
        // Find existing artefak
        Artefak artefak = artefakRepository.findById(id);
        if (artefak == null) {
            throw new RuntimeException("Artefak not found with ID: " + id);
        }
        
        // Update status
        artefak.setStatus(status);
        artefakRepository.update(artefak);
        
        return convertEntityToDto(artefak);
    }
    
    @Override
    public boolean validateArtefakData(ArtefakDto artefakDto) {
        try {
            return ValidationUtil.validateArtefakData(artefakDto);
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public long getTotalArtefaks() {
        return artefakRepository.count();
    }
    
    // Helper method to convert entity to DTO
    private ArtefakDto convertEntityToDto(Artefak artefak) {
        ArtefakDto dto = new ArtefakDto();
        dto.setArtefakId(artefak.getArtefakId());
        dto.setNamaArtefak(artefak.getNamaArtefak());
        dto.setDeskripsiArtefak(artefak.getDeskripsiArtefak());
        dto.setStatus(artefak.getStatus().name());
        dto.setGambar(artefak.getGambar());
        dto.setAsalDaerah(artefak.getAsalDaerah());
        dto.setPeriode(artefak.getPeriode());
        dto.setTanggalRegistrasi(artefak.getTanggalRegistrasi());
        // Add curator ID if available
        dto.setCuratorId(artefak.getCuratorId());
        
        return dto;
    }
    
    // Helper method to convert DTO to entity
    private Artefak convertDtoToEntity(ArtefakDto dto) {
        Artefak artefak = new Artefak();
        
        // Only set ID for existing entities
        if (dto.getArtefakId() != null) {
            artefak.setArtefakId(dto.getArtefakId());
        }
        
        artefak.setNamaArtefak(dto.getNamaArtefak());
        artefak.setDeskripsiArtefak(dto.getDeskripsiArtefak());
        
        // Set status if provided, otherwise use default
        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            artefak.setStatus(StatusArtefak.fromString(dto.getStatus()));
        } else {
            artefak.setStatus(StatusArtefak.TERSEDIA);
        }
        
        artefak.setGambar(dto.getGambar());
        artefak.setAsalDaerah(dto.getAsalDaerah());
        artefak.setPeriode(dto.getPeriode());
        
        // Set timestamp if provided, otherwise will use default (current time)
        if (dto.getTanggalRegistrasi() != null) {
            artefak.setTanggalRegistrasi(dto.getTanggalRegistrasi());
        }
        
        // Set curator ID if provided
        if (dto.getCuratorId() != null) {
            artefak.setCuratorId(dto.getCuratorId());
        }
        
        return artefak;
    }
}