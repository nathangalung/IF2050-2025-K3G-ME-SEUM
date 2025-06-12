package main.ui.views.auth;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.ui.components.common.NavigationBar;
import main.controller.AuthController;
import main.model.dto.UserDto;
import main.model.enums.UserRole;

/**
 * Register page for ME-SEUM application
 */
public class RegisterPage extends BorderPane {
    
    private NavigationBar navigationBar;
    private TextField usernameField;
    private TextField fullNameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<UserRole> roleComboBox;
    private CheckBox agreeTermsCheckBox;
    private Button signUpButton;
    private Label signInLabel;
    private RegisterListener registerListener;
    
    // Add AuthController for database integrations
    private AuthController authController;
    
    /**
     * Constructor for RegisterPage
     */
    public RegisterPage() {
        this.authController = new AuthController();
        initComponents();
        setupLayout();
    }
    
    /**
     * Constructor with register listener
     */
    public RegisterPage(RegisterListener listener) {
        this.registerListener = listener;
        this.authController = new AuthController();
        initComponents();
        setupLayout();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        navigationBar = new NavigationBar();
        
        // Username field
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(350);
        usernameField.setPrefHeight(45);
        usernameField.setStyle(getFieldStyle());
        
        // Full name field
        fullNameField = new TextField();
        fullNameField.setPromptText("Enter your full name");
        fullNameField.setPrefWidth(350);
        fullNameField.setPrefHeight(45);
        fullNameField.setStyle(getFieldStyle());
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(350);
        emailField.setPrefHeight(45);
        emailField.setStyle(getFieldStyle());
        
        passwordField = new PasswordField();
        passwordField.setPromptText("••••••••••");
        passwordField.setPrefWidth(350);
        passwordField.setPrefHeight(45);
        passwordField.setStyle(getFieldStyle());
        
        // Confirm password field
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("••••••••••");
        confirmPasswordField.setPrefWidth(350);
        confirmPasswordField.setPrefHeight(45);
        confirmPasswordField.setStyle(getFieldStyle());
        
        // Add role selection
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(UserRole.CUSTOMER, UserRole.CURATOR, UserRole.CLEANER);
        roleComboBox.setValue(UserRole.CUSTOMER); // Default to customer
        roleComboBox.setPrefWidth(350);
        roleComboBox.setPrefHeight(45);
        roleComboBox.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #666666;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;"
        );
        
        agreeTermsCheckBox = new CheckBox("I agree to the Terms and Conditions");
        agreeTermsCheckBox.setTextFill(Color.web("#999999"));
        agreeTermsCheckBox.setFont(Font.font("System", 12));
        
        signUpButton = new Button("Sign up");
        signUpButton.setPrefWidth(350);
        signUpButton.setPrefHeight(45);
        signUpButton.setStyle(
            "-fx-background-color: #6B7280;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        signInLabel = new Label("Already have an account? Sign in now!");
        signInLabel.setTextFill(Color.web("#999999"));
        signInLabel.setFont(Font.font("System", 12));
        signInLabel.setStyle("-fx-cursor: hand;");
        
        setupEventHandlers();
    }
    
    /**
     * Helper method for field styling
     */
    private String getFieldStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-border-color: #666666;" +
               "-fx-border-width: 0 0 1 0;" +
               "-fx-text-fill: white;" +
               "-fx-prompt-text-fill: #999999;" +
               "-fx-font-size: 14px;" +
               "-fx-padding: 10 0;";
    }
    
    /**
     * Setup layout - UPDATED WITH SCROLLABLE RIGHT SECTION
     */
    private void setupLayout() {
        // Create the background with login image (same as login page)
        HBox mainContainer = new HBox();
        mainContainer.setPrefSize(1200, 800);
        
        // Left side - Login background image
        Region leftSide = new Region();
        leftSide.setPrefWidth(600);
        
        try {
            String loginImagePath = "/img/login.png";
            if (getClass().getResourceAsStream(loginImagePath) != null) {
                leftSide.setStyle(
                    "-fx-background-image: url('" + loginImagePath + "');" +
                    "-fx-background-size: cover;" +
                    "-fx-background-position: center;"
                );
                System.out.println("✅ Successfully loaded login image for register: " + loginImagePath);
            } else {
                // Fallback to gradient background
                leftSide.setStyle("-fx-background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);");
                System.out.println("⚠️ Login image not found for register: " + loginImagePath + " - using fallback background");
            }
        } catch (Exception e) {
            leftSide.setStyle("-fx-background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);");
            System.out.println("⚠️ Error loading login image for register - using fallback background");
        }
        
        // Right side - MAKE THIS SCROLLABLE
        ScrollPane rightScrollPane = new ScrollPane();
        rightScrollPane.setPrefWidth(600);
        rightScrollPane.setStyle("-fx-background-color: rgba(31, 31, 31, 0.95);");
        rightScrollPane.setFitToWidth(true);
        rightScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        rightScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // Hide scrollbar background to maintain dark theme
        rightScrollPane.setStyle(
            "-fx-background: rgba(31, 31, 31, 0.95);" +
            "-fx-background-color: rgba(31, 31, 31, 0.95);"
        );
        
        // Content container for the scroll pane
        VBox scrollContent = new VBox();
        scrollContent.setAlignment(Pos.CENTER);
        scrollContent.setPadding(new Insets(60, 20, 60, 20)); // Added horizontal padding for better spacing
        scrollContent.setStyle("-fx-background-color: rgba(31, 31, 31, 0.95);");
        scrollContent.setMinHeight(900); // Increased minimum height for registration form
        
        // Museum logo and name - make it clickable
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setStyle("-fx-cursor: hand;");
        
        Label logoIcon = new Label("🏛️");
        logoIcon.setFont(Font.font("System", 24));
        logoIcon.setTextFill(Color.web("#D4AF37"));
        
        Label museumName = new Label("Museum\nNusantara");
        museumName.setFont(Font.font("System", FontWeight.BOLD, 16));
        museumName.setTextFill(Color.web("#D4AF37"));
        museumName.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        
        // Add click handler to logo
        logoBox.setOnMouseClicked(e -> {
            if (registerListener != null) {
                registerListener.onHomeRequested();
            }
        });
        
        // Add hover effect to logo
        logoBox.setOnMouseEntered(e -> logoBox.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        logoBox.setOnMouseExited(e -> logoBox.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));
        
        logoBox.getChildren().addAll(logoIcon, museumName);
        
        // Welcome title
        Label joinTitle = new Label("JOIN US");
        joinTitle.setFont(Font.font("System", FontWeight.BOLD, 32));
        joinTitle.setTextFill(Color.web("#D4AF37"));
        
        Label joinSubtitle = new Label("A present from the past");
        joinSubtitle.setFont(Font.font("System", 14));
        joinSubtitle.setTextFill(Color.web("#999999"));
        
        // Form labels
        Label usernameLabel = createLabel("Username");
        Label fullNameLabel = createLabel("Full Name");
        Label emailLabel = createLabel("Email");
        Label passwordLabel = createLabel("Password");
        Label confirmPasswordLabel = createLabel("Confirm Password");
        Label roleLabel = createLabel("Role");
        
        // Form container with semi-transparent background
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(350);
        formContainer.setStyle("-fx-background-color: rgba(0,0,0,0.3); " +
                            "-fx-padding: 30; " +
                            "-fx-background-radius: 10;");
        
        formContainer.getChildren().addAll(
            logoBox,
            new Region() {{ setPrefHeight(20); }}, // Spacer
            joinTitle,
            joinSubtitle,
            new Region() {{ setPrefHeight(15); }}, // Spacer
            usernameLabel,
            usernameField,
            fullNameLabel,
            fullNameField,
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            confirmPasswordLabel,
            confirmPasswordField,
            roleLabel,
            roleComboBox,
            agreeTermsCheckBox,
            new Region() {{ setPrefHeight(10); }}, // Spacer
            signUpButton,
            new Region() {{ setPrefHeight(15); }}, // Spacer
            signInLabel
        );
        
        // Add form container to scroll content
        scrollContent.getChildren().add(formContainer);
        
        // Set scroll content to scroll pane
        rightScrollPane.setContent(scrollContent);
        
        // Add both sides to main container
        mainContainer.getChildren().addAll(leftSide, rightScrollPane);
        
        setCenter(mainContainer);
    }
    
    /**
     * Helper method to create labels
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#D4AF37"));
        return label;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        signUpButton.setOnAction(e -> handleRegister());
        
        // Enter key on confirm password field
        confirmPasswordField.setOnAction(e -> handleRegister());
        
        // Sign in label click
        signInLabel.setOnMouseClicked(e -> handleBackToLogin());
    }
    
    /**
     * Handle register - UPDATED TO USE DATABASE REGISTRATION
     */
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        UserRole role = roleComboBox.getValue();
        
        // Validation
        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }
        
        if (password.length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long.");
            return;
        }
        
        if (!agreeTermsCheckBox.isSelected()) {
            showAlert("Error", "You must agree to the Terms and Conditions.");
            return;
        }
        
        // Email format validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }
        
        // Show loading state
        signUpButton.setText("Creating account...");
        signUpButton.setDisable(true);
        
        try {
            System.out.println("🔄 Attempting registration for: " + username + " (" + email + ")");
            
            // Use AuthController to register in database
            UserDto user = authController.register(username, email, password, fullName, role);
            
            if (user != null) {
                System.out.println("✅ Registration successful for user: " + user.getUsername());
                
                // Show success message
                showAlert("Success", 
                    "Account created successfully!\n\n" +
                    "Username: " + user.getUsername() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "Role: " + user.getRole().name() + "\n\n" +
                    "You can now login with your credentials.");
                
                // Clear fields
                clearForm();
                
                // Notify listener
                if (registerListener != null) {
                    registerListener.onRegisterSuccess(user);
                }
                
            } else {
                showAlert("Error", "Registration failed. Please try again.");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Email already exists")) {
                showAlert("Error", "This email is already registered. Please use a different email or try logging in.");
            } else if (errorMessage.contains("Username already exists")) {
                showAlert("Error", "This username is already taken. Please choose a different username.");
            } else {
                showAlert("Error", "Registration failed: " + errorMessage + "\n\nPlease check your database connection.");
            }
        } finally {
            // Reset button state
            signUpButton.setText("Sign up");
            signUpButton.setDisable(false);
        }
    }
    
    /**
     * Clear the form
     */
    private void clearForm() {
        usernameField.clear();
        fullNameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        roleComboBox.setValue(UserRole.CUSTOMER);
        agreeTermsCheckBox.setSelected(false);
    }
    
    /**
     * Handle back to login
     */
    private void handleBackToLogin() {
        if (registerListener != null) {
            registerListener.onBackToLogin();
        }
    }
    
    /**
     * Show alert
     */
    private void showAlert(String title, String message) {
        Alert.AlertType type = title.equals("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Set register listener
     */
    public void setRegisterListener(RegisterListener listener) {
        this.registerListener = listener;
    }
    
    /**
     * Interface for register events
     */
    public interface RegisterListener {
        void onRegisterSuccess(UserDto user);
        void onBackToLogin();
        void onHomeRequested();
    }
}