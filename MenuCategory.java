// Main package for the application
package com.foodordering.system;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite class for the Composite pattern.
 * Represents a category that can contain other MenuItems (FoodItems, SizedFoodItems, or other MenuCategories).
 * Modified to include database-related ID.
 */
class MenuCategory implements MenuItem {
    private int categoryId; // New: Corresponds to CategoryID in DB
    private String name;
    private List<MenuItem> items;

    // New constructor for database-fetched categories
    public MenuCategory(String name, int categoryId) {
        this.name = name;
        this.categoryId = categoryId;
        this.items = new ArrayList<>();
    }

    // Original constructor (might be kept for root menu or non-DB categories)
    public MenuCategory(String name) {
        this(name, 0); // Use 0 for categoryId if not from DB
    }

    public int getCategoryId() { // New Getter
        return categoryId;
    }

    public void add(MenuItem item) {
        items.add(item);
    }

    public void remove(MenuItem item) {
        items.remove(item);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        // Price of a category is not directly applicable.
        return 0.0;
    }

    /**
     * Display method for MenuCategory is mainly for organization in the GUI.
     * It does not display the category itself as an item.
     */
    @Override
    public void display(JPanel panel, ShoppingCart cart, JFrame parentFrame) {
        // This method is not used directly for display in the main menu flow.
        // Its children are iterated and displayed directly in FoodOrderingSystemGUI.
    }

    public List<MenuItem> getChildren() {
        return items;
    }

    @Override
    public boolean contains(String searchText) {
        // A category contains the search text if its name matches
        // or any of its children match.
        if (name.toLowerCase().contains(searchText.toLowerCase())) {
            return true;
        }
        for (MenuItem item : items) {
            if (item.contains(searchText)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<FoodItem> getItems() {
        List<FoodItem> allItems = new ArrayList<>();
        for (MenuItem item : items) {
            allItems.addAll(item.getItems()); // Recursively get items (either FoodItem or SizedFoodItem variants)
        }
        return allItems;
    }
}