// Main package for the application
package com.foodordering.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a food item that has different sizes (Small, Medium, Large) with varying prices.
 * It implements the MenuItem interface but handles its display and price calculation differently
 * by presenting size options to the user via a dialog.
 */
class SizedFoodItem implements MenuItem {
    private String baseName;          // Base name without size (e.g., "Classic Beef Burger")
    private String description;
    private String category;          // Category name (e.g., "Burgers")
    private Map<String, Double> sizePrices; // Map: "Small" -> price, "Medium" -> price, "Large" -> price

    /**
     * Constructs a SizedFoodItem with a base name, description, category, and a map of size-to-price mappings.
     * @param baseName The base name of the food item.
     * @param description A description of the food item.
     * @param category The category name this item belongs to.
     * @param sizePrices A map where keys are size names (e.g., "Small") and values are their corresponding prices.
     */
    public SizedFoodItem(String baseName, String description, String category, Map<String, Double> sizePrices) {
        this.baseName = baseName;
        this.description = description;
        this.category = category;
        this.sizePrices = sizePrices;
    }

    @Override
    public String getName() {
        return baseName; // Display only the base name (e.g., "Classic Beef Burger") in the menu list
    }

    @Override
    public double getPrice() {
        // This method is primarily for MenuItem interface compatibility when a single price is needed.
        // The actual price will be selected by the user in the size dialog.
        // Returns the smallest price as a representative, or 0.0 if no sizes are defined.
        return sizePrices.values().stream().min(Double::compare).orElse(0.0);
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public Map<String, Double> getSizePrices() {
        return sizePrices;
    }

    /**
     * Displays the SizedFoodItem on the provided JPanel.
     * Instead of an "Add to Cart" button directly, it shows a "Select Size" button
     * that opens a modal dialog for size selection.
     * @param panel The JPanel where this food item's display component will be added.
     * @param cart The ShoppingCart instance to add items to.
     * @param parentFrame The parent JFrame, used for displaying modal dialogs (e.g., JDialog).
     */
    @Override
    public void display(JPanel panel, ShoppingCart cart, JFrame parentFrame) {
        // Create the main panel for the sized food item's display
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout(5, 5)); // BorderLayout with gaps
        itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); // Light gray border
        itemPanel.setPreferredSize(new Dimension(200, 150)); // Fixed preferred size
        itemPanel.setBackground(Color.WHITE); // White background
        itemPanel.setOpaque(true); // Ensure background is painted
        itemPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Inner padding

        // Labels for base name and description
        JLabel nameLabel = new JLabel("<html><b>" + baseName + "</b></html>", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.ITALIC, 10));

        // Button to trigger size selection dialog
        JButton selectSizeButton = new JButton("Select Size");
        selectSizeButton.setBackground(new Color(70, 130, 180)); // SteelBlue background
        selectSizeButton.setForeground(Color.WHITE); // White text
        selectSizeButton.setFocusPainted(false); // Remove focus border
        selectSizeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        selectSizeButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        // Action listener to open the size selection dialog
        selectSizeButton.addActionListener(e -> {
            showSizeSelectionDialog(parentFrame, cart);
        });

        // Panel for info labels
        JPanel infoPanel = new JPanel(new GridLayout(2, 1)); // Two rows for name and description
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(nameLabel);
        infoPanel.add(descLabel);

        // Add infoPanel and selectSizeButton to the main item panel
        itemPanel.add(infoPanel, BorderLayout.CENTER);
        itemPanel.add(selectSizeButton, BorderLayout.SOUTH);

        // Add the complete item panel to the parent panel
        panel.add(itemPanel);
    }

    /**
     * Shows a modal dialog that allows the user to select a size for the food item
     * and then add the selected sized variant to the shopping cart.
     * @param parentFrame The parent JFrame for the dialog.
     * @param cart The ShoppingCart instance to add the selected item to.
     */
    private void showSizeSelectionDialog(JFrame parentFrame, ShoppingCart cart) {
        JDialog sizeDialog = new JDialog(parentFrame, "Select Size for " + baseName, true); // Modal dialog
        sizeDialog.setLayout(new BorderLayout(10, 10)); // BorderLayout with gaps
        sizeDialog.setSize(300, 200); // Fixed size for the dialog
        sizeDialog.setLocationRelativeTo(parentFrame); // Center relative to parent frame
        sizeDialog.setResizable(false); // Prevent resizing
        sizeDialog.setBackground(new Color(240, 248, 255)); // Light blue background

        // Panel for size options (radio buttons)
        JPanel sizeOptionsPanel = new JPanel(new GridLayout(sizePrices.size(), 1, 5, 5)); // Grid layout for options
        sizeOptionsPanel.setBorder(new EmptyBorder(10, 10, 0, 10)); // Padding
        sizeOptionsPanel.setBackground(new Color(240, 248, 255)); // Match dialog background

        ButtonGroup sizeButtonGroup = new ButtonGroup(); // Ensures only one size can be selected
        Map<JRadioButton, FoodItem> radioToFoodItemMap = new HashMap<>(); // Maps radio button to its corresponding FoodItem variant

        // Define a custom order for common sizes to ensure consistency (Small, Medium, Large)
        List<String> orderedSizes = Arrays.asList("Small", "Medium", "Large");
        List<String> sortedSizes = new ArrayList<>(sizePrices.keySet());

        // Sort sizes based on the custom order, falling back to alphabetical for other sizes
        sortedSizes.sort(Comparator.comparingInt(s -> {
            int index = orderedSizes.indexOf(s);
            return index == -1 ? Integer.MAX_VALUE : index; // Place undefined sizes at the end
        }));

        // Create a radio button for each available size
        for (String size : sortedSizes) {
            double price = sizePrices.get(size);
            JRadioButton radioButton = new JRadioButton(String.format("%s - $%.2f", size, price));
            radioButton.setFont(new Font("Arial", Font.PLAIN, 12));
            radioButton.setBackground(new Color(240, 248, 255)); // Match panel background
            sizeButtonGroup.add(radioButton); // Add to button group
            sizeOptionsPanel.add(radioButton); // Add to panel

            // Create a temporary FoodItem object representing this specific sized variant.
            // This FoodItem will be added to the cart if selected.
            FoodItem sizedVariant = new FoodItem(baseName + " (" + size + ")", price, description + " (" + size + " size)", category);
            radioToFoodItemMap.put(radioButton, sizedVariant); // Map radio button to its FoodItem
        }

        // Select the first size option by default if available
        if (!sortedSizes.isEmpty()) {
            ((JRadioButton) sizeOptionsPanel.getComponent(0)).setSelected(true);
        }

        // Panel for action buttons (Add to Cart, Cancel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Right-aligned flow layout
        buttonPanel.setBackground(new Color(240, 248, 255)); // Match dialog background

        // "Add to Cart" button
        JButton addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setBackground(new Color(60, 179, 113)); // MediumSeaGreen
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- MODIFIED: Use FoodWrapperBuilder before adding to cart ---
        addToCartBtn.addActionListener(e -> {
            FoodItem selectedFoodItem = null;
            // Find the selected radio button and get its corresponding FoodItem variant
            for (JRadioButton rb : radioToFoodItemMap.keySet()) {
                if (rb.isSelected()) {
                    selectedFoodItem = radioToFoodItemMap.get(rb);
                    break;
                }
            }

            if (selectedFoodItem != null) {
                // 1. Create a FoodWrapperBuilder instance
                FoodWrapperBuilder builder = new FoodWrapperBuilder();
                // 2. Build the FoodWrapper using the selected sized FoodItem
                FoodWrapper wrappedItem = builder.withFoodItem(selectedFoodItem).build();
                // 3. Create the AddToCartCommand with the wrapped item
                Command addToCartCommand = new AddToCartCommand(wrappedItem, cart);
                // 4. Execute the command. ShoppingCart.addItem will handle the message display.
                addToCartCommand.execute();
                sizeDialog.dispose(); // Close dialog after adding to cart
            } else {
                // If no size is selected (should not happen if first is default), warn the user.
                JOptionPane.showMessageDialog(sizeDialog, "Please select a size.", "No Size Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        // --- END MODIFICATION ---

        // "Cancel" button
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(220, 20, 60)); // Crimson
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> sizeDialog.dispose()); // Close dialog on cancel

        // Add buttons to the button panel
        buttonPanel.add(addToCartBtn);
        buttonPanel.add(cancelBtn);

        // Add panels to the dialog
        sizeDialog.add(sizeOptionsPanel, BorderLayout.CENTER);
        sizeDialog.add(buttonPanel, BorderLayout.SOUTH);
        sizeDialog.setVisible(true); // Make the dialog visible
    }

    /**
     * Checks if this SizedFoodItem's base name, description, or any of its sized variants'
     * full names contain the search text.
     * @param searchText The text to search for (case-insensitive).
     * @return true if the item or any of its variants matches the search text, false otherwise.
     */
    @Override
    public boolean contains(String searchText) {
        String lowerCaseSearchText = searchText.toLowerCase();
        // Check base name or description first
        if (baseName.toLowerCase().contains(lowerCaseSearchText) ||
                description.toLowerCase().contains(lowerCaseSearchText)) {
            return true;
        }
        // Also check if any of the size variants' generated names (e.g., "Burger (Small)")
        // would contain the search text.
        for (String size : sizePrices.keySet()) {
            if ((baseName + " (" + size + ")").toLowerCase().contains(lowerCaseSearchText)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of all possible FoodItem variants for this SizedFoodItem (e.g., "Small Burger", "Medium Burger").
     * This is crucial for search functionality and displaying "All Items" lists correctly,
     * as `SizedFoodItem` itself doesn't represent a single purchasable item but a concept with variants.
     * @return A list of FoodItem objects, each representing a specific size variant of this SizedFoodItem.
     */
    @Override
    public List<FoodItem> getItems() {
        List<FoodItem> items = new ArrayList<>();
        for(Map.Entry<String, Double> entry : sizePrices.entrySet()) {
            // Create a FoodItem for each size variant. Note: these temporary FoodItems
            // will have default foodItemId and categoryId as 0 if not explicitly set.
            items.add(new FoodItem(
                    baseName + " (" + entry.getKey() + ")", // Full name like "Burger (Small)"
                    entry.getValue(),                        // Price for this specific size
                    description + " (" + entry.getKey() + " size)", // Description specific to size
                    category                                 // Inherit category
            ));
        }
        return items;
    }
}
