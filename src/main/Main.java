package main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// CORRECTED IMPORTS - use main.* instead of just class names
import main.controller.ManajemenArtefakController;
import main.controller.PameranController;
import main.model.dto.UserDto;
import main.model.dto.ArtefakDto;
import main.model.dto.PameranDto;
import main.model.enums.UserRole;
import main.repository.impl.ArtefakRepositoryImpl;
import main.repository.impl.PameranRepositoryImpl;
import main.service.impl.ArtefakServiceImpl;
import main.service.impl.PameranServiceImpl;
import main.controller.PemeliharaanController;
import main.repository.impl.PemeliharaanRepositoryImpl;
import main.service.impl.PemeliharaanServiceImpl;
import main.ui.views.cleaner.MaintenanceTaskPage;
import main.ui.views.curator.MaintenanceListPage;
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
import main.ui.views.customer.EventPage;
import main.ui.views.customer.HomePage;
import main.ui.views.customer.YourOrdersPage;
import main.utils.SessionManager;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    // ADD THESE FIELDS for order management
    private static List<OrderInfo> customerOrders = new ArrayList<>();

    // Order info class
    public static class OrderInfo {
        private String orderId;
        private String eventName;
        private String eventId;
        private String dateRange;
        private String timeRange;
        private String location;
        private String status;

        public OrderInfo(String orderId, String eventName, String eventId, String dateRange,
                String timeRange, String location, String status) {
            this.orderId = orderId;
            this.eventName = eventName;
            this.eventId = eventId;
            this.dateRange = dateRange;
            this.timeRange = timeRange;
            this.location = location;
            this.status = status;
        }

        // Getters
        public String getOrderId() {
            return orderId;
        }

        public String getEventName() {
            return eventName;
        }

        public String getEventId() {
            return eventId;
        }

        public String getDateRange() {
            return dateRange;
        }

        public String getTimeRange() {
            return timeRange;
        }

        public String getLocation() {
            return location;
        }

        public String getStatus() {
            return status;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

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
        try {
            currentUser = null;
            currentUserRole = "GUEST";

            showHomePage();

            Alert logoutAlert = new Alert(Alert.AlertType.INFORMATION);
            logoutAlert.setTitle("Logout");
            logoutAlert.setHeaderText("Logout Successful");
            logoutAlert.setContentText("You have been logged out successfully.");
            logoutAlert.showAndWait();

            System.out.println("✅ User logged out successfully");

        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
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

            // Create controllers for EventPage
            PameranRepositoryImpl pameranRepository = new PameranRepositoryImpl();
            PameranServiceImpl pameranService = new PameranServiceImpl(pameranRepository);
            ArtefakRepositoryImpl artefakRepository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl artefakService = new ArtefakServiceImpl(artefakRepository);
            PameranController pameranController = new PameranController(pameranService, artefakService);

            // Create EventPage with correct parameters:
            // 1. NavigationListener (this)
            // 2. EventNavigationListener (this)
            // 3. PameranController
            EventPage eventPage = new EventPage(this, new EventPage.EventNavigationListener() {

                public void onEventDetails(String eventId) {
                    showEventDetailsPage(eventId);
                }

                public void onYourOrdersClicked() {
                    showYourOrdersPage();
                }
            }, pameranController);

            mainLayout.setCenter(eventPage);

            // Update active menu
            navigationBar.setActiveDestination("EVENT");

            System.out.println("✅ Event Page loaded with database events!");

        } catch (Exception e) {
            System.err.println("Error loading Event Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Show Your Orders page
    private void showYourOrdersPage() {
        try {
            // Show navigation bar for guest/customer pages
            showNavigationBar();
            // Clear sidebar if present
            mainLayout.setLeft(null);

            // Create YourOrdersPage with navigation listeners
            YourOrdersPage yourOrdersPage = new YourOrdersPage(this, this);

            // Refresh orders to show latest purchases
            yourOrdersPage.refreshOrders();

            mainLayout.setCenter(yourOrdersPage);

            // Update active menu
            navigationBar.setActiveDestination("EVENT");

            System.out.println("✅ Your Orders Page loaded with " + customerOrders.size() + " orders!");

        } catch (Exception e) {
            System.err.println("Error loading Your Orders Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Implementation of EventPage.EventNavigationListener

    public void onEventDetails(String eventId) {
        System.out.println("Showing details for event: " + eventId);
        showEventDetailsPage(eventId);
    }

    public void onYourOrdersClicked() {
        System.out.println("Your Orders clicked - showing orders");
        showYourOrdersPage();
    }

    // Implementation of YourOrdersPage.OrderNavigationListener

    public void onOpenTicket(String orderId) {
        System.out.println("Opening ticket for order: " + orderId);
        showTicketDetailsPage(orderId);
    }

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

    // Show event details page with artifacts and buy ticket functionality
    private void showEventDetailsPage(String eventId) {
        try {
            showNavigationBar();
            mainLayout.setLeft(null);

            // Get actual event details from database
            PameranRepositoryImpl pameranRepository = new PameranRepositoryImpl();
            PameranServiceImpl pameranService = new PameranServiceImpl(pameranRepository);
            ArtefakRepositoryImpl artefakRepository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl artefakService = new ArtefakServiceImpl(artefakRepository);
            PameranController pameranController = new PameranController(pameranService, artefakService);
            ManajemenArtefakController artefakController = new ManajemenArtefakController(artefakService);

            Long pameranId = Long.parseLong(eventId);
            PameranDto event = pameranController.getPameranById(pameranId);

            if (event != null) {
                // Create detailed event page with artifacts
                VBox eventDetailsPage = createDetailedEventPage(event, pameranController, artefakController);
                mainLayout.setCenter(eventDetailsPage);
                System.out.println("✅ Detailed Event Page loaded for: " + event.getNamaPameran());
            } else {
                VBox content = createSimpleContentPage("Event Details", "Event not found with ID: " + eventId);
                mainLayout.setCenter(content);
            }

        } catch (Exception e) {
            System.err.println("Error loading Event Details Page: " + e.getMessage());
            e.printStackTrace();

            VBox content = createSimpleContentPage("Event Details", "Error loading event details: " + e.getMessage());
            mainLayout.setCenter(content);
        }
    }

    // Create detailed event page with artifacts and buy ticket
    private VBox createDetailedEventPage(PameranDto event, PameranController pameranController,
            ManajemenArtefakController artefakController) {
        VBox mainContainer = new VBox();
        mainContainer.setStyle("-fx-background-color: #f5f3e8;");

        // Create scroll pane for the entire content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f3e8;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox contentContainer = new VBox(30);
        contentContainer.setPadding(new Insets(40, 50, 40, 50));
        contentContainer.setStyle("-fx-background-color: #f5f3e8;");

        // 1. Event Header Section
        VBox headerSection = createEventHeaderSection(event);

        // 2. Event Details Section
        VBox detailsSection = createEventDetailsSection(event);

        // 3. Artifacts Collection Section
        VBox artifactsSection = createArtifactsCollectionSection(event, artefakController);

        // 4. Buy Ticket Section
        VBox buyTicketSection = createBuyTicketSection(event);

        contentContainer.getChildren().addAll(headerSection, detailsSection, artifactsSection, buyTicketSection);

        scrollPane.setContent(contentContainer);
        mainContainer.getChildren().add(scrollPane);

        return mainContainer;
    }

    // Create event header section
    private VBox createEventHeaderSection(PameranDto event) {
        VBox headerSection = new VBox(15);
        headerSection.setAlignment(Pos.CENTER_LEFT);

        // Back button
        Button backButton = new Button("← Back to Events");
        backButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #007bff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;" +
                        "-fx-underline: true;");
        backButton.setOnAction(e -> showEventPage());

        // Event title
        Label titleLabel = new Label(event.getNamaPameran());
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);
        titleLabel.setWrapText(true);

        // Event dates
        String dateInfo = "Event Period: ";
        if (event.getTanggalMulai() != null && event.getTanggalSelesai() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            dateInfo += event.getTanggalMulai().format(formatter) + " - " +
                    event.getTanggalSelesai().format(formatter);
        } else {
            dateInfo += "To be announced";
        }

        Label dateLabel = new Label(dateInfo);
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        dateLabel.setTextFill(Color.web("#d4a574"));

        headerSection.getChildren().addAll(backButton, titleLabel, dateLabel);
        return headerSection;
    }

    // Create event details section
    private VBox createEventDetailsSection(PameranDto event) {
        VBox detailsSection = new VBox(20);
        detailsSection.setPadding(new Insets(0, 0, 30, 0));

        // Description
        Label descriptionLabel = new Label(
                event.getDeskripsiPameran() != null ? event.getDeskripsiPameran()
                        : "Experience a fascinating journey through Indonesia's rich cultural heritage.");
        descriptionLabel.setFont(Font.font("Arial", 16));
        descriptionLabel.setTextFill(Color.web("#333333"));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setLineSpacing(2);

        // Event info grid
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(30);
        infoGrid.setVgap(15);

        // Location - use fixed location instead of event.getLokasi()
        VBox locationBox = new VBox(5);
        Label locationTitle = new Label("📍 Location");
        locationTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        locationTitle.setTextFill(Color.web("#666666"));
        Label locationValue = new Label("Museum Nusantara");
        locationValue.setFont(Font.font("Arial", 14));
        locationValue.setTextFill(Color.BLACK);
        locationBox.getChildren().addAll(locationTitle, locationValue);

        // Time
        VBox timeBox = new VBox(5);
        Label timeTitle = new Label("🕒 Time");
        timeTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        timeTitle.setTextFill(Color.web("#666666"));
        
        String timeInfo = "TBD";
        if (event.getTanggalMulai() != null && event.getTanggalSelesai() != null) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
            String startTime = event.getTanggalMulai().format(timeFormatter);
            String endTime = event.getTanggalSelesai().format(timeFormatter);
            timeInfo = startTime + " - " + endTime;
        }
        
        Label timeValue = new Label(timeInfo);
        timeValue.setFont(Font.font("Arial", 14));
        timeValue.setTextFill(Color.BLACK);
        timeBox.getChildren().addAll(timeTitle, timeValue);

        // Date
        VBox dateBox = new VBox(5);
        Label dateTitle = new Label("📅 Date");
        dateTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        dateTitle.setTextFill(Color.web("#666666"));
        
        String dateInfo = "TBD";
        if (event.getTanggalMulai() != null && event.getTanggalSelesai() != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            String startDate = event.getTanggalMulai().format(dateFormatter);
            String endDate = event.getTanggalSelesai().format(dateFormatter);
            dateInfo = startDate + " - " + endDate;
        }
        
        Label dateValue = new Label(dateInfo);
        dateValue.setFont(Font.font("Arial", 14));
        dateValue.setTextFill(Color.BLACK);
        dateBox.getChildren().addAll(dateTitle, dateValue);

        // Add to grid
        infoGrid.add(locationBox, 0, 0);
        infoGrid.add(timeBox, 1, 0);
        infoGrid.add(dateBox, 2, 0);

        detailsSection.getChildren().addAll(descriptionLabel, infoGrid);
        return detailsSection;
    }

    // Create artifacts collection section
    private VBox createArtifactsCollectionSection(PameranDto event, ManajemenArtefakController artefakController) {
        VBox artifactsSection = new VBox(20);

        // Section title
        HBox titleBox = new HBox(15);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label sectionTitle = new Label("Artifact Collection");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        sectionTitle.setTextFill(Color.BLACK);

        // Artifact count badge
        int artifactCount = event.getArtefakIds() != null ? event.getArtefakIds().size() : 0;
        Label countBadge = new Label(artifactCount + " artifacts");
        countBadge.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        countBadge.setTextFill(Color.WHITE);
        countBadge.setPadding(new Insets(5, 12, 5, 12));
        countBadge.setStyle(
                "-fx-background-color: #d4a574;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;");

        titleBox.getChildren().addAll(sectionTitle, countBadge);

        // Artifacts grid
        TilePane artifactsGrid = new TilePane();
        artifactsGrid.setHgap(20);
        artifactsGrid.setVgap(20);
        artifactsGrid.setPrefTileWidth(280);
        artifactsGrid.setPrefTileHeight(340);
        artifactsGrid.setAlignment(Pos.CENTER_LEFT);

        // Load artifacts
        if (event.getArtefakIds() != null && !event.getArtefakIds().isEmpty()) {
            for (Long artifactId : event.getArtefakIds()) {
                try {
                    ArtefakDto artifact = artefakController.getArtefakById(artifactId);
                    if (artifact != null) {
                        VBox artifactCard = createArtifactCard(artifact);
                        artifactsGrid.getChildren().add(artifactCard);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading artifact " + artifactId + ": " + e.getMessage());
                }
            }
        }

        if (artifactsGrid.getChildren().isEmpty()) {
            Label noArtifactsLabel = new Label("No artifacts available for this event");
            noArtifactsLabel.setFont(Font.font("Arial", 16));
            noArtifactsLabel.setTextFill(Color.GRAY);
            noArtifactsLabel.setPadding(new Insets(40));
            artifactsGrid.getChildren().add(noArtifactsLabel);
        }

        // Wrap in scroll pane if many artifacts
        ScrollPane artifactsScrollPane = new ScrollPane(artifactsGrid);
        artifactsScrollPane.setFitToWidth(true);
        artifactsScrollPane.setMaxHeight(400);
        artifactsScrollPane.setStyle("-fx-background-color: transparent;");
        artifactsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        artifactsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        artifactsSection.getChildren().addAll(titleBox, artifactsScrollPane);
        return artifactsSection;
    }

    // Create individual artifact card
    private VBox createArtifactCard(ArtefakDto artifact) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(280);
        card.setPrefHeight(340);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);");

        // Image placeholder
        Label imageLabel = new Label("🏺");
        imageLabel.setFont(Font.font("System", 48));
        imageLabel.setPrefSize(180, 120);
        imageLabel.setAlignment(Pos.CENTER);
        imageLabel.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-border-color: #e9ecef;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;");

        // Artifact info
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.TOP_LEFT);

        Label nameLabel = new Label(artifact.getNamaArtefak());
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#2c3e50"));
        nameLabel.setWrapText(true);
        nameLabel.setMaxHeight(45);

        Label regionLabel = new Label("📍 " + artifact.getAsalDaerah());
        regionLabel.setFont(Font.font("Arial", 12));
        regionLabel.setTextFill(Color.web("#6c757d"));

        Label periodLabel = new Label("⏳ " + artifact.getPeriode());
        periodLabel.setFont(Font.font("Arial", 12));
        periodLabel.setTextFill(Color.web("#6c757d"));

        // Status badge
        Label statusBadge = new Label(artifact.getStatus());
        statusBadge.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        statusBadge.setTextFill(Color.WHITE);
        statusBadge.setPadding(new Insets(3, 8, 3, 8));
        statusBadge.setStyle(
                "-fx-background-color: " + getStatusColor(artifact.getStatus()) + ";" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;");

        infoBox.getChildren().addAll(nameLabel, regionLabel, periodLabel, statusBadge);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 4);" +
                        "-fx-cursor: hand;"));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 6, 0, 0, 2);"));

        card.getChildren().addAll(imageLabel, infoBox);
        return card;
    }

    // Helper method for status colors
    private String getStatusColor(String status) {
        switch (status.toUpperCase()) {
            case "TERSEDIA":
                return "#28a745";
            case "DIPAMERKAN":
                return "#17a2b8";
            case "DIPELIHARA":
                return "#ffc107";
            default:
                return "#6c757d";
        }
    }

    // Create buy ticket section
    private VBox createBuyTicketSection(PameranDto event) {
        VBox ticketSection = new VBox(20);
        ticketSection.setAlignment(Pos.CENTER);

        // Ticket card
        VBox ticketCard = new VBox(25);
        ticketCard.setAlignment(Pos.CENTER);
        ticketCard.setPadding(new Insets(40));
        ticketCard.setMaxWidth(600);
        ticketCard.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #d4a574, #b8956a);" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 12, 0, 0, 4);");

        // Ticket icon and title
        VBox ticketHeader = new VBox(10);
        ticketHeader.setAlignment(Pos.CENTER);

        Label ticketIcon = new Label("🎫");
        ticketIcon.setFont(Font.font("System", 48));

        Label ticketTitle = new Label("Get Your Ticket Now!");
        ticketTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        ticketTitle.setTextFill(Color.WHITE);

        Label ticketSubtitle = new Label("Join us for this amazing cultural experience");
        ticketSubtitle.setFont(Font.font("Arial", 16));
        ticketSubtitle.setTextFill(Color.web("#f5f5f5"));

        ticketHeader.getChildren().addAll(ticketIcon, ticketTitle, ticketSubtitle);

        // Price info
        HBox priceBox = new HBox(10);
        priceBox.setAlignment(Pos.CENTER);

        Label priceLabel = new Label("Ticket Price:");
        priceLabel.setFont(Font.font("Arial", 16));
        priceLabel.setTextFill(Color.WHITE);

        Label priceValue = new Label("FREE");
        priceValue.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        priceValue.setTextFill(Color.web("#fff3cd"));

        priceBox.getChildren().addAll(priceLabel, priceValue);

        // Buy ticket button
        Button buyTicketButton = new Button("🎫 Buy Ticket");
        buyTicketButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        buyTicketButton.setPrefWidth(250);
        buyTicketButton.setPrefHeight(50);
        buyTicketButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #d4a574;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);");

        // Button hover effects
        buyTicketButton.setOnMouseEntered(e -> buyTicketButton.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-text-fill: #d4a574;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 4);"));

        buyTicketButton.setOnMouseExited(e -> buyTicketButton.setStyle(
                "-fx-background-color: white;" +
                        "-fx-text-fill: #d4a574;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"));

        // Buy ticket action
        buyTicketButton.setOnAction(e -> {
            buyTicketForEvent(event);
        });

        ticketCard.getChildren().addAll(ticketHeader, priceBox, buyTicketButton);
        ticketSection.getChildren().add(ticketCard);

        return ticketSection;
    }

    // Handle buy ticket action - this will add the event to Your Orders
    private void buyTicketForEvent(PameranDto event) {
        try {
            // Show confirmation dialog
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Ticket Purchase");
            confirmDialog.setHeaderText("Buy Ticket for: " + event.getNamaPameran());
            confirmDialog.setContentText("Are you sure you want to get a ticket for this event?");

            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Generate order ID
                    String orderId = "ORDER_" + System.currentTimeMillis();

                    // Store the order information (you might want to implement a proper order
                    // management system)
                    addEventToYourOrders(event, orderId);

                    // Show success message
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Ticket Purchased Successfully!");
                    successAlert.setHeaderText("Your ticket has been confirmed");
                    successAlert.setContentText(
                            "Event: " + event.getNamaPameran() + "\n" +
                                    "Order ID: " + orderId + "\n" +
                                    "Status: Confirmed\n\n" +
                                    "You can view your ticket in 'Your Orders' page.");

                    successAlert.showAndWait();

                    // Navigate to Your Orders page
                    showYourOrdersPage();
                }
            });

        } catch (Exception e) {
            System.err.println("Error buying ticket: " + e.getMessage());
            e.printStackTrace();

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Failed to purchase ticket");
            errorAlert.setContentText("An error occurred while processing your ticket purchase. Please try again.");
            errorAlert.showAndWait();
        }
    }

    // Store the order information properly
    private void addEventToYourOrders(PameranDto event, String orderId) {
        try {
            // Format dates for display
            String dateRange = "TBD";
            String timeRange = "TBD";

            if (event.getTanggalMulai() != null && event.getTanggalSelesai() != null) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h a");

                String startDate = event.getTanggalMulai().format(dateFormatter);
                String endDate = event.getTanggalSelesai().format(dateFormatter);
                dateRange = startDate + " - " + endDate;

                String startTime = event.getTanggalMulai().format(timeFormatter);
                String endTime = event.getTanggalSelesai().format(timeFormatter);
                timeRange = startTime + " - " + endTime;
            }

            // Create order info
            OrderInfo orderInfo = new OrderInfo(
                    orderId,
                    event.getNamaPameran(),
                    String.valueOf(event.getPameranId()),
                    dateRange,
                    timeRange,
                    "Museum Nusantara",
                    "Confirmed");

            // Add to orders list
            customerOrders.add(orderInfo);

            System.out.println("✅ Order stored successfully:");
            System.out.println("   Order ID: " + orderId);
            System.out.println("   Event: " + event.getNamaPameran());
            System.out.println("   Total orders: " + customerOrders.size());

        } catch (Exception e) {
            System.err.println("❌ Error storing order: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add method to get all orders
    public static List<OrderInfo> getAllOrders() {
        return new ArrayList<>(customerOrders);
    }

    // Add these methods to your Main class

    // Missing onSidebarNavigate method implementation

    public void onSidebarNavigate(String destination) {
        System.out.println("Sidebar navigation to: " + destination);
        switch (destination) {
            case "DASHBOARD":
                showArtefakListPage(); // CHANGED: use artifact page as dashboard
                break;
            case "ARTIFACT": // FIXED: changed from "ARTEFAK_LIST" to "ARTIFACT"
                showArtefakListPage();
                break;
            case "EVENT_ARTIFACT":
                showEventArtifactPage();
                break;
            case "MAINTENANCE": // FIXED: changed from "FEEDBACK_LIST" to "MAINTENANCE"
                showMaintenancePage();
                break;
            case "FEEDBACK": // FIXED: added FEEDBACK case
                showFeedbackListPage();
                break;
            case "LOGIN": // FIXED: added LOGIN case for logout redirect
                showLoginPage();
                break;
            case "LOGOUT":
                handleLogout();
                break;
            default:
                System.out.println("Unknown sidebar destination: " + destination);
                showArtefakListPage(); // CHANGED: default to artifact page instead of dashboard
                break;
        }
    }

    // Missing showTicketDetailsPage method
    private void showTicketDetailsPage(String orderId) {
        try {
            showNavigationBar();
            mainLayout.setLeft(null);

            VBox content = createSimpleContentPage("Ticket Details",
                    "Ticket ID: " + orderId + "\n\n" +
                            "Event: Museum Exhibition\n" +
                            "Date: Valid for selected event\n" +
                            "Status: Active\n\n" +
                            "Please present this ticket at the museum entrance.");

            mainLayout.setCenter(content);
            System.out.println("✅ Ticket Details Page loaded for: " + orderId);

        } catch (Exception e) {
            System.err.println("Error loading Ticket Details Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Missing createSimpleContentPage method
    private VBox createSimpleContentPage(String title, String content) {
        VBox page = new VBox(20);
        page.setPadding(new Insets(40, 50, 40, 50));
        page.setStyle("-fx-background-color: #f5f3e8;");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.BLACK);

        // Content
        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Arial", 16));
        contentLabel.setTextFill(Color.web("#333333"));
        contentLabel.setWrapText(true);
        contentLabel.setLineSpacing(2);

        // Back button
        Button backButton = new Button("← Back");
        backButton.setStyle(
                "-fx-background-color: #d4a574;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-padding: 10 20;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;");
        backButton.setOnAction(e -> showHomePage());

        page.getChildren().addAll(titleLabel, contentLabel, backButton);
        return page;
    }

    private void showArtefakListPage() {
        try {
            hideNavigationBar();
            sideBar.setActiveMenuByDestination("ARTIFACT"); // Set active menu

            ArtefakRepositoryImpl repository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl service = new ArtefakServiceImpl(repository);
            ManajemenArtefakController controller = new ManajemenArtefakController(service);

            ArtefakListPage artefakListPage = new ArtefakListPage(controller);

            mainLayout.setLeft(sideBar);
            mainLayout.setCenter(artefakListPage);

            System.out.println("✅ Artefak List Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Artefak List Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showEventArtifactPage() {
        hideNavigationBar();
        sideBar.setActiveMenuByDestination("EVENT_ARTIFACT");

        try {
            PameranRepositoryImpl pameranRepository = new PameranRepositoryImpl();
            PameranServiceImpl pameranService = new PameranServiceImpl(pameranRepository);
            ArtefakRepositoryImpl artefakRepository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl artefakService = new ArtefakServiceImpl(artefakRepository);
            PameranController pameranController = new PameranController(pameranService, artefakService);

            EventArtefakPage eventArtefakPage = new EventArtefakPage(pameranController);

            mainLayout.setLeft(sideBar);
            mainLayout.setCenter(eventArtefakPage);

            System.out.println("✅ Event Artifact Management loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Event Artifact Management: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showMaintenancePage() {
        try {
            hideNavigationBar();
            sideBar.setActiveMenuByDestination("MAINTENANCE");

            // Create maintenance controllers
            ArtefakRepositoryImpl artefakRepository = new ArtefakRepositoryImpl();
            ArtefakServiceImpl artefakService = new ArtefakServiceImpl(artefakRepository);
            ManajemenArtefakController artefakController = new ManajemenArtefakController(artefakService);

            PemeliharaanRepositoryImpl pemeliharaanRepository = new PemeliharaanRepositoryImpl();
            PemeliharaanServiceImpl pemeliharaanService = new PemeliharaanServiceImpl(pemeliharaanRepository);
            PemeliharaanController pemeliharaanController = new PemeliharaanController(pemeliharaanService);

            // Check user role to show appropriate maintenance view
            if (currentUser != null && currentUser.getRole() == UserRole.CLEANER) {
                // Show cleaner maintenance task page
                MaintenanceTaskPage maintenanceTaskPage = new MaintenanceTaskPage(pemeliharaanController);
                mainLayout.setLeft(sideBar);
                mainLayout.setCenter(maintenanceTaskPage);
                System.out.println("✅ Maintenance Task Page loaded for cleaner!");
            } else {
                // Show curator maintenance list page
                MaintenanceListPage maintenanceListPage = new MaintenanceListPage(artefakController, pemeliharaanController);
                mainLayout.setLeft(sideBar);
                mainLayout.setCenter(maintenanceListPage);
                System.out.println("✅ Maintenance List Page loaded for curator!");
            }

        } catch (Exception e) {
            System.err.println("Error loading Maintenance Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showFeedbackListPage() {
        try {
            hideNavigationBar();

            VBox feedbackContent = new VBox(20);
            feedbackContent.setPadding(new Insets(30));
            feedbackContent.setStyle("-fx-background-color: white;");

            Label titleLabel = new Label("Feedback Management");
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
            titleLabel.setTextFill(Color.web("#2c3e50"));

            Label infoLabel = new Label("Feedback management functionality will be implemented here.");
            infoLabel.setFont(Font.font("System", 16));
            infoLabel.setTextFill(Color.web("#34495e"));

            feedbackContent.getChildren().addAll(titleLabel, infoLabel);

            mainLayout.setLeft(sideBar);
            mainLayout.setCenter(feedbackContent);

            System.out.println("✅ Feedback List Page loaded!");

        } catch (Exception e) {
            System.err.println("Error loading Feedback List Page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Implementation of LoginPage.LoginListener
    public void onHomeRequested() {
        System.out.println("Home requested from login page");
        showHomePage();
    }

    // Existing LoginPage.LoginListener methods
    public void onLoginSuccess(String role, UserDto user) {
        this.currentUser = user;
        this.currentUserRole = role;

        System.out.println("✅ Login successful! Role: " + role + ", User: " + user.getNamaLengkap());

        // Set user in sidebar
        sideBar.setCurrentUser(user);

        // Navigate based on role
        switch (role.toUpperCase()) {
            case "CURATOR":
                // Show curator artifact page as dashboard
                showArtefakListPage(); // CHANGED: artifact page as landing page
                break;
            case "CLEANER":
                // Show cleaner artifact page (you can implement this later)
                showMaintenancePage(); // CHANGED: artifact page as landing page
                break;
            case "CUSTOMER":
            default:
                // Show home page for customers
                showHomePage();
                break;
        }
    }

    public void onRegisterRequested() {
        System.out.println("Register requested from login page");
        showRegisterPage();
    }

    // Add all missing interface methods for RegisterPage.RegisterListener:

    // Implementation of RegisterPage.RegisterListener

    public void onRegisterSuccess(UserDto user) {
        System.out.println("✅ Registration successful for: " + user.getEmail());

        // Show success message
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Registration Successful");
        successAlert.setHeaderText("Welcome to ME-SEUM!");
        successAlert.setContentText("Your account has been created successfully. Please login to continue.");
        successAlert.showAndWait();

        // Navigate to login page
        showLoginPage();
    }

    public void onBackToLogin() {
        System.out.println("Back to login requested from register page");
        showLoginPage();
    }

    public void onLoginRequested() {
        System.out.println("Login requested from register page");
        showLoginPage();
    }
}

