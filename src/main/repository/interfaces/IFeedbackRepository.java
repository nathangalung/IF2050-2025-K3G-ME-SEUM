package main.repository.interfaces;

import main.model.entities.Feedback;
import java.util.List;

public interface IFeedbackRepository {
    Feedback save(Feedback feedback);
    Feedback update(Feedback feedback);
    void delete(Long id);
    Feedback findById(Long id);
    List<Feedback> findAll();
    List<Feedback> findByRating(int rating);
    List<Feedback> findByUserId(Long userId);
}