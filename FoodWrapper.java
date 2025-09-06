// Main package for the application
package com.foodordering.system;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * A wrapper class that extends FoodItem to include a specific message
 * indicating how the food item is "wrapped" or prepared for delivery.
 * This acts as a decorator for FoodItem with an added message property.
 */
class FoodWrapper extends FoodItem {
    private String wrapperMessage;

    /**
     * Constructor for FoodWrapper using the detailed FoodItem constructor.
     * Inherits all properties from FoodItem and adds a wrapper message.
     */
    public FoodWrapper(int foodItemId, String name, double price, String description, String categoryName, int categoryId, String wrapperMessage) {
        super(foodItemId, name, price, description, categoryName, categoryId);
        this.wrapperMessage = wrapperMessage;
    }

    /**
     * Constructor for FoodWrapper using the simpler FoodItem constructor.
     * Useful for items not fetched from DB or for testing.
     */
    public FoodWrapper(String name, double price, String description, String categoryName, String wrapperMessage) {
        super(name, price, description, categoryName);
        this.wrapperMessage = wrapperMessage;
    }

    /**
     * Retrieves the specific wrapper message for this food item.
     * @return The wrapper message string.
     */
    public String getWrapperMessage() {
        return wrapperMessage;
    }

    /**
     * Overrides the display method to potentially add wrapper-specific UI.
     * For this feature, the primary change is the message displayed on cart addition,
     * so it delegates to the superclass display method.
     */
    @Override
    public void display(JPanel panel, ShoppingCart cart, JFrame parentFrame) {
        // Call the superclass method to display the base food item details.
        // If additional visual changes related to the 'wrapper' were needed,
        // they would be implemented here.
        super.display(panel, cart, parentFrame);
    }
}
