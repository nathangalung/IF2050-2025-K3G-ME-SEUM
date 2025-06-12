package main.service.impl;

import main.model.dto.FeedbackDto;
import main.model.entities.Feedback;
import main.repository.interfaces.IFeedbackRepository;
import main.service.interfaces.IFeedbackService;
import main.utils.ValidationUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackServiceImpl implements IFeedbackService {
    private final IFeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(IFeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public FeedbackDto createFeedback(FeedbackDto feedbackDto) {
        try {
            validateFeedbackData(feedbackDto);

            // Convert DTO to Entity
            Feedback feedback = new Feedback();
            feedback.setUserId(feedbackDto.getUserId());
            feedback.setRating(feedbackDto.getRating());
            feedback.setKomentar(feedbackDto.getKomentar());
            feedback.setTanggalFeedback(feedbackDto.getTanggalFeedback() != null ? 
                feedbackDto.getTanggalFeedback() : LocalDateTime.now());

            // Save to repository
            Feedback savedFeedback = feedbackRepository.save(feedback);

            // Convert back to DTO and return
            return convertToDto(savedFeedback);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public FeedbackDto updateFeedback(Long id, FeedbackDto feedbackDto) {
        try {
            validateFeedbackData(feedbackDto);

            // Get existing feedback
            Feedback existingFeedback = feedbackRepository.findById(id);
            if (existingFeedback == null) {
                throw new RuntimeException("Feedback not found with ID: " + id);
            }

            // Update fields
            existingFeedback.setRating(feedbackDto.getRating());
            existingFeedback.setKomentar(feedbackDto.getKomentar());

            // Save updated feedback
            Feedback updatedFeedback = feedbackRepository.update(existingFeedback);
            return convertToDto(updatedFeedback);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFeedback(Long id) {
        try {
            Feedback existingFeedback = feedbackRepository.findById(id);
            if (existingFeedback == null) {
                throw new RuntimeException("Feedback not found with ID: " + id);
            }
            feedbackRepository.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public FeedbackDto getFeedbackById(Long id) {
        try {
            Feedback feedback = feedbackRepository.findById(id);
            if (feedback == null) {
                throw new RuntimeException("Feedback not found with ID: " + id);
            }
            return convertToDto(feedback);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get feedback: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();
            return feedbacks.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all feedbacks: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FeedbackDto> getFeedbacksByRating(int rating) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findByRating(rating);
            return feedbacks.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get feedbacks by rating: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FeedbackDto> getFeedbacksByUser(Long userId) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findByUserId(userId);
            return feedbacks.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get feedbacks by user: " + e.getMessage(), e);
        }
    }

    // Helper methods
    private void validateFeedbackData(FeedbackDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Feedback data cannot be null");
        }

        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        if (dto.getKomentar() == null || dto.getKomentar().trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }

        if (dto.getKomentar().length() > 500) {
            throw new IllegalArgumentException("Comment cannot exceed 500 characters");
        }
    }

    private FeedbackDto convertToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setFeedbackId(feedback.getFeedbackId());
        dto.setUserId(feedback.getUserId());
        dto.setRating(feedback.getRating());
        dto.setKomentar(feedback.getKomentar());
        dto.setTanggalFeedback(feedback.getTanggalFeedback());
        return dto;
    }
}