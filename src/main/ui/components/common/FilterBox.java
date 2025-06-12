package main.ui.components.collection;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Filter box component for ME-SEUM collection page
 * Creates a filter section with search and region filters
 */
public class FilterBox extends HBox {
    
    private TextField searchField;
    private Button searchButton;
    private ComboBox<String> filterCombo;
    private FilterListener filterListener;
    
    /**
     * Constructor for FilterBox
     */
    public FilterBox() {
        setupFilterBox();
    }
    
    /**
     * Constructor with filter listener
     * @param listener The filter listener
     */
    public FilterBox(FilterListener listener) {
        this.filterListener = listener;
        setupFilterBox();
    }
    
    /**
     * Set up the filter box appearance and elements
     */
    private void setupFilterBox() {
        // Set appearance
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(15, 20, 15, 20));
        setSpacing(15);
        setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        
        // Search section
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        // Search label
        Label searchLabel = new Label("Cari Artefak:");
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Cari nama artefak...");
        searchField.setPrefWidth(300);
        
        // Search button
        searchButton = new Button("Cari");
        searchButton.setStyle(
            "-fx-background-color: #1976D2;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 16;" +
            "-fx-background-radius: 4px;" +
            "-fx-cursor: hand;"
        );
        
        // Add search elements to search box
        searchBox.getChildren().addAll(searchLabel, searchField, searchButton);
        
        // Filter section
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        
        // Filter label
        Label filterLabel = new Label("Filter:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        // Filter combo box
        filterCombo = new ComboBox<>();
        filterCombo.getItems().addAll(
            "Semua Daerah", "Jawa", "Sumatra", "Kalimantan", 
            "Sulawesi", "Papua", "Bali", "NTT", "NTB"
        );
        filterCombo.setValue("Semua Daerah");
        filterCombo.setPrefWidth(150);
        
        // Add filter elements to filter box
        filterBox.getChildren().addAll(filterLabel, filterCombo);
        
        // Spacer to push filter to right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Add all elements to the filter box
        getChildren().addAll(searchBox, spacer, filterBox);
        
        // Set up event handlers
        setupEventHandlers();
    }
    
    /**
     * Set up event handlers for filter box elements
     */
    private void setupEventHandlers() {
        // Search button clicked
        searchButton.setOnAction(e -> {
            if (filterListener != null) {
                filterListener.onSearch(searchField.getText().trim());
            }
        });
        
        // Enter pressed in search field
        searchField.setOnAction(e -> {
            if (filterListener != null) {
                filterListener.onSearch(searchField.getText().trim());
            }
        });
        
        // Filter combo box changed
        filterCombo.setOnAction(e -> {
            if (filterListener != null) {
                filterListener.onFilter(filterCombo.getValue());
            }
        });
    }
    
    /**
     * Get the search field
     * @return The search field
     */
    public TextField getSearchField() {
        return searchField;
    }
    
    /**
     * Get the filter combo box
     * @return The filter combo box
     */
    public ComboBox<String> getFilterCombo() {
        return filterCombo;
    }
    
    /**
     * Set the filter listener
     * @param listener The filter listener
     */
    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }
    
    /**
     * Interface for filter listener
     */
    public interface FilterListener {
        void onSearch(String searchTerm);
        void onFilter(String filter);
    }
}