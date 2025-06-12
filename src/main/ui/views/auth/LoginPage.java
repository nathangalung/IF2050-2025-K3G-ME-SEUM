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
 * Login page for ME-SEUM application
 */
public class LoginPage extends BorderPane {
    
    private NavigationBar navigationBar;
    private TextField emailField;
    private PasswordField passwordField;
    private CheckBox rememberMeCheckBox;
    private Button signInButton;
    private Label signUpLabel;
    private LoginListener loginListener;
    
    // Add AuthController for database integration
    private AuthController authController;
    
    /**
     * Constructor for LoginPage
     */
    public LoginPage() {
        this.authController = new AuthController();
        initComponents();
        setupLayout();
    }
    
    /**
     * Constructor with login listener
     */
    public LoginPage(LoginListener listener) {
        this.loginListener = listener;
        this.authController = new AuthController();
        initComponents();
        setupLayout();
    }
    
    /**
     * Initialize components
     */
    private void initComponents() {
        navigationBar = new NavigationBar();
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setPrefWidth(350);
        emailField.setPrefHeight(45);
        emailField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #666666;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: #999999;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 0;"
        );
        
        passwordField = new PasswordField();
        passwordField.setPromptText("••••••••••");
        passwordField.setPrefWidth(350);
        passwordField.setPrefHeight(45);
        passwordField.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: #666666;" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-text-fill: white;" +
            "-fx-prompt-text-fill: #999999;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 0;"
        );
        
        rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setTextFill(Color.web("#999999"));
        rememberMeCheckBox.setFont(Font.font("System", 12));
        
        signInButton = new Button("Sign in");
        signInButton.setPrefWidth(350);
        signInButton.setPrefHeight(45);
        signInButton.setStyle(
            "-fx-background-color: #6B7280;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        signUpLabel = new Label("Don't have an account? Sign up for free!");
        signUpLabel.setTextFill(Color.web("#999999"));
        signUpLabel.setFont(Font.font("System", 12));
        signUpLabel.setStyle("-fx-cursor: hand;");
        
        setupEventHandlers();
    }
    
    /**
     * Setup layout - UPDATED WITH SCROLLABLE RIGHT SECTION
     */
    private void setupLayout() {
        // Create the background with login image
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
                System.out.println("✅ Successfully loaded login image: " + loginImagePath);
            } else {
                // Fallback to gradient background
                leftSide.setStyle("-fx-background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);");
                System.out.println("⚠️ Login image not found: " + loginImagePath + " - using fallback background");
            }
        } catch (Exception e) {
            leftSide.setStyle("-fx-background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);");
            System.out.println("⚠️ Error loading login image - using fallback background");
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
        scrollContent.setMinHeight(800); // Ensure minimum height to fill the screen
        
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
            if (loginListener != null) {
                loginListener.onHomeRequested();
            }
        });
        
        // Add hover effect to logo
        logoBox.setOnMouseEntered(e -> logoBox.setStyle("-fx-cursor: hand; -fx-opacity: 0.8;"));
        logoBox.setOnMouseExited(e -> logoBox.setStyle("-fx-cursor: hand; -fx-opacity: 1.0;"));
        
        logoBox.getChildren().addAll(logoIcon, museumName);
        
        // Welcome title
        Label welcomeTitle = new Label("WELCOME BACK");
        welcomeTitle.setFont(Font.font("System", FontWeight.BOLD, 32));
        welcomeTitle.setTextFill(Color.web("#D4AF37"));
        
        Label welcomeSubtitle = new Label("Welcome back! Please enter your details.");
        welcomeSubtitle.setFont(Font.font("System", 14));
        welcomeSubtitle.setTextFill(Color.web("#999999"));
        
        // Email label
        Label emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        emailLabel.setTextFill(Color.web("#D4AF37"));
        
        // Password label and forgot password
        HBox passwordHeader = new HBox();
        passwordHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label passwordLabel = new Label("Password");
        passwordLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        passwordLabel.setTextFill(Color.web("#D4AF37"));
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label forgotPassword = new Label("Forgot password");
        forgotPassword.setFont(Font.font("System", 12));
        forgotPassword.setTextFill(Color.web("#D4AF37"));
        forgotPassword.setStyle("-fx-cursor: hand;");
        
        passwordHeader.getChildren().addAll(passwordLabel, spacer, forgotPassword);
        
        // Sample users info panel
        VBox sampleUsersBox = createSampleUsersInfo();
        
        // Form container with semi-transparent background
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setMaxWidth(350);
        formContainer.setStyle("-fx-background-color: rgba(0,0,0,0.3); " +
                            "-fx-padding: 30; " +
                            "-fx-background-radius: 10;");
        
        formContainer.getChildren().addAll(
            logoBox,
            new Region() {{ setPrefHeight(30); }}, // Spacer
            welcomeTitle,
            welcomeSubtitle,
            new Region() {{ setPrefHeight(20); }}, // Spacer
            emailLabel,
            emailField,
            passwordHeader,
            passwordField,
            rememberMeCheckBox,
            new Region() {{ setPrefHeight(10); }}, // Spacer
            signInButton,
            new Region() {{ setPrefHeight(20); }}, // Spacer
            signUpLabel,
            new Region() {{ setPrefHeight(30); }}, // Spacer
            sampleUsersBox
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
     * Create sample users info panel - KEEP YOUR EXISTING DESIGN
     */
    private VBox createSampleUsersInfo() {
        VBox sampleUsersBox = new VBox(10);
        sampleUsersBox.setAlignment(Pos.CENTER_LEFT);
        sampleUsersBox.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.1);" +
            "-fx-padding: 15;" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #D4AF37;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;"
        );
        sampleUsersBox.setMaxWidth(350);
        
        Label titleLabel = new Label("Database Users:");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        titleLabel.setTextFill(Color.web("#D4AF37"));
        
        // Database samples - updated to match your actual database
        Label customerTitle = new Label("👤 Customers:");
        customerTitle.setFont(Font.font("System", FontWeight.BOLD, 11));
        customerTitle.setTextFill(Color.web("#999999"));
        
        Label customer1 = new Label("• customer@email.com / password123");
        customer1.setFont(Font.font("System", 10));
        customer1.setTextFill(Color.web("#CCCCCC"));
        
        // Curator samples
        Label curatorTitle = new Label("👨‍🎓 Curators:");
        curatorTitle.setFont(Font.font("System", FontWeight.BOLD, 11));
        curatorTitle.setTextFill(Color.web("#999999"));
        
        Label curator1 = new Label("• curator@museum.id / password123");
        curator1.setFont(Font.font("System", 10));
        curator1.setTextFill(Color.web("#CCCCCC"));
        
        // Cleaner samples
        Label cleanerTitle = new Label("🧹 Cleaners:");
        cleanerTitle.setFont(Font.font("System", FontWeight.BOLD, 11));
        cleanerTitle.setTextFill(Color.web("#999999"));
        
        Label cleaner1 = new Label("• cleaner@museum.id / password123");
        cleaner1.setFont(Font.font("System", 10));
        cleaner1.setTextFill(Color.web("#CCCCCC"));
        
        sampleUsersBox.getChildren().addAll(
            titleLabel,
            customerTitle, customer1,
            curatorTitle, curator1,
            cleanerTitle, cleaner1
        );
        
        return sampleUsersBox;
    }
    
    /**
     * Setup event handlers
     */
    private void setupEventHandlers() {
        signInButton.setOnAction(e -> handleLogin());
        
        // Enter key on password field
        passwordField.setOnAction(e -> handleLogin());
        
        // Sign up label click
        signUpLabel.setOnMouseClicked(e -> handleRegister());
        
        // Forgot password click
        // Add if needed
    }
    
    /**
     * Handle login - UPDATED TO USE DATABASE AUTHENTICATION
     */
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        
        // Validation
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both email and password.");
            return;
        }
        
        // Show loading state
        signInButton.setText("Signing in...");
        signInButton.setDisable(true);
        
        try {
            System.out.println("🔄 Attempting login for: " + email);
            
            // Use AuthController to authenticate against database
            UserDto user = authController.login(email, password);
            
            if (user != null) {
                System.out.println("✅ Login successful for user: " + user.getUsername() + " (" + user.getRole() + ")");
                
                // Show success message
                showAlert("Success", "Welcome back, " + user.getNamaLengkap() + "!");
                
                // Notify listener with role from database
                if (loginListener != null) {
                    loginListener.onLoginSuccess(user.getRole().name(), user);
                }
                
                // Clear fields
                emailField.clear();
                passwordField.clear();
                
            } else {
                System.out.println("❌ Login failed for: " + email);
                showAlert("Error", "Invalid email or password.\n\nTry using:\n• curator@museum.id / password123\n• customer@email.com / password123");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Login failed: " + e.getMessage() + "\n\nPlease check your database connection.");
        } finally {
            // Reset button state
            signInButton.setText("Sign in");
            signInButton.setDisable(false);
        }
    }
    
    /**
     * Handle register
     */
    private void handleRegister() {
        if (loginListener != null) {
            loginListener.onRegisterRequested();
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
     * Set login listener
     */
    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }
    
    /**
     * Get current user from auth controller
     */
    public UserDto getCurrentUser() {
        return authController.getCurrentUser();
    }
    
    /**
     * Interface for login events - UPDATED SIGNATURE
     */
    public interface LoginListener {
        void onLoginSuccess(String role, UserDto user);
        void onRegisterRequested();
        void onHomeRequested(); // Keep this method from your original
    }
}