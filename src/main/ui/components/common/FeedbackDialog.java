package main.ui.components.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.model.dto.FeedbackDto;

import java.time.LocalDateTime;
import java.util.Optional;

public class FeedbackDialog {
    
    public static Optional<FeedbackDto> showFeedbackDialog(Long userId, String eventName) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Share Your Feedback");
        dialogStage.setWidth(500);
        dialogStage.setHeight(400);
        dialogStage.setResizable(false);
        
        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setStyle("-fx-background-color: white;");
        
        // Header
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("How was your experience?");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label eventLabel = new Label("Event: " + eventName);
        eventLabel.setFont(Font.font("Arial", 16));
        eventLabel.setTextFill(Color.web("#7f8c8d"));
        
        headerBox.getChildren().addAll(titleLabel, eventLabel);
        
        // Rating section
        VBox ratingSection = new VBox(10);
        ratingSection.setAlignment(Pos.CENTER);
        
        Label ratingLabel = new Label("Rating:");
        ratingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        HBox starsBox = new HBox(10);
        starsBox.setAlignment(Pos.CENTER);
        
        ToggleGroup ratingGroup = new ToggleGroup();
        RadioButton[] starButtons = new RadioButton[5];
        
        for (int i = 0; i < 5; i++) {
            final int rating = i + 1;
            RadioButton starButton = new RadioButton("⭐".repeat(rating));
            starButton.setToggleGroup(ratingGroup);
            starButton.setFont(Font.font("Arial", 14));
            starButton.setUserData(rating);
            starButtons[i] = starButton;
            starsBox.getChildren().add(starButton);
        }
        
        // Set default to 5 stars
        starButtons[4].setSelected(true);
        
        ratingSection.getChildren().addAll(ratingLabel, starsBox);
        
        // Comment section
        VBox commentSection = new VBox(10);
        commentSection.setAlignment(Pos.TOP_LEFT);
        
        Label commentLabel = new Label("Your Comments:");
        commentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Tell us about your experience...");
        commentArea.setPrefRowCount(4);
        commentArea.setWrapText(true);
        commentArea.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 10px;"
        );
        
        commentSection.getChildren().addAll(commentLabel, commentArea);
        
        // Buttons section
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button submitButton = new Button("Submit Feedback");
        submitButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        submitButton.setPrefWidth(150);
        submitButton.setPrefHeight(40);
        submitButton.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        );
        
        Button skipButton = new Button("Skip");
        skipButton.setFont(Font.font("Arial", 14));
        skipButton.setPrefWidth(100);
        skipButton.setPrefHeight(40);
        skipButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #7f8c8d;" +
            "-fx-border-color: #bdc3c7;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        );
        
        buttonBox.getChildren().addAll(submitButton, skipButton);
        
        // Add all sections to main container
        mainContainer.getChildren().addAll(headerBox, ratingSection, commentSection, buttonBox);
        
        // Result holder
        final FeedbackDto[] result = {null};
        
        // Button actions
        submitButton.setOnAction(e -> {
            RadioButton selectedRating = (RadioButton) ratingGroup.getSelectedToggle();
            String comment = commentArea.getText().trim();
            
            if (selectedRating != null && !comment.isEmpty()) {
                FeedbackDto feedback = new FeedbackDto();
                feedback.setUserId(userId);
                feedback.setRating((Integer) selectedRating.getUserData());
                feedback.setKomentar(comment);
                feedback.setTanggalFeedback(LocalDateTime.now());
                
                result[0] = feedback;
                dialogStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incomplete Feedback");
                alert.setHeaderText("Please complete your feedback");
                alert.setContentText("Please select a rating and add your comments.");
                alert.showAndWait();
            }
        });
        
        skipButton.setOnAction(e -> {
            dialogStage.close();
        });
        
        // Add hover effects
        submitButton.setOnMouseEntered(e -> submitButton.setStyle(
            "-fx-background-color: #2980b9;" +
            "-fx-text-fill: white;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        ));
        
        submitButton.setOnMouseExited(e -> submitButton.setStyle(
            "-fx-background-color: #3498db;" +
            "-fx-text-fill: white;" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        ));
        
        // Setup scene and show
        Scene scene = new Scene(mainContainer);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
        
        return Optional.ofNullable(result[0]);
    }
}