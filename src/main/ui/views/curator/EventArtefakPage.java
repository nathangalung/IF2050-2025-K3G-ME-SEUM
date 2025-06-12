package main.ui.views.curator;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import main.controller.PameranController;
import main.model.dto.PameranDto;
import main.ui.components.forms.PameranForm;

import java.time.format.DateTimeFormatter;
import java.util.List;

// Halaman manajemen event artefak untuk kurator
public class EventArtefakPage extends BorderPane {
    private final PameranController controller;

    // UI Components
    private TextField searchField;
    private Button newEventButton;
    private Label titleLabel;
    private Label subtitleLabel;
    private TableView<PameranDto> eventTable;

    public EventArtefakPage(PameranController controller) {
        this.controller = controller;
        initComponents();
        setupLayout();
        setupEventHandlers();
        refreshEventList();
    }

    private void initComponents() {
        // Title and subtitle
        titleLabel = new Label("Event Artefak");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        subtitleLabel = new Label("Dashboard");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.setTextFill(Color.GRAY);

        // NEW EVENT button
        newEventButton = new Button("NEW EVENT");
        newEventButton.setStyle(
                "-fx-background-color: #1976D2;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;");

        // Search field
        searchField = new TextField();
        searchField.setPromptText("Search events...");
        searchField.setPrefWidth(300);
        searchField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-padding: 8px;");

        // Event table
        eventTable = new TableView<>();
        setupEventTable();
    }

    private void setupEventTable() {
        eventTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        eventTable.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;");

        // ID Column
        TableColumn<PameranDto, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("pameranId"));
        idColumn.setPrefWidth(60);
        idColumn.setStyle("-fx-alignment: CENTER;");

        // Name Event Column
        TableColumn<PameranDto, String> nameColumn = new TableColumn<>("Name Event");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("namaPameran"));
        nameColumn.setPrefWidth(200);

        // Start Date Column
        TableColumn<PameranDto, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(cellData -> {
            PameranDto pameran = cellData.getValue();
            if (pameran.getTanggalMulai() != null) {
                String formattedDate = pameran.getTanggalMulai()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                return new javafx.beans.property.SimpleStringProperty(formattedDate);
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        startDateColumn.setPrefWidth(130);

        // End Date Column
        TableColumn<PameranDto, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(cellData -> {
            PameranDto pameran = cellData.getValue();
            if (pameran.getTanggalSelesai() != null) {
                String formattedDate = pameran.getTanggalSelesai()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                return new javafx.beans.property.SimpleStringProperty(formattedDate);
            }
            return new javafx.beans.property.SimpleStringProperty("-");
        });
        endDateColumn.setPrefWidth(130);

        // Description Column
        TableColumn<PameranDto, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("deskripsiPameran"));
        descColumn.setPrefWidth(250);

        // Truncate long descriptions
        descColumn.setCellFactory(column -> new TableCell<PameranDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    String displayText = item.length() > 50 ? item.substring(0, 47) + "..." : item;
                    setText(displayText);
                    setTooltip(new Tooltip(item));
                }
            }
        });

        // Artifact Column - Show artifact names and IDs in vertical list
        TableColumn<PameranDto, String> artifactColumn = new TableColumn<>("Artifacts");
        artifactColumn.setPrefWidth(250);
        artifactColumn.setCellValueFactory(cellData -> {
            PameranDto pameran = cellData.getValue();
            List<Long> artefakIds = pameran.getArtefakIds();

            if (artefakIds == null || artefakIds.isEmpty()) {
                return new javafx.beans.property.SimpleStringProperty("No artifacts");
            }

            // Create vertical list format with line breaks
            StringBuilder artifactInfo = new StringBuilder();
            try {
                for (int i = 0; i < artefakIds.size(); i++) {
                    Long artifactId = artefakIds.get(i);
                    String artifactName = getArtifactNameById(artifactId);

                    // Format as "ID: Name" with line break
                    artifactInfo.append(artifactId).append(": ").append(artifactName);

                    // Add line break except for the last item
                    if (i < artefakIds.size() - 1) {
                        artifactInfo.append("\n");
                    }

                    // Limit to prevent overcrowding (show max 4 items)
                    if (i >= 3 && artefakIds.size() > 4) {
                        artifactInfo.append("\n+(").append(artefakIds.size() - 4).append(" more)");
                        break;
                    }
                }
            } catch (Exception e) {
                return new javafx.beans.property.SimpleStringProperty("Error loading artifacts");
            }

            return new javafx.beans.property.SimpleStringProperty(artifactInfo.toString());
        });

        // Custom cell factory for vertical list display
        artifactColumn.setCellFactory(column -> new TableCell<PameranDto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                    setGraphic(null);
                } else {
                    // Create VBox for vertical artifact list
                    VBox container = new VBox(2);
                    container.setAlignment(Pos.CENTER_LEFT);
                    container.setPadding(new Insets(5));

                    // Split the text by newlines and create labels
                    String[] lines = item.split("\n");
                    for (String line : lines) {
                        Label artifactLabel = new Label(line);
                        artifactLabel.setStyle(
                                "-fx-font-size: 11px;" +
                                        "-fx-text-fill: #333;" +
                                        "-fx-padding: 1px 0;");
                        artifactLabel.setWrapText(false);
                        container.getChildren().add(artifactLabel);
                    }

                    // + Button in a separate container
                    HBox buttonContainer = new HBox();
                    buttonContainer.setAlignment(Pos.CENTER);
                    buttonContainer.setPadding(new Insets(5, 0, 0, 0));

                    Button addBtn = new Button("+");
                    addBtn.setStyle(
                            "-fx-background-color: #28a745;" +
                                    "-fx-text-fill: white;" +
                                    "-fx-border-radius: 50%;" +
                                    "-fx-background-radius: 50%;" +
                                    "-fx-min-width: 20;" +
                                    "-fx-min-height: 20;" +
                                    "-fx-max-width: 20;" +
                                    "-fx-max-height: 20;" +
                                    "-fx-font-size: 10px;" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-cursor: hand;");
                    addBtn.setTooltip(new Tooltip("Add Artifact to Event"));

                    addBtn.setOnAction(event -> {
                        PameranDto pameran = getTableView().getItems().get(getIndex());
                        showAddArtifactDialog(pameran);
                    });

                    buttonContainer.getChildren().add(addBtn);

                    // Main container for artifacts and button
                    VBox mainContainer = new VBox(3);
                    mainContainer.getChildren().addAll(container, buttonContainer);

                    setText(null);
                    setGraphic(mainContainer);

                    // Full tooltip with all artifacts
                    PameranDto pameran = getTableView().getItems().get(getIndex());
                    if (pameran.getArtefakIds() != null && !pameran.getArtefakIds().isEmpty()) {
                        StringBuilder fullTooltip = new StringBuilder("All artifacts:\n");
                        for (Long artifactId : pameran.getArtefakIds()) {
                            try {
                                String artifactName = getArtifactNameById(artifactId);
                                fullTooltip.append("• ").append(artifactId).append(": ").append(artifactName)
                                        .append("\n");
                            } catch (Exception e) {
                                fullTooltip.append("• ").append(artifactId).append(": Error loading name\n");
                            }
                        }
                        setTooltip(new Tooltip(fullTooltip.toString()));
                    }
                }
            }
        });

        // Actions Column
        TableColumn<PameranDto, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setPrefWidth(100);
        actionsColumn.setCellFactory(param -> new TableCell<PameranDto, Void>() {
            private final Button editBtn = new Button("✏️");
            private final Button deleteBtn = new Button("🗑️");
            private final HBox actionsBox = new HBox(5);

            {
                editBtn.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #1976D2;" +
                                "-fx-border-color: transparent;" +
                                "-fx-padding: 5px;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-size: 14px;");
                editBtn.setTooltip(new Tooltip("Edit Event"));

                deleteBtn.setStyle(
                        "-fx-background-color: transparent;" +
                                "-fx-text-fill: #D32F2F;" +
                                "-fx-border-color: transparent;" +
                                "-fx-padding: 5px;" +
                                "-fx-cursor: hand;" +
                                "-fx-font-size: 14px;");
                deleteBtn.setTooltip(new Tooltip("Delete Event"));

                actionsBox.getChildren().addAll(editBtn, deleteBtn);
                actionsBox.setAlignment(Pos.CENTER);

                editBtn.setOnAction(event -> {
                    PameranDto pameran = getTableView().getItems().get(getIndex());
                    editEvent(pameran);
                });

                deleteBtn.setOnAction(event -> {
                    PameranDto pameran = getTableView().getItems().get(getIndex());
                    deleteEvent(pameran);
                });
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

        eventTable.getColumns().addAll(idColumn, nameColumn, startDateColumn, endDateColumn,
                descColumn, artifactColumn, actionsColumn);

        // Set custom row factory to control height
        eventTable.setRowFactory(tv -> {
            TableRow<PameranDto> row = new TableRow<>();

            // Set dynamic row height based on content
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null && newItem.getArtefakIds() != null) {
                    int artifactCount = newItem.getArtefakIds().size();
                    // Calculate height: base height + extra for each artifact
                    double baseHeight = 40;
                    double extraHeight = Math.min(artifactCount * 18, 4 * 18); // Max 4 lines
                    row.setPrefHeight(baseHeight + extraHeight);
                    row.setMaxHeight(baseHeight + extraHeight);
                } else {
                    row.setPrefHeight(40);
                    row.setMaxHeight(40);
                }
            });

            return row;
        });

        // Set table properties for better appearance
        eventTable.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-radius: 5px;" +
                        "-fx-background-radius: 5px;" +
                        "-fx-selection-bar: #e3f2fd;" +
                        "-fx-selection-bar-non-focused: #f5f5f5;");
    }

    private void setupLayout() {
        // Set white background for the entire page
        setStyle("-fx-background-color: white;");
        setPadding(new Insets(20));

        // Top section with title and subtitle
        VBox headerBox = new VBox(5);
        headerBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Action row with NEW EVENT button on left and search on right
        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        // Spacer to push search field to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actionRow.getChildren().addAll(newEventButton, spacer, searchField);

        // Container for header and action row
        VBox topContainer = new VBox(20);
        topContainer.getChildren().addAll(headerBox, actionRow);

        // Table container with padding
        VBox tableContainer = new VBox(10);
        tableContainer.setPadding(new Insets(20, 0, 0, 0));
        tableContainer.getChildren().add(eventTable);
        VBox.setVgrow(eventTable, Priority.ALWAYS);

        // Add all components to the main layout
        setTop(topContainer);
        setCenter(tableContainer);
    }

    private void setupEventHandlers() {
        // NEW EVENT button
        newEventButton.setOnAction(e -> showAddEventForm());

        // Search field
        searchField.setOnAction(e -> performSearch());

        // Add text change listener for real-time search
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals(oldValue)) {
                performSearch();
            }
        });
    }

    private void showAddEventForm() {
        try {
            PameranForm form = new PameranForm("Add New Event", null);
            form.showAndWait().ifPresent(pameran -> {
                try {
                    controller.createPameran(pameran);
                    refreshEventList();
                    showAlert("Success", "Event added successfully!", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Error", "Failed to add event: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Failed to open form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void editEvent(PameranDto pameran) {
        try {
            PameranForm form = new PameranForm("Edit Event", pameran);
            form.showAndWait().ifPresent(updatedPameran -> {
                try {
                    controller.updatePameran(pameran.getPameranId(), updatedPameran);
                    refreshEventList();
                    showAlert("Success", "Event updated successfully!", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Error", "Failed to update event: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception e) {
            showAlert("Error", "Failed to open edit form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void deleteEvent(PameranDto pameran) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Event");
        alert.setContentText("Are you sure you want to delete event: " + pameran.getNamaPameran() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deletePameran(pameran.getPameranId());
                    refreshEventList();
                    showAlert("Success", "Event deleted successfully!", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    showAlert("Error", "Failed to delete event: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void showAddArtifactDialog(PameranDto pameran) {
        // Create a dialog with better UI
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Artifact to Exhibition");
        dialog.setHeaderText("Add Artifact to: " + pameran.getNamaPameran());

        // Set button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField artifactIdField = new TextField();
        artifactIdField.setPromptText("Enter Artifact ID (e.g., 1, 2, 3...)");

        Label infoLabel = new Label("Available Artifact IDs: 1-10 (check your database)");
        infoLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

        grid.add(new Label("Artifact ID:"), 0, 0);
        grid.add(artifactIdField, 1, 0);
        grid.add(infoLabel, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Focus on text field
        Platform.runLater(() -> artifactIdField.requestFocus());

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return artifactIdField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(artifactIdStr -> {
            if (artifactIdStr.isEmpty()) {
                showAlert("Error", "Please enter an artifact ID!", Alert.AlertType.ERROR);
                return;
            }

            try {
                Long artifactId = Long.parseLong(artifactIdStr);

                // Debug: Check if artifact exists
                String artifactName = controller.getArtefakNameById(artifactId);
                if ("Unknown Artifact".equals(artifactName)) {
                    showAlert("Error", "Artifact with ID " + artifactId + " does not exist!", Alert.AlertType.ERROR);
                    return;
                }

                // Check if artifact is already in the exhibition
                if (pameran.getArtefakIds() != null && pameran.getArtefakIds().contains(artifactId)) {
                    showAlert("Warning", "Artifact '" + artifactName + "' is already in this exhibition!",
                            Alert.AlertType.WARNING);
                    return;
                }

                // Add artifact to exhibition
                System.out
                        .println("🔧 Adding artifact ID " + artifactId + " to exhibition ID " + pameran.getPameranId());
                controller.addArtefakToPameran(pameran.getPameranId(), artifactId);

                // Refresh the table to show updated data
                refreshEventList();

                showAlert("Success",
                        "Artifact '" + artifactName + "' (ID: " + artifactId + ") added successfully to '"
                                + pameran.getNamaPameran() + "'!",
                        Alert.AlertType.INFORMATION);

            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid numeric artifact ID!", Alert.AlertType.ERROR);
            } catch (Exception e) {
                System.err.println("❌ Error adding artifact: " + e.getMessage());
                e.printStackTrace();
                showAlert("Error", "Failed to add artifact: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        });
    }

    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        try {
            List<PameranDto> results;
            if (searchTerm.isEmpty()) {
                results = controller.getAllPameransOrderById();
            } else {
                results = controller.searchPameransOrderById(searchTerm);
            }
            loadEventData(results);
        } catch (Exception e) {
            showAlert("Error", "Search failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void refreshEventList() {
        try {
            List<PameranDto> pamerans = controller.getAllPameransOrderById();
            loadEventData(pamerans);
            searchField.clear();
        } catch (Exception e) {
            showAlert("Error", "Failed to load events: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadEventData(List<PameranDto> pamerans) {
        eventTable.getItems().clear();
        eventTable.getItems().addAll(pamerans);

        // Optional: Show count in subtitle
        subtitleLabel.setText("Dashboard - Total Events: " + pamerans.size());
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Add this helper method to get artifact names
    private String getArtifactNameById(Long artifactId) {
        try {
            // Now use the PameranController to get artifact names
            return controller.getArtefakNameById(artifactId);
        } catch (Exception e) {
            System.err.println("Error getting artifact name for ID " + artifactId + ": " + e.getMessage());
            return "Unknown Artifact";
        }
    }
}
