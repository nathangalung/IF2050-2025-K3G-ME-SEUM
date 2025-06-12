package test.model.enums;

import main.model.enums.StatusPemeliharaan;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StatusPemeliharaan enum
 * Tests enum functionality and string conversion
 */
@DisplayName("StatusPemeliharaan Enum Tests")
class StatusPemeliharaanTest {

    @Test
    @DisplayName("Should have all required enum values")
    void testEnumValues() {
        StatusPemeliharaan[] values = StatusPemeliharaan.values();
        
        assertEquals(3, values.length);
        assertEquals(StatusPemeliharaan.DIJADWALKAN, values[0]);
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, values[1]);
        assertEquals(StatusPemeliharaan.SELESAI, values[2]);
    }

    @Test
    @DisplayName("Should return correct display names")
    void testGetDisplayName() {
        assertEquals("Scheduled", StatusPemeliharaan.DIJADWALKAN.getDisplayName());
        assertEquals("In Progress", StatusPemeliharaan.SEDANG_BERLANGSUNG.getDisplayName());
        assertEquals("Done", StatusPemeliharaan.SELESAI.getDisplayName());
    }

    @Test
    @DisplayName("Should return correct descriptions")
    void testGetDescription() {
        assertEquals("Maintenance is scheduled", StatusPemeliharaan.DIJADWALKAN.getDescription());
        assertEquals("Maintenance is currently in progress", StatusPemeliharaan.SEDANG_BERLANGSUNG.getDescription());
        assertEquals("Maintenance has been completed", StatusPemeliharaan.SELESAI.getDescription());
    }

    @Test
    @DisplayName("Should convert from string by enum name")
    void testFromStringByEnumName() {
        assertEquals(StatusPemeliharaan.DIJADWALKAN, StatusPemeliharaan.fromString("DIJADWALKAN"));
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, StatusPemeliharaan.fromString("SEDANG_BERLANGSUNG"));
        assertEquals(StatusPemeliharaan.SELESAI, StatusPemeliharaan.fromString("SELESAI"));
    }

    @Test
    @DisplayName("Should convert from string by display name")
    void testFromStringByDisplayName() {
        assertEquals(StatusPemeliharaan.DIJADWALKAN, StatusPemeliharaan.fromString("Scheduled"));
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, StatusPemeliharaan.fromString("In Progress"));
        assertEquals(StatusPemeliharaan.SELESAI, StatusPemeliharaan.fromString("Done"));
    }

    @Test
    @DisplayName("Should handle case insensitive conversion")
    void testFromStringCaseInsensitive() {
        // Test enum names case insensitive
        assertEquals(StatusPemeliharaan.DIJADWALKAN, StatusPemeliharaan.fromString("dijadwalkan"));
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, StatusPemeliharaan.fromString("sedang_berlangsung"));
        assertEquals(StatusPemeliharaan.SELESAI, StatusPemeliharaan.fromString("selesai"));

        // Test display names case insensitive
        assertEquals(StatusPemeliharaan.DIJADWALKAN, StatusPemeliharaan.fromString("scheduled"));
        assertEquals(StatusPemeliharaan.SEDANG_BERLANGSUNG, StatusPemeliharaan.fromString("in progress"));
        assertEquals(StatusPemeliharaan.SELESAI, StatusPemeliharaan.fromString("done"));
    }

    @Test
    @DisplayName("Should throw exception for invalid status")
    void testFromStringInvalidStatus() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            StatusPemeliharaan.fromString("INVALID_STATUS");
        });
        
        assertTrue(exception.getMessage().contains("Unknown maintenance status"));
        assertTrue(exception.getMessage().contains("INVALID_STATUS"));
    }    @Test
    @DisplayName("Should throw exception for null status")
    void testFromStringNullStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatusPemeliharaan.fromString(null);
        });
    }

    @Test
    @DisplayName("Should handle empty string")
    void testFromStringEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            StatusPemeliharaan.fromString("");
        });
        
        assertTrue(exception.getMessage().contains("Unknown maintenance status"));
    }

    @Test
    @DisplayName("Should validate enum constant names")
    void testEnumConstantNames() {
        assertEquals("DIJADWALKAN", StatusPemeliharaan.DIJADWALKAN.name());
        assertEquals("SEDANG_BERLANGSUNG", StatusPemeliharaan.SEDANG_BERLANGSUNG.name());
        assertEquals("SELESAI", StatusPemeliharaan.SELESAI.name());
    }

    @Test
    @DisplayName("Should support toString method")
    void testToString() {
        assertEquals("DIJADWALKAN", StatusPemeliharaan.DIJADWALKAN.toString());
        assertEquals("SEDANG_BERLANGSUNG", StatusPemeliharaan.SEDANG_BERLANGSUNG.toString());
        assertEquals("SELESAI", StatusPemeliharaan.SELESAI.toString());
    }

    @Test
    @DisplayName("Should support ordinal values")
    void testOrdinals() {
        assertEquals(0, StatusPemeliharaan.DIJADWALKAN.ordinal());
        assertEquals(1, StatusPemeliharaan.SEDANG_BERLANGSUNG.ordinal());
        assertEquals(2, StatusPemeliharaan.SELESAI.ordinal());
    }

    @Test
    @DisplayName("Should maintain enum order for workflow")
    void testWorkflowOrder() {
        // Test that enum order matches logical workflow progression
        StatusPemeliharaan scheduled = StatusPemeliharaan.DIJADWALKAN;
        StatusPemeliharaan inProgress = StatusPemeliharaan.SEDANG_BERLANGSUNG;
        StatusPemeliharaan completed = StatusPemeliharaan.SELESAI;

        assertTrue(scheduled.ordinal() < inProgress.ordinal());
        assertTrue(inProgress.ordinal() < completed.ordinal());
        
        // Verify the logical workflow progression
        assertEquals(0, scheduled.ordinal()); // First step
        assertEquals(1, inProgress.ordinal()); // Second step
        assertEquals(2, completed.ordinal()); // Final step
    }
}
