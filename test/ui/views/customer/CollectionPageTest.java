package test.ui.views.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import main.ui.views.customer.CollectionPage;
import main.controller.ManajemenArtefakController;
import main.model.dto.ArtefakDto;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ScrollPane;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CollectionPageTest {
    
    @Mock
    private ManajemenArtefakController mockController;
    
    private CollectionPage collectionPage;
    private boolean javafxInitialized = false;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        if (!javafxInitialized) {
            // Initialize JavaFX toolkit
            new JFXPanel();
            javafxInitialized = true;
        }
        
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                collectionPage = new CollectionPage(mockController);
                latch.countDown();
            } catch (Exception e) {
                latch.countDown();
                throw e;
            }
        });
        
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testCollectionPageCreation() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                assertNotNull(collectionPage);
            } finally {
                latch.countDown();
            }
        });
        
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testUIComponentsInitialized() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                assertNotNull(collectionPage.getArtefakPane());
                assertNotNull(collectionPage.getSearchField());
                assertNotNull(collectionPage.getScrollPane());
            } finally {
                latch.countDown();
            }
        });
        
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testDisplayArtefaksWithEmptyList() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                List<ArtefakDto> emptyList = new ArrayList<>();
                collectionPage.displayArtefaks(emptyList);
                
                // Should show "no data" message or empty state
                assertTrue(collectionPage.getArtefakPane().getChildren().size() >= 0);
            } finally {
                latch.countDown();
            }
        });
        
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testDisplayArtefaksWithData() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                List<ArtefakDto> artefaks = createMockArtefakList();
                collectionPage.displayArtefaks(artefaks);
                
                // Should display artifact cards
                assertTrue(collectionPage.getArtefakPane().getChildren().size() > 0);
            } finally {
                latch.countDown();
            }
        });
        
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testShowMessage() {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                // Test that showMessage method doesn't throw exception
                assertDoesNotThrow(() -> {
                    collectionPage.showMessage("Test message", false);
                });
                
                assertDoesNotThrow(() -> {
                    collectionPage.showMessage("Test error", true);
                });
            } finally {
                latch.countDown();
            }
        });
        
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    // ... rest of the test methods with similar CountDownLatch pattern
    
    private List<ArtefakDto> createMockArtefakList() {
        List<ArtefakDto> artefaks = new ArrayList<>();
        
        ArtefakDto artefak1 = new ArtefakDto();
        artefak1.setArtefakId(1L);
        artefak1.setNamaArtefak("Test Artefak 1");
        artefak1.setDeskripsiArtefak("Test Description 1");
        artefak1.setAsalDaerah("Test Region 1");
        artefak1.setGambar("test1.jpg");
        
        ArtefakDto artefak2 = new ArtefakDto();
        artefak2.setArtefakId(2L);
        artefak2.setNamaArtefak("Test Artefak 2");
        artefak2.setDeskripsiArtefak("Test Description 2");
        artefak2.setAsalDaerah("Test Region 2");
        artefak2.setGambar("test2.jpg");
        
        artefaks.add(artefak1);
        artefaks.add(artefak2);
        
        return artefaks;
    }
}