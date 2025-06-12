package main.controller;

import main.model.dto.FeedbackDto;
import main.service.interfaces.IFeedbackService;
import java.util.List;

public class FeedbackController {
    private final IFeedbackService feedbackService;

    public FeedbackController(IFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    public List<FeedbackDto> getAllFeedbacks() {
        try {
            return feedbackService.getAllFeedbacks();
        } catch (Exception e) {
            System.err.println("Error getting all feedbacks: " + e.getMessage());
            throw new RuntimeException("Failed to get feedbacks: " + e.getMessage());
        }
    }

    public FeedbackDto getFeedbackById(Long id) {
        try {
            return feedbackService.getFeedbackById(id);
        } catch (Exception e) {
            System.err.println("Error getting feedback by ID: " + e.getMessage());
            throw new RuntimeException("Failed to get feedback: " + e.getMessage());
        }
    }

    public void createFeedback(FeedbackDto feedback) {
        try {
            feedbackService.createFeedback(feedback);
        } catch (Exception e) {
            System.err.println("Error creating feedback: " + e.getMessage());
            throw new RuntimeException("Failed to create feedback: " + e.getMessage());
        }
    }

    public void updateFeedback(Long id, FeedbackDto feedback) {
        try {
            feedbackService.updateFeedback(id, feedback);
        } catch (Exception e) {
            System.err.println("Error updating feedback: " + e.getMessage());
            throw new RuntimeException("Failed to update feedback: " + e.getMessage());
        }
    }

    public void deleteFeedback(Long id) {
        try {
            feedbackService.deleteFeedback(id);
        } catch (Exception e) {
            System.err.println("Error deleting feedback: " + e.getMessage());
            throw new RuntimeException("Failed to delete feedback: " + e.getMessage());
        }
    }

    public List<FeedbackDto> getFeedbacksByRating(int rating) {
        try {
            return feedbackService.getFeedbacksByRating(rating);
        } catch (Exception e) {
            System.err.println("Error getting feedbacks by rating: " + e.getMessage());
            throw new RuntimeException("Failed to get feedbacks by rating: " + e.getMessage());
        }
    }

    public List<FeedbackDto> getFeedbacksByUser(Long userId) {
        try {
            return feedbackService.getFeedbacksByUser(userId);
        } catch (Exception e) {
            System.err.println("Error getting feedbacks by user: " + e.getMessage());
            throw new RuntimeException("Failed to get feedbacks by user: " + e.getMessage());
        }
    }
}