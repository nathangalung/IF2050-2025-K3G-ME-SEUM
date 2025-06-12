package main.model.enums;

/**
 * User account status
 */
public enum UserStatus {
    AKTIF("Active", "User account is active"),
    NONAKTIF("Inactive", "User account is deactivated");
    
    private final String displayName;
    private final String description;
    
    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static UserStatus fromString(String status) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.name().equalsIgnoreCase(status)) {
                return userStatus;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}