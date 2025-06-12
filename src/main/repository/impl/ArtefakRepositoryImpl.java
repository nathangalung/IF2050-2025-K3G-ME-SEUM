package main.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.model.entities.Artefak;
import main.model.enums.StatusArtefak;
import main.repository.interfaces.IArtefakRepository;
import main.utils.DatabaseUtil;

public class ArtefakRepositoryImpl implements IArtefakRepository {
    private final DatabaseUtil dbUtil;
    
    public ArtefakRepositoryImpl() {
        this.dbUtil = new DatabaseUtil();
    }
    
    @Override
    public List<Artefak> findAll() {
        List<Artefak> artefaks = new ArrayList<>();
        String sql = "SELECT * FROM artefak ORDER BY tanggal_registrasi DESC";
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                try {
                    Artefak artefak = mapResultSetToArtefak(rs);
                    artefaks.add(artefak);
                } catch (Exception e) {
                    System.err.println("⚠️ Error mapping artefak ID " + rs.getLong("artefak_id") + ": " + e.getMessage());
                    // Continue with next record instead of failing completely
                }
            }
            
            System.out.println("✅ Successfully loaded " + artefaks.size() + " artefaks from database");
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in findAll: " + e.getMessage());
            throw new RuntimeException("Error finding all artefak", e);
        }
        
        return artefaks;
    }
    
    @Override
    public List<Artefak> findAllOrderById() {
        List<Artefak> artefaks = new ArrayList<>();
        String sql = "SELECT * FROM artefak ORDER BY artefak_id ASC";
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                try {
                    Artefak artefak = mapResultSetToArtefak(rs);
                    artefaks.add(artefak);
                } catch (Exception e) {
                    System.err.println("⚠️ Error mapping artefak ID " + rs.getLong("artefak_id") + ": " + e.getMessage());
                    // Continue with next record instead of failing completely
                }
            }
            
            System.out.println("✅ Successfully loaded " + artefaks.size() + " artefaks from database (ordered by ID)");
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in findAllOrderById: " + e.getMessage());
            throw new RuntimeException("Error finding all artefak ordered by ID", e);
        }
        
        return artefaks;
    }
    
    @Override
    public List<Artefak> findByNamaArtefakContaining(String nama) {
        List<Artefak> artefaks = new ArrayList<>();
        String sql = "SELECT * FROM artefak WHERE nama_artefak LIKE ? OR deskripsi_artefak LIKE ? OR asal_daerah LIKE ? ORDER BY nama_artefak";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + nama + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                try {
                    artefaks.add(mapResultSetToArtefak(rs));
                } catch (Exception e) {
                    System.err.println("⚠️ Error mapping search result: " + e.getMessage());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in search: " + e.getMessage());
            throw new RuntimeException("Error searching artefak", e);
        }
        
        return artefaks;
    }
    
    @Override
    public List<Artefak> findByNamaArtefakContainingOrderById(String nama) {
        List<Artefak> artefaks = new ArrayList<>();
        String sql = "SELECT * FROM artefak WHERE nama_artefak LIKE ? OR deskripsi_artefak LIKE ? OR asal_daerah LIKE ? ORDER BY artefak_id ASC";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + nama + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                try {
                    artefaks.add(mapResultSetToArtefak(rs));
                } catch (Exception e) {
                    System.err.println("⚠️ Error mapping search result: " + e.getMessage());
                }
            }
            
            System.out.println("✅ Search found " + artefaks.size() + " artefaks for term: " + nama);
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in search by name ordered by ID: " + e.getMessage());
            throw new RuntimeException("Error searching artefak", e);
        }
        
        return artefaks;
    }
    
    @Override
    public void deleteById(Long id) {
        Connection conn = null;
        
        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // First, delete from dependent tables
            String deletePameranRelationSql = "DELETE FROM artefak_pameran WHERE artefak_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deletePameranRelationSql)) {
                stmt.setLong(1, id);
                int deletedRelations = stmt.executeUpdate();
                System.out.println("✅ Removed " + deletedRelations + " artefak-pameran relations");
            }
            
            String deletePemeliharaanSql = "DELETE FROM pemeliharaan WHERE artefak_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deletePemeliharaanSql)) {
                stmt.setLong(1, id);
                int deletedPemeliharaan = stmt.executeUpdate();
                System.out.println("✅ Removed " + deletedPemeliharaan + " pemeliharaan records");
            }
            
            // Finally, delete the artefak itself
            String deleteArtefakSql = "DELETE FROM artefak WHERE artefak_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteArtefakSql)) {
                stmt.setLong(1, id);
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("✅ Artefak deleted successfully: " + id);
                } else {
                    System.err.println("❓ No artefak found with ID: " + id);
                    throw new RuntimeException("No artefak found with ID: " + id);
                }
            }
            
            // Commit the transaction
            conn.commit();
            
        } catch (SQLException e) {
            // Roll back transaction on error
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("⚠️ Transaction rolled back due to error");
                } catch (SQLException ex) {
                    System.err.println("❌ Error during rollback: " + ex.getMessage());
                }
            }
            
            System.err.println("❌ Database error in deleteById: " + e.getMessage());
            throw new RuntimeException("Error deleting artefak: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    private Artefak mapResultSetToArtefak(ResultSet rs) throws SQLException {
        Artefak artefak = new Artefak();
        
        try {
            artefak.setArtefakId(rs.getLong("artefak_id"));
            artefak.setNamaArtefak(rs.getString("nama_artefak"));
            artefak.setDeskripsiArtefak(rs.getString("deskripsi_artefak"));
            
            // Safe status conversion
            String statusStr = rs.getString("status");
            artefak.setStatus(StatusArtefak.fromDatabaseValue(statusStr));
            
            artefak.setGambar(rs.getString("gambar"));
            artefak.setAsalDaerah(rs.getString("asal_daerah"));
            artefak.setPeriode(rs.getString("periode"));
            
            Timestamp timestamp = rs.getTimestamp("tanggal_registrasi");
            if (timestamp != null) {
                artefak.setTanggalRegistrasi(timestamp.toLocalDateTime());
            } else {
                artefak.setTanggalRegistrasi(LocalDateTime.now());
            }
            
            // Get curator ID if present
            Long curatorId = rs.getLong("curator_id");
            if (!rs.wasNull()) {
                artefak.setCuratorId(curatorId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error mapping artefak fields: " + e.getMessage());
            throw e;
        }
        
        return artefak;
    }
    
    @Override
    public Artefak findById(Long id) {
        String sql = "SELECT * FROM artefak WHERE artefak_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToArtefak(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in findById: " + e.getMessage());
            throw new RuntimeException("Error finding artefak by ID: " + id, e);
        }
        
        return null;
    }
    
    @Override
    public Artefak save(Artefak artefak) {
        String sql = "INSERT INTO artefak (nama_artefak, deskripsi_artefak, status, gambar, asal_daerah, periode, curator_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, artefak.getNamaArtefak());
            stmt.setString(2, artefak.getDeskripsiArtefak());
            stmt.setString(3, artefak.getStatus().name()); // Use enum name
            stmt.setString(4, artefak.getGambar());
            stmt.setString(5, artefak.getAsalDaerah());
            stmt.setString(6, artefak.getPeriode());
            
            if (artefak.getCuratorId() != null) {
                stmt.setLong(7, artefak.getCuratorId());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    artefak.setArtefakId(generatedKeys.getLong(1));
                }
                System.out.println("✅ Artefak saved successfully with ID: " + artefak.getArtefakId());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in save: " + e.getMessage());
            throw new RuntimeException("Error saving artefak", e);
        }
        
        return artefak;
    }
    
    @Override
    public void update(Artefak artefak) {
        String sql = "UPDATE artefak SET nama_artefak = ?, deskripsi_artefak = ?, status = ?, gambar = ?, asal_daerah = ?, periode = ?, curator_id = ? WHERE artefak_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, artefak.getNamaArtefak());
            stmt.setString(2, artefak.getDeskripsiArtefak());
            stmt.setString(3, artefak.getStatus().name()); // Use enum name
            stmt.setString(4, artefak.getGambar());
            stmt.setString(5, artefak.getAsalDaerah());
            stmt.setString(6, artefak.getPeriode());
            
            if (artefak.getCuratorId() != null) {
                stmt.setLong(7, artefak.getCuratorId());
            } else {
                stmt.setNull(7, java.sql.Types.BIGINT);
            }
            
            stmt.setLong(8, artefak.getArtefakId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("✅ Artefak updated successfully: " + artefak.getArtefakId());
            } else {
                System.err.println("❓ No artefak found with ID: " + artefak.getArtefakId());
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in update: " + e.getMessage());
            throw new RuntimeException("Error updating artefak: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Artefak> findByAsalDaerah(String asalDaerah) {
        List<Artefak> artefaks = new ArrayList<>();
        String sql = "SELECT * FROM artefak WHERE asal_daerah LIKE ? ORDER BY artefak_id ASC";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + asalDaerah + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                try {
                    artefaks.add(mapResultSetToArtefak(rs));
                } catch (Exception e) {
                    System.err.println("⚠️ Error mapping search result: " + e.getMessage());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in findByAsalDaerah: " + e.getMessage());
            throw new RuntimeException("Error searching artefak by asal daerah", e);
        }
        
        return artefaks;
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM artefak WHERE artefak_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in existsById: " + e.getMessage());
            throw new RuntimeException("Error checking if artefak exists", e);
        }
        
        return false;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM artefak";
        
        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Database error in count: " + e.getMessage());
            throw new RuntimeException("Error counting artefaks", e);
        }
        
        return 0;
    }
}