package main.service.impl;

import main.model.dto.PemeliharaanDto;
import main.model.entities.Pemeliharaan;
import main.model.enums.StatusPemeliharaan;
import main.repository.interfaces.IPemeliharaanRepository;
import main.service.interfaces.IPemeliharaanService;
import main.utils.DatabaseUtil;
import main.utils.ValidationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PemeliharaanServiceImpl implements IPemeliharaanService {
    private final IPemeliharaanRepository pemeliharaanRepository;
    private final DatabaseUtil dbUtil;
    
    public PemeliharaanServiceImpl(IPemeliharaanRepository pemeliharaanRepository) {
        this.pemeliharaanRepository = pemeliharaanRepository;
        this.dbUtil = new DatabaseUtil();
    }
    
    @Override
    public PemeliharaanDto createPemeliharaan(PemeliharaanDto pemeliharaanDto) {
        validatePemeliharaanData(pemeliharaanDto);
        Pemeliharaan pemeliharaan = convertToEntity(pemeliharaanDto);
        pemeliharaan = pemeliharaanRepository.save(pemeliharaan);
        return convertToDto(pemeliharaan);
    }
    
    @Override
    public PemeliharaanDto updatePemeliharaan(Long id, PemeliharaanDto pemeliharaanDto) {
        validatePemeliharaanData(pemeliharaanDto);
        Pemeliharaan existing = pemeliharaanRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Maintenance record not found with ID: " + id);
        }
        
        Pemeliharaan updated = convertToEntity(pemeliharaanDto);
        updated.setPemeliharaanId(id);
        pemeliharaanRepository.update(updated);
        return convertToDto(updated);
    }
    
    @Override
    public void deletePemeliharaan(Long id) {
        pemeliharaanRepository.deleteById(id);
    }
    
    @Override
    public PemeliharaanDto getPemeliharaanById(Long id) {
        Pemeliharaan pemeliharaan = pemeliharaanRepository.findById(id);
        return pemeliharaan != null ? convertToDto(pemeliharaan) : null;
    }
    
    @Override
    public List<PemeliharaanDto> getAllPemeliharaan() {
        return pemeliharaanRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public PemeliharaanDto mulaiPemeliharaan(Long id) {
        Pemeliharaan pemeliharaan = pemeliharaanRepository.findById(id);
        if (pemeliharaan == null) {
            throw new RuntimeException("Maintenance record not found with ID: " + id);
        }
        
        pemeliharaan.mulaiPemeliharaan();
        pemeliharaanRepository.update(pemeliharaan);
        return convertToDto(pemeliharaan);
    }
    
    @Override
    public PemeliharaanDto selesaikanPemeliharaan(Long id, String catatan) {
        Pemeliharaan pemeliharaan = pemeliharaanRepository.findById(id);
        if (pemeliharaan == null) {
            throw new RuntimeException("Maintenance record not found with ID: " + id);
        }
        
        pemeliharaan.selesaikanPemeliharaan(catatan);
        pemeliharaanRepository.update(pemeliharaan);
        return convertToDto(pemeliharaan);
    }
    
    @Override
    public List<PemeliharaanDto> getPemeliharaanByStatus(StatusPemeliharaan status) {
        return pemeliharaanRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PemeliharaanDto> getPemeliharaanByPetugas(Long petugasId) {
        return pemeliharaanRepository.findByPetugasId(petugasId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PemeliharaanDto> getPemeliharaanByArtefak(Long artefakId) {
        return pemeliharaanRepository.findByArtefakId(artefakId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PemeliharaanDto> getUpcomingPemeliharaan() {
        return pemeliharaanRepository.findUpcomingMaintenance().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PemeliharaanDto> getPemeliharaanByDateRange(LocalDateTime start, LocalDateTime end) {
        return pemeliharaanRepository.findByTanggalMulaiBetween(start, end).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public long countPemeliharaanByStatus(StatusPemeliharaan status) {
        return pemeliharaanRepository.countByStatus(status);
    }
    
    @Override
    public long countPemeliharaanByPetugas(Long petugasId) {
        return pemeliharaanRepository.countByPetugasId(petugasId);
    }
    
    @Override
    public long countPemeliharaanByArtefak(Long artefakId) {
        return pemeliharaanRepository.countByArtefakId(artefakId);
    }
      @Override
    public boolean validatePemeliharaanData(PemeliharaanDto pemeliharaanDto) {
        if (pemeliharaanDto == null) {
            throw new IllegalArgumentException("Maintenance data cannot be null");
        }
        if (pemeliharaanDto.getArtefakId() == null) {
            throw new IllegalArgumentException("Artifact ID is required");
        }
        // Note: petugasId (cleaner ID) is now optional - can be assigned later
        if (pemeliharaanDto.getDeskripsiPemeliharaan() == null || 
            pemeliharaanDto.getDeskripsiPemeliharaan().trim().isEmpty()) {
            throw new IllegalArgumentException("Maintenance description is required");
        }
        return true;
    }
    
    @Override
    public PemeliharaanDto convertToDto(Pemeliharaan pemeliharaan) {
        if (pemeliharaan == null) return null;
        
        PemeliharaanDto dto = new PemeliharaanDto();
        dto.setPemeliharaanId(pemeliharaan.getPemeliharaanId());
        dto.setArtefakId(pemeliharaan.getArtefakId());
        
        // Get artifact name from database
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT nama_artefak FROM artefak WHERE artefak_id = ?")) {
            stmt.setLong(1, pemeliharaan.getArtefakId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dto.setNamaArtefak(rs.getString("nama_artefak"));
            }
        } catch (SQLException e) {
            dto.setNamaArtefak("Unknown Artifact");
        }
          dto.setPetugasId(pemeliharaan.getPetugasId());
        dto.setJenisPemeliharaan(pemeliharaan.getJenisPemeliharaan());
        dto.setDeskripsiPemeliharaan(pemeliharaan.getDeskripsiPemeliharaan());
        dto.setTanggalMulai(pemeliharaan.getTanggalMulai());
        dto.setTanggalSelesai(pemeliharaan.getTanggalSelesai());
        dto.setStatus(pemeliharaan.getStatus().name());
        dto.setCatatan(pemeliharaan.getCatatan());
        
        return dto;
    }
    
    @Override
    public Pemeliharaan convertToEntity(PemeliharaanDto dto) {
        if (dto == null) return null;
        
        Pemeliharaan pemeliharaan = new Pemeliharaan();
        if (dto.getPemeliharaanId() != null) {
            pemeliharaan.setPemeliharaanId(dto.getPemeliharaanId());
        }
          pemeliharaan.setArtefakId(dto.getArtefakId());
        pemeliharaan.setPetugasId(dto.getPetugasId());
        pemeliharaan.setJenisPemeliharaan(dto.getJenisPemeliharaan());
        pemeliharaan.setDeskripsiPemeliharaan(dto.getDeskripsiPemeliharaan());
        pemeliharaan.setTanggalMulai(dto.getTanggalMulai());
        pemeliharaan.setTanggalSelesai(dto.getTanggalSelesai());
        
        if (dto.getStatus() != null) {
            pemeliharaan.setStatus(StatusPemeliharaan.fromString(dto.getStatus()));
        }
        
        pemeliharaan.setCatatan(dto.getCatatan());
        
        return pemeliharaan;
    }
    
    @Override
    public PemeliharaanDto recordMaintenanceAction(Long id, String action) {
        Pemeliharaan pemeliharaan = pemeliharaanRepository.findById(id);
        if (pemeliharaan == null) {
            throw new RuntimeException("Maintenance record not found with ID: " + id);
        }
        
        String currentCatatan = pemeliharaan.getCatatan();
        String newCatatan = currentCatatan != null ? 
            currentCatatan + "\n" + action : action;
        
        pemeliharaan.setCatatan(newCatatan);
        pemeliharaanRepository.update(pemeliharaan);
        return convertToDto(pemeliharaan);
    }
      @Override
    public PemeliharaanDto scheduleNewMaintenance(Long artefakId, Long petugasId, String description) {
        // Default to "RUTIN" if not specified
        Pemeliharaan pemeliharaan = new Pemeliharaan(artefakId, petugasId, "RUTIN", description, LocalDateTime.now());
        pemeliharaan = pemeliharaanRepository.save(pemeliharaan);
        return convertToDto(pemeliharaan);
    }
}
