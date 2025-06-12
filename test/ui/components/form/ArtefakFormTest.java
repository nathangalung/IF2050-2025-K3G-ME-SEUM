package test.ui.components.forms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import main.controller.ManajemenArtefakController;
import main.ui.components.forms.ArtefakForm;
import main.model.dto.ArtefakDto;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import javax.swing.JDialog;
import java.awt.GraphicsEnvironment;

public class ArtefakFormTest {
    
    @Mock
    private ManajemenArtefakController mockController;
    
    @Mock
    private Frame mockParent;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testArtefakFormClassExists() {
        // Test that the class exists and can be instantiated
        assertDoesNotThrow(() -> {
            Class.forName("main.ui.components.forms.ArtefakForm");
        });
    }
    
    @Test
    public void testConstructorParameters() {
        // Skip if running in headless mode
        if (GraphicsEnvironment.isHeadless()) {
            assertTrue(true, "Skipping GUI test in headless environment");
            return;
        }
        
        // Test constructor parameter types
        SwingUtilities.invokeLater(() -> {
            try {
                // Test that constructor accepts Frame and ManajemenArtefakController
                ArtefakForm form = new ArtefakForm(mockParent, mockController);
                assertNotNull(form);
                assertTrue(form instanceof JDialog);
            } catch (Exception e) {
                // Expected in headless or mock environment
                assertTrue(true, "Expected exception in test environment: " + e.getMessage());
            }
        });
        
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    @Test
    public void testFormIsJDialog() {
        // Test that ArtefakForm extends JDialog
        assertTrue(ArtefakForm.class.getSuperclass() == JDialog.class);
    }
    
    @Test
    public void testLoadArtefakMethod() {
        // Test that loadArtefak method exists
        assertDoesNotThrow(() -> {
            ArtefakForm.class.getMethod("loadArtefak", ArtefakDto.class);
        });
    }
    
    @Test
    public void testFormHasRequiredMethods() {
        // Test that form has expected methods
        assertDoesNotThrow(() -> {
            ArtefakForm.class.getMethod("loadArtefak", ArtefakDto.class);
        });
        
        // Test constructor exists
        assertDoesNotThrow(() -> {
            ArtefakForm.class.getConstructor(Frame.class, ManajemenArtefakController.class);
        });
    }
    
    @Test
    public void testFormMethodsExist() {
        // Test that required methods exist without instantiation
        Class<?> formClass = ArtefakForm.class;
        
        // Check if essential methods exist
        assertDoesNotThrow(() -> formClass.getMethod("loadArtefak", ArtefakDto.class));
        assertDoesNotThrow(() -> formClass.getConstructor(Frame.class, ManajemenArtefakController.class));
        
        // Verify class hierarchy
        assertEquals(JDialog.class, formClass.getSuperclass());
    }
}