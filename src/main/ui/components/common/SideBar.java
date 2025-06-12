package main.ui.components.common;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.model.dto.UserDto;
import main.utils.SessionManager;

/**
 * SideBar component for ME-SEUM application.
 * Creates a vertical black sidebar with navigation menu items and logout functionality.
 */
public class SideBar extends VBox implements SessionManager.SessionListener {
    
    private SidebarMenuItem activeMenuItem;
    private NavigationListener navigationListener;
    private SessionManager sessionManager;
    private Button logoutButton;
    private Label userInfoLabel;
    
    /**
     * Constructor for SideBar
     */
    public SideBar() {
        this.sessionManager = SessionManager.getInstance();
        setupSideBar();
        createMenuItems();
        createLogoutSection();
        
        // Register for session changes
        sessionManager.addSessionListener(this);
        
        // Update initial state
        updateSessionState();
    }
    
    /**
     * Constructor for SideBar with navigation listener
     * @param listener The navigation listener
     */
    public SideBar(NavigationListener listener) {
        this.navigationListener = listener;
        this.sessionManager = SessionManager.getInstance();
        setupSideBar();
        createMenuItems();
        createLogoutSection();
        
        // Register for session changes
        sessionManager.addSessionListener(this);
        
        // Update initial state
        updateSessionState();
    }
    
    /**
     * Set up the sidebar appearance
     */
    private void setupSideBar() {
        // Set size constraints
        setPrefWidth(180);
        setMinWidth(180);
        setMaxWidth(180);
        setPrefHeight(Double.MAX_VALUE); // Stretch to full height
        
        // Set appearance - Dark background matching the image
        setStyle("-fx-background-color: #121212;"); // Black background
        setPadding(new Insets(20, 0, 20, 0));
        setSpacing(10); // Increased spacing to match image
        setAlignment(Pos.TOP_CENTER);
        
        // Add logo/title - ME-SEUM
        Label logo = new Label("ME-SEUM");
        logo.setFont(Font.font("System", FontWeight.BOLD, 18));
        logo.setTextFill(Color.WHITE);
        logo.setPadding(new Insets(0, 0, 20, 0));
        
        getChildren().add(logo);
    }
    
    /**
     * Create the menu items for the sidebar
     */
    private void createMenuItems() {
        // Create menu items matching the curator requirements
        // ARTIFACT as main page, EVENT ARTIFACT for events, MAINTENANCE, FEEDBACK
        SidebarMenuItem artifactItem = new SidebarMenuItem("📊", "ARTIFACT LIST", "ARTIFACT");
        SidebarMenuItem eventArtifactItem = new SidebarMenuItem("🏛️", "EVENT ARTIFACT", "EVENT_ARTIFACT");
        SidebarMenuItem maintenanceItem = new SidebarMenuItem("🔧", "MAINTANANCE", "MAINTENANCE");
        SidebarMenuItem feedbackItem = new SidebarMenuItem("📝", "FEEDBACK", "FEEDBACK");
        
        // Add menu items to sidebar
        getChildren().addAll(
            artifactItem,
            eventArtifactItem,
            maintenanceItem, 
            feedbackItem
        );
        
        // Set default active item to ARTIFACT
        setActiveMenuItem(artifactItem);
    }
    
    /**
     * Create logout section at the bottom of sidebar
     */
    private void createLogoutSection() {
        // Spacer to push logout section to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // User info section
        VBox userSection = new VBox(8);
        userSection.setPadding(new Insets(15, 15, 0, 15));
        userSection.setAlignment(Pos.CENTER_LEFT);
        
        // User info label (shows current user name and role)
        userInfoLabel = new Label();
        userInfoLabel.setFont(Font.font("System", 11));
        userInfoLabel.setTextFill(Color.LIGHTGRAY);
        userInfoLabel.setWrapText(true);
        userInfoLabel.setVisible(false); // Hidden by default
        
        // Logout button
        logoutButton = new Button("🚪 Logout");
        logoutButton.setPrefWidth(150);
        logoutButton.setStyle(
            "-fx-background-color: #2c2c2c;" +
            "-fx-text-fill: #ff6b6b;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: normal;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: #404040;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;"
        );
        
        // Logout button hover effects
        logoutButton.setOnMouseEntered(e -> 
            logoutButton.setStyle(
                "-fx-background-color: #ff6b6b;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 12;" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #ff6b6b;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;"
            )
        );
        
        logoutButton.setOnMouseExited(e -> 
            logoutButton.setStyle(
                "-fx-background-color: #2c2c2c;" +
                "-fx-text-fill: #ff6b6b;" +
                "-fx-font-size: 11px;" +
                "-fx-font-weight: normal;" +
                "-fx-padding: 8 12;" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #404040;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;"
            )
        );
        
        // Logout button action
        logoutButton.setOnAction(e -> handleLogout());
        
        // Initially hidden (shown only when logged in)
        logoutButton.setVisible(false);
        
        userSection.getChildren().addAll(userInfoLabel, logoutButton);
        
        // Add spacer and user section to sidebar
        getChildren().addAll(spacer, userSection);
    }

    
    /**
     * Handle logout action
     */
    /**
 * Handle logout action
 */
    /**
 * Handle logout action
 */
    private void handleLogout() {
        // Show confirmation dialog (optional)
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Logout");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Are you sure you want to logout?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Logout from session manager
            sessionManager.logout();
            
            // Notify navigation listener if available
            if (navigationListener != null) {
                // Navigate to login page after logout
                navigationListener.onSidebarNavigate("LOGIN");
            }
            
            System.out.println("🔓 User logged out from sidebar - redirecting to login");
        }
    }
    
    /**
     * Update UI based on session state
     */
    private void updateSessionState() {
        if (sessionManager.isLoggedIn()) {
            UserDto user = sessionManager.getCurrentUser();
            
            // Show user info
            userInfoLabel.setText("Logged in as:\n" + user.getNamaLengkap() + "\n(" + user.getRole().getDisplayName() + ")");
            userInfoLabel.setVisible(true);
            
            // Show logout button
            logoutButton.setVisible(true);
            
        } else {
            // Hide user info and logout button
            userInfoLabel.setVisible(false);
            logoutButton.setVisible(false);
        }
    }
    
    /**
     * Session change listener implementation
     */
    @Override
    public void onSessionChanged(UserDto user) {
        // Update UI when session changes
        updateSessionState();
        System.out.println("🔄 SideBar updated for session change: " + 
                          (user != null ? user.getUsername() : "logged out"));
    }
    
    /**
     * Set the active menu item
     * @param menuItem The menu item to set as active
     */
    public void setActiveMenuItem(SidebarMenuItem menuItem) {
        // Deactivate current active item if exists
        if (activeMenuItem != null) {
            activeMenuItem.setActive(false);
        }
        
        // Activate new item
        menuItem.setActive(true);
        activeMenuItem = menuItem;
    }
    
    /**
     * Set the active menu item by destination ID
     * @param destination The destination ID of the menu item
     */
    public void setActiveMenuByDestination(String destination) {
        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i) instanceof SidebarMenuItem) {
                SidebarMenuItem item = (SidebarMenuItem) getChildren().get(i);
                if (item.getDestination().equals(destination)) {
                    setActiveMenuItem(item);
                    break;
                }
            }
        }
    }
    
    /**
     * Set the navigation listener
     * @param listener The navigation listener
     */
    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }
    
    /**
     * Cleanup when component is destroyed
     */
    public void cleanup() {
        sessionManager.removeSessionListener(this);
    }
    
    /**
     * Inner class representing a menu item in the sidebar
     */
    public class SidebarMenuItem extends VBox {
        
        private final Label iconLabel;
        private final Label textLabel;
        private final String destination;
        
        /**
         * Constructor for SidebarMenuItem
         * @param icon The icon text/emoji
         * @param text The menu item text
         * @param destination The navigation destination
         */
        public SidebarMenuItem(String icon, String text, String destination) {
            this.destination = destination;
            
            // Setup container to match image design
            setSpacing(4);
            setPadding(new Insets(8, 0, 8, 20));
            setAlignment(Pos.CENTER_LEFT);
            setPrefWidth(180);
            setCursor(javafx.scene.Cursor.HAND);
            
            // Create icon label
            iconLabel = new Label(icon);
            iconLabel.setFont(Font.font("System", 14));
            iconLabel.setTextFill(Color.LIGHTGRAY);
            
            // Create text label with appropriate size
            textLabel = new Label(text);
            textLabel.setFont(Font.font("System", 12));
            textLabel.setTextFill(Color.LIGHTGRAY);
            
            // Add labels to container
            getChildren().addAll(iconLabel, textLabel);
            
            // Set hover style
            setupHoverEffect();
            
            // Add click event
            setOnMouseClicked(e -> {
                if (navigationListener != null) {
                    // FIXED METHOD NAME - Line 328
                    navigationListener.onSidebarNavigate(destination);
                }
            });
        }
        
        /**
         * Set up hover effect for menu item
         */
        private void setupHoverEffect() {
            setOnMouseEntered(e -> {
                if (this != activeMenuItem) {
                    setStyle("-fx-background-color: #333333;");
                }
            });
            
            setOnMouseExited(e -> {
                if (this != activeMenuItem) {
                    setStyle("-fx-background-color: transparent;");
                }
            });
        }
        
        /**
         * Set the active state of the menu item
         * @param active Whether the menu item is active
         */
        public void setActive(boolean active) {
            if (active) {
                // Dark background for active item
                setStyle("-fx-background-color: #3E3E3E;");
                iconLabel.setTextFill(Color.WHITE);
                textLabel.setTextFill(Color.WHITE);
                textLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            } else {
                setStyle("-fx-background-color: transparent;");
                iconLabel.setTextFill(Color.LIGHTGRAY);
                textLabel.setTextFill(Color.LIGHTGRAY);
                textLabel.setFont(Font.font("System", 12));
            }
        }
        
        /**
         * Get the destination of this menu item
         * @return The destination
         */
        public String getDestination() {
            return destination;
        }
    }
    
    /**
     * Interface for sidebar navigation events
     */
    public interface NavigationListener {
        void onSidebarNavigate(String destination);
    }

    // Add this method after createMenuItems()
    public void createCleanerMenuItems() {
        // Clear existing items
        getChildren().clear();
        
        // Re-add logo
        Label logo = new Label("ME-SEUM");
        logo.setFont(Font.font("System", FontWeight.BOLD, 18));
        logo.setTextFill(Color.WHITE);
        logo.setPadding(new Insets(0, 0, 20, 0));
        
        // Create maintenance menu item only
        SidebarMenuItem maintenanceItem = new SidebarMenuItem("🔧", "MAINTENANCE", "MAINTENANCE");
        
        // Add spacer to push logout to bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        // Create user info and logout section
        VBox userSection = new VBox(8);
        userSection.setPadding(new Insets(15));
        
        // User info label
        userInfoLabel = new Label();
        userInfoLabel.setFont(Font.font("System", 11));
        userInfoLabel.setTextFill(Color.LIGHTGRAY);
        userInfoLabel.setWrapText(true);
        
        // Logout button
        logoutButton = new Button("🚪 Logout");
        logoutButton.setPrefWidth(150);
        logoutButton.setStyle(
            "-fx-background-color: #2c2c2c;" +
            "-fx-text-fill: #ff6b6b;" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: normal;" +
            "-fx-padding: 8 12;" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: #404040;" +
            "-fx-border-width: 1;" +
            "-fx-cursor: hand;"
        );
        logoutButton.setOnAction(e -> handleLogout());
        
        userSection.getChildren().addAll(userInfoLabel, logoutButton);
        
        // Add all components (removed userItem)
        getChildren().addAll(
            logo,
            maintenanceItem,
            spacer,
            userSection
        );
        
        // Set default active item
        setActiveMenuItem(maintenanceItem);
        
        // Update session state
        updateSessionState();
    }
}