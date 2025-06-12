package main.ui.views.customer;

import main.Main; // ADD THIS
import java.util.List; // ADD THIS if missing

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.ui.components.common.NavigationBar;
import main.ui.components.common.Footer;

/**
 * Your Orders page for ME-SEUM application
 * Shows customer's order history and tickets
 */
public class YourOrdersPage extends VBox implements NavigationBar.NavigationListener {

    // Navigation listener interface
    public interface OrderNavigationListener {
        void onOpenTicket(String eventId);

        void onListEventClicked();
    }

    private Footer footer;
    private ScrollPane scrollPane;
    private VBox mainContent;
    private VBox ordersContainer;
    private NavigationBar.NavigationListener externalNavigationListener;
    private OrderNavigationListener orderNavigationListener;

    /**
     * Constructor with navigation listeners
     */
    public YourOrdersPage(NavigationBar.NavigationListener externalListener,
            OrderNavigationListener orderListener) {
        this.externalNavigationListener = externalListener;
        this.orderNavigationListener = orderListener;
        initComponents();
        setupLayout();
        loadSampleOrders();
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

        // Create orders container
        ordersContainer = new VBox(15);
        ordersContainer.setPadding(new Insets(20, 50, 20, 50));
        ordersContainer.setAlignment(Pos.CENTER);

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
                ordersContainer,
                footer // Footer is now INSIDE the scrollable content
        );

        // Set up scroll pane with ALL content (header + tabs + orders + footer)
        scrollPane.setContent(mainContent);

        // Add only scroll pane to this YourOrdersPage VBox
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

        // List Event tab (inactive)
        Button listEventTab = createTabButton("List Event", false);
        listEventTab.setOnAction(e -> {
            if (orderNavigationListener != null) {
                orderNavigationListener.onListEventClicked();
            }
        });

        // Your Orders tab (active - golden)
        Button yourOrdersTab = createTabButton("Your Orders", true);

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
     * Load real orders instead of sample orders
     */
    private void loadSampleOrders() {
        ordersContainer.getChildren().clear();

        // Get orders from Main class - FIX THE REFERENCE
        List<Main.OrderInfo> orders = Main.getAllOrders();

        if (orders.isEmpty()) {
            // Show message when no orders
            Label noOrdersLabel = new Label("No tickets purchased yet");
            noOrdersLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            noOrdersLabel.setTextFill(Color.GRAY);
            noOrdersLabel.setPadding(new Insets(50));

            VBox emptyContainer = new VBox(20);
            emptyContainer.setAlignment(Pos.CENTER);
            emptyContainer.setPadding(new Insets(50));

            Label emptyIcon = new Label("🎫");
            emptyIcon.setFont(Font.font("System", 48));

            Label emptyMessage = new Label("You haven't purchased any tickets yet.");
            emptyMessage.setFont(Font.font("Arial", 16));
            emptyMessage.setTextFill(Color.web("#666666"));

            Button goToEventsBtn = new Button("Browse Events");
            goToEventsBtn.setStyle(
                    "-fx-background-color: #d4a574;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 10 20;" +
                            "-fx-border-radius: 5;" +
                            "-fx-background-radius: 5;" +
                            "-fx-cursor: hand;");
            goToEventsBtn.setOnAction(e -> {
                if (orderNavigationListener != null) {
                    orderNavigationListener.onListEventClicked();
                }
            });

            emptyContainer.getChildren().addAll(emptyIcon, noOrdersLabel, emptyMessage, goToEventsBtn);
            ordersContainer.getChildren().add(emptyContainer);

        } else {
            // Show real orders
            for (Main.OrderInfo order : orders) {
                createOrderCard(
                        order.getEventName(),
                        order.getDateRange(),
                        order.getTimeRange(),
                        order.getLocation(),
                        order.getOrderId());
            }
        }

        System.out.println("📋 Loaded " + orders.size() + " orders in YourOrdersPage");
    }

    /**
     * Create individual order card with "Open Ticket" button
     */
    private void createOrderCard(String eventName, String dateRange, String timeRange, String location,
            String orderId) {
        HBox orderCard = new HBox(20);
        orderCard.setAlignment(Pos.CENTER_LEFT);
        orderCard.setPadding(new Insets(20));
        orderCard.setStyle(
                "-fx-background-color: #e8e2d5;" +
                        "-fx-border-color: #d4c4a8;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");
        orderCard.setPrefHeight(120);
        orderCard.setMaxWidth(1000);
        orderCard.setPrefWidth(900);

        // Left section - Date and time
        VBox leftSection = new VBox(5);
        leftSection.setAlignment(Pos.CENTER_LEFT);
        leftSection.setPrefWidth(120);
        leftSection.setMinWidth(120);

        Label dateLabel = new Label(dateRange);
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        dateLabel.setTextFill(Color.BLACK);

        Label timeLabel = new Label(timeRange);
        timeLabel.setFont(Font.font("Arial", 12));
        timeLabel.setTextFill(Color.web("#666666"));

        leftSection.getChildren().addAll(dateLabel, timeLabel);

        // Middle section - Event details
        VBox middleSection = new VBox(8);
        middleSection.setAlignment(Pos.CENTER_LEFT);
        middleSection.setPrefWidth(450);
        middleSection.setMinWidth(400);
        HBox.setHgrow(middleSection, Priority.ALWAYS);

        Label eventNameLabel = new Label(eventName);
        eventNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        eventNameLabel.setTextFill(Color.BLACK);
        eventNameLabel.setWrapText(true);

        HBox locationBox = new HBox(5);
        locationBox.setAlignment(Pos.CENTER_LEFT);

        Label bullet = new Label("•");
        bullet.setFont(Font.font("Arial", 14));
        bullet.setTextFill(Color.web("#666666"));

        Label locationLabel = new Label(location);
        locationLabel.setFont(Font.font("Arial", 14));
        locationLabel.setTextFill(Color.web("#666666"));

        locationBox.getChildren().addAll(bullet, locationLabel);
        middleSection.getChildren().addAll(eventNameLabel, locationBox);

        // Right section - Only Open Ticket button (NO gray box above)
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER);
        rightSection.setPrefWidth(150);
        rightSection.setMinWidth(150);

        // Open Ticket button (different from Details button)
        Button openTicketButton = new Button("Open Ticket ↗");
        openTicketButton.setPrefWidth(120);
        openTicketButton.setStyle(
                "-fx-background-color: #007bff;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 16;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12;" +
                        "-fx-cursor: hand;");

        // Hover effect
        openTicketButton.setOnMouseEntered(e -> openTicketButton.setStyle(
                "-fx-background-color: #0056b3;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 16;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12;" +
                        "-fx-cursor: hand;"));

        openTicketButton.setOnMouseExited(e -> openTicketButton.setStyle(
                "-fx-background-color: #007bff;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8 16;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-font-size: 12;" +
                        "-fx-cursor: hand;"));

        // Open Ticket button action
        openTicketButton.setOnAction(e -> {
            if (orderNavigationListener != null) {
                orderNavigationListener.onOpenTicket(orderId);
            }
        });

        // Add only the button to rightSection (NO image/gray box)
        rightSection.getChildren().add(openTicketButton);
        orderCard.getChildren().addAll(leftSection, middleSection, rightSection);
        ordersContainer.getChildren().add(orderCard);
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
        scrollPane.setVvalue(1.0); // Scroll to bottom
    }

    // Method to add a new order dynamically
    public void addOrder(String eventName, String dateRange, String timeRange, String location, String orderId) {
        createOrderCard(eventName, dateRange, timeRange, location, orderId);
    }

    /**
     * Refresh orders - call this when returning from event purchase
     */
    public void refreshOrders() {
        System.out.println("🔄 Refreshing orders...");
        loadSampleOrders();
    }
}