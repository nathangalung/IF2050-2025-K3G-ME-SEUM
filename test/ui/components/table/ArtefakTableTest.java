package test.ui.components.tables;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import main.ui.components.tables.ArtefakTable;
import main.model.dto.ArtefakDto;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.TableView;
import java.util.List;
import java.util.ArrayList;

public class ArtefakTableTest {
    
    private ArtefakTable artefakTable;
    private boolean javafxInitialized = false;
    
    @BeforeEach
    public void setUp() {
        if (!javafxInitialized) {
            // Initialize JavaFX toolkit
            new JFXPanel();
            javafxInitialized = true;
        }
        
        Platform.runLater(() -> {
            artefakTable = new ArtefakTable();
        });
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testArtefakTableCreation() {
        Platform.runLater(() -> {
            assertNotNull(artefakTable);
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testTableIsTableView() {
        Platform.runLater(() -> {
            assertTrue(artefakTable instanceof TableView);
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testUpdateDataWithEmptyList() {
        Platform.runLater(() -> {
            List<ArtefakDto> emptyList = new ArrayList<>();
            assertDoesNotThrow(() -> {
                artefakTable.updateData(emptyList);
            });
            assertEquals(0, artefakTable.getItems().size());
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testUpdateDataWithArtefaks() {
        Platform.runLater(() -> {
            List<ArtefakDto> artefaks = createMockArtefakList();
            assertDoesNotThrow(() -> {
                artefakTable.updateData(artefaks);
            });
            
            // Verify data was added
            assertEquals(2, artefakTable.getItems().size());
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testGetSelectedArtefak() {
        Platform.runLater(() -> {
            // Test that getSelectedArtefak doesn't throw exception
            assertDoesNotThrow(() -> {
                ArtefakDto selected = artefakTable.getSelectedArtefak();
                // May be null if nothing is selected
            });
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testClearSelection() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> {
                artefakTable.clearSelection();
            });
            assertNull(artefakTable.getSelectedArtefak());
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testAddArtefak() {
        Platform.runLater(() -> {
            ArtefakDto newArtefak = new ArtefakDto();
            newArtefak.setArtefakId(3L);
            newArtefak.setNamaArtefak("New Artefak");
            
            int initialSize = artefakTable.getItems().size();
            
            assertDoesNotThrow(() -> {
                artefakTable.addArtefak(newArtefak);
            });
            
            assertEquals(initialSize + 1, artefakTable.getItems().size());
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testRemoveArtefak() {
        Platform.runLater(() -> {
            // First add some data
            List<ArtefakDto> artefaks = createMockArtefakList();
            artefakTable.updateData(artefaks);
            
            int initialSize = artefakTable.getItems().size();
            
            // Then test removal
            ArtefakDto toRemove = artefaks.get(0);
            assertDoesNotThrow(() -> {
                artefakTable.removeArtefak(toRemove);
            });
            
            assertEquals(initialSize - 1, artefakTable.getItems().size());
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testUpdateArtefak() {
        Platform.runLater(() -> {
            // First add some data
            List<ArtefakDto> artefaks = createMockArtefakList();
            artefakTable.updateData(artefaks);
            
            // Then test update
            ArtefakDto updated = artefaks.get(0);
            updated.setNamaArtefak("Updated Name");
            
            assertDoesNotThrow(() -> {
                artefakTable.updateArtefak(updated);
            });
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testGetData() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> {
                var data = artefakTable.getData();
                assertNotNull(data);
            });
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testTableColumnsExist() {
        Platform.runLater(() -> {
            assertTrue(artefakTable.getColumns().size() > 0);
            
            // Check if table has expected column structure
            boolean hasIdColumn = artefakTable.getColumns().stream()
                .anyMatch(col -> "ID".equals(col.getText()));
            assertTrue(hasIdColumn);
            
            boolean hasNameColumn = artefakTable.getColumns().stream()
                .anyMatch(col -> "Nama Artefak".equals(col.getText()));
            assertTrue(hasNameColumn);
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testTableResizePolicy() {
        Platform.runLater(() -> {
            assertEquals(TableView.CONSTRAINED_RESIZE_POLICY, 
                        artefakTable.getColumnResizePolicy());
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private List<ArtefakDto> createMockArtefakList() {
        List<ArtefakDto> artefaks = new ArrayList<>();
        
        ArtefakDto artefak1 = new ArtefakDto();
        artefak1.setArtefakId(1L);
        artefak1.setNamaArtefak("Test Artefak 1");
        artefak1.setDeskripsiArtefak("Test Description 1");
        artefak1.setAsalDaerah("Test Region 1");
        
        ArtefakDto artefak2 = new ArtefakDto();
        artefak2.setArtefakId(2L);
        artefak2.setNamaArtefak("Test Artefak 2");
        artefak2.setDeskripsiArtefak("Test Description 2");
        artefak2.setAsalDaerah("Test Region 2");
        
        artefaks.add(artefak1);
        artefaks.add(artefak2);
        
        return artefaks;
    }
}