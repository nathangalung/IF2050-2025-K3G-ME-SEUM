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

import main.model.entities.Pameran;
import main.repository.interfaces.IPameranRepository;
import main.utils.DatabaseUtil;

/**
 * Implementation of Pameran repository for database operations
 */
public class PameranRepositoryImpl implements IPameranRepository {
    private final DatabaseUtil dbUtil;

    public PameranRepositoryImpl() {
        this.dbUtil = new DatabaseUtil();
    }

    @Override
    public List<Pameran> findAll() {
        List<Pameran> pamerans = new ArrayList<>();
        String sql = "SELECT * FROM pameran ORDER BY tanggal_dibuat DESC";

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    Pameran pameran = mapResultSetToPameran(rs);
                    pamerans.add(pameran);
                } catch (Exception e) {
                    System.err
                            .println("⚠️ Error mapping pameran ID " + rs.getLong("pameran_id") + ": " + e.getMessage());
                    // Continue with next record instead of failing completely
                }
            }

            System.out.println("✅ Successfully loaded " + pamerans.size() + " pamerans from database");

        } catch (SQLException e) {
            System.err.println("❌ Database error in findAll: " + e.getMessage());
            throw new RuntimeException("Error finding all pamerans", e);
        }

        return pamerans;
    }

    @Override
    public List<Pameran> findAllOrderById() {
        List<Pameran> pamerans = new ArrayList<>();
        String sql = "SELECT * FROM pameran ORDER BY pameran_id ASC";

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                try {
                    Pameran pameran = mapResultSetToPameran(rs);
                    pamerans.add(pameran);
                } catch (Exception e) {
                    System.err
                            .println("⚠️ Error mapping pameran ID " + rs.getLong("pameran_id") + ": " + e.getMessage());
                    // Continue with next record instead of failing completely
                }
            }

            System.out.println("✅ Successfully loaded " + pamerans.size() + " pamerans ordered by ID");

        } catch (SQLException e) {
            System.err.println("❌ Database error in findAllOrderById: " + e.getMessage());
            throw new RuntimeException("Error finding pamerans ordered by ID", e);
        }

        return pamerans;
    }

    @Override
    public List<Pameran> findByNamaPameranContaining(String nama) {
        List<Pameran> pamerans = new ArrayList<>();
        String sql = "SELECT * FROM pameran WHERE nama_pameran LIKE ? ORDER BY tanggal_dibuat DESC";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nama + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    Pameran pameran = mapResultSetToPameran(rs);
                    pamerans.add(pameran);
                } catch (Exception e) {
                    System.err
                            .println("⚠️ Error mapping pameran ID " + rs.getLong("pameran_id") + ": " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in findByNamaPameranContaining: " + e.getMessage());
            throw new RuntimeException("Error searching pamerans by name", e);
        }

        return pamerans;
    }

    @Override
    public List<Pameran> findByNamaPameranContainingOrderById(String nama) {
        List<Pameran> pamerans = new ArrayList<>();
        String sql = "SELECT * FROM pameran WHERE nama_pameran LIKE ? ORDER BY pameran_id ASC";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nama + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    Pameran pameran = mapResultSetToPameran(rs);
                    pamerans.add(pameran);
                } catch (Exception e) {
                    System.err
                            .println("⚠️ Error mapping pameran ID " + rs.getLong("pameran_id") + ": " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in findByNamaPameranContainingOrderById: " + e.getMessage());
            throw new RuntimeException("Error searching pamerans by name ordered by ID", e);
        }

        return pamerans;
    }

    @Override
    public List<Pameran> findByIsActive(Boolean isActive) {
        List<Pameran> pamerans = new ArrayList<>();
        String sql = "SELECT * FROM pameran WHERE is_active = ? ORDER BY tanggal_dibuat DESC";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isActive);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                try {
                    Pameran pameran = mapResultSetToPameran(rs);
                    pamerans.add(pameran);
                } catch (Exception e) {
                    System.err
                            .println("⚠️ Error mapping pameran ID " + rs.getLong("pameran_id") + ": " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in findByIsActive: " + e.getMessage());
            throw new RuntimeException("Error finding pamerans by active status", e);
        }

        return pamerans;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM pameran WHERE pameran_id = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Pameran deleted successfully with ID: " + id);
            } else {
                System.out.println("⚠️ No pameran found with ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in deleteById: " + e.getMessage());
            throw new RuntimeException("Error deleting pameran", e);
        }
    }

    private Pameran mapResultSetToPameran(ResultSet rs) throws SQLException {
        Pameran pameran = new Pameran();

        try {
            // Basic fields
            pameran.setPameranId(rs.getLong("pameran_id"));
            pameran.setNamaPameran(rs.getString("nama_pameran"));
            pameran.setDeskripsiPameran(rs.getString("deskripsi_pameran"));

            // Date fields - handle potential null values
            Timestamp tanggalMulai = rs.getTimestamp("tanggal_mulai");
            if (tanggalMulai != null) {
                pameran.setTanggalMulai(tanggalMulai.toLocalDateTime());
            }

            Timestamp tanggalSelesai = rs.getTimestamp("tanggal_selesai");
            if (tanggalSelesai != null) {
                pameran.setTanggalSelesai(tanggalSelesai.toLocalDateTime());
            }

            // Created date with fallback
            Timestamp tanggalDibuat = rs.getTimestamp("tanggal_dibuat");
            if (tanggalDibuat != null) {
                pameran.setTanggalDibuat(tanggalDibuat.toLocalDateTime());
            } else {
                pameran.setTanggalDibuat(LocalDateTime.now());
            }

            // Active status with fallback
            Boolean isActive = rs.getBoolean("is_active");
            if (!rs.wasNull()) {
                pameran.setIsActive(isActive);
            } else {
                pameran.setIsActive(true);
            }

            // Artifact IDs - handle gracefully if column doesn't exist
            try {
                String artefakIds = rs.getString("artefak_ids");
                pameran.setArtefakIds(artefakIds != null ? artefakIds : "");
            } catch (SQLException e) {
                // Column might not exist, set empty
                pameran.setArtefakIds("");
                System.out.println("⚠️ artefak_ids column not found, setting empty");
            }

        } catch (Exception e) {
            System.err.println("❌ Error mapping pameran fields: " + e.getMessage());
            throw e;
        }

        return pameran;
    }

    @Override
    public Pameran findById(Long id) {
        String sql = "SELECT * FROM pameran WHERE pameran_id = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPameran(rs);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in findById: " + e.getMessage());
            throw new RuntimeException("Error finding pameran by ID", e);
        }

        return null;
    }

    @Override
    public Pameran save(Pameran pameran) {
        String sql = "INSERT INTO pameran (nama_pameran, deskripsi_pameran, tanggal_mulai, tanggal_selesai, tanggal_dibuat, is_active, artefak_ids) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pameran.getNamaPameran());
            stmt.setString(2, pameran.getDeskripsiPameran());

            // Handle date fields
            if (pameran.getTanggalMulai() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(pameran.getTanggalMulai()));
            } else {
                stmt.setNull(3, java.sql.Types.TIMESTAMP);
            }

            if (pameran.getTanggalSelesai() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(pameran.getTanggalSelesai()));
            } else {
                stmt.setNull(4, java.sql.Types.TIMESTAMP);
            }

            if (pameran.getTanggalDibuat() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(pameran.getTanggalDibuat()));
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            stmt.setBoolean(6, pameran.getIsActive() != null ? pameran.getIsActive() : true);
            stmt.setString(7, pameran.getArtefakIds() != null ? pameran.getArtefakIds() : "");

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    pameran.setPameranId(generatedKeys.getLong(1));
                }
                System.out.println("✅ Pameran saved successfully with ID: " + pameran.getPameranId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in save: " + e.getMessage());
            throw new RuntimeException("Error saving pameran", e);
        }

        return pameran;
    }

    @Override
    public void update(Pameran pameran) {
        String sql = "UPDATE pameran SET nama_pameran = ?, deskripsi_pameran = ?, tanggal_mulai = ?, tanggal_selesai = ?, is_active = ?, artefak_ids = ? WHERE pameran_id = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pameran.getNamaPameran());
            stmt.setString(2, pameran.getDeskripsiPameran());

            if (pameran.getTanggalMulai() != null) {
                stmt.setTimestamp(3, Timestamp.valueOf(pameran.getTanggalMulai()));
            } else {
                stmt.setNull(3, java.sql.Types.TIMESTAMP);
            }

            if (pameran.getTanggalSelesai() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(pameran.getTanggalSelesai()));
            } else {
                stmt.setNull(4, java.sql.Types.TIMESTAMP);
            }

            stmt.setBoolean(5, pameran.getIsActive() != null ? pameran.getIsActive() : true);
            stmt.setString(6, pameran.getArtefakIds() != null ? pameran.getArtefakIds() : "");
            stmt.setLong(7, pameran.getPameranId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Pameran updated successfully with ID: " + pameran.getPameranId());
            } else {
                System.out.println("⚠️ No pameran found with ID: " + pameran.getPameranId());
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in update: " + e.getMessage());
            throw new RuntimeException("Error updating pameran", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM pameran WHERE pameran_id = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in existsById: " + e.getMessage());
            throw new RuntimeException("Error checking pameran existence", e);
        }

        return false;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM pameran";

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            System.err.println("❌ Database error in count: " + e.getMessage());
            throw new RuntimeException("Error counting pamerans", e);
        }

        return 0;
    }
}
