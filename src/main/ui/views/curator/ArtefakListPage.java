package main.ui.views.curator;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.controller.ManajemenArtefakController;
import main.model.dto.ArtefakDto;
import main.ui.interfaces.ManajemenArtefakInterface;

// Halaman manajemen daftar artefak untuk kurator (JavaFX)
public class ArtefakListPage extends BorderPane implements ManajemenArtefakInterface {
    private final ManajemenArtefakController controller;
    
    // UI Components
    private TableView<ArtefakDto> artefakTable;
    private TextField searchField;
    private Button newArtefactButton;
    private Label titleLabel;
    private Label subtitleLabel;
    private Pagination pagination;
    
    // Pagination variables
    private static final int ITEMS_PER_PAGE = 10;
    private List<ArtefakDto> allArtefaks;
    private int currentPageIndex = 0;
    
    public ArtefakListPage(ManajemenArtefakController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        setupEventHandlers();
        refreshArtefakList();
    }
    
    // Initialize components
    private void initComponents() {
        // Title and subtitle
        titleLabel = new Label("Artifact List");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        subtitleLabel = new Label("Dashboard");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.setTextFill(Color.GRAY);
        
        // Search field with rounded corner style
        searchField = new TextField();
        searchField.setPromptText("Search...");
        searchField.setPrefWidth(250);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 5px;"
        );
        
        // New Artifact button with + icon
        newArtefactButton = new Button("NEW ARTIFACT");
        newArtefactButton.setStyle(
            "-fx-background-color: #1976D2;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 7px 15px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-font-size: 12px;"
        );
        
        // Create table with columns matching the image
        artefakTable = new TableView<>();
        artefakTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        artefakTable.setStyle(
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );
        
        // ID Column
        TableColumn<ArtefakDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("artefakId"));
        idColumn.setPrefWidth(60);
        
        // Name Column
        TableColumn<ArtefakDto, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("namaArtefak"));
        nameColumn.setPrefWidth(150);
        
        // From Column (Asal Daerah)
        TableColumn<ArtefakDto, String> fromColumn = new TableColumn<>("From");
        fromColumn.setCellValueFactory(new PropertyValueFactory<>("asalDaerah"));
        fromColumn.setPrefWidth(120);
        
        // Description Column
        TableColumn<ArtefakDto, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("deskripsiArtefak"));
        descColumn.setPrefWidth(250);
        
        // Images Column
        TableColumn<ArtefakDto, String> imagesColumn = new TableColumn<>("Images");
        imagesColumn.setCellValueFactory(new PropertyValueFactory<>("gambar"));
        imagesColumn.setPrefWidth(120);
        
        // Actions Column
        TableColumn<ArtefakDto, Void> actionsColumn = new TableColumn<>("");
        actionsColumn.setPrefWidth(100);
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button();
            private final Button deleteBtn = new Button();
            private final HBox actionsBox = new HBox(5);
            
            {
                // Edit button
                editBtn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: #1976D2;" +
                    "-fx-border-color: transparent;" +
                    "-fx-padding: 5px;" +
                    "-fx-cursor: hand;"
                );
                editBtn.setGraphic(new javafx.scene.text.Text("✏️"));
                editBtn.setTooltip(new Tooltip("Edit"));
                editBtn.setOnAction(event -> {
                    ArtefakDto data = getTableView().getItems().get(getIndex());
                    showEditArtefakForm(data.getArtefakId());
                });
                
                // Delete button
                deleteBtn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: #D32F2F;" +
                    "-fx-border-color: transparent;" +
                    "-fx-padding: 5px;" +
                    "-fx-cursor: hand;"
                );
                deleteBtn.setGraphic(new javafx.scene.text.Text("🗑️"));
                deleteBtn.setTooltip(new Tooltip("Delete"));
                deleteBtn.setOnAction(event -> {
                    ArtefakDto data = getTableView().getItems().get(getIndex());
                    deleteArtefak(data);
                });
                
                actionsBox.getChildren().addAll(editBtn, deleteBtn);
                actionsBox.setAlignment(Pos.CENTER);
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionsBox);
                }
            }
        });
        
        // Add columns to table
        artefakTable.getColumns().addAll(
            idColumn, 
            nameColumn, 
            fromColumn, 
            descColumn, 
            imagesColumn,
            actionsColumn
        );
        
        // Pagination to match the design in the image
        pagination = new Pagination();
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setStyle("-fx-border-color: transparent;");
        
        // Set page factory for pagination
        pagination.setPageFactory(this::createPage);
    }
    
    // Create page content for pagination
    private TableView<ArtefakDto> createPage(int pageIndex) {
        currentPageIndex = pageIndex;
        loadArtefaksByPage(pageIndex);
        return artefakTable;
    }
    
    // Setup layout
    private void setupLayout() {
        // Set white background for the entire page
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20));
        
        // Top section with title and subtitle
        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Action row with search and add button
        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        actionRow.getChildren().addAll(newArtefactButton, spacer, searchField);
        
        // Container for header and action row
        VBox topContainer = new VBox(20);
        topContainer.getChildren().addAll(headerBox, actionRow);
        
        // Table container with padding
        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(20, 0, 0, 0));
        
        // Bottom section with pagination - Fixed positioning to match design
        HBox paginationControls = new HBox(5);
        paginationControls.setAlignment(Pos.CENTER);
        
        // Add table to container
        tableContainer.getChildren().add(artefakTable);
        
        // Create custom pagination controls
        createCustomPaginationControls(tableContainer);
        
        // Add all components to the main layout
        setTop(topContainer);
        setCenter(tableContainer);
    }

    // Custom pagination controls that match the design in the second image
    private void createCustomPaginationControls(VBox container) {
        // Custom pagination controls
        HBox paginationBox = new HBox(5);
        paginationBox.setAlignment(Pos.CENTER_LEFT);
        paginationBox.setPadding(new Insets(10, 0, 0, 0));
        
        // Previous page button
        Button prevButton = new Button("«");
        prevButton.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6;");
        prevButton.setOnAction(e -> {
            if (currentPageIndex > 0) {
                pagination.setCurrentPageIndex(currentPageIndex - 1);
            }
        });
        
        // Page number buttons
        HBox pageButtons = new HBox(2);
        
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) -> {
            updatePageButtons(pageButtons, newVal.intValue());
        });
        
        // Next page button
        Button nextButton = new Button("»");
        nextButton.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6;");
        nextButton.setOnAction(e -> {
            if (currentPageIndex < pagination.getPageCount() - 1) {
                pagination.setCurrentPageIndex(currentPageIndex + 1);
            }
        });
        
        // Page size selector
        ComboBox<Integer> pageSizeSelector = new ComboBox<>();
        pageSizeSelector.getItems().addAll(10, 25, 50, 100);
        pageSizeSelector.setValue(ITEMS_PER_PAGE);
        pageSizeSelector.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6;");
        
        // Page label
        Label pageLabel = new Label("/Page");
        pageLabel.setFont(Font.font("System", 12));
        pageLabel.setTextFill(Color.GRAY);
        
        // Add components to pagination box
        paginationBox.getChildren().addAll(prevButton, pageButtons, nextButton, pageSizeSelector, pageLabel);
        
        // Initialize page buttons
        updatePageButtons(pageButtons, 0);
        
        // Add pagination to container
        container.getChildren().add(paginationBox);
    }

    // Page buttons based on current page
    private void updatePageButtons(HBox pageButtons, int currentPage) {
        pageButtons.getChildren().clear();
        
        int totalPages = pagination.getPageCount();
        int start = Math.max(0, currentPage - 2);
        int end = Math.min(totalPages, start + 5);
        
        for (int i = start; i < end; i++) {
            Button pageButton = new Button(String.valueOf(i + 1));
            int pageIndex = i;
            
            if (i == currentPage) {
                pageButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-border-color: #007bff;");
            } else {
                pageButton.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6;");
            }
            
            pageButton.setOnAction(e -> pagination.setCurrentPageIndex(pageIndex));
            pageButtons.getChildren().add(pageButton);
        }
    }
    
    // Setup event handlers
    private void setupEventHandlers() {
        // New artifact button
        newArtefactButton.setOnAction(e -> showAddArtefakForm());
        
        // Search field
        searchField.setOnAction(e -> performSearch());
        
        // Add text change listener for real-time search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Only search if user stopped typing for 500ms
            if (newValue != null && !newValue.equals(oldValue)) {
                performSearch();
            }
        });
    }
    
    // Load artefaks by page - ensuring order by ID
    private void loadArtefaksByPage(int page) {
        try {
            if (allArtefaks == null) {
                return;
            }
            
            // Calculate pagination
            int fromIndex = page * ITEMS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allArtefaks.size());
            
            // Clear table first
            artefakTable.getItems().clear();
            
            // Add items for this page if there are items
            if (fromIndex < allArtefaks.size()) {
                List<ArtefakDto> pageItems = allArtefaks.subList(fromIndex, toIndex);
                artefakTable.getItems().addAll(pageItems);
            }
            
            // Force table refresh
            artefakTable.refresh();
            
        } catch (Exception e) {
            showErrorMessage("Failed to load page: " + e.getMessage());
        }
    }
    
    // Load all artifacts ordered by ID
    private void loadAllArtefaks() {
        try {
            // Get artifacts ordered by ID
            allArtefaks = controller.getAllArtefaksOrderById();
            
            // Update pagination
            int totalPages = (int) Math.ceil((double) allArtefaks.size() / ITEMS_PER_PAGE);
            pagination.setPageCount(Math.max(1, totalPages));
            
            // Reset to first page and load data
            pagination.setCurrentPageIndex(0);
            currentPageIndex = 0;
            
            // Force load the first page
            loadArtefaksByPage(0);
            
        } catch (Exception e) {
            showErrorMessage("Failed to load artifacts: " + e.getMessage());
        }
    }
    
    // Perform search operation
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        try {
            if (searchTerm.isEmpty()) {
                // Load all artifacts ordered by ID
                loadAllArtefaks();
            } else {
                // Search artifacts and order by ID
                allArtefaks = controller.searchArtefaksOrderById(searchTerm);
                
                // Update pagination
                int totalPages = (int) Math.ceil((double) allArtefaks.size() / ITEMS_PER_PAGE);
                pagination.setPageCount(Math.max(1, totalPages));
                
                // Reset to first page
                pagination.setCurrentPageIndex(0);
                currentPageIndex = 0;
                
                // Force reload the first page
                loadArtefaksByPage(0);
            }
            
        } catch (Exception e) {
            showErrorMessage("Search failed: " + e.getMessage());
        }
    }
    
    // Delete artefak with confirmation
    private void deleteArtefak(ArtefakDto artefak) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Artifact");
        alert.setContentText("Are you sure you want to delete artifact: " + artefak.getNamaArtefak() + "?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deleteArtefak(artefak.getArtefakId());
                    
                    // Refresh the list and maintain current page if possible
                    refreshArtefakListAndMaintainPage();
                    
                    showSuccessMessage("Artifact deleted successfully!");
                } catch (Exception e) {
                    showErrorMessage("Failed to delete artifact: " + e.getMessage());
                }
            }
        });
    }
    
    // Refresh list and try to maintain current page
    private void refreshArtefakListAndMaintainPage() {
        int savedPageIndex = currentPageIndex;
        loadAllArtefaks();
        
        // Try to go back to the same page, or go to last available page
        int maxPageIndex = pagination.getPageCount() - 1;
        int targetPage = Math.min(savedPageIndex, maxPageIndex);
        
        pagination.setCurrentPageIndex(Math.max(0, targetPage));
        
        // Force reload the page content
        loadArtefaksByPage(pagination.getCurrentPageIndex());
    }

    // Interface implementations
    @Override
    public void showAddArtefakForm() {
        try {
            Dialog<ArtefakDto> dialog = createArtefakDialog("Add New Artifact", null);
            dialog.showAndWait().ifPresent(artefak -> {
                try {
                    controller.createArtefak(artefak);
                    
                    // Refresh and go to last page to show the new artifact
                    loadAllArtefaks();
                    int lastPage = Math.max(0, pagination.getPageCount() - 1);
                    pagination.setCurrentPageIndex(lastPage);
                    
                    // Force reload the last page content
                    loadArtefaksByPage(lastPage);
                    
                    showSuccessMessage("Artifact added successfully!");
                } catch (Exception e) {
                    showErrorMessage("Failed to add artifact: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showErrorMessage("Failed to open form: " + e.getMessage());
        }
    }
    
    @Override
    public void showEditArtefakForm(Long artefakId) {
        try {
            ArtefakDto artefak = controller.getArtefakById(artefakId);
            Dialog<ArtefakDto> dialog = createArtefakDialog("Edit Artifact", artefak);
            dialog.showAndWait().ifPresent(updatedArtefak -> {
                try {
                    controller.updateArtefak(artefakId, updatedArtefak);
                    
                    // Refresh the list and maintain page position
                    refreshArtefakListAndMaintainPage();
                    
                    showSuccessMessage("Artifact updated successfully!");
                } catch (Exception e) {
                    showErrorMessage("Failed to update artifact: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            showErrorMessage("Failed to load artifact data: " + e.getMessage());
        }
    }
    
    // Create dialog for add/edit
    private Dialog<ArtefakDto> createArtefakDialog(String title, ArtefakDto artefak) {
        Dialog<ArtefakDto> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Create form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Artifact name");
        
        TextField fromField = new TextField();
        fromField.setPromptText("Origin location");
        
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description");
        descriptionArea.setPrefRowCount(3);
        
        TextField imagesField = new TextField();
        imagesField.setPromptText("Image filename");
        
        // Fill fields if editing
        if (artefak != null) {
            nameField.setText(artefak.getNamaArtefak());
            fromField.setText(artefak.getAsalDaerah());
            descriptionArea.setText(artefak.getDeskripsiArtefak());
            imagesField.setText(artefak.getGambar());
        }
        
        // Add fields to grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("From:"), 0, 1);
        grid.add(fromField, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionArea, 1, 2);
        grid.add(new Label("Images:"), 0, 3);
        grid.add(imagesField, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on name field
        nameField.requestFocus();
        
        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                ArtefakDto dto = new ArtefakDto();
                dto.setNamaArtefak(nameField.getText().trim());
                dto.setAsalDaerah(fromField.getText().trim());
                dto.setDeskripsiArtefak(descriptionArea.getText().trim());
                dto.setGambar(imagesField.getText().trim());
                
                if (artefak != null) {
                    dto.setArtefakId(artefak.getArtefakId());
                    dto.setStatus(artefak.getStatus());
                    dto.setPeriode(artefak.getPeriode());
                    dto.setTanggalRegistrasi(artefak.getTanggalRegistrasi());
                    dto.setCuratorId(artefak.getCuratorId());
                } else {
                    dto.setStatus("TERSEDIA");
                    dto.setPeriode("Unknown");
                    dto.setCuratorId(1L); // Default curator
                }
                
                return dto;
            }
            return null;
        });
        
        return dialog;
    }
    
    @Override
    public void displayArtefakList(List<ArtefakDto> artefaks) {
        allArtefaks = artefaks;
        
        // Update pagination
        int totalPages = (int) Math.ceil((double) artefaks.size() / ITEMS_PER_PAGE);
        pagination.setPageCount(Math.max(1, totalPages));
        
        // Load first page
        pagination.setCurrentPageIndex(0);
        currentPageIndex = 0;
        
        // Force reload the page content
        loadArtefaksByPage(0);
    }
    
    @Override
    public void showArtefakDetails(ArtefakDto artefak) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Artifact Details");
        alert.setHeaderText(artefak.getNamaArtefak());
        
        String content = String.format(
            "ID: %d\n" +
            "Name: %s\n" +
            "From: %s\n" +
            "Description: %s\n" +
            "Images: %s\n" +
            "Status: %s\n" +
            "Period: %s",
            artefak.getArtefakId(),
            artefak.getNamaArtefak(),
            artefak.getAsalDaerah(),
            artefak.getDeskripsiArtefak(),
            artefak.getGambar(),
            artefak.getStatus(),
            artefak.getPeriode()
        );
        
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @Override
    public List<ArtefakDto> searchArtefak(String criteria) {
        try {
            return controller.searchArtefaksOrderById(criteria);
        } catch (Exception e) {
            showErrorMessage("Search failed: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public void showSearchResults(List<ArtefakDto> results) {
        displayArtefakList(results);
    }
    
    @Override
    public void refreshArtefakList() {
        loadAllArtefaks();
        searchField.clear();
    }
    
    @Override
    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @Override
    public void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}