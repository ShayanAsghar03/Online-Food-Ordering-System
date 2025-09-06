// Main package for the application
package com.foodordering.system;

/**
 * Concrete Factory for creating Sandwich items.
 */
class SandwichFactory implements FoodItemFactory {
    @Override
    public FoodItem createFoodItem(String name, double price, String description) {
        return new FoodItem(name, price, description, "Sandwiches");
    }
}
