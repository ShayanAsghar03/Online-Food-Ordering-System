// Main package for the application
package com.foodordering.system;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder class for creating FoodWrapper instances.
 * This builder determines a specific wrapper message based on the food item's category,
 * allowing for customized messages when items are added to the cart.
 */
class FoodWrapperBuilder {
    private FoodItem foodItem;
    private String determinedMessage;

    /**
     * Sets the FoodItem that will be wrapped and automatically determines
     * the appropriate wrapper message based on its category.
     * @param item The FoodItem object to be wrapped.
     * @return The current FoodWrapperBuilder instance for method chaining.
     */
    public FoodWrapperBuilder withFoodItem(FoodItem item) {
        this.foodItem = item;
        // Determine the message based on the item's category and name
        this.determinedMessage = determineWrapperMessage(item.getCategoryName(), item.getName());
        return this;
    }

    /**
     * Constructs and returns a new FoodWrapper instance.
     * This method should be called after 'withFoodItem' has been set.
     * @return A new FoodWrapper object containing the original FoodItem's data and the determined wrapper message.
     * @throws IllegalStateException If 'withFoodItem' has not been called before 'build'.
     */
    public FoodWrapper build() {
        if (foodItem == null) {
            throw new IllegalStateException("FoodItem must be set using withFoodItem() before calling build().");
        }
        // Create a new FoodWrapper using all properties from the original FoodItem
        // and the dynamically determined wrapper message.
        return new FoodWrapper(
                foodItem.getFoodItemId(),     // Assuming FoodItem has a getFoodItemId()
                foodItem.getName(),
                foodItem.getPrice(),
                foodItem.getDescription(),
                foodItem.getCategoryName(),
                foodItem.getCategoryId(),     // Assuming FoodItem has a getCategoryId()
                this.determinedMessage
        );
    }

    /**
     * Internal method to determine the specific wrapper message for a food item
     * based on its category name and item name.
     * @param categoryName The name of the category the food item belongs to.
     * @param itemName The name of the food item itself.
     * @return A tailored string message indicating how the item is wrapped.
     */
    private String determineWrapperMessage(String categoryName, String itemName) {
        // Customize messages for each category to be distinct and informative.
        switch (categoryName) {
            case "Burgers":
                return String.format("Your delicious %s is securely nestled in a sturdy box!", itemName);
            case "Cold Drinks":
                return String.format("Refreshing %s is perfectly bottled and chilled!", itemName);
            case "Desserts":
                return String.format("A sweet %s has been delicately placed in a charming dessert container!", itemName);
            case "Fried Chicken":
                return String.format("Your crispy %s feast is hot and ready in a specialized bucket!", itemName);
            case "Fries":
                return String.format("Golden %s are crisply bagged for your enjoyment!", itemName);
            case "Noodles":
                return String.format("Steaming %s are carefully sealed in a convenient takeout bowl!", itemName);
            case "Pizzas":
                return String.format("Your freshly baked %s is snug in its signature delivery box!", itemName);
            case "Salads":
                return String.format("The vibrant %s is packed in a clear, eco-friendly container!", itemName);
            case "Sandwiches":
                return String.format("Your gourmet %s is neatly wrapped and ready for a bite!", itemName);
            case "Wraps & Rolls":
                return String.format("The flavorful %s is tightly rolled and secured for delivery!", itemName);
            default:
                // A generic fallback message for categories not explicitly handled.
                return String.format("%s has been thoughtfully packaged for your order!", itemName);
        }
    }
}
