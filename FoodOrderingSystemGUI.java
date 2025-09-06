// Main package for the application
package com.foodordering.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.NumberFormat; // Import for currency formatting
import java.util.Locale;       // Import for locale for currency formatting

public class FoodOrderingSystemGUI extends JFrame implements ShoppingCartListener {
    private JTextField searchField;
    private JPanel categoryPanel;
    private JPanel menuDisplayPanel;
    private JPanel cartPanel;
    private CardLayout menuCardLayout;
    private JPanel menuCards;

    private ShoppingCart cart;
    private MenuCategory rootMenu;
    private OrderProcessor orderProcessor;
    private FoodItemDAO foodItemDAO;

    private List<FoodItem> allIndividualFoodItemsForSearch;

    private static final Logger LOGGER = Logger.getLogger(FoodOrderingSystemGUI.class.getName());

    // UI Components for Cart Display
    private JTextArea cartTextArea; // To display items in the cart
    private JLabel totalLabel;     // To display the total price
    private JButton checkoutButton;

    public FoodOrderingSystemGUI() {
        // Basic JFrame setup
        setTitle("Food Ordering System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Initialize cart and add this GUI as a listener
        cart = ShoppingCart.getInstance();
        cart.addListener(this); // Register GUI to listen for cart changes

        orderProcessor = new OrderProcessor();
        foodItemDAO = new FoodItemDAO();

        // Initialize UI Components
        setupNorthPanel();
        setupMenuPanel();
        setupCartPanel();
        loadMenuData(); // Load menu from database

        // Initial cart display update
        cartUpdated(); // Call immediately to set up initial cart state

        // Add padding to the main frame
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void setupNorthPanel() {
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.setBackground(new Color(25, 25, 112)); // Midnight Blue
        northPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Food Ordering System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        northPanel.add(titleLabel, BorderLayout.NORTH);

        searchField = new JTextField("Search menu...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setForeground(Color.GRAY);
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search menu...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search menu...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        searchField.addActionListener(e -> performSearch(searchField.getText()));
        northPanel.add(searchField, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);
    }

    private void setupMenuPanel() {
        menuCardLayout = new CardLayout();
        menuCards = new JPanel(menuCardLayout);
        menuCards.setBorder(BorderFactory.createTitledBorder("Menu Categories"));

        categoryPanel = new JPanel();
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
        JScrollPane categoryScrollPane = new JScrollPane(categoryPanel);
        categoryScrollPane.setPreferredSize(new Dimension(200, 0));
        categoryScrollPane.setBorder(BorderFactory.createEmptyBorder());

        menuDisplayPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // Grid for menu items
        JScrollPane menuScrollPane = new JScrollPane(menuDisplayPanel);
        menuScrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.add(categoryScrollPane, BorderLayout.WEST);
        centerPanel.add(menuScrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupCartPanel() {
        cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout(5, 5));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Shopping Cart"));
        cartPanel.setPreferredSize(new Dimension(300, getHeight()));
        cartPanel.setBackground(new Color(240, 248, 255)); // Alice Blue

        cartTextArea = new JTextArea();
        cartTextArea.setEditable(false);
        cartTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane cartScrollPane = new JScrollPane(cartTextArea);
        cartPanel.add(cartScrollPane, BorderLayout.CENTER);

        JPanel southCartPanel = new JPanel(new BorderLayout());
        southCartPanel.setBackground(new Color(240, 248, 255));

        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(0, 128, 0)); // Green
        totalLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        southCartPanel.add(totalLabel, BorderLayout.NORTH);

        checkoutButton = new JButton("Checkout");
        checkoutButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        checkoutButton.setFocusPainted(false);
        checkoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutButton.addActionListener(e -> showPaymentDialog());
        southCartPanel.add(checkoutButton, BorderLayout.SOUTH);

        cartPanel.add(southCartPanel, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.EAST);
    }

    private void loadMenuData() {
        try {
            rootMenu = new MenuCategory("Root Menu", 0); // Root category
            allIndividualFoodItemsForSearch = new ArrayList<>();

            List<MenuCategory> categories = foodItemDAO.getAllCategories();
            for (MenuCategory category : categories) {
                // Add category button
                JButton categoryButton = new JButton(category.getName());
                styleCategoryButton(categoryButton);
                categoryButton.addActionListener(e -> displayMenuItems(category));
                categoryPanel.add(categoryButton);
                categoryPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Spacer

                // Add food items to the category using the new DAO method
                List<FoodItem> itemsInCategory = foodItemDAO.getFoodItemsByCategory(category.getCategoryId());
                for (FoodItem item : itemsInCategory) {
                    category.add(item); // Add food item to the MenuCategory composite
                    allIndividualFoodItemsForSearch.add(item); // For overall search
                }
            }
            // Display the first category's items by default if available
            if (!categories.isEmpty()) {
                displayMenuItems(categories.get(0));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load menu data from database.", e);
            JOptionPane.showMessageDialog(this, "Error loading menu: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleCategoryButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    }

    private void displayMenuItems(MenuCategory category) {
        menuDisplayPanel.removeAll(); // Clear previous items

        for (MenuItem item : category.getChildren()) {
            JPanel itemCard = new JPanel(new BorderLayout());
            itemCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            itemCard.setBackground(Color.WHITE);

            // Use the display method from MenuItem (FoodItem or SizedFoodItem)
            item.display(itemCard, cart, this); // 'this' refers to the parent JFrame for dialogs

            menuDisplayPanel.add(itemCard);
        }
        menuDisplayPanel.revalidate();
        menuDisplayPanel.repaint();
    }

    private void performSearch(String query) {
        if (query.equals("Search menu...") || query.trim().isEmpty()) {
            // If search field is empty or default text, redisplay the first category
            if (rootMenu != null && !rootMenu.getChildren().isEmpty()) {
                // Assuming categories are direct children of rootMenu for simplicity here
                // In a more complex composite, you'd traverse to find the first MenuCategory
                for (MenuItem item : rootMenu.getChildren()) {
                    if (item instanceof MenuCategory) {
                        displayMenuItems((MenuCategory) item);
                        break;
                    }
                }
            }
            return;
        }

        menuDisplayPanel.removeAll();
        String lowerCaseQuery = query.toLowerCase();

        // Search through all individual FoodItems collected for search
        for (FoodItem item : allIndividualFoodItemsForSearch) {
            if (item.contains(lowerCaseQuery)) {
                JPanel itemCard = new JPanel(new BorderLayout());
                itemCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                itemCard.setBackground(Color.WHITE);
                item.display(itemCard, cart, this);
                menuDisplayPanel.add(itemCard);
            }
        }

        menuDisplayPanel.revalidate();
        menuDisplayPanel.repaint();

        if (menuDisplayPanel.getComponentCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void showPaymentDialog() {
        // Check if cart is empty before showing payment dialog
        if (cart.getCartItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Please add items before checking out.", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog paymentDialog = new JDialog(this, "Complete Your Order", true);
        paymentDialog.setLayout(new BorderLayout(10, 10));
        paymentDialog.setSize(400, 350);
        paymentDialog.setLocationRelativeTo(this);
        paymentDialog.setResizable(false);

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        formPanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField cardNumberField = new JTextField();
        JPasswordField cvvField = new JPasswordField(); // Use JPasswordField for CVV

        formPanel.add(new JLabel("Customer Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Delivery Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Card Number:"));
        formPanel.add(cardNumberField);
        formPanel.add(new JLabel("CVV:"));
        formPanel.add(cvvField);

        paymentDialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(0, 15, 15, 15));

        JButton placeOrderBtn = new JButton("Place Order");
        placeOrderBtn.setBackground(new Color(34, 139, 34)); // Forest Green
        placeOrderBtn.setForeground(Color.WHITE);
        placeOrderBtn.setFont(new Font("Arial", Font.BOLD, 14));
        placeOrderBtn.setFocusPainted(false);
        placeOrderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        placeOrderBtn.addActionListener(e -> {
            String customerName = nameField.getText().trim();
            String customerAddress = addressField.getText().trim();
            String cardNumber = cardNumberField.getText().trim();
            char[] cvv = cvvField.getPassword();

            if (customerName.isEmpty() || customerAddress.isEmpty() ||
                    cardNumber.isEmpty() || cvv.length == 0) {
                JOptionPane.showMessageDialog(paymentDialog, "Please fill in all details.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Command Pattern: Invoker (Button) executing a Command
            // Pass customer details to the PlaceOrderCommand which will pass to OrderProcessor
            Command placeOrderCommand = new PlaceOrderCommand(cart, orderProcessor, this, customerName, customerAddress);
            placeOrderCommand.execute();
            paymentDialog.dispose(); // Close dialog after order attempt
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(220, 20, 60)); // Crimson
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> paymentDialog.dispose());

        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(cancelBtn);

        paymentDialog.add(buttonPanel, BorderLayout.SOUTH);
        paymentDialog.setVisible(true);
    }

    // This method is called by the ShoppingCart whenever its contents change.
    @Override
    public void cartUpdated() {
        updateCartDisplay(); // Delegate to a helper method to update the UI
    }

    /**
     * Updates the shopping cart display in the GUI.
     * This method clears the existing display, iterates through the current cart items,
     * and repopulates the JTextArea and updates the total price JLabel.
     */
    private void updateCartDisplay() {
        StringBuilder cartContent = new StringBuilder();
        double currentTotal = 0.0;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US); // For formatting currency

        // Iterate through items in the shopping cart map
        for (Map.Entry<FoodItem, Integer> entry : cart.getCartItems().entrySet()) {
            FoodItem item = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = item.getPrice();
            double subtotal = itemPrice * quantity;

            // Append item details to the display area
            cartContent.append(String.format("%d x %s @ %s = %s%n",
                    quantity,
                    item.getName(),
                    currencyFormat.format(itemPrice),
                    currencyFormat.format(subtotal)));

            currentTotal += subtotal; // Accumulate total
        }

        // Set the text area content
        cartTextArea.setText(cartContent.toString());

        // Update the total price label
        totalLabel.setText("Total: " + currencyFormat.format(currentTotal));

        // Enable/disable checkout button based on cart content
        checkoutButton.setEnabled(!cart.getCartItems().isEmpty());
    }

    public static void main(String[] args) {
        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new FoodOrderingSystemGUI().setVisible(true);
        });
    }
}