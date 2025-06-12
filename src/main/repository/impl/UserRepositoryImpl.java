package main.repository.impl;

import main.model.entities.User;
import main.model.enums.UserRole;
import main.model.enums.UserStatus;
import main.repository.interfaces.IUserRepository;
import main.utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of IUserRepository
 */
public class UserRepositoryImpl implements IUserRepository {
    private final DatabaseUtil dbUtil;
    
    public UserRepositoryImpl() {
        this.dbUtil = new DatabaseUtil();
    }
    
    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }
    
    private User insert(User user) {
        String sql = "INSERT INTO users (username, email, password, role, nama_lengkap, no_telepon, tanggal_registrasi, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getNamaLengkap());
            stmt.setString(6, user.getNoTelepon());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getTanggalRegistrasi()));
            stmt.setString(8, user.getStatus().name());
            
            System.out.println("🔄 Executing SQL: " + sql);
            System.out.println("📊 Parameters: " + user.getUsername() + ", " + user.getEmail() + ", " + user.getRole());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getLong(1));
                    System.out.println("✅ User created with ID: " + user.getUserId());
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            return user;
        } catch (SQLException e) {
            System.err.println("❌ Error saving user: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }
    
    private User update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, role = ?, nama_lengkap = ?, " +
                     "no_telepon = ?, status = ? WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole().name());
            stmt.setString(5, user.getNamaLengkap());
            stmt.setString(6, user.getNoTelepon());
            stmt.setString(7, user.getStatus().name());
            stmt.setLong(8, user.getUserId());
            
            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("🔍 Looking for user with email: " + email);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                System.out.println("✅ Found user: " + user.getUsername());
                return Optional.of(user);
            }
            System.out.println("❌ No user found with email: " + email);
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("❌ Error finding user by email: " + e.getMessage());
            throw new RuntimeException("Error finding user by email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND status = 'AKTIF'";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            System.out.println("🔍 Authenticating user: " + email);
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = mapResultSetToUser(rs);
                System.out.println("✅ Authentication successful for: " + user.getUsername());
                return Optional.of(user);
            }
            System.out.println("❌ Authentication failed for: " + email);
            return Optional.empty();
        } catch (SQLException e) {
            System.err.println("❌ Error authenticating user: " + e.getMessage());
            throw new RuntimeException("Error authenticating user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by username: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'AKTIF'";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error authenticating user by username: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                System.out.println("📊 Email " + email + " exists: " + exists);
                return exists;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Error checking email existence: " + e.getMessage());
            throw new RuntimeException("Error checking email existence: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                boolean exists = rs.getInt(1) > 0;
                System.out.println("📊 Username " + username + " exists: " + exists);
                return exists;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("❌ Error checking username existence: " + e.getMessage());
            throw new RuntimeException("Error checking username existence: " + e.getMessage(), e);
        }
    }
    
    // Helper method to map ResultSet to User entity
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setNamaLengkap(rs.getString("nama_lengkap"));
        user.setNoTelepon(rs.getString("no_telepon"));
        user.setTanggalRegistrasi(rs.getTimestamp("tanggal_registrasi").toLocalDateTime());
        user.setStatus(UserStatus.valueOf(rs.getString("status")));
        return user;
    }
    
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY tanggal_registrasi DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users: " + e.getMessage(), e);
        }
        
        return users;
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking user existence: " + e.getMessage(), e);
        }
    }
    
    // Remaining stub implementations
    @Override
    public List<User> findByRole(UserRole role) {
        return new ArrayList<>();
    }
    
    @Override
    public List<User> findByStatus(UserStatus status) {
        return new ArrayList<>();
    }
    
    @Override
    public List<User> findByRoleAndStatus(UserRole role, UserStatus status) {
        return new ArrayList<>();
    }
    
    @Override
    public List<User> findByNamaLengkapContaining(String nama) {
        return new ArrayList<>();
    }
    
    @Override
    public void updateStatus(Long userId, UserStatus status) {
        // Implementation needed
    }
    
    @Override
    public List<User> findAllWithPagination(int offset, int limit) {
        return new ArrayList<>();
    }
    
    @Override
    public long count() {
        return 0;
    }
    
    @Override
    public long countByRole(UserRole role) {
        return 0;
    }
    
    @Override
    public long countByStatus(UserStatus status) {
        return 0;
    }
}