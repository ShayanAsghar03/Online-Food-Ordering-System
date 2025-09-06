// Main package for the application
package com.foodordering.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Leaf class for the Composite pattern.
 * Represents an individual food item with a fixed name and price (e.g., "Veggie Delight Burger" or "Classic Beef Burger (Small)").
 * Modified to include database-related IDs for better integration with a persistence layer.
 */
class FoodItem implements MenuItem {
    private int foodItemId;       // Corresponds to FoodItemID in the database
    private String name;
    private double price;
    private String description;
    private String categoryName;  // Stores the category's name string (e.g., "Burgers")
    private int categoryId;       // Corresponds to CategoryID in the database

    /**
     * Detailed constructor for FoodItem, typically used when loading data from the database.
     * @param foodItemId Unique ID of the food item.
     * @param name The name of the food item.
     * @param price The price of the food item.
     * @param description A description of the food item.
     * @param categoryName The name of the category this item belongs to.
     * @param categoryId The ID of the category this item belongs to.
     */
    public FoodItem(int foodItemId, String name, double price, String description, String categoryName, int categoryId) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    /**
     * Simpler constructor for FoodItem, useful for creating items not directly tied to a database ID
     * or for initial data population/testing. FoodItemId and CategoryId default to 0.
     * @param name The name of the food item.
     * @param price The price of the food item.
     * @param description A description of the food item.
     * @param categoryName The name of the category this item belongs to.
     */
    public FoodItem(String name, double price, String description, String categoryName) {
        this(0, name, price, description, categoryName, 0); // Use 0 for IDs if not from DB
    }

    // --- Getters for all properties ---
    public int getFoodItemId() {
        return foodItemId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Displays the individual fixed-price food item on a given JPanel.
     * Includes an "Add to Cart" button which, when clicked, triggers an AddToCartCommand.
     * The command now uses the FoodWrapperBuilder to add a "wrapped" version of the item.
     * @param panel The JPanel where this food item's display component will be added.
     * @param cart The ShoppingCart instance to add items to.
     * @param parentFrame The parent JFrame, used for displaying messages (e.g., JOptionPane).
     */
    @Override
    public void display(JPanel panel, ShoppingCart cart, JFrame parentFrame) {
        // Create the main panel for the food item's display
        JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
        itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Padding around content
        itemPanel.setBackground(Color.WHITE); // Background color
        itemPanel.setPreferredSize(new Dimension(150, 150)); // Fixed preferred size for uniform display

        // Labels for name, price, and description with custom styling
        JLabel nameLabel = new JLabel("<html><b>" + name + "</b></html>");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(34, 139, 34)); // ForestGreen color for name

        JLabel priceLabel = new JLabel(String.format("$%.2f", price));
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setForeground(new Color(255, 69, 0)); // OrangeRed color for price

        // Use HTML to allow text wrapping for description
        JLabel descLabel = new JLabel("<html><p style=\"width:150px;\">" + description + "</p></html>");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        descLabel.setForeground(new Color(105, 105, 105)); // DimGray for description

        // Button to add the item to the cart
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 12));
        addToCartButton.setBackground(new Color(60, 179, 113)); // MediumSeaGreen background
        addToCartButton.setForeground(Color.WHITE); // White text
        addToCartButton.setFocusPainted(false); // Remove focus border
        addToCartButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // --- MODIFIED: Use FoodWrapperBuilder before adding to cart ---
        addToCartButton.addActionListener(e -> {
            // 1. Create a FoodWrapperBuilder instance
            FoodWrapperBuilder builder = new FoodWrapperBuilder();
            // 2. Build the FoodWrapper using the current FoodItem (this refers to the FoodItem instance itself)
            FoodWrapper wrappedItem = builder.withFoodItem(this).build();
            // 3. Create the AddToCartCommand with the wrapped item
            Command addToCartCommand = new AddToCartCommand(wrappedItem, cart);
            // 4. Execute the command. The ShoppingCart's addItem method will now display the custom wrapper message.
            addToCartCommand.execute();
            // The JOptionPane.showMessageDialog previously here is now handled within ShoppingCart.addItem()
        });
        // --- END MODIFICATION ---

        // Panel to hold the info labels
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(descLabel);

        // Add infoPanel and addToCartButton to the main item panel
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(addToCartButton, BorderLayout.SOUTH);

        // Finally, add the complete item panel to the parent panel provided
        panel.add(itemPanel);
    }

    /**
     * Checks if this food item's name, description, or category name contains the search text.
     * Used for filtering menu items based on user search input.
     * @param searchText The text to search for (case-insensitive).
     * @return true if the item contains the search text, false otherwise.
     */
    @Override
    public boolean contains(String searchText) {
        String lowerCaseSearchText = searchText.toLowerCase();
        return name.toLowerCase().contains(lowerCaseSearchText) ||
                description.toLowerCase().contains(lowerCaseSearchText) ||
                categoryName.toLowerCase().contains(lowerCaseSearchText); // Also search by category name
    }

    /**
     * Returns a list containing only this FoodItem.
     * This method is part of the MenuItem interface for consistency in the Composite pattern,
     * allowing uniform handling of leaf (FoodItem) and composite (MenuCategory) nodes.
     * @return A list containing this single FoodItem instance.
     */
    @Override
    public List<FoodItem> getItems() {
        List<FoodItem> singleItemList = new ArrayList<>();
        singleItemList.add(this);
        return singleItemList;
    }

    /**
     * Overrides the equals method for proper functioning in collections (e.g., HashMap keys in ShoppingCart).
     * Ensures that two FoodItem objects with the same `foodItemId` are considered equal.
     * @param o The object to compare with.
     * @return true if objects are equal (based on foodItemId), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodItem foodItem = (FoodItem) o;
        return foodItemId == foodItem.foodItemId; // Equality based on unique database ID
    }

    /**
     * Overrides the hashCode method, consistent with the equals method.
     * If two objects are equal according to the equals() method, then calling the hashCode method
     * on each of the two objects must produce the same integer result.
     * @return A hash code value for this object, based on its foodItemId.
     */
    @Override
    public int hashCode() {
        return Integer.valueOf(foodItemId).hashCode();
    }
}
