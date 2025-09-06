// Main package for the application
package com.foodordering.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages database connections for the Food Ordering System.
 * This class provides a singleton-like access to database connection.
 * Remember to add the SQL Server JDBC driver to your project dependencies.
 */
public class DatabaseManager {
    // JDBC URL for your SQL Server instance
    // Replace 'localhost:1433' with your server address if different
    // Replace 'FoodOrderingSystem' with your database name if different
    // 'encrypt=true;trustServerCertificate=true;' is often needed for modern SQL Server connections.
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=FoodOrderingSystem;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa"; // !!! REPLACE WITH YOUR DATABASE USERNAME !!!
    private static final String PASS = "fitlife12345678"; // !!! REPLACE WITH YOUR DATABASE PASSWORD !!!

    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    /**
     * Establishes and returns a new database connection.
     * @return A valid database Connection object.
     * @throws SQLException If a database access error occurs or the URL is null.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure the JDBC driver is loaded (optional for modern JDBC 4.0+, but good practice)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQL Server JDBC Driver not found in classpath.", e);
            throw new SQLException("SQL Server JDBC Driver not found.", e);
        }
        LOGGER.info("Attempting to connect to database...");
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Closes the given database connection, statement, and result set, suppressing SQLExceptions.
     * @param connection The Connection to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection.", e);
            }
        }
    }
}