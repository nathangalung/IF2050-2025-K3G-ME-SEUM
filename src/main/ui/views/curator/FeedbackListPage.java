package main.ui.views.curator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.controller.FeedbackController;
import main.model.dto.FeedbackDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Feedback List Page for Curator - displays all customer feedback
 */
public class FeedbackListPage extends BorderPane {
    private final FeedbackController feedbackController;
    private TableView<FeedbackDto> feedbackTable;
    private TextField searchField;
    private ComboBox<String> ratingFilter;
    private Label totalFeedbackLabel;
    private Label averageRatingLabel;

    public FeedbackListPage(FeedbackController feedbackController) {
        this.feedbackController = feedbackController;
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadFeedbacks();
    }

    private void initComponents() {
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search feedback by customer name or comment...");
        searchField.setPrefWidth(300);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 8px;"
        );

        // Rating filter
        ratingFilter = new ComboBox<>();
        ratingFilter.getItems().addAll("All Ratings", "5 Stars", "4 Stars", "3 Stars", "2 Stars", "1 Star");
        ratingFilter.setValue("All Ratings");
        ratingFilter.setPrefWidth(150);
        ratingFilter.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );

        // Statistics labels
        totalFeedbackLabel = new Label("Total Feedback: 0");
        totalFeedbackLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        totalFeedbackLabel.setTextFill(Color.web("#2c3e50"));

        averageRatingLabel = new Label("Average Rating: 0.0 ⭐");
        averageRatingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        averageRatingLabel.setTextFill(Color.web("#2c3e50"));

        // Feedback table
        setupFeedbackTable();
    }

    private void setupFeedbackTable() {
        feedbackTable = new TableView<>();
        feedbackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        feedbackTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );

        // ID Column
        TableColumn<FeedbackDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("feedbackId"));
        idColumn.setPrefWidth(60);
        idColumn.setStyle("-fx-alignment: CENTER;");

        // Customer Name Column
        TableColumn<FeedbackDto, String> customerColumn = new TableColumn<>("Customer");
        customerColumn.setCellValueFactory(cellData -> {
            // Show user ID for now - you can enhance this to show actual name
            return new javafx.beans.property.SimpleStringProperty("User " + cellData.getValue().getUserId());
        });
        customerColumn.setPrefWidth(150);

        // Rating Column with stars
        TableColumn<FeedbackDto, Integer> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        ratingColumn.setPrefWidth(100);
        ratingColumn.setCellFactory(column -> new TableCell<FeedbackDto, Integer>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                } else {
                    // Convert rating to stars
                    StringBuilder stars = new StringBuilder();
                    for (int i = 1; i <= 5; i++) {
                        stars.append(i <= rating ? "⭐" : "☆");
                    }
                    setText(stars.toString());
                }
            }
        });

        // Comment Column
        TableColumn<FeedbackDto, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("komentar"));
        commentColumn.setPrefWidth(300);
        commentColumn.setCellFactory(column -> new TableCell<FeedbackDto, String>() {
            @Override
            protected void updateItem(String comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty || comment == null) {
                    setText(null);
                } else {
                    // Truncate long comments
                    String displayText = comment.length() > 50 ? comment.substring(0, 50) + "..." : comment;
                    setText(displayText);
                    setTooltip(new Tooltip(comment)); // Show full comment on hover
                }
            }
        });

        // Date Column
        TableColumn<FeedbackDto, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTanggalFeedback() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
                return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getTanggalFeedback().format(formatter)
                );
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        dateColumn.setPrefWidth(150);

        // Actions Column
        TableColumn<FeedbackDto, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(120);
        actionsColumn.setCellFactory(column -> new TableCell<FeedbackDto, Void>() {
            private final Button viewButton = new Button("👁");
            private final Button deleteButton = new Button("🗑");

            {
                viewButton.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: #1976D2;" +
                    "-fx-border-color: transparent;" +
                    "-fx-padding: 5px;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-size: 14px;"
                );
                viewButton.setTooltip(new Tooltip("View Details"));

                deleteButton.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: #D32F2F;" +
                    "-fx-border-color: transparent;" +
                    "-fx-padding: 5px;" +
                    "-fx-cursor: hand;" +
                    "-fx-font-size: 14px;"
                );
                deleteButton.setTooltip(new Tooltip("Delete Feedback"));

                HBox actionsBox = new HBox(5);
                actionsBox.setAlignment(Pos.CENTER);
                actionsBox.getChildren().addAll(viewButton, deleteButton);

                viewButton.setOnAction(event -> {
                    FeedbackDto feedback = getTableView().getItems().get(getIndex());
                    showFeedbackDetails(feedback);
                });

                deleteButton.setOnAction(event -> {
                    FeedbackDto feedback = getTableView().getItems().get(getIndex());
                    deleteFeedback(feedback);
                });

                setGraphic(actionsBox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(getGraphic());
                }
            }
        });

        feedbackTable.getColumns().addAll(idColumn, customerColumn, ratingColumn, commentColumn, dateColumn, actionsColumn);
    }

    private void setupLayout() {
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20));

        // Header section
        VBox headerSection = new VBox(15);
        
        // Title
        Label titleLabel = new Label("Feedback Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label subtitleLabel = new Label("Monitor and manage customer feedback");
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));

        headerSection.getChildren().addAll(titleLabel, subtitleLabel);

        // Controls section
        HBox controlsSection = new HBox(15);
        controlsSection.setAlignment(Pos.CENTER_LEFT);
        controlsSection.setPadding(new Insets(20, 0, 20, 0));

        // Statistics section
        HBox statsSection = new HBox(30);
        statsSection.setAlignment(Pos.CENTER_LEFT);
        
        VBox totalStats = new VBox(5);
        totalStats.getChildren().add(totalFeedbackLabel);
        
        VBox ratingStats = new VBox(5);
        ratingStats.getChildren().add(averageRatingLabel);

        statsSection.getChildren().addAll(totalStats, ratingStats);

        // Search and filter section
        HBox searchSection = new HBox(15);
        searchSection.setAlignment(Pos.CENTER_LEFT);
        
        Label searchLabel = new Label("Search:");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label filterLabel = new Label("Filter by Rating:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        searchSection.getChildren().addAll(searchLabel, searchField, filterLabel, ratingFilter);

        controlsSection.getChildren().addAll(statsSection, new Region() {{ HBox.setHgrow(this, Priority.ALWAYS); }}, searchSection);

        // Main content
        VBox mainContent = new VBox(20);
        mainContent.getChildren().addAll(headerSection, controlsSection, feedbackTable);

        setTop(mainContent);

        // Make table fill available space
        VBox.setVgrow(feedbackTable, Priority.ALWAYS);
    }

    private void setupEventHandlers() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterFeedbacks();
        });

        // Rating filter
        ratingFilter.setOnAction(e -> filterFeedbacks());
    }

    private void loadFeedbacks() {
        try {
            List<FeedbackDto> feedbacks = feedbackController.getAllFeedbacks();
            feedbackTable.getItems().clear();
            feedbackTable.getItems().addAll(feedbacks);
            
            updateStatistics(feedbacks);
            System.out.println("✅ Loaded " + feedbacks.size() + " feedbacks");
        } catch (Exception e) {
            System.err.println("❌ Error loading feedbacks: " + e.getMessage());
            showAlert("Error", "Failed to load feedbacks: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void filterFeedbacks() {
        try {
            List<FeedbackDto> allFeedbacks = feedbackController.getAllFeedbacks();
            String searchText = searchField.getText().toLowerCase().trim();
            String ratingFilterValue = ratingFilter.getValue();

            List<FeedbackDto> filteredFeedbacks = allFeedbacks.stream()
                .filter(feedback -> {
                    // Search filter
                    boolean matchesSearch = searchText.isEmpty() || 
                        feedback.getKomentar().toLowerCase().contains(searchText);

                    // Rating filter
                    boolean matchesRating = ratingFilterValue.equals("All Ratings") ||
                        (ratingFilterValue.equals("5 Stars") && feedback.getRating() == 5) ||
                        (ratingFilterValue.equals("4 Stars") && feedback.getRating() == 4) ||
                        (ratingFilterValue.equals("3 Stars") && feedback.getRating() == 3) ||
                        (ratingFilterValue.equals("2 Stars") && feedback.getRating() == 2) ||
                        (ratingFilterValue.equals("1 Star") && feedback.getRating() == 1);

                    return matchesSearch && matchesRating;
                })
                .toList();

            feedbackTable.getItems().clear();
            feedbackTable.getItems().addAll(filteredFeedbacks);
            updateStatistics(filteredFeedbacks);

        } catch (Exception e) {
            System.err.println("❌ Error filtering feedbacks: " + e.getMessage());
        }
    }

    private void updateStatistics(List<FeedbackDto> feedbacks) {
        totalFeedbackLabel.setText("Total Feedback: " + feedbacks.size());
        
        if (!feedbacks.isEmpty()) {
            double averageRating = feedbacks.stream()
                .mapToInt(FeedbackDto::getRating)
                .average()
                .orElse(0.0);
            averageRatingLabel.setText(String.format("Average Rating: %.1f ⭐", averageRating));
        } else {
            averageRatingLabel.setText("Average Rating: 0.0 ⭐");
        }
    }

    private void showFeedbackDetails(FeedbackDto feedback) {
        Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
        detailsAlert.setTitle("Feedback Details");
        detailsAlert.setHeaderText("Feedback from User " + feedback.getUserId());
        
        StringBuilder details = new StringBuilder();
        details.append("Rating: ").append(feedback.getRating()).append(" stars\n\n");
        details.append("Comment:\n").append(feedback.getKomentar()).append("\n\n");
        details.append("Date: ");
        if (feedback.getTanggalFeedback() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' HH:mm");
            details.append(feedback.getTanggalFeedback().format(formatter));
        } else {
            details.append("N/A");
        }
        
        detailsAlert.setContentText(details.toString());
        detailsAlert.showAndWait();
    }

    private void deleteFeedback(FeedbackDto feedback) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Feedback");
        confirmDialog.setHeaderText("Delete this feedback?");
        confirmDialog.setContentText("Are you sure you want to delete this feedback? This action cannot be undone.");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    feedbackController.deleteFeedback(feedback.getFeedbackId());
                    loadFeedbacks(); // Reload the table
                    showAlert("Success", "Feedback deleted successfully!", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete feedback: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshFeedbacks() {
        loadFeedbacks();
    }
}