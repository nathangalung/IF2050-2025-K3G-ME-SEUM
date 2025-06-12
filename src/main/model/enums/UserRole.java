package main.model.enums;

/**
 * User roles in the ME-SEUM system
 */
public enum UserRole {
    CURATOR("Curator", "Museum curator with administrative privileges"),
    CUSTOMER("Customer", "Museum visitor who can buy tickets and give feedback"),
    CLEANER("Cleaner", "Maintenance staff responsible for artifact care");
    
    private final String displayName;
    private final String description;
    
    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}