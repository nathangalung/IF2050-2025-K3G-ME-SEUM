package main.ui.components.forms;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import main.model.dto.PameranDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * Form dialog for creating and editing Pameran (Exhibition) data
 */
public class PameranForm extends Dialog<PameranDto> {
    private TextField nameField;
    private TextArea descriptionArea;
    private TextField startDateField;
    private TextField endDateField;
    private CheckBox activeCheckBox;

    private PameranDto currentPameran;

    public PameranForm(String title, PameranDto pameran) {
        this.currentPameran = pameran;
        setTitle(title);
        setHeaderText(null);

        initComponents();
        setupLayout();
        setupResult();

        // Load data if editing
        if (pameran != null) {
            loadPameranData(pameran);
        }
    }

    private void initComponents() {
        // Name field
        nameField = new TextField();
        nameField.setPromptText("Enter exhibition name");
        nameField.setPrefWidth(300);

        // Description area
        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Enter exhibition description");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setPrefColumnCount(40);
        descriptionArea.setWrapText(true);

        // Start date field
        startDateField = new TextField();
        startDateField.setPromptText("dd/MM/yyyy HH:mm");
        startDateField.setPrefWidth(200);

        // End date field
        endDateField = new TextField();
        endDateField.setPromptText("dd/MM/yyyy HH:mm");
        endDateField.setPrefWidth(200);

        // Active checkbox
        activeCheckBox = new CheckBox("Active Exhibition");
        activeCheckBox.setSelected(true); // Default to active
    }

    private void setupLayout() {
        // Create grid layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add form fields
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionArea, 1, 1);

        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDateField, 1, 2);

        grid.add(new Label("End Date:"), 0, 3);
        grid.add(endDateField, 1, 3);

        grid.add(new Label("Status:"), 0, 4);
        grid.add(activeCheckBox, 1, 4);

        // Add date format help
        Label dateHelpLabel = new Label("Date format: dd/MM/yyyy HH:mm (e.g., 25/12/2024 10:00)");
        dateHelpLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");
        grid.add(dateHelpLabel, 1, 5);

        // Set dialog content
        getDialogPane().setContent(grid);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Disable save button initially if creating new
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        if (currentPameran == null) {
            saveButton.setDisable(true);

            // Enable save button when name is entered
            nameField.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });
        }

        // Request focus on name field
        nameField.requestFocus();
    }

    private void setupResult() {
        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return createPameranFromForm();
            }
            return null;
        });
    }

    private void loadPameranData(PameranDto pameran) {
        nameField.setText(pameran.getNamaPameran());
        descriptionArea.setText(pameran.getDeskripsiPameran());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        if (pameran.getTanggalMulai() != null) {
            startDateField.setText(pameran.getTanggalMulai().format(formatter));
        }

        if (pameran.getTanggalSelesai() != null) {
            endDateField.setText(pameran.getTanggalSelesai().format(formatter));
        }

        activeCheckBox.setSelected(pameran.getIsActive() != null ? pameran.getIsActive() : true);
    }

    private PameranDto createPameranFromForm() {
        try {
            PameranDto dto = new PameranDto();

            // Set ID if editing
            if (currentPameran != null) {
                dto.setPameranId(currentPameran.getPameranId());
                dto.setTanggalDibuat(currentPameran.getTanggalDibuat());
                dto.setArtefakIds(currentPameran.getArtefakIds());
            }

            // Set form data
            dto.setNamaPameran(nameField.getText().trim());
            dto.setDeskripsiPameran(descriptionArea.getText().trim());
            dto.setIsActive(activeCheckBox.isSelected());

            // Parse dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            try {
                if (!startDateField.getText().trim().isEmpty()) {
                    dto.setTanggalMulai(LocalDateTime.parse(startDateField.getText().trim(), formatter));
                }
            } catch (DateTimeParseException e) {
                showAlert("Invalid Start Date", "Please enter start date in format: dd/MM/yyyy HH:mm");
                return null;
            }

            try {
                if (!endDateField.getText().trim().isEmpty()) {
                    dto.setTanggalSelesai(LocalDateTime.parse(endDateField.getText().trim(), formatter));
                }
            } catch (DateTimeParseException e) {
                showAlert("Invalid End Date", "Please enter end date in format: dd/MM/yyyy HH:mm");
                return null;
            }

            // Validate dates
            if (dto.getTanggalMulai() != null && dto.getTanggalSelesai() != null) {
                if (dto.getTanggalMulai().isAfter(dto.getTanggalSelesai())) {
                    showAlert("Invalid Date Range", "Start date must be before end date");
                    return null;
                }
            }

            return dto;

        } catch (Exception e) {
            showAlert("Error", "Error creating exhibition data: " + e.getMessage());
            return null;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Static factory methods for convenience
    public static Optional<PameranDto> showAddDialog() {
        PameranForm form = new PameranForm("Add New Exhibition", null);
        return form.showAndWait();
    }

    public static Optional<PameranDto> showEditDialog(PameranDto pameran) {
        PameranForm form = new PameranForm("Edit Exhibition", pameran);
        return form.showAndWait();
    }
}
