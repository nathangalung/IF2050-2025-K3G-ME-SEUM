package main.ui.components.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.dto.PemeliharaanDto;
import java.time.format.DateTimeFormatter;
import java.util.List; // Add this import

public class PemeliharaanTable extends TableView<PemeliharaanDto> {
    
    private final ObservableList<PemeliharaanDto> pemeliharaanList = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    
    public PemeliharaanTable() {
        initializeColumns();
        setItems(pemeliharaanList);
    }
    
    private void initializeColumns() {
        // ID Column
        TableColumn<PemeliharaanDto, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("pemeliharaanId"));
        
        // Artifact Column
        TableColumn<PemeliharaanDto, String> artifactColumn = new TableColumn<>("Artifact");
        artifactColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaArtefak()));
        
        // Description Column
        TableColumn<PemeliharaanDto, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("deskripsiPemeliharaan"));
        descColumn.setPrefWidth(200);
        
        // Cleaner Column
        TableColumn<PemeliharaanDto, String> cleanerColumn = new TableColumn<>("Cleaner");
        cleanerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPetugas()));
        
        // Start Date Column
        TableColumn<PemeliharaanDto, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getTanggalMulai().format(dateFormatter)
        ));
        
        // End Date Column
        TableColumn<PemeliharaanDto, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getTanggalSelesai() != null ? 
            data.getValue().getTanggalSelesai().format(dateFormatter) : "-"
        ));
        
        // Status Column
        TableColumn<PemeliharaanDto, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add columns to table
        getColumns().addAll(
            idColumn, 
            artifactColumn, 
            descColumn, 
            cleanerColumn, 
            startDateColumn, 
            endDateColumn, 
            statusColumn
        );
        
        // Set table properties
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    public void updateData(List<PemeliharaanDto> pemeliharaans) {
        pemeliharaanList.clear();
        pemeliharaanList.addAll(pemeliharaans);
    }
    
    public PemeliharaanDto getSelectedItem() {
        return getSelectionModel().getSelectedItem();
    }
}
