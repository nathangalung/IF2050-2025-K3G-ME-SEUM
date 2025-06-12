package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Database utility class for managing database connections
 * Supports both MySQL and PostgreSQL databases
 */
public class DatabaseUtil {
    
    // Database configuration constants
    private static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    
    // Default database configuration (can be overridden by properties file)
    private String databaseUrl = "jdbc:mysql://localhost:3306/museum_db";
    private String username = "root";
    private String password = "";
    private String driverClass = MYSQL_DRIVER;
    
    /**
     * Default constructor that loads database configuration
     */
    public DatabaseUtil() {
        loadDatabaseConfiguration();
    }
    
    /**
     * Constructor with custom database configuration
     */
    public DatabaseUtil(String url, String username, String password, String driver) {
        this.databaseUrl = url;
        this.username = username;
        this.password = password;
        this.driverClass = driver;
        loadDriver();
    }
    
    /**
     * Get a database connection
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(databaseUrl, username, password);
        } catch (SQLException e) {
            throw new SQLException("Failed to establish database connection: " + e.getMessage(), e);
        }
    }
    
    /**
     * Load database configuration from properties file
     */
    private void loadDatabaseConfiguration() {
        Properties props = new Properties();
        
        // Try to load from database.properties file
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (is != null) {
                props.load(is);
                
                // Load configuration from properties
                databaseUrl = props.getProperty("database.url", databaseUrl);
                username = props.getProperty("database.username", username);
                password = props.getProperty("database.password", password);
                driverClass = props.getProperty("database.driver", driverClass);
            }
        } catch (IOException e) {
            // If properties file not found, use default configuration
            System.out.println("Database properties file not found, using default configuration");
        }
        
        loadDriver();
    }
    
    /**
     * Load the appropriate JDBC driver
     */
    private void loadDriver() {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found: " + driverClass, e);
        }
    }
    
    /**
     * Test database connection
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Close database connection safely
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get the current database URL
     * @return Database URL
     */
    public String getDatabaseUrl() {
        return databaseUrl;
    }
    
    /**
     * Get the current username
     * @return Database username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Check if using MySQL database
     * @return true if using MySQL, false otherwise
     */
    public boolean isMySQL() {
        return databaseUrl.contains("mysql");
    }
    
    /**
     * Check if using PostgreSQL database
     * @return true if using PostgreSQL, false otherwise
     */
    public boolean isPostgreSQL() {
        return databaseUrl.contains("postgresql");
    }
}