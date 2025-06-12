package main.service.interfaces;

import main.model.dto.FeedbackDto;
import java.util.List;

public interface IFeedbackService {
    FeedbackDto createFeedback(FeedbackDto feedback);
    FeedbackDto updateFeedback(Long id, FeedbackDto feedback);
    void deleteFeedback(Long id);
    FeedbackDto getFeedbackById(Long id);
    List<FeedbackDto> getAllFeedbacks();
    List<FeedbackDto> getFeedbacksByRating(int rating);
    List<FeedbackDto> getFeedbacksByUser(Long userId);
}