package test.ui.views.curator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.ui.views.curator.ArtefakListPage;
import main.controller.ManajemenArtefakController;
import main.ui.interfaces.ManajemenArtefakInterface;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class ArtefakListPageTest {
    
    @Mock
    private ManajemenArtefakController mockController;
    
    private ArtefakListPage artefakListPage;
    private boolean javafxInitialized = false;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        if (!javafxInitialized) {
            new JFXPanel();
            javafxInitialized = true;
        }
        
        Platform.runLater(() -> {
            artefakListPage = new ArtefakListPage(mockController);
        });
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testArtefakListPageCreation() {
        Platform.runLater(() -> {
            assertNotNull(artefakListPage);
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testImplementsInterface() {
        Platform.runLater(() -> {
            assertTrue(artefakListPage instanceof ManajemenArtefakInterface);
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testInterfaceMethods() {
        Platform.runLater(() -> {
            assertDoesNotThrow(() -> {
                artefakListPage.showAddArtefakForm();
            });
            
            assertDoesNotThrow(() -> {
                artefakListPage.refreshArtefakList();
            });
            
            assertDoesNotThrow(() -> {
                artefakListPage.showErrorMessage("Test error");
            });
            
            assertDoesNotThrow(() -> {
                artefakListPage.showSuccessMessage("Test success");
            });
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}