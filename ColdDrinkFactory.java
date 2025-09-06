// Main package for the application
package com.foodordering.system;

/**
 * Concrete Factory for creating Cold Drink items.
 */
class ColdDrinkFactory implements FoodItemFactory {
    @Override
    public FoodItem createFoodItem(String name, double price, String description) {
        return new FoodItem(name, price, description, "Cold Drinks / Beverages");
    }
}
