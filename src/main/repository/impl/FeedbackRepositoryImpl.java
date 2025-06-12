package main.repository.impl;

import main.model.entities.Feedback;
import main.repository.interfaces.IFeedbackRepository;
import main.utils.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepositoryImpl implements IFeedbackRepository {
    
    private final DatabaseUtil dbUtil;
    
    public FeedbackRepositoryImpl() {
        this.dbUtil = new DatabaseUtil();
    }
    
    @Override
    public Feedback save(Feedback feedback) {
        String sql = "INSERT INTO feedback (user_id, rating, komentar, tanggal_feedback) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, feedback.getUserId());
            stmt.setInt(2, feedback.getRating());
            stmt.setString(3, feedback.getKomentar());
            stmt.setTimestamp(4, Timestamp.valueOf(feedback.getTanggalFeedback()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Creating feedback failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    feedback.setFeedbackId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating feedback failed, no ID obtained.");
                }
            }
            
            return feedback;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving feedback: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Feedback update(Feedback feedback) {
        String sql = "UPDATE feedback SET rating = ?, komentar = ? WHERE feedback_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, feedback.getRating());
            stmt.setString(2, feedback.getKomentar());
            stmt.setLong(3, feedback.getFeedbackId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Updating feedback failed, no rows affected.");
            }
            
            return feedback;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error updating feedback: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM feedback WHERE feedback_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Deleting feedback failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting feedback: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Feedback findById(Long id) {
        String sql = "SELECT * FROM feedback WHERE feedback_id = ?";
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFeedback(rs);
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding feedback by ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Feedback> findAll() {
        String sql = "SELECT * FROM feedback ORDER BY tanggal_feedback DESC";
        List<Feedback> feedbacks = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                feedbacks.add(mapResultSetToFeedback(rs));
            }
            
            return feedbacks;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all feedbacks: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Feedback> findByRating(int rating) {
        String sql = "SELECT * FROM feedback WHERE rating = ? ORDER BY tanggal_feedback DESC";
        List<Feedback> feedbacks = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, rating);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbacks.add(mapResultSetToFeedback(rs));
                }
            }
            
            return feedbacks;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding feedbacks by rating: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Feedback> findByUserId(Long userId) {
        String sql = "SELECT * FROM feedback WHERE user_id = ? ORDER BY tanggal_feedback DESC";
        List<Feedback> feedbacks = new ArrayList<>();
        
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    feedbacks.add(mapResultSetToFeedback(rs));
                }
            }
            
            return feedbacks;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding feedbacks by user ID: " + e.getMessage(), e);
        }
    }
    
    private Feedback mapResultSetToFeedback(ResultSet rs) throws SQLException {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(rs.getLong("feedback_id"));
        feedback.setUserId(rs.getLong("user_id"));
        feedback.setRating(rs.getInt("rating"));
        feedback.setKomentar(rs.getString("komentar"));
        
        Timestamp timestamp = rs.getTimestamp("tanggal_feedback");
        if (timestamp != null) {
            feedback.setTanggalFeedback(timestamp.toLocalDateTime());
        }
        
        return feedback;
    }
}