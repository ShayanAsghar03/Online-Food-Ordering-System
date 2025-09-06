// Main package for the application
package com.foodordering.system;

import javax.swing.*;
import java.util.List;

/**
 * Component interface for the Composite pattern.
 * Represents any item or category in the menu.
 */
interface MenuItem {
    String getName();
    double getPrice(); // This price might be a base price or 0.0 for sizable items.
    void display(JPanel panel, ShoppingCart cart, JFrame parentFrame);
    boolean contains(String searchText);
    List<FoodItem> getItems(); // For MenuCategory to return its children, for SizedFoodItem, returns its variants.
}
