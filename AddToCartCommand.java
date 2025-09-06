// Main package for the application
package com.foodordering.system;

/**
 * Concrete Command to add a food item to the shopping cart.
 */
class AddToCartCommand implements Command {
    private FoodItem item;
    private ShoppingCart cart;

    public AddToCartCommand(FoodItem item, ShoppingCart cart) {
        this.item = item;
        this.cart = cart;
    }

    @Override
    public void execute() {
        cart.addItem(item);
        System.out.println("Command: Added " + item.getName() + " to cart.");
    }
}
