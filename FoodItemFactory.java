// Main package for the application
package com.foodordering.system;

/**
 * Abstract Factory interface for creating FoodItems.
 */
interface FoodItemFactory {
    FoodItem createFoodItem(String name, double price, String description);
}
