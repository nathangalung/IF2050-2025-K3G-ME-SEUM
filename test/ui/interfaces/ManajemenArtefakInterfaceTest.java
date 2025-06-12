package test.ui.interfaces;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.ui.interfaces.ManajemenArtefakInterface;
import main.model.dto.ArtefakDto;

import java.util.ArrayList;
import java.util.List;

public class ManajemenArtefakInterfaceTest {
    
    @Test
    public void testInterfaceExists() {
        // Test that the interface exists
        assertDoesNotThrow(() -> {
            Class.forName("main.ui.interfaces.ManajemenArtefakInterface");
        });
    }
    
    @Test
    public void testInterfaceIsInterface() {
        // Test that ManajemenArtefakInterface is actually an interface
        Class<?> clazz = ManajemenArtefakInterface.class;
        assertTrue(clazz.isInterface());
    }
    
    @Test
    public void testInterfaceHasRequiredMethods() {
        // Test that interface has all required methods
        Class<?> clazz = ManajemenArtefakInterface.class;
        
        assertDoesNotThrow(() -> {
            clazz.getMethod("showAddArtefakForm");
            clazz.getMethod("showEditArtefakForm", Long.class);
            clazz.getMethod("displayArtefakList", List.class);
            clazz.getMethod("showArtefakDetails", ArtefakDto.class);
            clazz.getMethod("searchArtefak", String.class);
            clazz.getMethod("showSearchResults", List.class);
            clazz.getMethod("refreshArtefakList");
            clazz.getMethod("showErrorMessage", String.class);
            clazz.getMethod("showSuccessMessage", String.class);
        });
    }
    
    @Test
    public void testInterfaceCanBeImplemented() {
        // Test that interface can be implemented
        assertDoesNotThrow(() -> {
            // Create anonymous implementation for testing
            ManajemenArtefakInterface implementation = new ManajemenArtefakInterface() {
                @Override
                public void showAddArtefakForm() {}
                
                @Override
                public void showEditArtefakForm(Long artefakId) {}
                
                @Override
                public void displayArtefakList(List<ArtefakDto> artefaks) {}
                
                @Override
                public void showArtefakDetails(ArtefakDto artefak) {}
                
                @Override
                public List<ArtefakDto> searchArtefak(String criteria) {
                    return new ArrayList<>();
                }
                
                @Override
                public void showSearchResults(List<ArtefakDto> results) {}
                
                @Override
                public void refreshArtefakList() {}
                
                @Override
                public void showErrorMessage(String message) {}
                
                @Override
                public void showSuccessMessage(String message) {}
            };
            
            assertNotNull(implementation);
        });
    }
}