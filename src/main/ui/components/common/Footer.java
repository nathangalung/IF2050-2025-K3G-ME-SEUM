package main.ui.components.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Footer component for ME-SEUM application
 * Displays museum gallery image and visitor information with social media links
 */
public class Footer extends HBox {
    
    /**
     * Constructor for Footer
     */
    public Footer() {
        setupFooter();
    }
    
    /**
     * Set up the footer layout and content
     */
    private void setupFooter() {
        // Set footer appearance - darker background to match design
        setStyle("-fx-background-color: #1a1a1a;"); // Very dark background
        setPadding(new Insets(0));
        setSpacing(0);
        setAlignment(Pos.CENTER);
        setPrefHeight(150); // Reduced height to match design
        setMaxHeight(150);
        
        // Left side - Gallery image container that fills the space
        VBox imageContainer = createImageContainer();
        
        // Right side - Visitor info and social media
        VBox rightContent = createVisitorInfoSection();
        
        // Set equal width distribution
        HBox.setHgrow(imageContainer, javafx.scene.layout.Priority.ALWAYS);
        HBox.setHgrow(rightContent, javafx.scene.layout.Priority.ALWAYS);
        
        // Add components to footer
        getChildren().addAll(imageContainer, rightContent);
    }
    
    /**
     * Create image container that fills the entire left side
     */
    private VBox createImageContainer() {
        VBox imageContainer = new VBox();
        imageContainer.setPadding(new Insets(0));
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPrefWidth(450);
        imageContainer.setPrefHeight(150);
        imageContainer.setMaxHeight(150);
        
        ImageView galleryImage = new ImageView();
        try {
            galleryImage.setImage(new Image("/img/component/footer.png"));
            galleryImage.setFitWidth(450); // Fill entire width
            galleryImage.setFitHeight(150); // Fill entire height
            galleryImage.setPreserveRatio(false); // Allow stretching to fill completely
            galleryImage.setSmooth(true);
            
            imageContainer.getChildren().add(galleryImage);
            
        } catch (Exception e) {
            // Fallback if image not found - create a placeholder that fills the space
            Label placeholderLabel = new Label("Gallery Image");
            placeholderLabel.setTextFill(Color.WHITE);
            placeholderLabel.setPrefSize(450, 150);
            placeholderLabel.setMaxSize(450, 150);
            placeholderLabel.setAlignment(Pos.CENTER);
            placeholderLabel.setStyle("-fx-background-color: #8B4513; -fx-border-color: #D4AF37; -fx-border-width: 2;");
            
            imageContainer.getChildren().add(placeholderLabel);
        }
        
        return imageContainer;
    }
    
    /**
     * Create visitor information section with social media links
     */
    private VBox createVisitorInfoSection() {
        VBox rightSection = new VBox(15); // Reduced spacing
        rightSection.setPadding(new Insets(20, 40, 20, 40)); // Reduced padding
        rightSection.setAlignment(Pos.TOP_LEFT);
        rightSection.setPrefWidth(450);
        rightSection.setPrefHeight(150);
        rightSection.setStyle("-fx-background-color: #1a1a1a;"); // Match footer background
        
        // Visitor Info title
        Label visitorInfoTitle = new Label("Visitor Info");
        visitorInfoTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Slightly smaller
        visitorInfoTitle.setTextFill(Color.WHITE);
        
        // Opening Hours section
        VBox openingHoursSection = createOpeningHoursSection();
        
        // Social Media section
        VBox socialMediaSection = createSocialMediaSection();
        
        rightSection.getChildren().addAll(
            visitorInfoTitle,
            openingHoursSection,
            socialMediaSection
        );
        
        return rightSection;
    }
    
    /**
     * Create opening hours section
     */
    private VBox createOpeningHoursSection() {
        VBox openingSection = new VBox(5); // Reduced spacing
        
        // Opening Hours title
        Label openingTitle = new Label("Opening Hours");
        openingTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Smaller font
        openingTitle.setTextFill(Color.WHITE);
        
        // Monday - Saturday hours
        HBox weekdayHours = new HBox(20); // Added spacing between day and time
        weekdayHours.setAlignment(Pos.CENTER_LEFT);
        
        Label weekdayLabel = new Label("Monday - Saturday");
        weekdayLabel.setFont(Font.font("Arial", 12)); // Smaller font
        weekdayLabel.setTextFill(Color.web("#cccccc"));
        
        Label weekdayTime = new Label("11 a.m - 8 p.m");
        weekdayTime.setFont(Font.font("Arial", 12)); // Smaller font
        weekdayTime.setTextFill(Color.web("#cccccc"));
        
        weekdayHours.getChildren().addAll(weekdayLabel, weekdayTime);
        
        // Sunday hours
        HBox sundayHours = new HBox(20); // Added spacing between day and time
        sundayHours.setAlignment(Pos.CENTER_LEFT);
        
        Label sundayLabel = new Label("Sunday");
        sundayLabel.setFont(Font.font("Arial", 12)); // Smaller font
        sundayLabel.setTextFill(Color.web("#cccccc"));
        
        Label sundayTime = new Label("10 a.m - 9 p.m");
        sundayTime.setFont(Font.font("Arial", 12)); // Smaller font
        sundayTime.setTextFill(Color.web("#cccccc"));
        
        sundayHours.getChildren().addAll(sundayLabel, sundayTime);
        
        openingSection.getChildren().addAll(
            openingTitle,
            weekdayHours,
            sundayHours
        );
        
        return openingSection;
    }
    
    /**
     * Create social media section with icons and handles
     */
    private VBox createSocialMediaSection() {
        VBox socialSection = new VBox(8); // Reduced spacing
        
        // Social Media title
        Label socialTitle = new Label("Social Media");
        socialTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Smaller font
        socialTitle.setTextFill(Color.WHITE);
        
        // Create two rows of social media as shown in design
        HBox socialRow1 = new HBox(30);
        socialRow1.setAlignment(Pos.CENTER_LEFT);
        
        HBox instagramItem = createSocialMediaItem("📷", "@MuseumNusantara");
        HBox tiktokItem = createSocialMediaItem("🎵", "@MuseumNusantara");
        
        socialRow1.getChildren().addAll(instagramItem, tiktokItem);
        
        HBox socialRow2 = new HBox(30);
        socialRow2.setAlignment(Pos.CENTER_LEFT);
        
        HBox youtubeItem = createSocialMediaItem("▶️", "MuseumNusantara");
        HBox xItem = createSocialMediaItem("✗", "@MuseumNusantara");
        
        socialRow2.getChildren().addAll(youtubeItem, xItem);
        
        socialSection.getChildren().addAll(
            socialTitle,
            socialRow1,
            socialRow2
        );
        
        return socialSection;
    }
    
    /**
     * Create individual social media item
     */
    private HBox createSocialMediaItem(String icon, String handle) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);
        
        // Social media icon
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font("Arial", 14)); // Smaller icon
        iconLabel.setTextFill(Color.WHITE);
        
        // Social media handle
        Label handleLabel = new Label(handle);
        handleLabel.setFont(Font.font("Arial", 11)); // Smaller font
        handleLabel.setTextFill(Color.web("#cccccc"));
        
        // Add hover effect
        item.setStyle("-fx-cursor: hand;");
        item.setOnMouseEntered(e -> {
            iconLabel.setTextFill(Color.web("#e6d28e")); // Gold color on hover
            handleLabel.setTextFill(Color.WHITE);
        });
        
        item.setOnMouseExited(e -> {
            iconLabel.setTextFill(Color.WHITE);
            handleLabel.setTextFill(Color.web("#cccccc"));
        });
        
        item.getChildren().addAll(iconLabel, handleLabel);
        return item;
    }
}
