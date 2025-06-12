package main.ui.views.customer;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.controller.ManajemenArtefakController;
import main.model.dto.ArtefakDto;

import java.util.List;

/**
 * Simplified Collection page for frontend testing - focuses on artifact management only
 */
public class CollectionPage extends BorderPane {
    private final ManajemenArtefakController controller;
    
    // Core UI Components (simplified)
    private TilePane artefakPane;
    private ScrollPane scrollPane;
    private TextField searchField;
    
    public CollectionPage(ManajemenArtefakController controller) {
        this.controller = controller;
        initializeComponents();
        setupLayout();
        loadInitialData();
    }
    
    private void initializeComponents() {
        // Initialize artifact display area
        artefakPane = new TilePane();
        artefakPane.setPrefColumns(3);
        artefakPane.setHgap(20);
        artefakPane.setVgap(20);
        artefakPane.setPadding(new Insets(20));
        
        // Create scroll pane for artifacts
        scrollPane = new ScrollPane(artefakPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // Simple search field
        searchField = new TextField();
        searchField.setPromptText("Search artifacts...");
        searchField.setPrefWidth(300);
    }
    
    private void setupLayout() {
        // Create simple header
        VBox header = createSimpleHeader();
        
        // Main content with artifacts
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(20));
        mainContent.getChildren().add(scrollPane);
        
        // Set layout
        setTop(header);
        setCenter(mainContent);
        
        // Style the page
        setStyle("-fx-background-color: #f5f5f5;");
    }
    
    private VBox createSimpleHeader() {
        VBox header = new VBox(15);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
        
        // Page title
        Label titleLabel = new Label("Koleksi Artefak");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        Label subtitleLabel = new Label("Jelajahi koleksi artefak bersejarah museum kami");
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));
        
        // Search area
        HBox searchArea = new HBox(10);
        searchArea.getChildren().addAll(new Label("Search:"), searchField);
        
        header.getChildren().addAll(titleLabel, subtitleLabel, searchArea);
        
        return header;
    }
    
    private void loadInitialData() {
        // Load initial artifacts (mock data for frontend testing)
        if (controller != null) {
            try {
                List<ArtefakDto> artifacts = controller.getAllArtefaks();
                displayArtefaks(artifacts);
            } catch (Exception e) {
                // For frontend-only testing, create mock data
                createMockArtefakDisplay();
            }
        } else {
            createMockArtefakDisplay();
        }
    }
    
    private void createMockArtefakDisplay() {
        // Create mock artifact cards for frontend testing
        for (int i = 1; i <= 6; i++) {
            VBox artefakCard = createArtefakCard(
                "Artefak " + i,
                "Deskripsi artefak " + i,
                "Daerah " + i,
                "mock_image_" + i + ".jpg"
            );
            artefakPane.getChildren().add(artefakCard);
        }
    }
    
    private VBox createArtefakCard(String name, String description, String region, String image) {
        VBox card = new VBox(10);
        card.setPrefWidth(200);
        card.setPrefHeight(250);
        card.setPadding(new Insets(15));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        
        // Image placeholder
        Label imageLabel = new Label("📷");
        imageLabel.setFont(Font.font(40));
        imageLabel.setStyle("-fx-background-color: #ecf0f1; -fx-border-radius: 4; -fx-alignment: center;");
        imageLabel.setPrefHeight(80);
        
        // Artifact name
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setWrapText(true);
        
        // Description
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setTextFill(Color.web("#7f8c8d"));
        descLabel.setWrapText(true);
        
        // Region
        Label regionLabel = new Label("📍 " + region);
        regionLabel.setFont(Font.font("Arial", 10));
        regionLabel.setTextFill(Color.web("#95a5a6"));
        
        // View button
        Button viewButton = new Button("Lihat Detail");
        viewButton.setStyle(
            "-fx-background-color: #3498db; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-border-radius: 4; " +
            "-fx-background-radius: 4;"
        );
        viewButton.setPrefWidth(Double.MAX_VALUE);
        
        card.getChildren().addAll(imageLabel, nameLabel, descLabel, regionLabel, viewButton);
        
        // Add hover effect
        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-scale-y: 1.02; -fx-scale-x: 1.02;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-scale-y: 1.02; -fx-scale-x: 1.02;", "")));
        
        return card;
    }
    
    public void displayArtefaks(List<ArtefakDto> artefaks) {
        artefakPane.getChildren().clear();
        
        if (artefaks == null || artefaks.isEmpty()) {
            Label noDataLabel = new Label("Tidak ada artefak yang ditemukan");
            noDataLabel.setFont(Font.font("Arial", 16));
            noDataLabel.setTextFill(Color.web("#7f8c8d"));
            artefakPane.getChildren().add(noDataLabel);
            return;
        }
        
        for (ArtefakDto artefak : artefaks) {
            VBox artefakCard = createArtefakCard(
                artefak.getNamaArtefak(),
                artefak.getDeskripsiArtefak(),
                artefak.getAsalDaerah(),
                artefak.getGambar()
            );
            artefakPane.getChildren().add(artefakCard);
        }
    }
    
    public void showMessage(String message, boolean isError) {
        Alert alert = new Alert(isError ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(isError ? "Error" : "Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public void performSearch(String searchTerm) {
        if (controller != null) {
            try {
                List<ArtefakDto> results = controller.searchArtefaks(searchTerm);
                displayArtefaks(results);
            } catch (Exception e) {
                showMessage("Error searching: " + e.getMessage(), true);
            }
        }
    }
    
    // Getters for testing
    public TilePane getArtefakPane() { 
        return artefakPane; 
    }
    
    public TextField getSearchField() { 
        return searchField; 
    }
    
    public ScrollPane getScrollPane() { 
        return scrollPane; 
    }
}