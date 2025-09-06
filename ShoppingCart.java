// Main package for the application
package com.foodordering.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane; // Added for displaying messages to the user

/**
 * Singleton class for the Shopping Cart.
 * Ensures only one instance of the shopping cart exists throughout the application.
 * Now includes logic to display wrapper messages for FoodWrapper items using JOptionPane.
 */
class ShoppingCart {
    private static ShoppingCart instance; // The single instance of the ShoppingCart
    private Map<FoodItem, Integer> items; // Map to store item and its quantity (FoodItem -> Quantity)
    private List<ShoppingCartListener> listeners; // List of observers to notify about cart changes

    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the item map and listener list.
     */
    private ShoppingCart() {
        items = new HashMap<>();
        listeners = new ArrayList<>();
    }

    /**
     * Provides the global access point to the ShoppingCart instance.
     * Creates the instance if it doesn't already exist (lazy initialization).
     * @return The singleton instance of ShoppingCart.
     */
    public static ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    /**
     * Adds a FoodItem to the shopping cart or increments its quantity if already present.
     * If the item is a FoodWrapper, it displays its specific wrapper message using a JOptionPane.
     * Otherwise, a generic "Item added" message is shown.
     * @param item The FoodItem (or FoodWrapper) to be added to the cart.
     */
    public void addItem(FoodItem item) {
        items.put(item, items.getOrDefault(item, 0) + 1); // Add item or increment quantity
        notifyListeners(); // Notify GUI listeners about cart change

        // --- START NEW/MODIFIED CODE FOR WRAPPER MESSAGE ---
        if (item instanceof FoodWrapper) {
            // If the item is a FoodWrapper, display its specific wrapper message.
            JOptionPane.showMessageDialog(null, ((FoodWrapper) item).getWrapperMessage(), "Item Packaged!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // For regular FoodItems (or if the FoodWrapperBuilder was not used), display a generic message.
            JOptionPane.showMessageDialog(null, item.getName() + " added to cart!", "Item Added", JOptionPane.INFORMATION_MESSAGE);
        }
        // --- END NEW/MODIFIED CODE FOR WRAPPER MESSAGE ---
    }

    /**
     * Removes one instance of a FoodItem from the cart. If the quantity drops to zero,
     * the item is completely removed from the cart.
     * @param item The FoodItem to be removed.
     */
    public void removeItem(FoodItem item) {
        if (items.containsKey(item)) {
            int quantity = items.get(item);
            if (quantity > 1) {
                items.put(item, quantity - 1); // Decrement quantity
            } else {
                items.remove(item); // Remove item if quantity is 1
            }
            notifyListeners(); // Notify GUI listeners
        }
    }

    /**
     * Clears all items from the shopping cart.
     */
    public void clearCart() {
        items.clear();
        notifyListeners(); // Notify GUI listeners
    }

    /**
     * Returns a copy of the current items in the shopping cart with their quantities.
     * This prevents external modification of the cart's internal item map.
     * @return A Map where keys are FoodItem objects and values are their quantities.
     */
    public Map<FoodItem, Integer> getCartItems() {
        return new HashMap<>(items); // Return a copy to prevent external modification
    }

    /**
     * Calculates the total price of all items currently in the shopping cart.
     * @return The total monetary value of the cart.
     */
    public double getTotal() {
        return items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    /**
     * Adds a listener to be notified when the shopping cart content changes.
     * This is part of the Observer pattern.
     * @param listener The ShoppingCartListener to add.
     */
    public void addListener(ShoppingCartListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the notification list.
     * @param listener The ShoppingCartListener to remove.
     */
    public void removeListener(ShoppingCartListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies all registered listeners that the shopping cart has been updated.
     * This method is called internally whenever items are added or removed.
     */
    private void notifyListeners() {
        for (ShoppingCartListener listener : listeners) {
            listener.cartUpdated();
        }
    }
}
