package test.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import main.model.enums.StatusArtefak;

public class StatusArtefakTest {

    @Test
    public void testGetDisplayName() {
        assertEquals("Tersedia", StatusArtefak.TERSEDIA.getDisplayName());
        assertEquals("Dipamerkan", StatusArtefak.DIPAMERKAN.getDisplayName());
        assertEquals("Dipelihara", StatusArtefak.DIPELIHARA.getDisplayName());
        assertEquals("Rusak", StatusArtefak.RUSAK.getDisplayName());
    }

    @Test
    public void testFromDatabaseValueValid() {
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromDatabaseValue("TERSEDIA"));
        assertEquals(StatusArtefak.DIPAMERKAN, StatusArtefak.fromDatabaseValue("DIPAMERKAN"));
        assertEquals(StatusArtefak.DIPELIHARA, StatusArtefak.fromDatabaseValue("DIPELIHARA"));
        assertEquals(StatusArtefak.RUSAK, StatusArtefak.fromDatabaseValue("RUSAK"));
    }

    @Test
    public void testFromDatabaseValueInvalid() {
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromDatabaseValue("INVALID"));
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromDatabaseValue(null));
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromDatabaseValue(""));
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromDatabaseValue("   "));
    }

    @Test
    public void testFromStringValid() {
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromString("Tersedia"));
        assertEquals(StatusArtefak.DIPAMERKAN, StatusArtefak.fromString("Dipamerkan"));
        assertEquals(StatusArtefak.DIPELIHARA, StatusArtefak.fromString("Dipelihara"));
        assertEquals(StatusArtefak.RUSAK, StatusArtefak.fromString("Rusak"));
    }

    @Test
    public void testFromStringCaseInsensitive() {
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromString("tersedia"));
        assertEquals(StatusArtefak.DIPAMERKAN, StatusArtefak.fromString("DIPAMERKAN"));
        assertEquals(StatusArtefak.DIPELIHARA, StatusArtefak.fromString("dipelihara"));
    }

    @Test
    public void testFromStringInvalid() {
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromString("INVALID"));
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromString(null));
        assertEquals(StatusArtefak.TERSEDIA, StatusArtefak.fromString(""));
    }

    @Test
    public void testToString() {
        assertEquals("Tersedia", StatusArtefak.TERSEDIA.toString());
        assertEquals("Dipamerkan", StatusArtefak.DIPAMERKAN.toString());
        assertEquals("Dipelihara", StatusArtefak.DIPELIHARA.toString());
        assertEquals("Rusak", StatusArtefak.RUSAK.toString());
    }
}