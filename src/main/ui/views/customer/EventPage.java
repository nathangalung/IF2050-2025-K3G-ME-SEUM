package main.ui.views.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.controller.PameranController;
import main.model.dto.PameranDto;
import main.ui.components.common.NavigationBar;
import main.ui.components.common.Footer;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Event page for ME-SEUM application
 * Shows available museum events from database
 */
public class EventPage extends VBox implements NavigationBar.NavigationListener {

    // Navigation listener interface
    public interface EventNavigationListener {
        void onEventDetails(String eventId);

        void onYourOrdersClicked();
    }

    private final PameranController pameranController;
    private Footer footer;
    private ScrollPane scrollPane;
    private VBox mainContent;
    private VBox eventsContainer;
    private NavigationBar.NavigationListener externalNavigationListener;
    private EventNavigationListener eventNavigationListener;

    /**
     * Constructor with navigation listeners and controller
     */
    public EventPage(NavigationBar.NavigationListener externalListener,
            EventNavigationListener eventListener, PameranController pameranController) {
        this.externalNavigationListener = externalListener;
        this.eventNavigationListener = eventListener;
        this.pameranController = pameranController;
        initComponents();
        setupLayout();
        loadEventsFromDatabase();
    }

    /**
     * Initialize components
     */
    private void initComponents() {
        // Create footer
        footer = new Footer();

        // Create main content container (includes footer for scrolling together)
        mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #f5f3e8;");

        // Create events container
        eventsContainer = new VBox(15);
        eventsContainer.setPadding(new Insets(20, 50, 20, 50));
        eventsContainer.setAlignment(Pos.CENTER);

        // Create scroll pane for ALL content (including footer)
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f3e8;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /**
     * Set up layout
     */
    private void setupLayout() {
        // Create page header
        VBox headerSection = createHeaderSection();

        // Create tabs section
        HBox tabsSection = createTabsSection();

        // Add ALL sections to main content (INCLUDING footer)
        // Footer will scroll with the content, not stick to bottom
        mainContent.getChildren().addAll(
                headerSection,
                tabsSection,
                eventsContainer,
                footer // Footer is now INSIDE the scrollable content
        );

        // Set up scroll pane with ALL content (header + tabs + events + footer)
        scrollPane.setContent(mainContent);

        // Add only scroll pane to this EventPage VBox
        getChildren().add(scrollPane);
        setStyle("-fx-background-color: #f5f3e8;");
    }

    /**
     * Create header section
     */
    private VBox createHeaderSection() {
        VBox headerSection = new VBox(10);
        headerSection.setPadding(new Insets(40, 50, 20, 50));
        headerSection.setAlignment(Pos.CENTER);
        headerSection.setStyle("-fx-background-color: #f5f3e8;");

        // Page title
        Label titleLabel = new Label("Event");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.BLACK);

        headerSection.getChildren().add(titleLabel);
        return headerSection;
    }

    /**
     * Create tabs section with pill-shaped buttons
     */
    private HBox createTabsSection() {
        HBox tabsSection = new HBox(20); // Add spacing between buttons
        tabsSection.setPadding(new Insets(0, 50, 30, 50)); // Increased bottom padding
        tabsSection.setAlignment(Pos.CENTER);

        // List Event tab (active - golden)
        Button listEventTab = createTabButton("List Event", true);

        // Your Orders tab (inactive)
        Button yourOrdersTab = createTabButton("Your Orders", false);
        yourOrdersTab.setOnAction(e -> {
            if (eventNavigationListener != null) {
                eventNavigationListener.onYourOrdersClicked();
            }
        });

        tabsSection.getChildren().addAll(listEventTab, yourOrdersTab);
        return tabsSection;
    }

    /**
     * Create tab button with rounded pill shape design
     */
    private Button createTabButton(String text, boolean isActive) {
        Button tab = new Button(text);
        tab.setPrefHeight(50); // Increased height for better pill shape
        tab.setPrefWidth(200); // Increased width for better pill shape

        if (isActive) {
            // Active tab style (golden background with rounded pill shape)
            tab.setStyle(
                    "-fx-background-color: #d4a574;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-border-color: #b8956a;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 25;" + // Half of height for perfect pill shape
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        } else {
            // Inactive tab style (light background with rounded pill shape)
            tab.setStyle(
                    "-fx-background-color: #f5f0e6;" +
                            "-fx-text-fill: #666666;" +
                            "-fx-font-size: 16px;" +
                            "-fx-border-color: #d4c4a8;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 25;" + // Half of height for perfect pill shape
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;");

            // Hover effect for inactive tab
            tab.setOnMouseEntered(e -> tab.setStyle(
                    "-fx-background-color: #e8e2d5;" +
                            "-fx-text-fill: black;" +
                            "-fx-font-size: 16px;" +
                            "-fx-border-color: #d4c4a8;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);"));

            tab.setOnMouseExited(e -> tab.setStyle(
                    "-fx-background-color: #f5f0e6;" +
                            "-fx-text-fill: #666666;" +
                            "-fx-font-size: 16px;" +
                            "-fx-border-color: #d4c4a8;" +
                            "-fx-border-width: 2;" +
                            "-fx-background-radius: 25;" +
                            "-fx-border-radius: 25;" +
                            "-fx-cursor: hand;"));
        }

        return tab;
    }

    /**
     * Load events from database - NEW METHOD
     */
    private void loadEventsFromDatabase() {
        eventsContainer.getChildren().clear();

        try {
            System.out.println("🔄 Loading events from database...");

            // Get active events from database
            List<PameranDto> events = pameranController.getActivePamerans();

            System.out.println("📊 Found " + events.size() + " active events");

            if (events.isEmpty()) {
                // Show message if no events available
                Label noEventsLabel = new Label("No events available at the moment");
                noEventsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                noEventsLabel.setTextFill(Color.GRAY);
                noEventsLabel.setPadding(new Insets(50));
                eventsContainer.getChildren().add(noEventsLabel);
            } else {
                // Create event cards from database data
                for (PameranDto event : events) {
                    createEventCardFromDatabase(event);
                }
            }

            System.out.println("✅ Events loaded successfully");

        } catch (Exception e) {
            System.err.println("❌ Error loading events: " + e.getMessage());
            e.printStackTrace();

            // Show error message
            Label errorLabel = new Label("Error loading events: " + e.getMessage());
            errorLabel.setFont(Font.font("Arial", 14));
            errorLabel.setTextFill(Color.RED);
            errorLabel.setPadding(new Insets(50));
            errorLabel.setWrapText(true);
            eventsContainer.getChildren().add(errorLabel);
        }
    }

    /**
     * Create event card from database data - NEW METHOD
     */
    private void createEventCardFromDatabase(PameranDto event) {
        HBox eventCard = new HBox(20);
        eventCard.setAlignment(Pos.CENTER_LEFT);
        eventCard.setPadding(new Insets(20));
        eventCard.setStyle(
                "-fx-background-color: #e8e2d5;" +
                        "-fx-border-color: #d4c4a8;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        eventCard.setPrefHeight(120);
        eventCard.setMaxWidth(1000);
        eventCard.setPrefWidth(900);

        // Left section - Date and time from database
        VBox leftSection = new VBox(5);
        leftSection.setAlignment(Pos.CENTER_LEFT);
        leftSection.setPrefWidth(120);
        leftSection.setMinWidth(120);

        // Format dates from database
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

        Label dateLabel = new Label(dateRange);
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        dateLabel.setTextFill(Color.BLACK);

        Label timeLabel = new Label(timeRange);
        timeLabel.setFont(Font.font("Arial", 12));
        timeLabel.setTextFill(Color.web("#666666"));

        leftSection.getChildren().addAll(dateLabel, timeLabel);

        // Middle section - Event details from database
        VBox middleSection = new VBox(8);
        middleSection.setAlignment(Pos.CENTER_LEFT);
        middleSection.setPrefWidth(450);
        middleSection.setMinWidth(400);
        HBox.setHgrow(middleSection, Priority.ALWAYS);

        Label eventNameLabel = new Label(event.getNamaPameran());
        eventNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        eventNameLabel.setTextFill(Color.BLACK);
        eventNameLabel.setWrapText(true);

        HBox locationBox = new HBox(5);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        Label bullet = new Label("•");
        bullet.setFont(Font.font("Arial", 14));
        bullet.setTextFill(Color.web("#666666"));

        Label locationLabel = new Label("Museum Nusantara"); // Use fixed location for now
        locationLabel.setFont(Font.font("Arial", 14));
        locationLabel.setTextFill(Color.web("#666666"));

        locationBox.getChildren().addAll(bullet, locationLabel);
        middleSection.getChildren().addAll(eventNameLabel, locationBox);

        // Right section - Details button
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER);
        rightSection.setPrefWidth(150);
        rightSection.setMinWidth(150);

        // Details button (with → arrow, transparent background with blue border)
        Button detailsButton = new Button("Details →");
        detailsButton.setPrefWidth(120);
        detailsButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #007bff;" +
                        "-fx-padding: 8 16;" +
                        "-fx-border-color: #007bff;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12;" +
                        "-fx-cursor: hand;");

        // Hover effect
        detailsButton.setOnMouseEntered(e -> detailsButton.setStyle(
                "-fx-background-color: #007bff;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 16;" +
                        "-fx-border-color: #007bff;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12;" +
                        "-fx-cursor: hand;"));

        detailsButton.setOnMouseExited(e -> detailsButton.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #007bff;" +
                        "-fx-padding: 8 16;" +
                        "-fx-border-color: #007bff;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12;" +
                        "-fx-cursor: hand;"));

        // Details button action - pass actual event ID
        detailsButton.setOnAction(e -> {
            if (eventNavigationListener != null) {
                eventNavigationListener.onEventDetails(String.valueOf(event.getPameranId()));
            }
        });

        rightSection.getChildren().add(detailsButton);
        eventCard.getChildren().addAll(leftSection, middleSection, rightSection);
        eventsContainer.getChildren().add(eventCard);

        System.out.println("🎪 Created event card: " + event.getNamaPameran() + " (ID: " + event.getPameranId() + ")");
    }

    /**
     * Public method to refresh events - can be called from outside
     */
    public void refreshEvents() {
        loadEventsFromDatabase();
    }

    @Override
    public void onNavigate(String destination) {
        if (externalNavigationListener != null) {
            externalNavigationListener.onNavigate(destination);
        }
    }

    /**
     * Scroll to footer when contact is clicked
     */
    public void scrollToFooter() {
        // This should work since scrollPane contains mainContent which includes footer
        scrollPane.setVvalue(1.0); // Scroll to bottom
        System.out.println("🔄 EventPage scrolled to footer");
    }
}