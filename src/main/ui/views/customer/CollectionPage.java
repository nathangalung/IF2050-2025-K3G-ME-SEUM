package main.ui.views.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.controller.ManajemenArtefakController;
import main.model.dto.ArtefakDto;
import main.ui.components.common.NavigationBar;
import main.ui.components.common.Footer;
import main.ui.components.collection.FilterBox;

import java.util.List;

/**
 * Collection page for displaying artifacts to museum visitors
 */
public class CollectionPage extends BorderPane implements FilterBox.FilterListener, NavigationBar.NavigationListener {
    private final ManajemenArtefakController controller;
    
    // UI Components
    private NavigationBar navigationBar;
    private FilterBox filterBox;
    private TilePane artefakPane;
    private ScrollPane scrollPane;           // For artifacts only
    private ScrollPane mainScrollPane;       // For entire page including footer - ADD THIS FIELD
    private Footer footer;
    
    // Add external navigation listener
    private NavigationBar.NavigationListener externalNavigationListener;
    
    /**
     * Constructor for CollectionPage
     * @param controller The artifact management controller
     */
    public CollectionPage(ManajemenArtefakController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        loadArtefaks();
    }
    
    /**
     * Constructor for CollectionPage with external navigation listener
     * @param controller The artifact management controller
     * @param externalNavigationListener The external navigation listener
     */
    public CollectionPage(ManajemenArtefakController controller, NavigationBar.NavigationListener externalNavigationListener) {
        this.controller = controller;
        this.externalNavigationListener = externalNavigationListener;
        initComponents();
        setupLayout();
        loadArtefaks();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        // Create navigation bar with this class as listener
        navigationBar = new NavigationBar(this);
        navigationBar.setActiveDestination("COLLECTION");
        
        // Create filter box with this class as listener
        filterBox = new FilterBox(this);
        
        // Create artifact display area
        artefakPane = new TilePane();
        artefakPane.setHgap(15);
        artefakPane.setVgap(15);
        artefakPane.setPadding(new Insets(20));
        artefakPane.setPrefTileWidth(280);
        artefakPane.setPrefTileHeight(350);
        
        // Put artifact pane in scroll pane
        scrollPane = new ScrollPane(artefakPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f3e8;");
        
        // Create footer
        footer = new Footer();
    }
    
    /**
     * Set up layout
     */
    private void setupLayout() {
        // Set background color for the entire page
        setStyle("-fx-background-color: #f5f3e8;");
        
        // Create header with title
        VBox headerBox = new VBox(20);
        headerBox.setAlignment(Pos.CENTER);
        
        // Add title
        Label titleLabel = new Label("Artwork Collection");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setPadding(new Insets(30, 0, 30, 0));
        headerBox.getChildren().add(titleLabel);
        
        // Style header box
        headerBox.setStyle("-fx-background-color: #f5f3e8;");
        
        // Create main content container that includes content and footer
        VBox mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #f5f3e8;");
        
        // Create content container for filter and artifacts
        VBox contentContainer = new VBox();
        contentContainer.getChildren().addAll(filterBox, artefakPane);
        contentContainer.setPadding(new Insets(20));
        
        // Add content and footer to main content
        mainContent.getChildren().addAll(
            contentContainer,
            footer 
        );
        
        // Create scroll pane for the entire main content (including footer)
        mainScrollPane = new ScrollPane();  // Use mainScrollPane as class field
        mainScrollPane.setContent(mainContent);
        mainScrollPane.setFitToWidth(true);
        mainScrollPane.setStyle("-fx-background-color: #f5f3e8;");
        mainScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        mainScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // Set the layout components
        setTop(headerBox);
        setCenter(mainScrollPane);  // Use mainScrollPane
        // Footer is now part of the scrollable content, not setBottom()
    }
    
    /**
     * Load all artifacts
     */
    private void loadArtefaks() {
        try {
            List<ArtefakDto> artefaks = controller.getAllArtefaks();
            displayArtefaks(artefaks);
        } catch (Exception e) {
            showAlert("Error", "Error loading artifacts: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Display artifacts in grid
     * @param artefaks The list of artifacts to display
     */
    private void displayArtefaks(List<ArtefakDto> artefaks) {
        artefakPane.getChildren().clear();
        
        if (artefaks.isEmpty()) {
            Label emptyLabel = new Label("No artifacts found");
            emptyLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            emptyLabel.setTextFill(Color.GRAY);
            artefakPane.getChildren().add(emptyLabel);
            return;
        }
        
        for (ArtefakDto artefak : artefaks) {
            VBox artefakCard = createArtefakCard(artefak);
            artefakPane.getChildren().add(artefakCard);
        }
    }
    
    /**
     * Create artifact card component
     * @param artefak The artifact to create card for
     * @return The artifact card
     */
    private VBox createArtefakCard(ArtefakDto artefak) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(15));
        card.setPrefWidth(250);
        card.setPrefHeight(320);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );
        
        // Image placeholder
        Label imageLabel = new Label("📷 No Image");
        imageLabel.setPrefSize(220, 150);
        imageLabel.setAlignment(Pos.CENTER);
        imageLabel.setStyle(
            "-fx-background-color: #f8f9fa;" +
            "-fx-border-color: #dee2e6;" +
            "-fx-border-width: 1;" +
            "-fx-text-fill: #6c757d;" +
            "-fx-font-size: 14px;"
        );
        
        // Info section
        VBox infoBox = new VBox(5);
        infoBox.setAlignment(Pos.TOP_LEFT);
        
        Label nameLabel = new Label(artefak.getNamaArtefak());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        nameLabel.setTextFill(Color.web("#2E7D32"));
        nameLabel.setWrapText(true);
        
        Label regionLabel = new Label("Asal: " + artefak.getAsalDaerah());
        regionLabel.setFont(Font.font("Arial", 12));
        regionLabel.setTextFill(Color.web("#666666"));
        
        Label periodLabel = new Label("Periode: " + artefak.getPeriode());
        periodLabel.setFont(Font.font("Arial", 12));
        periodLabel.setTextFill(Color.web("#666666"));
        
        Label statusLabel = new Label("Status: " + artefak.getStatus());
        statusLabel.setFont(Font.font("Arial", 12));
        statusLabel.setTextFill(Color.web("#28a745"));
        
        infoBox.getChildren().addAll(nameLabel, regionLabel, periodLabel, statusLabel);
        
        // Detail button
        Button detailButton = new Button("Lihat Detail");
        detailButton.setStyle(
            "-fx-background-color: #17a2b8;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6 12;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        detailButton.setOnAction(e -> showArtefakDetail(artefak));
        
        // Add hover effect to card
        card.setOnMouseEntered(e -> 
            card.setStyle(
                "-fx-background-color: #f8f9fa;" +
                "-fx-border-color: #2E7D32;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 4);"
            )
        );
        
        card.setOnMouseExited(e -> 
            card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #dee2e6;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
            )
        );
        
        card.getChildren().addAll(imageLabel, infoBox, detailButton);
        return card;
    }
    
    /**
     * Show artifact detail dialog
     * @param artefak The artifact to show details for
     */
    private void showArtefakDetail(ArtefakDto artefak) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Artefak");
        alert.setHeaderText(artefak.getNamaArtefak());
        
        String content = String.format(
            "Deskripsi: %s%n%n" +
            "Asal Daerah: %s%n" +
            "Periode: %s%n" +
            "Status: %s",
            artefak.getDeskripsiArtefak(),
            artefak.getAsalDaerah(),
            artefak.getPeriode(),
            artefak.getStatus()
        );
        
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Helper method to show alerts
     * @param title The alert title
     * @param message The alert message
     * @param type The alert type
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Implementation of FilterBox.FilterListener
     * @param searchTerm The search term
     */
    @Override
    public void onSearch(String searchTerm) {
        try {
            List<ArtefakDto> results = controller.searchArtefaks(searchTerm);
            displayArtefaks(results);
        } catch (Exception e) {
            showAlert("Error", "Error searching artifacts: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Implementation of FilterBox.FilterListener
     * @param filter The selected filter
     */
    @Override
    public void onFilter(String filter) {
        try {
            List<ArtefakDto> results;
            if ("Semua Daerah".equals(filter)) {
                results = controller.getAllArtefaks();
            } else {
                results = controller.getArtefaksByAsalDaerah(filter);
            }
            displayArtefaks(results);
        } catch (Exception e) {
            showAlert("Error", "Error filtering artifacts: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    /**
     * Implementation of NavigationBar.NavigationListener
     * @param destination The navigation destination
     */
    @Override
    public void onNavigate(String destination) {
        // Forward navigation to external listener (Main.java)
        if (externalNavigationListener != null) {
            externalNavigationListener.onNavigate(destination);
        }
    }

    /**
     * Scroll to footer when contact is clicked
     */
    public void scrollToFooter() {
        // FIXED: Use mainScrollPane instead of scrollPane
        mainScrollPane.setVvalue(1.0); // Scroll to bottom
    }
}