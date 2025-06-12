package main.ui.components.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.model.dto.ArtefakDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

// Tabel untuk menampilkan data artefak (JavaFX)
public class ArtefakTable extends TableView<ArtefakDto> {
    private ObservableList<ArtefakDto> data;
    
    public ArtefakTable() {
        data = FXCollections.observableArrayList();
        setItems(data);
        setupColumns();
        setupTable();
    }
    
    // Setup table columns
    @SuppressWarnings("unchecked")
    private void setupColumns() {
        // ID Column
        TableColumn<ArtefakDto, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.valueOf(cellData.getValue().getArtefakId())));
        idCol.setPrefWidth(60);
        
        // Name Column
        TableColumn<ArtefakDto, String> nameCol = new TableColumn<>("Nama Artefak");
        nameCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNamaArtefak()));
        nameCol.setPrefWidth(200);
        
        // Region Column
        TableColumn<ArtefakDto, String> regionCol = new TableColumn<>("Asal Daerah");
        regionCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getAsalDaerah()));
        regionCol.setPrefWidth(150);
        
        // Period Column
        TableColumn<ArtefakDto, String> periodCol = new TableColumn<>("Periode");
        periodCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPeriode()));
        periodCol.setPrefWidth(100);
        
        // Status Column
        TableColumn<ArtefakDto, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
        statusCol.setPrefWidth(120);
        
        // Date Column
        TableColumn<ArtefakDto, String> dateCol = new TableColumn<>("Tanggal Registrasi");
        dateCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getTanggalRegistrasi() != null) {
                return new SimpleStringProperty(
                    cellData.getValue().getTanggalRegistrasi()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
            }
            return new SimpleStringProperty("-");
        });
        dateCol.setPrefWidth(130);
        
        getColumns().addAll(idCol, nameCol, regionCol, periodCol, statusCol, dateCol);
    }
    
    // Setup table properties
    private void setupTable() {
        setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setRowFactory(tv -> {
            var row = new javafx.scene.control.TableRow<ArtefakDto>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    // Double click action can be handled here
                    System.out.println("Double clicked: " + row.getItem().getNamaArtefak());
                }
            });
            return row;
        });
    }
    
    // Update table data
    public void updateData(List<ArtefakDto> artefaks) {
        data.clear();
        if (artefaks != null) {
            data.addAll(artefaks);
        }
    }
    
    // Get selected artefak
    public ArtefakDto getSelectedArtefak() {
        return getSelectionModel().getSelectedItem();
    }
    
    // Clear selection
    public void clearSelection() {
        getSelectionModel().clearSelection();
    }
    
    // Add artefak to table
    public void addArtefak(ArtefakDto artefak) {
        data.add(artefak);
    }
    
    // Remove artefak from table
    public void removeArtefak(ArtefakDto artefak) {
        data.remove(artefak);
    }
    
    // Update specific artefak
    public void updateArtefak(ArtefakDto updatedArtefak) {
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getArtefakId().equals(updatedArtefak.getArtefakId())) {
                index = i;
                break;
            }
        }
        
        if (index >= 0) {
            data.set(index, updatedArtefak);
        }
    }
    
    // Get all data
    public ObservableList<ArtefakDto> getData() {
        return data;
    }
}