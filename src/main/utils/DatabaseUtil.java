package main.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Utility untuk koneksi database dengan fallback MySQL/PostgreSQL
public class DatabaseUtil { // Database configurations
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/ME_SEUM?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String POSTGRES_URL = "jdbc:postgresql://localhost:5433/me_seum"; // Docker port 5433
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "NathanGalung_SQL_1965";

    // PostgreSQL Docker credentials - prioritize meseum_user
    private static final String POSTGRES_USER = "meseum_user"; // Primary Docker user
    private static final String POSTGRES_PASSWORD = "NathanGalung_SQL_1965";
    private static final String POSTGRES_ALT_USER = "postgres"; // Alternative user

    private String currentDbType = null;
    private String currentUrl = null;
    private String currentUser = null;
    private String currentPassword = null;

    public DatabaseUtil() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        // Try MySQL first
        if (tryMySQLConnection()) {
            currentDbType = "MySQL";
            currentUrl = MYSQL_URL;
            currentUser = DB_USER;
            currentPassword = DB_PASSWORD;
            System.out.println("✅ Using MySQL database");
            return;
        }

        // Fallback to PostgreSQL
        if (tryPostgreSQLConnection()) {
            currentDbType = "PostgreSQL";
            currentUrl = POSTGRES_URL;
            currentUser = POSTGRES_USER;
            currentPassword = POSTGRES_PASSWORD;
            System.out.println("✅ Using PostgreSQL database");
            return;
        }

        // Neither database is available
        System.err.println("❌ Neither MySQL nor PostgreSQL is available!");
        System.err.println("Please ensure at least one database server is running.");
    }

    private boolean tryMySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("🔄 Testing MySQL connection...");
            try (Connection conn = DriverManager.getConnection(MYSQL_URL, DB_USER, DB_PASSWORD)) {
                return conn != null && !conn.isClosed();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("⚠️  MySQL driver not found, trying PostgreSQL...");
            return false;
        } catch (SQLException e) {
            System.out.println("⚠️  MySQL connection failed: " + e.getMessage());
            System.out.println("🔄 Trying PostgreSQL...");
            return false;
        }
    }

    private boolean tryPostgreSQLConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("🔄 Testing PostgreSQL connection (Docker on port 5433)...");

            // Try Docker-specific authentication methods
            String[] userAttempts = {
                    POSTGRES_USER, // meseum_user (primary)
                    POSTGRES_ALT_USER // postgres (fallback)
            };

            for (String user : userAttempts) {
                try {
                    Properties props = new Properties();
                    props.setProperty("user", user);
                    props.setProperty("password", POSTGRES_PASSWORD);

                    Connection conn = DriverManager.getConnection(POSTGRES_URL, props);

                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                        currentUser = user;
                        currentPassword = POSTGRES_PASSWORD;
                        System.out.println("✅ PostgreSQL Docker connection successful with user: " + user);
                        return true;
                    }

                } catch (SQLException e) {
                    System.out.println("⚠️  PostgreSQL connection failed for user '" + user + "': " + e.getMessage());
                    continue;
                }
            }

        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL driver not found");
            return false;
        }

        System.err.println("❌ All PostgreSQL Docker connection attempts failed");
        System.err.println("💡 Make sure Docker PostgreSQL is running: docker-compose up -d");
        return false;
    }

    public Connection getConnection() throws SQLException {
        if (currentUrl == null) {
            throw new SQLException("No database connection available. Please check database servers.");
        }

        try {
            Connection conn;

            if ("PostgreSQL".equals(currentDbType)) {
                Properties props = new Properties();
                props.setProperty("user", currentUser != null ? currentUser : POSTGRES_USER);
                if (currentPassword != null && !currentPassword.isEmpty()) {
                    props.setProperty("password", currentPassword);
                }
                conn = DriverManager.getConnection(currentUrl, props);
            } else {
                // MySQL connection
                conn = DriverManager.getConnection(currentUrl, currentUser, currentPassword);
            }

            System.out.println("✅ Database connection successful (" + currentDbType + ")");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            System.err.println("Please check:");
            System.err.println("1. " + currentDbType + " server is running");
            if ("PostgreSQL".equals(currentDbType)) {
                System.err.println("2. Docker PostgreSQL is running: docker-compose up -d");
                System.err.println("3. Database 'me_seum' exists (should be auto-created)");
                System.err.println("4. Port 5433 is accessible");
                System.err.println("5. Check container status: docker-compose ps");
                System.err.println("6. Check container logs: docker-compose logs postgres");
            } else {
                System.err.println("2. Database 'ME_SEUM' exists");
                System.err.println("3. Username/password is correct");
            }
            System.err.println("5. Port is not blocked");

            throw new SQLException("Unable to connect to database: " + e.getMessage(), e);
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Database connection closed");
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
            }
        }
    }

    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean isValid = conn != null && !conn.isClosed();
            if (isValid) {
                System.out.println("✅ Database connection test passed (" + currentDbType + ")");
            }
            return isValid;
        } catch (SQLException e) {
            System.err.println("❌ Connection test failed: " + e.getMessage());
            return false;
        }
    }

    public String getCurrentDatabaseType() {
        return currentDbType;
    }

    public boolean isUsingMySQL() {
        return "MySQL".equals(currentDbType);
    }

    public boolean isUsingPostgreSQL() {
        return "PostgreSQL".equals(currentDbType);
    }

    // Method to test database connectivity
    public static void main(String[] args) {
        System.out.println("Testing ME-SEUM Database Connection...");
        DatabaseUtil dbUtil = new DatabaseUtil();

        if (dbUtil.testConnection()) {
            System.out.println("🎉 Database is ready for use with " + dbUtil.getCurrentDatabaseType() + "!");
        } else {
            System.out.println("❌ Database connection failed. Please check database setup.");
        }
    }
}