// Main package for the application
package com.foodordering.system;

import javax.swing.*;

/**
 * Concrete Command to simulate placing an order.
 * Modified to pass customer name and address to the OrderProcessor.
 */
class PlaceOrderCommand implements Command {
    private ShoppingCart cart;
    private OrderProcessor processor;
    private JFrame parentFrame; // To show messages
    private String customerName; // New
    private String customerAddress; // New

    // Updated constructor
    public PlaceOrderCommand(ShoppingCart cart, OrderProcessor processor, JFrame parentFrame, String customerName, String customerAddress) {
        this.cart = cart;
        this.processor = processor;
        this.parentFrame = parentFrame;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
    }

    @Override
    public void execute() {
        if (cart.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "Your cart is empty. Please add items before placing an order.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulate order processing, now passing customer details
        boolean success = processor.processOrder(cart, customerName, customerAddress);

        if (success) {
            // Updated confirmation message
            JOptionPane.showMessageDialog(parentFrame, "Order Placed Successfully!\\nYour order is on the way to you!", "Order Confirmation", JOptionPane.INFORMATION_MESSAGE);
            cart.clearCart(); // Clear cart after successful order
        } else {
            JOptionPane.showMessageDialog(parentFrame, "Order placement failed. Please check your details.", "Order Failed", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Command: Order processing initiated.");
    }
}