package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// CORRECTED IMPORTS - use main.* instead of just class names
import main.controller.ManajemenArtefakController;
import main.controller.PameranController;
import main.model.dto.UserDto;
import main.repository.impl.ArtefakRepositoryImpl;
import main.repository.impl.PameranRepositoryImpl;
import main.service.impl.ArtefakServiceImpl;
import main.service.impl.PameranServiceImpl;
import main.ui.components.common.Footer;
import main.ui.components.common.NavigationBar;
import main.ui.components.common.SideBar;
import main.ui.views.auth.LoginPage;
import main.ui.views.auth.RegisterPage;
import main.ui.views.curator.ArtefakListPage;
import main.ui.views.curator.EventArtefakPage;
import main.ui.views.curator.FeedbackListPage;
import main.ui.views.customer.AboutUsPage;
import main.ui.views.customer.CollectionPage;
import main.ui.views.customer.EventPage; // ADD THIS IMPORT
import main.ui.views.customer.HomePage;
import main.ui.views.customer.YourOrdersPage;
import main.utils.SessionManager;

public class Main extends Application implements NavigationBar.NavigationListener, SideBar.NavigationListener,
        LoginPage.LoginListener, RegisterPage.RegisterListener,
        YourOrdersPage.OrderNavigationListener,
        EventPage.EventNavigationListener {

    private Stage primaryStage;
    private BorderPane mainLayout;
    private SideBar sideBar;
    private NavigationBar navigationBar;
    private String currentUserRole = "GUEST";
    private UserDto currentUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;
            primaryStage.setTitle("ME-SEUM - Museum Nusantara Management System");

            // Create main layout
            mainLayout = new BorderPane();

            // Create sidebar with navigation listener for curator/admin view
            sideBar = new SideBar(this);

            // Create navigation bar with navigation listener for customer view
            navigationBar = new NavigationBar(this);

            // Set default view - show home page
            showHomePage();

            // Setup scene
            Scene scene = new Scene(mainLayout, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigation listener for NavigationBar (public user interface)
    @Override
    public void onNavigate(String destination) {
        System.out.println("Navigating to: " + destination);

        switch (destination) {
            case "HOME":
                showHomePage();
                break;
            case "ABOUT":
                showAboutPage();
                break;
            case "COLLECTION":
                showCollectionPage();
                break;
            case "LOGIN":
                showLoginPage();
                break;
            case "EVENT":
                showEventPage();
                break;
            case "CONTACT":
                // FIXED: Actually trigger the scroll to footer functionality
                scrollToFooterOnCurrentPage();
                break;
            case "LOGOUT":
                // UPDATED: Always redirect to login after logout
                handleLogout();
                break;
            default:
                showHomePage();
                break;
        }
    }

    /**
     * Scroll to footer on the currently displayed page
     */
    private void scrollToFooterOnCurrentPage() {
        // Get the current center component
        javafx.scene.Node currentNode = mainLayout.getCenter();

        if (currentNode instanceof HomePage) {
            ((HomePage) currentNode).scrollToFooter();
        } else if (currentNode instanceof AboutUsPage) {
            ((AboutUsPage) currentNode).scrollToFooter();
        } else if (currentNode instanceof CollectionPage) {
            ((CollectionPage) currentNode).scrollToFooter();
        } else if (currentNode instanceof EventPage) {
            // ADD THIS: Handle EventPage scrolling
            ((EventPage) currentNode).scrollToFooter();
        } else if (currentNode instanceof BorderPane) {
            // For other pages, try to find ScrollPane and scroll to bottom
            findAndScrollToBottom((BorderPane) currentNode);
        } else if (currentNode instanceof VBox) {
            // For VBox pages like EventPage, try to find ScrollPane
            findAndScrollToBottomInVBox((VBox) currentNode);
        }

        System.out.println("📍 Scrolled to footer on current page");
    }

    /**
     * Helper method to find ScrollPane and scroll to bottom
     */
    private void findAndScrollToBottom(BorderPane page) {
        if (page.getCenter() instanceof ScrollPane) {
            ScrollPane scrollPane = (ScrollPane) page.getCenter();
            scrollPane.setVvalue(1.0);
        }
    }

    /**
     * Helper method to find ScrollPane and scroll to bottom
     */
    private void findAndScrollToBottomInVBox(VBox page) {
        // Look for ScrollPane in the VBox children
        for (javafx.scene.Node child : page.getChildren()) {
            if (child instanceof ScrollPane) {
                ScrollPane scrollPane = (ScrollPane) child;
                scrollPane.setVvalue(1.0);
                break;
            }
        }
    }

    /**
     * Handle logout from any component
     */
    private void handleLogout() {
        // Logout is already handled by SessionManager in the sidebar
        // Just redirect to home page
        showLoginPage();
        System.out.println("🔓 User logged out - redirected to home");
    }

    /**
     * Show navigation bar for guest and customer pages only
     */
    private void showNavigationBar() {
        mainLayout.setTop(navigationBar);
    }

    /**
     * Hide navigation bar for login/register/curator pages
     */
    private void hideNavigationBar() {
        mainLayout.setTop(null);
    }

    // Show home page
    private void showHomePage() {
        try {
            // Show navigation bar for guest/customer pages
            showNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            // Create HomePage WITHOUT navigation listener (FIXED - remove parameter)
            HomePage homePage = new HomePage();
            mainLayout.setCenter(homePage);

            // Update active menu
            navigationBar.setActiveDestination("HOME");

            System.out.println("✅ Home Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Home Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show about page
    private void showAboutPage() {
        try {
            // Show navigation bar for guest/customer pages
            showNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            // Create AboutUsPage with external navigation listener (like HomePage)
            AboutUsPage aboutPage = new AboutUsPage(this);
            mainLayout.setCenter(aboutPage);

            // Update active menu
            navigationBar.setActiveDestination("ABOUT");

            System.out.println("✅ About Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading About Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show event page (EventPage)
    private void showEventPage() {
        try {
            // Show navigation bar for guest/customer pages
            showNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            EventPage eventPage = new EventPage(this, this);
            mainLayout.setCenter(eventPage);

            // Update active menu
            navigationBar.setActiveDestination("EVENT");

            System.out.println("✅ Event Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Event Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show Your Orders page
    private void showYourOrdersPage() {
        try {
            showNavigationBar();
            mainLayout.setLeft(null);

            YourOrdersPage yourOrdersPage = new YourOrdersPage(this, this);
            mainLayout.setCenter(yourOrdersPage);

            navigationBar.setActiveDestination("EVENT");
            System.out.println("✅ Your Orders Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Your Orders Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Implementation of EventPage.EventNavigationListener
    @Override
    public void onEventDetails(String eventId) {
        System.out.println("Showing details for event: " + eventId);
        showEventDetailsPage(eventId);
    }

    @Override
    public void onYourOrdersClicked() {
        System.out.println("Your Orders clicked - showing orders");
        showYourOrdersPage();
    }

    // Implementation of YourOrdersPage.OrderNavigationListener
    @Override
    public void onOpenTicket(String orderId) {
        System.out.println("Opening ticket for order: " + orderId);
        showTicketDetailsPage(orderId);
    }

    @Override
    public void onListEventClicked() {
        System.out.println("List Event clicked - showing event list");
        showEventPage(); // Navigate to EventPage
    }

    // Show public user collection page
    private void showCollectionPage() {
        try {
            // Show navigation bar for guest/customer pages
            showNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            // Create the controller with dependencies
            ArtefakRepositoryImpl repository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl service = new ArtefakServiceImpl(repository);
            ManajemenArtefakController controller = new ManajemenArtefakController(service);

            // Create the CollectionPage with this class as navigation listener
            CollectionPage collectionPage = new CollectionPage(controller, this);
            mainLayout.setCenter(collectionPage);

            // Update active menu
            navigationBar.setActiveDestination("COLLECTION");

            System.out.println("✅ Collection Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Collection Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show login page - NO NAVIGATION BAR
    private void showLoginPage() {
        try {
            // Hide navigation bar for auth pages
            hideNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            LoginPage loginPage = new LoginPage(this);
            mainLayout.setCenter(loginPage);

            System.out.println("✅ Login Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Login Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show register page - NO NAVIGATION BAR
    private void showRegisterPage() {
        try {
            // Hide navigation bar for auth pages
            hideNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            RegisterPage registerPage = new RegisterPage(this);
            mainLayout.setCenter(registerPage);

            System.out.println("✅ Register Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Register Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show event details page
    private void showEventDetailsPage(String eventId) {
        try {
            showNavigationBar();
            mainLayout.setLeft(null);

            VBox content = createSimpleContentPage("Event Details",
                    "Details for event: " + eventId + "\n\n" +
                            "Event: Matahari Terbit dari Barat\n" +
                            "Date: Apr 21 - 24\n" +
                            "Time: 9 a.m. - 6 p.m.\n" +
                            "Location: Museum Nusantara\n" +
                            "Description: Beautiful sunrise exhibition...");
            mainLayout.setCenter(content);
            System.out.println("✅ Event Details Page loaded for event: " + eventId);

        } catch (Exception e) {
            System.err.println("Error loading Event Details Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show ticket details page
    private void showTicketDetailsPage(String orderId) {
        try {
            showNavigationBar();
            mainLayout.setLeft(null);

            VBox content = createSimpleContentPage("Ticket Details",
                    "Ticket details for order: " + orderId + "\n\n" +
                            "Order ID: " + orderId + "\n" +
                            "Event: Matahari Terbit dari Barat\n" +
                            "Date: Apr 21 - 24\n" +
                            "Time: 9 a.m. - 6 p.m.\n" +
                            "Location: Museum Nusantara\n" +
                            "Status: Confirmed\n" +
                            "Ticket Code: TKT-" + orderId);
            mainLayout.setCenter(content);
            System.out.println("✅ Ticket Details Page loaded for order: " + orderId);

        } catch (Exception e) {
            System.err.println("Error loading Ticket Details Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Simple content page helper
    private VBox createSimpleContentPage(String title, String description) {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f5f0e6;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("System", 14));
        descLabel.setWrapText(true);

        // Add footer
        Footer footer = new Footer();

        VBox mainContent = new VBox();
        mainContent.getChildren().addAll(titleLabel, descLabel, footer);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f0e6;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        content.getChildren().add(scrollPane);
        return content;
    }

    // Login event handlers
    @Override
    public void onLoginSuccess(String role, UserDto user) {
        // Use SessionManager
        SessionManager.getInstance().login(user);

        System.out
                .println("Login successful! Role: " + role + ", User: " + (user != null ? user.getUsername() : "null"));

        switch (role) {
            case "CURATOR":
                // Curator pages use sidebar - Direct to ARTIFACT page (main dashboard)
                showArtifactPage();
                break;
            case "CUSTOMER":
                // Customer pages use navigation bar
                showHomePage();
                break;
            case "CLEANER":
                // Cleaner pages - you can decide if they need navbar or sidebar
                showCleanerDashboard();
                break;
            default:
                showHomePage();
        }
    }

    @Override
    public void onRegisterRequested() {
        showRegisterPage();
    }

    @Override
    public void onHomeRequested() {
        showHomePage();
    }

    @Override
    public void onRegisterSuccess(UserDto user) {
        System.out.println("Registration successful! User: " + user.getUsername());
        showLoginPage(); // Redirect to login after successful registration
    }

    @Override
    public void onBackToLogin() {
        showLoginPage();
    }

    public UserDto getCurrentUser() {
        return SessionManager.getInstance().getCurrentUser();
    }

    // Show cleaner dashboard
    private void showCleanerDashboard() {
        // Hide navigation bar - cleaners might use a different interface
        hideNavigationBar();
        mainLayout.setLeft(null);

        VBox cleanerContent = new VBox(20);
        cleanerContent.setPadding(new Insets(30));
        cleanerContent.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Cleaner Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        Label descLabel = new Label("Welcome to the cleaning staff dashboard");
        descLabel.setFont(Font.font("System", 14));

        cleanerContent.getChildren().addAll(titleLabel, descLabel);
        mainLayout.setCenter(cleanerContent);

        System.out.println("✅ Cleaner Dashboard loaded!");
    }

    // Curator interface methods - use sidebar, NO navigation bar
    @Override
    public void onSidebarNavigate(String destination) {
        System.out.println("🔄 Sidebar navigating to: " + destination);

        switch (destination) {
            case "ARTIFACT":
                showArtifactPage(); // This is now the main curator dashboard
                break;
            case "EVENT_ARTIFACT":
                showEventArtifactPage(); // For events management
                break;
            case "MAINTENANCE":
                showMaintenancePage();
                break;
            case "FEEDBACK":
                showFeedbackPage();
                break;
            case "LOGIN":
                // FIXED: Handle logout redirect to login from sidebar
                System.out.println("🔓 Curator logged out - redirecting to login page");
                showLoginPage();
                break;
            case "LOGOUT":
                // ALSO HANDLE: In case LOGOUT is used instead of LOGIN
                System.out.println("🔓 Curator logged out via LOGOUT - redirecting to login page");
                showLoginPage();
                break;
            default:
                showArtifactPage(); // Default to artifact page
                break;
        }
    }

    /**
     * Show artifact page - this is now the main curator dashboard with artifact
     * list
     */
    private void showArtifactPage() {
        // Hide navigation bar for curator pages
        hideNavigationBar();

        // Update active menu item
        sideBar.setActiveMenuByDestination("ARTIFACT");

        try {
            // Create the controller with dependencies
            ArtefakRepositoryImpl repository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl service = new ArtefakServiceImpl(repository);
            ManajemenArtefakController controller = new ManajemenArtefakController(service);

            // Create the ArtefakListPage
            ArtefakListPage artefakListPage = new ArtefakListPage(controller);

            // Set sidebar as left content and ArtefakListPage as center
            mainLayout.setLeft(sideBar);
            mainLayout.setCenter(artefakListPage);

            System.out.println("✅ Artifact Management (Curator Dashboard) loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Artifact Management: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show event artifact page - for managing events and exhibitions
     */
    private void showEventArtifactPage() {
        // Hide navigation bar for curator pages
        hideNavigationBar();

        // Update active menu item
        sideBar.setActiveMenuByDestination("EVENT_ARTIFACT");

        try {
            // Create the Pameran dependencies
            PameranRepositoryImpl pameranRepository = new PameranRepositoryImpl();
            PameranServiceImpl pameranService = new PameranServiceImpl(pameranRepository);

            // Create the Artefak dependencies
            ArtefakRepositoryImpl artefakRepository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl artefakService = new ArtefakServiceImpl(artefakRepository);

            // Create the PameranController with BOTH services
            PameranController pameranController = new PameranController(pameranService, artefakService);

            // Create the EventArtefakPage
            EventArtefakPage eventArtefakPage = new EventArtefakPage(pameranController);

            // Set sidebar as left content and EventArtefakPage as center
            mainLayout.setLeft(sideBar);
            mainLayout.setCenter(eventArtefakPage);

            System.out.println("✅ Event Artifact Management loaded with artifact name lookup!");

        } catch (Exception e) {
            System.err.println("Error loading Event Artifact Management: " + e.getMessage());
            e.printStackTrace();

            // Fallback content if there's an error
            VBox content = new VBox(20);
            content.setPadding(new Insets(30));
            content.setStyle("-fx-background-color: white;");

            Label titleLabel = new Label("Event Artifact Management");
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
            titleLabel.setTextFill(Color.web("#2c3e50"));

            Label errorLabel = new Label("❌ Error loading event management: " + e.getMessage());
            errorLabel.setFont(Font.font("System", 16));
            errorLabel.setTextFill(Color.web("#dc3545"));
            errorLabel.setPadding(new Insets(20, 0, 0, 0));

            content.getChildren().addAll(titleLabel, errorLabel);

            mainLayout.setLeft(sideBar);
            mainLayout.setCenter(content);
        }
    }

    private void showMaintenancePage() {
        // Hide navigation bar for curator pages
        hideNavigationBar();

        // Update active menu item
        sideBar.setActiveMenuByDestination("MAINTANANCE");

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Maintenance Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label descLabel = new Label("Schedule and track artifact maintenance activities");
        descLabel.setFont(Font.font("System", 14));
        descLabel.setTextFill(Color.web("#6c757d"));

        Label placeholderLabel = new Label("🔧 Maintenance features will be implemented here");
        placeholderLabel.setFont(Font.font("System", 16));
        placeholderLabel.setTextFill(Color.web("#28a745"));
        placeholderLabel.setPadding(new Insets(20, 0, 0, 0));

        content.getChildren().addAll(titleLabel, descLabel, placeholderLabel);

        mainLayout.setLeft(sideBar);
        mainLayout.setCenter(content);

        System.out.println("✅ Maintenance Page loaded!");
    }

    private void showFeedbackPage() {
        // Hide navigation bar for curator pages
        hideNavigationBar();

        // Update active menu item
        sideBar.setActiveMenuByDestination("FEEDBACK");

        // Create the FeedbackListPage
        FeedbackListPage feedbackListPage = new FeedbackListPage();

        // Set sidebar as left content and FeedbackListPage as center
        mainLayout.setLeft(sideBar);
        mainLayout.setCenter(feedbackListPage);

        System.out.println("✅ Feedback List Page loaded!");
    }
}