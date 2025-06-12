package main.model.dto;

import java.time.LocalDateTime;

public class FeedbackDto {
    private Long feedbackId;
    private Long userId;
    private Integer rating;
    private String komentar;
    private LocalDateTime tanggalFeedback;

    // Default constructor
    public FeedbackDto() {}

    // Constructor with parameters
    public FeedbackDto(Long feedbackId, Long userId, Integer rating, String komentar, LocalDateTime tanggalFeedback) {
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
        return "FeedbackDto{" +
                "feedbackId=" + feedbackId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", komentar='" + komentar + '\'' +
                ", tanggalFeedback=" + tanggalFeedback +
                '}';
    }
}