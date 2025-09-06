// Main package for the application
package com.foodordering.system;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating SizedFoodItem instances.
 * This is a separate factory as SizedFoodItem has different construction parameters.
 */
class SizedFoodItemFactory {
    public SizedFoodItem createSizedFoodItem(String baseName, String description, String category, double smallPrice, double mediumPrice, double largePrice) {
        Map<String, Double> sizePrices = new HashMap<>();
        sizePrices.put("Small", smallPrice);
        sizePrices.put("Medium", mediumPrice);
        sizePrices.put("Large", largePrice);
        return new SizedFoodItem(baseName, description, category, sizePrices);
    }
}
