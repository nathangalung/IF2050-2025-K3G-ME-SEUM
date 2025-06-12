package main.ui.components.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.model.dto.UserDto;
import main.utils.SessionManager;

/**
 * Session-aware Navigation bar component for ME-SEUM application
 */
public class NavigationBar extends HBox implements SessionManager.SessionListener {
    
    private NavigationListener navigationListener;
    private String activeDestination;
    private Button authButton; // Login/Logout button
    private Label userWelcome; // Welcome message for logged-in users
    private SessionManager sessionManager;
    
    /**
     * Constructor for NavigationBar
     */
    public NavigationBar() {
        this.sessionManager = SessionManager.getInstance();
        setupNavigationBar();
        
        // Register for session changes
        sessionManager.addSessionListener(this);
        
        // Update initial state
        updateAuthenticationState();
    }
    
    /**
     * Constructor with navigation listener
     */
    public NavigationBar(NavigationListener listener) {
        this.navigationListener = listener;
        this.sessionManager = SessionManager.getInstance();
        setupNavigationBar();
        
        // Register for session changes
        sessionManager.addSessionListener(this);
        
        // Update initial state
        updateAuthenticationState();
    }
    
    /**
     * Set up the navigation bar appearance and elements
     */
    private void setupNavigationBar() {
        // Set appearance
        setStyle("-fx-background-color: #000000;");
        setPadding(new Insets(10, 20, 10, 20));
        setSpacing(25);
        setAlignment(Pos.CENTER_LEFT);
        setPrefHeight(60);
        
        // Logo and museum name
        HBox logoBox = createLogoBox();
        
        // Spacer to separate logo from menu items
        Region logoSpacer = new Region();
        HBox.setHgrow(logoSpacer, Priority.SOMETIMES);
        logoSpacer.setMinWidth(20);
        
        // Create navigation buttons
        Button homeButton = createNavButton("Home", "HOME");
        Button aboutButton = createNavButton("About Us", "ABOUT");
        Button eventButton = createNavButton("Event", "EVENT");
        Button collectionButton = createNavButton("Collection", "COLLECTION");
        Button contactButton = createNavButton("Contact", "CONTACT");
        
        // Set Collection as default active
        setActiveDestination("COLLECTION");
        
        // Spacer to push auth section to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Authentication section (login/logout + user welcome)
        HBox authSection = createAuthSection();
        
        // Add all elements to the navigation bar
        getChildren().addAll(
            logoBox,
            logoSpacer,
            homeButton,
            aboutButton,
            eventButton,
            collectionButton,
            contactButton,
            spacer,
            authSection
        );
    }
    
    /**
     * Create logo section
     */
    private HBox createLogoBox() {
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        
        // Create museum logo (temple icon)
        Label logoIcon = new Label("🏛️");
        logoIcon.setFont(Font.font("System", 24));
        logoIcon.setTextFill(Color.GOLD);
        
        // Museum name
        VBox museumNameBox = new VBox(0);
        museumNameBox.setAlignment(Pos.CENTER_LEFT);
        
        Label museumLabel = new Label("Museum");
        museumLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        museumLabel.setTextFill(Color.GOLD);
        
        Label nusantaraLabel = new Label("Nusantara");
        nusantaraLabel.setFont(Font.font("System", 12));
        nusantaraLabel.setTextFill(Color.GOLD);
        
        museumNameBox.getChildren().addAll(museumLabel, nusantaraLabel);
        logoBox.getChildren().addAll(logoIcon, museumNameBox);
        
        return logoBox;
    }
    
    /**
     * Create authentication section (login/logout + user info)
     */
    private HBox createAuthSection() {
        HBox authSection = new HBox(15);
        authSection.setAlignment(Pos.CENTER_RIGHT);
        
        // User welcome message (hidden by default)
        userWelcome = new Label();
        userWelcome.setFont(Font.font("System", 12));
        userWelcome.setTextFill(Color.WHITE);
        userWelcome.setVisible(false);
        
        // Login/Logout button
        authButton = new Button("Login");
        authButton.setStyle(
            "-fx-background-color: #e6d28e;" +
            "-fx-text-fill: #000000;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 15;" +
            "-fx-background-radius: 5px;" +
            "-fx-cursor: hand;"
        );
        
        authButton.setOnAction(e -> handleAuthAction());
        
        authSection.getChildren().addAll(userWelcome, authButton);
        return authSection;
    }
    
    /**
     * Handle login/logout button click
     */
    private void handleAuthAction() {
        if (sessionManager.isLoggedIn()) {
            // Logout
            sessionManager.logout();
            if (navigationListener != null) {
                navigationListener.onNavigate("HOME"); // Redirect to home after logout
            }
        } else {
            // Login
            if (navigationListener != null) {
                navigationListener.onNavigate("LOGIN");
            }
        }
    }
    
    /**
     * Update authentication state based on session
     */
    private void updateAuthenticationState() {
        if (sessionManager.isLoggedIn()) {
            UserDto user = sessionManager.getCurrentUser();
            
            // Update button to logout with door emoji and red styling (similar to sidebar)
            authButton.setText("🚪 Logout");
            authButton.setStyle(
                "-fx-background-color: #2c2c2c;" +
                "-fx-text-fill: #ff6b6b;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: normal;" +
                "-fx-padding: 8 15;" +
                "-fx-background-radius: 5px;" +
                "-fx-border-radius: 5px;" +
                "-fx-border-color: #404040;" +
                "-fx-border-width: 1;" +
                "-fx-cursor: hand;"
            );
            
            // Add hover effects similar to sidebar
            authButton.setOnMouseEntered(e -> 
                authButton.setStyle(
                    "-fx-background-color: #ff6b6b;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 8 15;" +
                    "-fx-background-radius: 5px;" +
                    "-fx-border-radius: 5px;" +
                    "-fx-border-color: #ff6b6b;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;"
                )
            );
            
            authButton.setOnMouseExited(e -> 
                authButton.setStyle(
                    "-fx-background-color: #2c2c2c;" +
                    "-fx-text-fill: #ff6b6b;" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: normal;" +
                    "-fx-padding: 8 15;" +
                    "-fx-background-radius: 5px;" +
                    "-fx-border-radius: 5px;" +
                    "-fx-border-color: #404040;" +
                    "-fx-border-width: 1;" +
                    "-fx-cursor: hand;"
                )
            );
            
            // Show welcome message
            userWelcome.setText("Welcome, " + user.getNamaLengkap() + " (" + user.getRole().getDisplayName() + ")");
            userWelcome.setVisible(true);
            
        } else {
            // Update button to login (original golden style)
            authButton.setText("Login");
            authButton.setStyle(
                "-fx-background-color: #e6d28e;" +
                "-fx-text-fill: #000000;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 15;" +
                "-fx-background-radius: 5px;" +
                "-fx-cursor: hand;"
            );
            
            // Remove hover effects for login button (keep it simple)
            authButton.setOnMouseEntered(e -> {});
            authButton.setOnMouseExited(e -> {});
            
            // Hide welcome message
            userWelcome.setVisible(false);
        }
    }
    
    /**
     * Session change listener implementation
     */
    @Override
    public void onSessionChanged(UserDto user) {
        // Update UI when session changes
        updateAuthenticationState();
        System.out.println("🔄 NavigationBar updated for session change: " + 
                          (user != null ? user.getUsername() : "logged out"));
    }
    
    /**
     * Create a navigation button
     */
    private Button createNavButton(String text, String destination) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 5 10;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> {
            if (!destination.equals(activeDestination)) {
                button.setStyle(
                    "-fx-background-color: #333333;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 5 10;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!destination.equals(activeDestination)) {
                button.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 5 10;" +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        button.setOnAction(e -> {
            if (navigationListener != null) {
                navigationListener.onNavigate(destination);
            }
            setActiveDestination(destination);
        });
        
        return button;
    }
    
    /**
     * Set active destination and update button styles
     */
    public void setActiveDestination(String destination) {
        this.activeDestination = destination;
        
        // Update button styles based on active destination
        for (int i = 0; i < getChildren().size(); i++) {
            if (getChildren().get(i) instanceof Button) {
                Button button = (Button) getChildren().get(i);
                String buttonText = button.getText();
                
                if ((buttonText.equals("Home") && destination.equals("HOME")) ||
                    (buttonText.equals("About Us") && destination.equals("ABOUT")) ||
                    (buttonText.equals("Event") && destination.equals("EVENT")) ||
                    (buttonText.equals("Collection") && destination.equals("COLLECTION")) ||
                    (buttonText.equals("Contact") && destination.equals("CONTACT"))) {
                    
                    button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #e6d28e;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 5 10;" +
                        "-fx-border-width: 0 0 2 0;" +
                        "-fx-border-color: #e6d28e;" +
                        "-fx-cursor: hand;"
                    );
                } else if (!buttonText.equals("Login") && !buttonText.contains("Logout")) {
                    button.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 5 10;" +
                        "-fx-cursor: hand;"
                    );
                }
            }
        }
    }
    
    /**
     * Set the navigation listener
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
     * Interface for navigation listener
     */
    public interface NavigationListener {
        void onNavigate(String destination);
    }
}