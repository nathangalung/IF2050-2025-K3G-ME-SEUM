package main.model.entities;

import java.time.LocalDateTime;

public class Feedback {
    private Long feedbackId;
    private Long userId;
    private Integer rating;
    private String komentar;
    private LocalDateTime tanggalFeedback;

    // Default constructor
    public Feedback() {}

    // Constructor with parameters
    public Feedback(Long feedbackId, Long userId, Integer rating, String komentar, LocalDateTime tanggalFeedback) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.rating = rating;
        this.komentar = komentar;
        this.tanggalFeedback = tanggalFeedback;
    }

    // Getters and Setters
    public Long getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getKomentar() {
        return komentar;
    }

    public void setKomentar(String komentar) {
        this.komentar = komentar;
    }

    public LocalDateTime getTanggalFeedback() {
        return tanggalFeedback;
    }

    public void setTanggalFeedback(LocalDateTime tanggalFeedback) {
        this.tanggalFeedback = tanggalFeedback;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", komentar='" + komentar + '\'' +
                ", tanggalFeedback=" + tanggalFeedback +
                '}';
    }
}