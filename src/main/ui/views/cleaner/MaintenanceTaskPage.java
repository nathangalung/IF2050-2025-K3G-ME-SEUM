package main.ui.views.cleaner;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.controller.PemeliharaanController;
import main.model.dto.PemeliharaanDto;
import main.model.enums.StatusPemeliharaan;
import main.ui.interfaces.PemeliharaanInterface;

import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;

public class MaintenanceTaskPage extends BorderPane implements PemeliharaanInterface {
    private final PemeliharaanController controller;
    
    // UI Components
    private TableView<PemeliharaanDto> taskTable;
    private TextField searchField;
    private Label titleLabel;
    private Label subtitleLabel;
      // Flag to prevent infinite update loops during table refresh
    private boolean isUpdatingTable = false;
      public MaintenanceTaskPage(PemeliharaanController controller) {
        this.controller = controller;
        // Set updating flag during initial construction to prevent false success messages
        isUpdatingTable = true;
        
        initComponents();
        setupLayout();
        setupEventHandlers();
        refreshPemeliharaanList();
        
        // Reset flag after initial load is complete
        Platform.runLater(() -> {
            isUpdatingTable = false;
            System.out.println("✅ MaintenanceTaskPage initialization complete");
        });
    }
    
    private void initComponents() {
        // Title and subtitle
        titleLabel = new Label("Maintenance Tasks");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        subtitleLabel = new Label("Maintenance");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.setTextFill(Color.GRAY);
        
        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search tasks...");
        searchField.setPrefWidth(250);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #e0e0e0;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 5px;"
        );
        
        // Initialize table
        initializeTable();
    }
    
    private void initializeTable() {
        taskTable = new TableView<>();
        taskTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
          // ID Column - Sequential numbering instead of database ID
        TableColumn<PemeliharaanDto, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(column -> {
            int index = taskTable.getItems().indexOf(column.getValue()) + 1;
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(index));
        });
        idColumn.setPrefWidth(60);
        
        // Name Column (Artifact Name)
        TableColumn<PemeliharaanDto, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("namaArtefak"));
        nameColumn.setPrefWidth(200);
        
        // Status Column with ComboBox
        TableColumn<PemeliharaanDto, String> statusColumn = new TableColumn<>("State");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setCellFactory(column -> new TableCell<PemeliharaanDto, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
              {
                comboBox.getItems().addAll(
                    StatusPemeliharaan.DIJADWALKAN.name(),
                    StatusPemeliharaan.SEDANG_BERLANGSUNG.name(),
                    StatusPemeliharaan.SELESAI.name()
                );
                // Action handler is now set in updateItem method to avoid initialization issues
            }@Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    PemeliharaanDto dto = getTableView().getItems().get(getIndex());
                    if (dto != null) {
                        // Temporarily disable the action handler while setting value
                        comboBox.setOnAction(null);
                        comboBox.setValue(dto.getStatus());
                        // Re-enable the action handler
                        comboBox.setOnAction(event -> {
                            if (!isEmpty() && !isUpdatingTable) {
                                PemeliharaanDto currentDto = getTableView().getItems().get(getIndex());
                                String newStatus = comboBox.getValue();
                                System.out.println("🔄 ComboBox action triggered - Status change: " + currentDto.getStatus() + " -> " + newStatus);
                                Platform.runLater(() -> {
                                    handleUpdateStatus(currentDto.getPemeliharaanId(), newStatus);
                                });
                            } else if (isUpdatingTable) {
                                System.out.println("🛑 ComboBox action blocked during table update");
                            }
                        });
                        setGraphic(comboBox);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
        statusColumn.setPrefWidth(150);
          // Deadline Column (showing tanggal mulai)
        TableColumn<PemeliharaanDto, String> deadlineColumn = new TableColumn<>("Deadline");
        deadlineColumn.setCellValueFactory(cellData -> {
            PemeliharaanDto dto = cellData.getValue();
            if (dto.getTanggalMulai() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    dto.getTanggalMulai().toLocalDate().toString()
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("Not set");
            }
        });
        deadlineColumn.setPrefWidth(150);
        
        taskTable.getColumns().addAll(idColumn, nameColumn, statusColumn, deadlineColumn);
    }
    
    private void setupLayout() {
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20));
        
        // Top section with title and subtitle
        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Action row with search
        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        actionRow.getChildren().addAll(spacer, searchField);
        
        // Container for header and action row
        VBox topContainer = new VBox(20);
        topContainer.getChildren().addAll(headerBox, actionRow);
        
        // Table container
        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(20, 0, 0, 0));
        tableContainer.getChildren().add(taskTable);
        
        setTop(topContainer);
        setCenter(tableContainer);
    }
    
    private void setupEventHandlers() {
        // Search field handler
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTasks(newValue);
        });

        // Double click handler for table rows
        taskTable.setRowFactory(tv -> {
            TableRow<PemeliharaanDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showPemeliharaanDetails(row.getItem());
                }
            });
            return row;
        });
    }
      private void filterTasks(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            refreshPemeliharaanList();
            return;
        }

        List<PemeliharaanDto> allTasks = controller.getAllPemeliharaan();
        List<PemeliharaanDto> filteredTasks = allTasks.stream()
            .filter(task -> !"SELESAI".equals(task.getStatus())) // Exclude completed tasks
            .filter(task -> 
                task.getNamaArtefak().toLowerCase().contains(searchText.toLowerCase()) ||
                task.getDeskripsiPemeliharaan().toLowerCase().contains(searchText.toLowerCase())
            )
            .collect(java.util.stream.Collectors.toList());
        
        displayPemeliharaanList(filteredTasks);
    }    @Override
    public void refreshPemeliharaanList() {
        try {
            isUpdatingTable = true;
            List<PemeliharaanDto> allTasks = controller.getAllPemeliharaan();
            
            System.out.println("🔍 DEBUG: All tasks from database:");
            for (PemeliharaanDto task : allTasks) {
                System.out.println("  - ID: " + task.getPemeliharaanId() + 
                                   ", Artifact: " + task.getNamaArtefak() + 
                                   ", Status: " + task.getStatus() + 
                                   ", Artifact ID: " + task.getArtefakId());
            }
            
            // Filter out completed tasks - cleaners should only see active maintenance tasks
            List<PemeliharaanDto> activeTasks = allTasks.stream()
                .filter(task -> !"SELESAI".equals(task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println("📋 Loaded " + allTasks.size() + " total tasks, showing " + activeTasks.size() + " active tasks (filtered out completed ones)");
            displayPemeliharaanList(activeTasks);
        } catch (Exception e) {
            showErrorMessage("Failed to refresh task list: " + e.getMessage());
        } finally {
            isUpdatingTable = false;
        }
    }

    @Override
    public void showAddPemeliharaanForm() {
        // Not needed for cleaner view
    }

    @Override
    public void showEditPemeliharaanForm(Long pemeliharaanId) {
        // Not needed for cleaner view
    }

    @Override
    public void showPemeliharaanDetails(PemeliharaanDto pemeliharaan) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Maintenance Details");
        alert.setHeaderText("Maintenance Task #" + pemeliharaan.getPemeliharaanId());
        
        String content = String.format(
            "Artifact: %s\n" +
            "Description: %s\n" +
            "Status: %s\n" +
            "Notes: %s",
            pemeliharaan.getNamaArtefak(),
            pemeliharaan.getDeskripsiPemeliharaan(),
            pemeliharaan.getStatus(),
            pemeliharaan.getCatatan() != null ? pemeliharaan.getCatatan() : "-"
        );
        
        alert.setContentText(content);
        alert.showAndWait();
    }    @Override
    public void displayPemeliharaanList(List<PemeliharaanDto> pemeliharaanList) {
        isUpdatingTable = true;
        try {
            taskTable.getItems().clear();
            taskTable.getItems().addAll(pemeliharaanList);
        } finally {
            // Use Platform.runLater to ensure the flag is reset after UI updates complete
            Platform.runLater(() -> {
                isUpdatingTable = false;
            });
        }
    }

    @Override
    public void displayUpcomingPemeliharaan(List<PemeliharaanDto> upcomingList) {
        // Not needed for cleaner view
    }

    @Override
    public void handleStartPemeliharaan(Long pemeliharaanId) {
        try {
            controller.mulaiPemeliharaan(pemeliharaanId);
            refreshPemeliharaanList();
            showSuccessMessage("Maintenance task started successfully");
        } catch (Exception e) {
            showErrorMessage("Failed to start maintenance: " + e.getMessage());
        }
    }

    @Override
    public void handleCompletePemeliharaan(Long pemeliharaanId) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Complete Maintenance");
        dialog.setHeaderText("Add completion notes");
        dialog.setContentText("Notes:");

        dialog.showAndWait().ifPresent(notes -> {
            try {
                controller.selesaikanPemeliharaan(pemeliharaanId, notes);
                refreshPemeliharaanList();
                showSuccessMessage("Maintenance task completed successfully");
            } catch (Exception e) {
                showErrorMessage("Failed to complete maintenance: " + e.getMessage());
            }
        });
    }    @Override
    public void handleUpdateStatus(Long pemeliharaanId, String status) {
        System.out.println("📝 handleUpdateStatus called - ID: " + pemeliharaanId + ", Status: " + status + ", isUpdatingTable: " + isUpdatingTable);
        try {
            controller.updateStatusPemeliharaan(pemeliharaanId, status);
            
            // Check if task was completed
            boolean wasCompleted = "SELESAI".equals(status);
            
            refreshPemeliharaanList();
            
            // Only show success message for user-initiated actions, not auto-refresh
            if (!isUpdatingTable) {
                if (wasCompleted) {
                    showSuccessMessage("Task completed successfully and removed from active task list!");
                    System.out.println("✅ Task completed and auto-removed from view - ID: " + pemeliharaanId);
                } else {
                    showSuccessMessage("Status updated successfully to: " + status);
                    System.out.println("✅ Status updated by user for maintenance ID: " + pemeliharaanId + " to: " + status);
                }
            } else {
                System.out.println("🔇 Success message suppressed during table update");
            }
        } catch (Exception e) {
            showErrorMessage("Failed to update status: " + e.getMessage());
        }
    }

    @Override
    public void handleAjukanPemeliharaan(Long artefakId) {
        // Not needed for cleaner view
    }

    @Override
    public void handleCatatTindakan(Long pemeliharaanId) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Record Action");
        dialog.setHeaderText("Record maintenance action");
        dialog.setContentText("Action description:");

        dialog.showAndWait().ifPresent(action -> {
            try {
                controller.catatTindakanPemeliharaan(pemeliharaanId, action);
                refreshPemeliharaanList();
                showSuccessMessage("Action recorded successfully");
            } catch (Exception e) {
                showErrorMessage("Failed to record action: " + e.getMessage());
            }
        });
    }

    @Override
    public void updateStatistics() {
        // Not needed for cleaner view
    }    @Override
    public void showSuccessMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @Override
    public void showErrorMessage(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }    @Override
    public void showConfirmationDialog(String message, Runnable onConfirm) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText(message);
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    onConfirm.run();
                }
            });
        });
    }    @Override
    public void applyStatusFilter(String status) {
        try {
            List<PemeliharaanDto> filteredTasks = controller.getPemeliharaanByStatus(
                StatusPemeliharaan.fromString(status)
            );
            // Also filter out completed tasks even when applying status filter
            List<PemeliharaanDto> activeTasks = filteredTasks.stream()
                .filter(task -> !"SELESAI".equals(task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            
            displayPemeliharaanList(activeTasks);
        } catch (Exception e) {
            showErrorMessage("Failed to apply status filter: " + e.getMessage());
        }
    }

    @Override
    public void applyDateFilter(String startDate, String endDate) {
        // Not needed for cleaner view
    }    @Override
    public void applyPetugasFilter(Long petugasId) {
        try {
            List<PemeliharaanDto> filteredTasks = controller.getPemeliharaanByPetugas(petugasId);
            // Also filter out completed tasks
            List<PemeliharaanDto> activeTasks = filteredTasks.stream()
                .filter(task -> !"SELESAI".equals(task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            
            displayPemeliharaanList(activeTasks);
        } catch (Exception e) {
            showErrorMessage("Failed to apply cleaner filter: " + e.getMessage());
        }
    }

    @Override
    public void applyArtefakFilter(Long artefakId) {
        try {
            List<PemeliharaanDto> filteredTasks = controller.getPemeliharaanByArtefak(artefakId);
            // Also filter out completed tasks
            List<PemeliharaanDto> activeTasks = filteredTasks.stream()
                .filter(task -> !"SELESAI".equals(task.getStatus()))
                .collect(java.util.stream.Collectors.toList());
            
            displayPemeliharaanList(activeTasks);
        } catch (Exception e) {
            showErrorMessage("Failed to apply artifact filter: " + e.getMessage());
        }
    }

    @Override
    public void navigateToMaintenanceList() {
        // Not needed - already on maintenance list
    }

    @Override
    public void navigateToMaintenanceCalendar() {
        // Not needed for cleaner view
    }

    @Override
    public void navigateToMaintenanceStats() {
        // Not needed for cleaner view
    }
}
