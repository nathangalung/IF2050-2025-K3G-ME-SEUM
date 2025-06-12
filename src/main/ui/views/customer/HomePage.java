package main.ui.views.customer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import main.ui.components.common.Footer;

public class HomePage extends BorderPane {
    
    private Footer footer;
    private ScrollPane scrollPane;
    private VBox mainContent;
    
    public HomePage() {
        initComponents();
        setupLayout();
    }

    private void initComponents() {
        footer = new Footer();
        
        mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #f5f3e8;");
        
        scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f5f3e8;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }
    
    private void setupLayout() {
        VBox heroSection = createHeroSection();
        VBox welcomeSection = createWelcomeSection();
        VBox exhibitionsSection = createExhibitionsSection();
        VBox collectionSection = createCollectionSection();
        mainContent.getChildren().addAll(
            heroSection,
            welcomeSection,
            exhibitionsSection,
            collectionSection,
            footer
        );
        setCenter(scrollPane);
    }
    private VBox createHeroSection() {
        VBox heroSection = new VBox();
        heroSection.setAlignment(Pos.CENTER);
        heroSection.setPadding(new Insets(100, 50, 100, 50));

        try {
            String heroImagePath = "/img/home/1.png";
            if (getClass().getResourceAsStream(heroImagePath) != null) {
                heroSection.setStyle(
                    "-fx-background-image: url('" + heroImagePath + "');" +
                    "-fx-background-size: cover;" +
                    "-fx-background-position: center;"
                );
                System.out.println("✅ Successfully loaded hero image: " + heroImagePath);
            } else {
                heroSection.setStyle(
                    "-fx-background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);"
                );
                System.out.println("⚠️ Hero image not found: " + heroImagePath + " - using fallback background");
            }
        } catch (Exception e) {
            heroSection.setStyle(
                "-fx-background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);"
            );
            System.out.println("⚠️ Error loading hero image - using fallback background");
        }
        
        heroSection.setPrefHeight(600);
        
        VBox heroContent = new VBox(20);
        heroContent.setAlignment(Pos.CENTER);
        heroContent.setMaxWidth(800);
        heroContent.setStyle("-fx-background-color: rgba(0,0,0,0.4); " +
                            "-fx-padding: 40; " +
                            "-fx-background-radius: 10;");
        
        Label subtitle = new Label("The Largest Museum in Indonesia");
        subtitle.setFont(Font.font("System", 18));
        subtitle.setTextFill(Color.WHITE);
        subtitle.setTextAlignment(TextAlignment.CENTER);
        
        Label mainTitle = new Label("Museum Nusantara at");
        mainTitle.setFont(Font.font("System", FontWeight.BOLD, 48));
        mainTitle.setTextFill(Color.WHITE);
        mainTitle.setTextAlignment(TextAlignment.CENTER);
        
        Label locationTitle = new Label("Ibu Kota Nusantara");
        locationTitle.setFont(Font.font("System", FontWeight.BOLD, 48));
        locationTitle.setTextFill(Color.WHITE);
        locationTitle.setTextAlignment(TextAlignment.CENTER);
        
        Button readMoreBtn = new Button("Read More");
        readMoreBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 25;" +
            "-fx-background-radius: 25;" +
            "-fx-cursor: hand;"
        );
        
        readMoreBtn.setOnMouseEntered(e -> 
            readMoreBtn.setStyle(
                "-fx-background-color: white;" +
                "-fx-text-fill: black;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 15 30;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 25;" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;"
            )
        );
        
        readMoreBtn.setOnMouseExited(e -> 
            readMoreBtn.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 15 30;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 25;" +
                "-fx-background-radius: 25;" +
                "-fx-cursor: hand;"
            )
        );
        
        readMoreBtn.setOnAction(e -> {
            System.out.println("Read More clicked - handled by Main navigation");
        });
        
        heroContent.getChildren().addAll(subtitle, mainTitle, locationTitle, readMoreBtn);
        heroSection.getChildren().add(heroContent);
        
        return heroSection;
    }

    private VBox createWelcomeSection() {
        VBox welcomeSection = new VBox(30);
        welcomeSection.setPadding(new Insets(80, 50, 80, 50));
        welcomeSection.setStyle("-fx-background-color: #f5f3e8;");
        
        HBox contentBox = new HBox(50);
        contentBox.setAlignment(Pos.CENTER);
        
        VBox textContent = new VBox(20);
        textContent.setMaxWidth(500);
        
        Label title = new Label("Welcome to Museum Nusantara and History Museum");
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.setTextFill(Color.BLACK);
        title.setWrapText(true);
        
        Label description = new Label("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                    "Quisque justo sem. Finibus sit amet magna nec, consectetur gravida diam. " +
                                    "Suspendisse commodo efficitur magna, sit amet fringilla dui convallis non. " +
                                    "Vestibulum ut tristique, consectetur nibh vitae, congue mi. Ut eget.");
        description.setFont(Font.font("System", 14));
        description.setTextFill(Color.web("#666666"));
        description.setWrapText(true);
        description.setLineSpacing(5);
        
        Button moreAboutBtn = new Button("More About");
        moreAboutBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 0;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-cursor: hand;"
        );
        
        moreAboutBtn.setOnAction(e -> {
            System.out.println("More About clicked - handled by Main navigation");
        });
        
        textContent.getChildren().addAll(title, description, moreAboutBtn);
        
        ImageView welcomeImage = new ImageView();
        try {
            String welcomeImagePath = "/img/home/2.png";
            if (getClass().getResourceAsStream(welcomeImagePath) != null) {
                welcomeImage.setImage(new Image(welcomeImagePath));
                System.out.println("✅ Successfully loaded welcome image: " + welcomeImagePath);
            } else {
                System.out.println("⚠️ Welcome image not found: " + welcomeImagePath);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error loading welcome image");
        }
        
        welcomeImage.setFitWidth(400);
        welcomeImage.setFitHeight(300);
        welcomeImage.setPreserveRatio(true);
        welcomeImage.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        contentBox.getChildren().addAll(textContent, welcomeImage);
        welcomeSection.getChildren().add(contentBox);
        
        return welcomeSection;
    }

    private VBox createExhibitionsSection() {
        VBox exhibitionsSection = new VBox(40);
        exhibitionsSection.setPadding(new Insets(80, 50, 80, 50));
        exhibitionsSection.setStyle("-fx-background-color: white;");
        
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        
        Label title = new Label("Upcoming Exhibitions");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        title.setTextFill(Color.BLACK);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button viewAllBtn = new Button("View All Exhibitions");
        viewAllBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: black;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 0;" +
            "-fx-border-color: transparent;" +
            "-fx-border-width: 0 0 2 0;" +
            "-fx-cursor: hand;"
        );
        
        viewAllBtn.setOnAction(e -> {
            System.out.println("View All Exhibitions clicked - handled by Main navigation");
        });
        
        headerBox.getChildren().addAll(title, spacer, viewAllBtn);
        
        ImageView exhibitionImage = new ImageView();
        try {
            String exhibitionImagePath = "/img/home/3.png";
            if (getClass().getResourceAsStream(exhibitionImagePath) != null) {
                exhibitionImage.setImage(new Image(exhibitionImagePath));
                System.out.println("✅ Successfully loaded exhibition image: " + exhibitionImagePath);
            } else {
                System.out.println("⚠️ Exhibition image not found: " + exhibitionImagePath);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error loading exhibition image");
        }
        
        exhibitionImage.setFitWidth(800);
        exhibitionImage.setFitHeight(300);
        exhibitionImage.setPreserveRatio(true);
        exhibitionImage.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        
        VBox detailsBox = new VBox(15);
        
        HBox exhibitionRow1 = createExhibitionRow("Matahari Terbenam dari Timur", "14 - 20 April 2025", 
                                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                                "Maecenas efficitur, velit eu efficitur lobortis, erat neque.");
        
        HBox exhibitionRow2 = createExhibitionRow("Matahari Terbit dari Barat", "21 - 27 April 2025", 
                                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                                "Maecenas efficitur, velit eu efficitur lobortis, erat neque.");
        
        detailsBox.getChildren().addAll(exhibitionRow1, exhibitionRow2);
        
        exhibitionsSection.getChildren().addAll(headerBox, exhibitionImage, detailsBox);
        
        return exhibitionsSection;
    }
    
    private HBox createExhibitionRow(String title, String date, String description) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15, 0, 15, 0));
        
        VBox textBox = new VBox(5);
        textBox.setMaxWidth(600);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.BLACK);
        
        Label dateLabel = new Label(date);
        dateLabel.setFont(Font.font("System", 12));
        dateLabel.setTextFill(Color.web("#666666"));
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("System", 12));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        
        textBox.getChildren().addAll(titleLabel, dateLabel, descLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button buyTicketBtn = new Button("Buy Ticket");
        buyTicketBtn.setStyle(
            "-fx-background-color: #28a745;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8 16;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        
        buyTicketBtn.setOnAction(e -> {
            System.out.println("Buy Ticket clicked - handled by Main navigation");
        });
        
        row.getChildren().addAll(textBox, spacer, buyTicketBtn);
        
        return row;
    }
    
    private VBox createCollectionSection() {
        VBox collectionSection = new VBox(40);
        collectionSection.setPadding(new Insets(80, 50, 80, 50));
        collectionSection.setStyle("-fx-background-color: #f5f3e8;");
        
        Label title = new Label("Our Collection");
        title.setFont(Font.font("System", FontWeight.BOLD, 32));
        title.setTextFill(Color.BLACK);
        title.setAlignment(Pos.CENTER);
        
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().add(title);
        
        GridPane collectionGrid = new GridPane();
        collectionGrid.setHgap(20);
        collectionGrid.setVgap(20);
        collectionGrid.setAlignment(Pos.CENTER);
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Label collectionItem = new Label("🏺 Artifact " + (row * 3 + col + 1));
                collectionItem.setPrefSize(200, 150);
                collectionItem.setAlignment(Pos.CENTER);
                collectionItem.setStyle(
                    "-fx-background-color: #e9ecef;" +
                    "-fx-border-color: #dee2e6;" +
                    "-fx-border-width: 1;" +
                    "-fx-text-fill: #6c757d;" +
                    "-fx-font-size: 14px;" +
                    "-fx-cursor: hand;"
                );
                
                collectionItem.setOnMouseEntered(e -> 
                    collectionItem.setStyle(
                        "-fx-background-color: #dee2e6;" +
                        "-fx-border-color: #adb5bd;" +
                        "-fx-border-width: 2;" +
                        "-fx-text-fill: #495057;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;"
                    )
                );
                
                collectionItem.setOnMouseExited(e -> 
                    collectionItem.setStyle(
                        "-fx-background-color: #e9ecef;" +
                        "-fx-border-color: #dee2e6;" +
                        "-fx-border-width: 1;" +
                        "-fx-text-fill: #6c757d;" +
                        "-fx-font-size: 14px;" +
                        "-fx-cursor: hand;"
                    )
                );
                
                collectionItem.setOnMouseClicked(e -> {
                    System.out.println("Collection item clicked - handled by Main navigation");
                });
                
                collectionGrid.add(collectionItem, col, row);
            }
        }
        
        collectionSection.getChildren().addAll(titleBox, collectionGrid);
        
        return collectionSection;
    }
    
    public void scrollToFooter() {
        scrollPane.setVvalue(1.0); 
    }
}