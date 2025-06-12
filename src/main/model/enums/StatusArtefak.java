package main.model.enums;

public enum StatusArtefak {
    TERSEDIA("Tersedia"),
    DIPAMERKAN("Dipamerkan"),
    DIPELIHARA("Dipelihara"),
    RUSAK("Rusak");
    
    private final String displayName;
    
    StatusArtefak(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    // Method to handle database values safely
    public static StatusArtefak fromDatabaseValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return TERSEDIA; // default value
        }
        
        try {
            return StatusArtefak.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Unknown status value: " + value + ", defaulting to TERSEDIA");
            return TERSEDIA; // fallback to default
        }
    }
    
    // Method to handle UI string values
    public static StatusArtefak fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return TERSEDIA;
        }
        
        // Try to match by display name first
        for (StatusArtefak status : StatusArtefak.values()) {
            if (status.getDisplayName().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }
        
        // If not found by display name, try by enum name
        try {
            return StatusArtefak.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Unknown status string: " + value + ", defaulting to TERSEDIA");
            return TERSEDIA;
        }
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}