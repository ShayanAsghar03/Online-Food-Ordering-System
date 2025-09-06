// Main package for the application
package com.foodordering.system;

/**
 * Concrete Factory for creating Fried Chicken items.
 */
class FriedChickenFactory implements FoodItemFactory {
    @Override
    public FoodItem createFoodItem(String name, double price, String description) {
        return new FoodItem(name, price, description, "Fried Chicken");
    }
}
