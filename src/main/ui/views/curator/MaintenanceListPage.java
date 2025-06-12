package main.ui.views.curator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import main.controller.ManajemenArtefakController;
import main.controller.PemeliharaanController;
import main.model.dto.ArtefakDto;
import main.model.dto.PemeliharaanDto;
import main.model.enums.StatusPemeliharaan;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.util.StringConverter;

/**
 * Maintenance List Page for curator to view artifact maintenance status
 * Shows artifacts with their maintenance deadlines and last maintenance dates
 * Auto-refreshes every 30 seconds to show status updates from cleaners
 */
public class MaintenanceListPage extends BorderPane {    // Controllers
    private final ManajemenArtefakController artefakController;
    private final PemeliharaanController pemeliharaanController;
    
    // UI Components
    private TableView<MaintenanceRecord> maintenanceTable;
    private TextField searchField;
    private Label titleLabel;
    private Label subtitleLabel;
    private ComboBox<String> statusFilter;
    private Pagination pagination;
    
    // Data
    private ObservableList<MaintenanceRecord> allMaintenanceRecords;
    private static final int ITEMS_PER_PAGE = 10;
    private int currentPageIndex = 0;
      // Auto-refresh mechanism
    private Timeline autoRefreshTimeline;
    private static final int REFRESH_INTERVAL_SECONDS = 60; // Increased to 60 seconds to reduce spam
    
    // Date formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    /**
     * Inner class to represent maintenance record for display
     */
    public static class MaintenanceRecord {
        private Long id;
        private String name;
        private String request;
        private String status;
        private String deadline;
        private String lastMaintenance;
        
        public MaintenanceRecord(Long id, String name, String request, String status, 
                               String deadline, String lastMaintenance) {
            this.id = id;
            this.name = name;
            this.request = request;
            this.status = status;
            this.deadline = deadline;
            this.lastMaintenance = lastMaintenance;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getRequest() { return request; }
        public String getStatus() { return status; }
        public String getDeadline() { return deadline; }
        public String getLastMaintenance() { return lastMaintenance; }
        
        // Setters
        public void setId(Long id) { this.id = id; }
        public void setName(String name) { this.name = name; }
        public void setRequest(String request) { this.request = request; }
        public void setStatus(String status) { this.status = status; }
        public void setDeadline(String deadline) { this.deadline = deadline; }
        public void setLastMaintenance(String lastMaintenance) { this.lastMaintenance = lastMaintenance; }
    }    public MaintenanceListPage(ManajemenArtefakController artefakController, 
                             PemeliharaanController pemeliharaanController) {
        this.artefakController = artefakController;
        this.pemeliharaanController = pemeliharaanController;
        this.allMaintenanceRecords = FXCollections.observableArrayList();
        
        initComponents();
        setupLayout();
        setupEventHandlers();
        loadMaintenanceData();
        setupAutoRefresh();
    }
    
    /**
     * Initialize UI components
     */
    private void initComponents() {
        // Title and subtitle
        titleLabel = new Label("Maintenance List");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));
        
        subtitleLabel = new Label("Maintenance");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.setTextFill(Color.GRAY);
        
        // Search field
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
        
        // Status filter
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Status", "Done", "No Request", "Not Started", "In Progress");
        statusFilter.setValue("All Status");
        statusFilter.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );
        
        // Initialize table
        initializeTable();
        
        // Pagination
        pagination = new Pagination();
        pagination.setPageCount(1);
        pagination.setCurrentPageIndex(0);
        pagination.setMaxPageIndicatorCount(5);
        pagination.setStyle("-fx-border-color: transparent;");
        pagination.setPageFactory(this::createPage);
    }
    
    /**
     * Initialize the maintenance table
     */
    private void initializeTable() {
        maintenanceTable = new TableView<>();
        maintenanceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        maintenanceTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );
        
        // ID Column
        TableColumn<MaintenanceRecord, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(60);
        
        // Name Column
        TableColumn<MaintenanceRecord, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(200);
          // Request Column with dropdown
        TableColumn<MaintenanceRecord, String> requestColumn = new TableColumn<>("Request");
        requestColumn.setCellValueFactory(new PropertyValueFactory<>("request"));
        requestColumn.setCellFactory(column -> new TableCell<MaintenanceRecord, String>() {
            private final ComboBox<String> requestComboBox = new ComboBox<>();
            
            {
                requestComboBox.getItems().addAll("No Request", "Request");
                requestComboBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 4px;");
                requestComboBox.setPrefWidth(100);                // Handle selection change
                requestComboBox.setOnAction(event -> {
                    MaintenanceRecord record = getTableRow().getItem();
                    if (record != null) {
                        String selectedValue = requestComboBox.getValue();
                        String previousValue = record.getRequest();
                        
                        // Update the UI record
                        record.setRequest(selectedValue);
                        
                        // Handle database operations based on selection
                        if ("Request".equals(selectedValue) && !"Request".equals(previousValue)) {
                            // Create new maintenance task
                            createMaintenanceTask(record);
                        } else if ("No Request".equals(selectedValue) && "Request".equals(previousValue)) {
                            // Cancel/remove maintenance task
                            cancelMaintenanceTask(record);
                        }
                    }
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    requestComboBox.setValue(item);
                    setGraphic(requestComboBox);
                    setText(null);
                }
            }
        });
        requestColumn.setPrefWidth(120);
        
        // Status Column with color coding
        TableColumn<MaintenanceRecord, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<MaintenanceRecord, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(status);
                    
                    // Create colored circle indicator
                    Region indicator = new Region();
                    indicator.setPrefSize(10, 10);
                    indicator.setMaxSize(10, 10);
                    indicator.setStyle("-fx-background-radius: 50%;");
                    
                    HBox statusBox = new HBox(8);
                    statusBox.setAlignment(Pos.CENTER_LEFT);
                    
                    // Set color based on status
                    switch (status) {
                        case "Done":
                            indicator.setStyle("-fx-background-color: #28a745; -fx-background-radius: 50%;");
                            break;
                        case "In Progress":
                            indicator.setStyle("-fx-background-color: #ffc107; -fx-background-radius: 50%;");
                            break;
                        case "Not Started":
                            indicator.setStyle("-fx-background-color: #dc3545; -fx-background-radius: 50%;");
                            break;
                        case "No Request":
                        default:
                            indicator.setStyle("-fx-background-color: #6c757d; -fx-background-radius: 50%;");
                            break;
                    }
                    
                    statusBox.getChildren().addAll(indicator, new Label(status));
                    setGraphic(statusBox);
                    setText(null);
                }
            }
        });
        statusColumn.setPrefWidth(120);
        
        // Deadline Column
        TableColumn<MaintenanceRecord, String> deadlineColumn = new TableColumn<>("Deadline");
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        deadlineColumn.setPrefWidth(100);
        
        // Last Maintenance Column
        TableColumn<MaintenanceRecord, String> lastMaintenanceColumn = new TableColumn<>("Last Maintenance");
        lastMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<>("lastMaintenance"));
        lastMaintenanceColumn.setPrefWidth(120);
        
        // Add columns to table
        maintenanceTable.getColumns().addAll(
            idColumn, nameColumn, requestColumn, statusColumn, 
            deadlineColumn, lastMaintenanceColumn
        );
    }
    
    /**
     * Setup layout
     */
    private void setupLayout() {
        // Set white background for the entire page
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20));
        
        // Top section with title and subtitle
        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Action row with search and filter
        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        actionRow.getChildren().addAll(statusFilter, spacer, searchField);
        
        // Container for header and action row
        VBox topContainer = new VBox(20);
        topContainer.getChildren().addAll(headerBox, actionRow);
        
        // Table container
        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(20, 0, 0, 0));
        tableContainer.getChildren().add(maintenanceTable);
        
        // Create custom pagination controls
        createCustomPaginationControls(tableContainer);
        
        // Add all components to the main layout
        setTop(topContainer);
        setCenter(tableContainer);
    }
    
    /**
     * Create custom pagination controls matching the design
     */
    private void createCustomPaginationControls(VBox container) {
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
    
    /**
     * Update page buttons based on current page
     */
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
    
    /**
     * Create page content for pagination
     */
    private TableView<MaintenanceRecord> createPage(int pageIndex) {
        currentPageIndex = pageIndex;
        loadMaintenanceDataByPage(pageIndex);
        return maintenanceTable;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        // Search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                performSearch();
            }
        });
        
        // Status filter
        statusFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                performSearch();
            }
        });
    }
    
    /**
     * Load maintenance data from database
     */
    private void loadMaintenanceData() {
        try {
            // Get all artifacts
            List<ArtefakDto> artifacts = artefakController.getAllArtefaksOrderById();
            
            // Get all maintenance records
            List<PemeliharaanDto> maintenanceRecords = pemeliharaanController.getAllPemeliharaan();
            
            // Group maintenance by artifact ID
            Map<Long, List<PemeliharaanDto>> maintenanceByArtifact = maintenanceRecords.stream()
                .collect(Collectors.groupingBy(PemeliharaanDto::getArtefakId));
            
            // Create maintenance records for display
            allMaintenanceRecords.clear();
            
            for (ArtefakDto artifact : artifacts) {
                List<PemeliharaanDto> artifactMaintenance = maintenanceByArtifact.get(artifact.getArtefakId());
                
                String request = "No Request";
                String status = "No Request";
                String deadline = "-";
                String lastMaintenance = "-";
                
                if (artifactMaintenance != null && !artifactMaintenance.isEmpty()) {
                    // Find latest maintenance for status
                    PemeliharaanDto latestMaintenance = artifactMaintenance.stream()
                        .max((m1, m2) -> m1.getTanggalMulai().compareTo(m2.getTanggalMulai()))
                        .orElse(null);
                      if (latestMaintenance != null) {
                        // Check if latest maintenance is completed
                        if ("SELESAI".equals(latestMaintenance.getStatus())) {
                            // If completed, show as "No Request" to allow new requests
                            request = "No Request";
                            status = "No Request";
                        } else {
                            // Active maintenance task exists
                            request = "Request";
                            
                            // Map status from PemeliharaanDto to display status
                            switch (latestMaintenance.getStatus()) {
                                case "SEDANG_BERLANGSUNG":
                                    status = "In Progress";
                                    break;
                                case "DIJADWALKAN":
                                    status = "Not Started";
                                    break;
                                default:
                                    status = "No Request";
                            }                        }
                        
                        // Deadline is tanggal_mulai of the latest scheduled maintenance (only for active tasks)
                        if (latestMaintenance.getTanggalMulai() != null && !"SELESAI".equals(latestMaintenance.getStatus())) {
                            deadline = latestMaintenance.getTanggalMulai().format(DATE_FORMATTER);
                        }
                        
                        // Last maintenance is tanggal_selesai of the most recently completed maintenance
                        PemeliharaanDto lastCompleted = artifactMaintenance.stream()
                            .filter(m -> "SELESAI".equals(m.getStatus()) && m.getTanggalSelesai() != null)
                            .max((m1, m2) -> m1.getTanggalSelesai().compareTo(m2.getTanggalSelesai()))
                            .orElse(null);
                        
                        if (lastCompleted != null) {
                            lastMaintenance = lastCompleted.getTanggalSelesai().format(DATE_FORMATTER);
                        }
                    }
                }
                
                allMaintenanceRecords.add(new MaintenanceRecord(
                    artifact.getArtefakId(),
                    artifact.getNamaArtefak(),
                    request,
                    status,
                    deadline,
                    lastMaintenance
                ));
            }
            
            // Update pagination
            int totalPages = (int) Math.ceil((double) allMaintenanceRecords.size() / ITEMS_PER_PAGE);
            pagination.setPageCount(Math.max(1, totalPages));
            
            // Reset to first page
            pagination.setCurrentPageIndex(0);
            currentPageIndex = 0;
            
            // Load first page
            loadMaintenanceDataByPage(0);
            
        } catch (Exception e) {
            showErrorMessage("Failed to load maintenance data: " + e.getMessage());
        }
    }
    
    /**
     * Load maintenance data by page
     */
    private void loadMaintenanceDataByPage(int page) {
        try {
            // Calculate pagination
            int fromIndex = page * ITEMS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, allMaintenanceRecords.size());
            
            // Clear table first
            maintenanceTable.getItems().clear();
            
            // Add items for this page if there are items
            if (fromIndex < allMaintenanceRecords.size()) {
                List<MaintenanceRecord> pageItems = allMaintenanceRecords.subList(fromIndex, toIndex);
                maintenanceTable.getItems().addAll(pageItems);
            }
            
            // Force table refresh
            maintenanceTable.refresh();
            
        } catch (Exception e) {
            showErrorMessage("Failed to load page: " + e.getMessage());
        }
    }
    
    /**
     * Perform search and filter
     */
    private void performSearch() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String statusFilter = this.statusFilter.getValue();
        
        try {
            // Start with all records
            List<MaintenanceRecord> filteredRecords = allMaintenanceRecords.stream()
                .filter(record -> {
                    // Text search filter
                    boolean matchesSearch = searchTerm.isEmpty() || 
                        record.getName().toLowerCase().contains(searchTerm) ||
                        record.getRequest().toLowerCase().contains(searchTerm) ||
                        record.getStatus().toLowerCase().contains(searchTerm);
                    
                    // Status filter
                    boolean matchesStatus = statusFilter.equals("All Status") || 
                        record.getStatus().equals(statusFilter);
                    
                    return matchesSearch && matchesStatus;
                })
                .collect(Collectors.toList());
            
            // Update pagination with filtered results
            int totalPages = (int) Math.ceil((double) filteredRecords.size() / ITEMS_PER_PAGE);
            pagination.setPageCount(Math.max(1, totalPages));
            
            // Reset to first page
            pagination.setCurrentPageIndex(0);
            currentPageIndex = 0;
            
            // Update table with filtered results
            maintenanceTable.getItems().clear();
            int toIndex = Math.min(ITEMS_PER_PAGE, filteredRecords.size());
            if (toIndex > 0) {
                maintenanceTable.getItems().addAll(filteredRecords.subList(0, toIndex));
            }
            
            // Store filtered results for pagination
            allMaintenanceRecords.clear();
            allMaintenanceRecords.addAll(filteredRecords);
            
        } catch (Exception e) {
            showErrorMessage("Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Show error message
     */
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();    }
      /**
     * Create a new maintenance task when Request is selected
     */
    private void createMaintenanceTask(MaintenanceRecord record) {        // Show dialog to select maintenance type and deadline
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create Maintenance Task");
        dialog.setHeaderText("Create maintenance request for: " + record.getName());
          // Create form content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        // Maintenance type selection
        ComboBox<String> maintenanceTypeComboBox = new ComboBox<>();
        maintenanceTypeComboBox.getItems().addAll("RUTIN", "DARURAT", "RESTORASI");
        maintenanceTypeComboBox.setValue("RUTIN"); // Default to routine maintenance
        
        // Deadline selection
        DatePicker deadlinePicker = new DatePicker();
        deadlinePicker.setValue(java.time.LocalDate.now().plusDays(7)); // Default to 1 week from now
        
        // Description field
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setText("Maintenance request from curator for: " + record.getName());
        
        grid.add(new Label("Maintenance Type:"), 0, 0);
        grid.add(maintenanceTypeComboBox, 1, 0);
        grid.add(new Label("Deadline:"), 0, 1);
        grid.add(deadlinePicker, 1, 1);
        grid.add(new Label("Description:"), 0, 2);
        grid.add(descriptionArea, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);        // Enable/disable OK button based on selection
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        
        Runnable validateForm = () -> {
            boolean isValid = maintenanceTypeComboBox.getValue() != null && 
                            deadlinePicker.getValue() != null;
            okButton.setDisable(!isValid);
        };
        
        maintenanceTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateForm.run());
        deadlinePicker.valueProperty().addListener((obs, oldVal, newVal) -> validateForm.run());
        
        // Initial validation
        validateForm.run();
          dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String selectedMaintenanceType = maintenanceTypeComboBox.getValue();
                java.time.LocalDate selectedDeadline = deadlinePicker.getValue();
                String description = descriptionArea.getText();
                
                try {
                    // Create new maintenance task in database
                    PemeliharaanDto newTask = new PemeliharaanDto();
                    newTask.setArtefakId(record.getId());
                    // Leave petugasId as null - will be assigned later by admin/cleaner
                    newTask.setJenisPemeliharaan(selectedMaintenanceType);
                    newTask.setTanggalMulai(selectedDeadline.atStartOfDay());
                    newTask.setStatus("DIJADWALKAN"); // Use string value instead of enum
                    newTask.setDeskripsiPemeliharaan(description);
                    
                    // Save to database
                    pemeliharaanController.createPemeliharaan(newTask);
                    
                    // Update the status in the UI to reflect the new maintenance task
                    record.setStatus("Not Started");
                    
                    // Refresh the table to show updated data
                    maintenanceTable.refresh();
                    
                    System.out.println("✅ Maintenance task created for: " + record.getName());
                    
                    // Show success message
                    showSuccessMessage("Maintenance task created successfully for " + record.getName() + 
                                     "\nType: " + selectedMaintenanceType +
                                     "\nDeadline: " + selectedDeadline.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                    
                } catch (Exception e) {
                    System.err.println("Error creating maintenance task: " + e.getMessage());
                    showErrorMessage("Failed to create maintenance task: " + e.getMessage());
                    
                    // Revert the dropdown selection on error
                    record.setRequest("No Request");
                    maintenanceTable.refresh();
                }
            } else {
                // User cancelled, revert the dropdown selection
                record.setRequest("No Request");
                maintenanceTable.refresh();
            }
        });
    }
    
    /**
     * Cancel/remove maintenance task when No Request is selected
     */
    private void cancelMaintenanceTask(MaintenanceRecord record) {
        try {            // Find and remove pending maintenance tasks for this artifact
            List<PemeliharaanDto> allTasks = pemeliharaanController.getAllPemeliharaan();
            List<PemeliharaanDto> artifactTasks = allTasks.stream()
                .filter(task -> task.getArtefakId().equals(record.getId()))
                .filter(task -> "DIJADWALKAN".equals(task.getStatus()) || "SEDANG_BERLANGSUNG".equals(task.getStatus()))
                .collect(Collectors.toList());
            
            for (PemeliharaanDto task : artifactTasks) {
                // Only cancel tasks that haven't been completed
                if (!"SELESAI".equals(task.getStatus())) {
                    pemeliharaanController.deletePemeliharaan(task.getPemeliharaanId());
                }
            }
            
            // Update the status in the UI
            if (artifactTasks.isEmpty()) {
                record.setStatus("No Request");
            }
            
            // Refresh the table
            maintenanceTable.refresh();
            
            System.out.println("✅ Maintenance task cancelled for: " + record.getName());
            
            // Show success message
            showSuccessMessage("Maintenance task cancelled for " + record.getName());
            
        } catch (Exception e) {
            System.err.println("Error cancelling maintenance task: " + e.getMessage());
            showErrorMessage("Failed to cancel maintenance task: " + e.getMessage());
            
            // Revert the dropdown selection on error
            record.setRequest("Request");
            maintenanceTable.refresh();
        }
    }
    
    /**
     * Show success message
     */
    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Refresh the maintenance data
     */
    public void refresh() {
        loadMaintenanceData();
    }
      /**
     * Setup auto-refresh mechanism to update maintenance status periodically
     */
    private void setupAutoRefresh() {
        autoRefreshTimeline = new Timeline(new KeyFrame(Duration.seconds(REFRESH_INTERVAL_SECONDS), event -> {
            // Refresh maintenance data silently
            loadMaintenanceData();
            
            // Only log once every 5 minutes to reduce spam
            long currentTime = System.currentTimeMillis();
            if (currentTime % (5 * 60 * 1000) < REFRESH_INTERVAL_SECONDS * 1000) {
                System.out.println("🔄 [" + java.time.LocalTime.now().toString() + "] Maintenance data auto-refreshed (curator view)");
            }
        }));
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
        System.out.println("✅ Auto-refresh started for MaintenanceListPage (every " + REFRESH_INTERVAL_SECONDS + " seconds)");
    }
    
    /**
     * Stop the auto-refresh mechanism to prevent memory leaks
     * Call this method when the view is no longer displayed
     */
    public void stopAutoRefresh() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
            System.out.println("🛑 Auto-refresh stopped for MaintenanceListPage");
        }
    }
    
    /**
     * Check if auto-refresh is currently running
     * @return true if auto-refresh is active, false otherwise
     */
    public boolean isAutoRefreshActive() {
        return autoRefreshTimeline != null && autoRefreshTimeline.getStatus() == Timeline.Status.RUNNING;
    }
}
