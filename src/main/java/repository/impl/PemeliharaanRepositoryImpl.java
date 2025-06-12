package main.repository.impl;

import main.model.entities.Pemeliharaan;
import main.model.enums.StatusPemeliharaan;
import main.repository.interfaces.IPemeliharaanRepository;
import main.utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PemeliharaanRepositoryImpl implements IPemeliharaanRepository {
    private final DatabaseUtil dbUtil;
    
    public PemeliharaanRepositoryImpl() {
        this.dbUtil = new DatabaseUtil();
    }    @Override
    public Pemeliharaan save(Pemeliharaan pemeliharaan) {
        String sql = "INSERT INTO pemeliharaan (artefak_id, petugas_id, jenis_pemeliharaan, " +
                    "deskripsi_pemeliharaan, tanggal_mulai, status, catatan) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, pemeliharaan.getArtefakId());
            
            // Handle null petugasId - set as NULL if not assigned
            if (pemeliharaan.getPetugasId() != null) {
                stmt.setLong(2, pemeliharaan.getPetugasId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            
            stmt.setString(3, pemeliharaan.getJenisPemeliharaan());
            stmt.setString(4, pemeliharaan.getDeskripsiPemeliharaan());
            stmt.setTimestamp(5, Timestamp.valueOf(pemeliharaan.getTanggalMulai()));
            stmt.setString(6, pemeliharaan.getStatus().name());
            stmt.setString(7, pemeliharaan.getCatatan());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        pemeliharaan.setPemeliharaanId(rs.getLong(1));
                    }
                }
            }
            
            return pemeliharaan;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving maintenance record: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Pemeliharaan> findByStatus(StatusPemeliharaan status) {
        String sql = "SELECT * FROM pemeliharaan WHERE status = ?";
        List<Pemeliharaan> pemeliharaanList = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pemeliharaanList.add(mapResultSetToPemeliharaan(rs));
                }
            }
            
            return pemeliharaanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding maintenance by status: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Pemeliharaan> findUpcomingMaintenance() {
        String sql = "SELECT * FROM pemeliharaan WHERE status = ? AND tanggal_mulai > CURRENT_TIMESTAMP";
        List<Pemeliharaan> pemeliharaanList = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, StatusPemeliharaan.DIJADWALKAN.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pemeliharaanList.add(mapResultSetToPemeliharaan(rs));
                }
            }
            
            return pemeliharaanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding upcoming maintenance: " + e.getMessage(), e);
        }
    }

    @Override
    public Pemeliharaan findById(Long id) {
        String sql = "SELECT * FROM pemeliharaan WHERE pemeliharaan_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPemeliharaan(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding maintenance by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pemeliharaan> findAll() {
        String sql = "SELECT * FROM pemeliharaan ORDER BY pemeliharaan_id ASC"; // Changed to ASC order
        List<Pemeliharaan> pemeliharaanList = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
        
            while (rs.next()) {
                pemeliharaanList.add(mapResultSetToPemeliharaan(rs));
            }
            return pemeliharaanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all maintenance records: " + e.getMessage(), e);
        }
    }    @Override
    public void update(Pemeliharaan pemeliharaan) {
        String sql = "UPDATE pemeliharaan SET artefak_id = ?, petugas_id = ?, jenis_pemeliharaan = ?, " +
                    "deskripsi_pemeliharaan = ?, tanggal_mulai = ?, tanggal_selesai = ?, " +
                    "status = ?, catatan = ? WHERE pemeliharaan_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, pemeliharaan.getArtefakId());
            
            // Handle null petugasId
            if (pemeliharaan.getPetugasId() != null) {
                stmt.setLong(2, pemeliharaan.getPetugasId());
            } else {
                stmt.setNull(2, java.sql.Types.BIGINT);
            }
            
            stmt.setString(3, pemeliharaan.getJenisPemeliharaan());
            stmt.setString(4, pemeliharaan.getDeskripsiPemeliharaan());
            stmt.setTimestamp(5, Timestamp.valueOf(pemeliharaan.getTanggalMulai()));
            stmt.setTimestamp(6, pemeliharaan.getTanggalSelesai() != null ? 
                            Timestamp.valueOf(pemeliharaan.getTanggalSelesai()) : null);
            stmt.setString(7, pemeliharaan.getStatus().name());
            stmt.setString(8, pemeliharaan.getCatatan());
            stmt.setLong(9, pemeliharaan.getPemeliharaanId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating maintenance: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM pemeliharaan WHERE pemeliharaan_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting maintenance: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pemeliharaan> findByPetugasId(Long petugasId) {
        String sql = "SELECT * FROM pemeliharaan WHERE petugas_id = ? ORDER BY tanggal_mulai DESC";
        List<Pemeliharaan> pemeliharaanList = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, petugasId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pemeliharaanList.add(mapResultSetToPemeliharaan(rs));
                }
            }
            return pemeliharaanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding maintenance by petugas ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pemeliharaan> findByArtefakId(Long artefakId) {
        String sql = "SELECT * FROM pemeliharaan WHERE artefak_id = ? ORDER BY tanggal_mulai DESC";
        List<Pemeliharaan> pemeliharaanList = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, artefakId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pemeliharaanList.add(mapResultSetToPemeliharaan(rs));
                }
            }
            return pemeliharaanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding maintenance by artefak ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pemeliharaan> findByTanggalMulaiBetween(LocalDateTime start, LocalDateTime end) {
        String sql = "SELECT * FROM pemeliharaan WHERE tanggal_mulai BETWEEN ? AND ? ORDER BY tanggal_mulai";
        List<Pemeliharaan> pemeliharaanList = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(start));
            stmt.setTimestamp(2, Timestamp.valueOf(end));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pemeliharaanList.add(mapResultSetToPemeliharaan(rs));
                }
            }
            return pemeliharaanList;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding maintenance between dates: " + e.getMessage(), e);
        }
    }

    @Override
    public long countByStatus(StatusPemeliharaan status) {
        String sql = "SELECT COUNT(*) FROM pemeliharaan WHERE status = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting maintenance by status: " + e.getMessage(), e);
        }
    }

    @Override
    public long countByPetugasId(Long petugasId) {
        String sql = "SELECT COUNT(*) FROM pemeliharaan WHERE petugas_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, petugasId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting maintenance by petugas ID: " + e.getMessage(), e);
        }
    }

    @Override
    public long countByArtefakId(Long artefakId) {
        String sql = "SELECT COUNT(*) FROM pemeliharaan WHERE artefak_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, artefakId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting maintenance by artefak ID: " + e.getMessage(), e);
        }
    }    // Helper method to map ResultSet to Pemeliharaan entity
    private Pemeliharaan mapResultSetToPemeliharaan(ResultSet rs) throws SQLException {
        Pemeliharaan pemeliharaan = new Pemeliharaan();
        pemeliharaan.setPemeliharaanId(rs.getLong("pemeliharaan_id"));
        pemeliharaan.setArtefakId(rs.getLong("artefak_id"));
        
        // Handle null petugas_id
        Long petugasId = rs.getLong("petugas_id");
        if (!rs.wasNull()) {
            pemeliharaan.setPetugasId(petugasId);
        } else {
            pemeliharaan.setPetugasId(null);
        }
        
        pemeliharaan.setJenisPemeliharaan(rs.getString("jenis_pemeliharaan"));
        pemeliharaan.setDeskripsiPemeliharaan(rs.getString("deskripsi_pemeliharaan"));
        pemeliharaan.setTanggalMulai(rs.getTimestamp("tanggal_mulai").toLocalDateTime());
        
        Timestamp tanggalSelesai = rs.getTimestamp("tanggal_selesai");
        if (tanggalSelesai != null) {
            pemeliharaan.setTanggalSelesai(tanggalSelesai.toLocalDateTime());
        }
        
        pemeliharaan.setStatus(StatusPemeliharaan.valueOf(rs.getString("status")));
        pemeliharaan.setCatatan(rs.getString("catatan"));
        
        return pemeliharaan;
    }
}
