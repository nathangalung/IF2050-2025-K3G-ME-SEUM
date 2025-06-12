package main.ui.components.forms;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.model.dto.PemeliharaanDto;
import java.time.LocalDateTime;

public class PemeliharaanForm extends VBox {
    
    private ComboBox<String> artefakComboBox;
    private ComboBox<String> petugasComboBox;
    private TextArea deskripsiArea;
    private DatePicker tanggalMulaiPicker;
    private TextArea catatanArea;
    private Button submitButton;
    private Button cancelButton;
    
    private final PemeliharaanDto pemeliharaanDto;
    private final boolean isEdit;
    
    public PemeliharaanForm(PemeliharaanDto dto, boolean isEdit) {
        this.pemeliharaanDto = dto != null ? dto : new PemeliharaanDto();
        this.isEdit = isEdit;
        
        initializeComponents();
        layoutComponents();
        if (isEdit) {
            populateForm();
        }
    }
    
    private void initializeComponents() {
        // Initialize artifact dropdown
        artefakComboBox = new ComboBox<>();
        artefakComboBox.setPromptText("Select Artifact");
        artefakComboBox.setPrefWidth(200);
        
        // Initialize cleaner dropdown
        petugasComboBox = new ComboBox<>();
        petugasComboBox.setPromptText("Select Cleaner");
        petugasComboBox.setPrefWidth(200);
        
        // Initialize description area
        deskripsiArea = new TextArea();
        deskripsiArea.setPromptText("Enter maintenance description");
        deskripsiArea.setPrefRowCount(3);
        
        // Initialize date picker
        tanggalMulaiPicker = new DatePicker();
        
        // Initialize notes area
        catatanArea = new TextArea();
        catatanArea.setPromptText("Enter notes (optional)");
        catatanArea.setPrefRowCount(2);
        
        // Initialize buttons
        submitButton = new Button(isEdit ? "Update" : "Create");
        cancelButton = new Button("Cancel");
        
        // Add button event handlers
        submitButton.setOnAction(e -> handleSubmit());
        cancelButton.setOnAction(e -> handleCancel());
    }
    
    private void layoutComponents() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        // Add components to grid
        int row = 0;
        grid.add(new Label("Artifact:"), 0, row);
        grid.add(artefakComboBox, 1, row++);
        
        grid.add(new Label("Cleaner:"), 0, row);
        grid.add(petugasComboBox, 1, row++);
        
        grid.add(new Label("Description:"), 0, row);
        grid.add(deskripsiArea, 1, row++);
        
        grid.add(new Label("Start Date:"), 0, row);
        grid.add(tanggalMulaiPicker, 1, row++);
        
        grid.add(new Label("Notes:"), 0, row);
        grid.add(catatanArea, 1, row++);
        
        // Add buttons
        ButtonBar buttonBar = new ButtonBar();
        buttonBar.getButtons().addAll(submitButton, cancelButton);
        grid.add(buttonBar, 1, row);
        
        getChildren().add(grid);
    }
    
    private void populateForm() {
        if (pemeliharaanDto.getArtefakId() != null) {
            artefakComboBox.setValue(pemeliharaanDto.getNamaArtefak());
        }
        if (pemeliharaanDto.getPetugasId() != null) {
            petugasComboBox.setValue(pemeliharaanDto.getNamaPetugas());
        }
        deskripsiArea.setText(pemeliharaanDto.getDeskripsiPemeliharaan());
        if (pemeliharaanDto.getTanggalMulai() != null) {
            tanggalMulaiPicker.setValue(pemeliharaanDto.getTanggalMulai().toLocalDate());
        }
        catatanArea.setText(pemeliharaanDto.getCatatan());
    }
    
    private void handleSubmit() {
        // Validation
        if (artefakComboBox.getValue() == null || 
            petugasComboBox.getValue() == null ||
            deskripsiArea.getText().trim().isEmpty() ||
            tanggalMulaiPicker.getValue() == null) {
            
            showError("Please fill in all required fields");
            return;
        }
        
        // Update DTO
        pemeliharaanDto.setDeskripsiPemeliharaan(deskripsiArea.getText());
        pemeliharaanDto.setTanggalMulai(
            LocalDateTime.of(tanggalMulaiPicker.getValue(), LocalDateTime.now().toLocalTime())
        );
        pemeliharaanDto.setCatatan(catatanArea.getText());
        
        // Fire success event
        fireEvent(new PemeliharaanFormEvent(
            PemeliharaanFormEvent.SUBMIT, 
            pemeliharaanDto
        ));
    }
    
    private void handleCancel() {
        fireEvent(new PemeliharaanFormEvent(
            PemeliharaanFormEvent.CANCEL,
            null
        ));
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Form Events
    public static class PemeliharaanFormEvent extends javafx.event.Event {
        public static final javafx.event.EventType<PemeliharaanFormEvent> SUBMIT = 
            new javafx.event.EventType<>(javafx.event.Event.ANY, "SUBMIT");
        public static final javafx.event.EventType<PemeliharaanFormEvent> CANCEL = 
            new javafx.event.EventType<>(javafx.event.Event.ANY, "CANCEL");
            
        private final PemeliharaanDto pemeliharaanDto;
        
        public PemeliharaanFormEvent(javafx.event.EventType<PemeliharaanFormEvent> eventType, 
                                   PemeliharaanDto dto) {
            super(eventType);
            this.pemeliharaanDto = dto;
        }
        
        public PemeliharaanDto getPemeliharaanDto() {
            return pemeliharaanDto;
        }
    }
}