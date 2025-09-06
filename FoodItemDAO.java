// Main package for the application
package com.foodordering.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for managing FoodItem and MenuCategory data
 * in the database. This class abstracts the database interactions, providing
 * methods to retrieve menu categories and food items.
 */
public class FoodItemDAO {
    private static final Logger LOGGER = Logger.getLogger(FoodItemDAO.class.getName());

    /**
     * Retrieves all categories from the Categories table in the database.
     * Categories are ordered by their name for consistent display.
     * @return A list of MenuCategory objects, each representing a category from the database.
     * @throws SQLException If a database access error occurs during the operation.
     */
    public List<MenuCategory> getAllCategories() throws SQLException {
        List<MenuCategory> categories = new ArrayList<>();
        String sql = "SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryName";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection(); // Get a database connection
            pstmt = conn.prepareStatement(sql);    // Prepare the SQL statement
            rs = pstmt.executeQuery();             // Execute the query

            while (rs.next()) {
                int categoryId = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");
                categories.add(new MenuCategory(categoryName, categoryId));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all categories from database.", e);
            throw e; // Re-throw the exception after logging
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return categories;
    }

    /**
     * Retrieves all food items belonging to a specific category from the database.
     * Items are joined with the Categories table to get category name.
     * @param categoryId The ID of the category to retrieve food items for.
     * @return A list of FoodItem objects belonging to the specified category.
     * @throws SQLException If a database access error occurs during the operation.
     */
    public List<FoodItem> getFoodItemsByCategory(int categoryId) throws SQLException {
        List<FoodItem> foodItems = new ArrayList<>();
        // SQL to join FoodItems with Categories to get category name
        String sql = "SELECT f.FoodItemID, f.Name, f.Price, f.Description, c.CategoryName, c.CategoryID " +
                "FROM FoodItems f JOIN Categories c ON f.CategoryID = c.CategoryID " +
                "WHERE f.CategoryID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId); // Set the category ID parameter
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int foodItemId = rs.getInt("FoodItemID");
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                String description = rs.getString("Description");
                String categoryName = rs.getString("CategoryName");
                int fetchedCategoryId = rs.getInt("CategoryID"); // Ensure this matches the input categoryId

                foodItems.add(new FoodItem(foodItemId, name, price, description, categoryName, fetchedCategoryId));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching food items for category ID " + categoryId + " from database.", e);
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return foodItems;
    }

    /**
     * Retrieves a single FoodItem by its FoodItemID from the database.
     * This method is useful for operations requiring details of a specific item,
     * e.g., when adding to cart if only the ID is known initially.
     * @param foodItemId The unique ID of the food item to retrieve.
     * @return A FoodItem object if found, null otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public FoodItem getFoodItemById(int foodItemId) throws SQLException {
        FoodItem foodItem = null;
        // SQL to join FoodItems with Categories to get category name
        String sql = "SELECT f.FoodItemID, f.Name, f.Price, f.Description, c.CategoryName, c.CategoryID " +
                "FROM FoodItems f JOIN Categories c ON f.CategoryID = c.CategoryID " +
                "WHERE f.FoodItemID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, foodItemId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                String description = rs.getString("Description");
                int categoryId = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName"); // Get the actual category name
                foodItem = new FoodItem(foodItemId, name, price, description, categoryName, categoryId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching food item by ID " + foodItemId + " from database.", e);
            throw e;
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return foodItem;
    }

    /**
     * A helper method to close database resources (Connection, PreparedStatement, ResultSet) safely.
     * This method suppresses SQLExceptions during closing, as it's typically used in finally blocks
     * where original exceptions should not be obscured.
     * @param conn The Connection object to close.
     * @param pstmt The PreparedStatement object to close.
     * @param rs The ResultSet object to close.
     */
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            // Use the DatabaseManager's utility method to close the connection
            DatabaseManager.closeConnection(conn);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database resources.", e);
        }
    }
}