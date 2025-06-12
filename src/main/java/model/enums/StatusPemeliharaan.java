package main.model.enums;

public enum StatusPemeliharaan {
    DIJADWALKAN("Scheduled", "Maintenance is scheduled"),
    SEDANG_BERLANGSUNG("In Progress", "Maintenance is currently in progress"),
    SELESAI("Done", "Maintenance has been completed");
    
    private final String displayName;
    private final String description;
    
    StatusPemeliharaan(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static StatusPemeliharaan fromString(String status) {
        for (StatusPemeliharaan s : StatusPemeliharaan.values()) {
            if (s.name().equalsIgnoreCase(status) || 
                s.getDisplayName().equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown maintenance status: " + status);
    }
}
