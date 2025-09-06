// Main package for the application
package com.foodordering.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an order processor that handles payment and database updates.
 * This class acts as the 'Receiver' in the Command pattern for PlaceOrderCommand.
 * Modified to interact with the database.
 */
class OrderProcessor {
    private static final Logger LOGGER = Logger.getLogger(OrderProcessor.class.getName());

    public boolean processOrder(ShoppingCart cart, String customerName, String customerAddress) {
        if (cart.getCartItems().isEmpty()) {
            LOGGER.warning("Attempted to process an empty cart.");
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtOrderDetail = null;
        ResultSet rs = null; // For getting generated keys

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert into Orders table
            String insertOrderSQL = "INSERT INTO Orders (CustomerName, CustomerAddress, OrderDate, TotalAmount) VALUES (?, ?, ?, ?)";
            pstmtOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);

            pstmtOrder.setString(1, customerName);
            pstmtOrder.setString(2, customerAddress);
            pstmtOrder.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Current timestamp
            pstmtOrder.setDouble(4, cart.getTotal());

            int affectedRows = pstmtOrder.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            int orderId = 0;
            rs = pstmtOrder.getGeneratedKeys();
            if (rs.next()) {
                orderId = rs.getInt(1); // Get the auto-generated OrderID
            } else {
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            LOGGER.info("Order placed successfully with ID: " + orderId + " for total: $" + cart.getTotal());

            // 2. Insert into OrderDetails table for each item in the cart
            String insertOrderDetailSQL = "INSERT INTO OrderDetails (OrderID, FoodItemID, Quantity, PricePerItem) VALUES (?, ?, ?, ?)";
            pstmtOrderDetail = conn.prepareStatement(insertOrderDetailSQL);

            for (Map.Entry<FoodItem, Integer> entry : cart.getCartItems().entrySet()) {
                FoodItem item = entry.getKey();
                int quantity = entry.getValue();

                pstmtOrderDetail.setInt(1, orderId);
                pstmtOrderDetail.setInt(2, item.getFoodItemId()); // Use the database ID of the FoodItem
                pstmtOrderDetail.setInt(3, quantity);
                pstmtOrderDetail.setDouble(4, item.getPrice()); // Price at the time of sale

                pstmtOrderDetail.addBatch(); // Add to batch for efficient insertion
            }

            pstmtOrderDetail.executeBatch(); // Execute all batched inserts
            conn.commit(); // Commit transaction

            LOGGER.info("Order details saved successfully for Order ID: " + orderId);
            return true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error processing order: " + e.getMessage(), e);
            if (conn != null) {
                try {
                    LOGGER.warning("Transaction is being rolled back.");
                    conn.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error during transaction rollback.", ex);
                }
            }
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmtOrder != null) pstmtOrder.close();
                if (pstmtOrderDetail != null) pstmtOrderDetail.close();
                if (conn != null) conn.setAutoCommit(true); // Reset auto-commit
                DatabaseManager.closeConnection(conn);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing resources after order processing.", e);
            }
        }
    }
}