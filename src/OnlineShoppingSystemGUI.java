import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.stream.Collectors;

public class OnlineShoppingSystemGUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JButton loginButton;
    private JButton createAccountButton;
    private JButton exitButton;
    UserManagement userManagement = new UserManagement();
    ShoppingCart shoppingCart = new ShoppingCart();
    String currentUser = null;
    Customer authenticatedUser;

    public OnlineShoppingSystemGUI() {
        setTitle("Online Shopping System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create Account");
        exitButton = new JButton("Exit");

        loginButton.addActionListener(e -> showLoginDialog());
        createAccountButton.addActionListener(e -> showAccountCreationDialog());
        exitButton.addActionListener(e -> System.exit(0));

        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginPanel.add(Box.createVerticalGlue());
        loginPanel.add(loginButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(createAccountButton);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(exitButton);
        loginPanel.add(Box.createVerticalGlue());

        cardPanel.add(loginPanel, "Login");

        add(cardPanel);
    }

    private void showLoginDialog() {

        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setLayout(new BoxLayout(loginDialog.getContentPane(), BoxLayout.Y_AXIS));
        loginDialog.setSize(300, 175);
    
        JLabel userLabel = new JLabel("Username:");
        JTextField userTextField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
    
        loginDialog.add(userLabel);
        loginDialog.add(userTextField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(loginButton);
        loginDialog.add(cancelButton);
    
        loginButton.addActionListener(e -> {
            String username = userTextField.getText();
            char[] password = passwordField.getPassword();
            
            boolean isAuthenticated = User.authenticate(username, new String(password));
            authenticatedUser = User.confirmAuth(username, new String(password));
            if (isAuthenticated && User.getUserRole(username, new String(password)).equals("admin")) {
                JOptionPane.showMessageDialog(loginDialog, "Login Successful");
                loginDialog.dispose();
                initializeAdminMenuBar();
                showLandingPage();
            }
            else if (isAuthenticated && User.getUserRole(username, new String(password)).equals("customer")) {
                JOptionPane.showMessageDialog(loginDialog, "Login Successful");
                currentUser = username;
                loginDialog.dispose();
                initializeCustomerMenuBar();
                showLandingPage();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                //clear the fields or take other actions
                userTextField.setText("");
                passwordField.setText("");
            }
        });
    
        cancelButton.addActionListener(e -> loginDialog.dispose());
    
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setVisible(true);
    }

    private void showAccountCreationDialog() {

        JDialog accountCreationDialog = new JDialog(this, "Create Account", true);
        accountCreationDialog.setLayout(new BoxLayout(accountCreationDialog.getContentPane(), BoxLayout.Y_AXIS));
        accountCreationDialog.setSize(300, 300);
    
        JLabel userLabel = new JLabel("Username:");
        JTextField userTextField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailTextField = new JTextField();
        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameTextField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressTextField = new JTextField();
        JButton createAccountButton = new JButton("Create Account");
        JButton cancelButton = new JButton("Cancel");
    
        accountCreationDialog.add(userLabel);
        accountCreationDialog.add(userTextField);
        accountCreationDialog.add(passwordLabel);
        accountCreationDialog.add(passwordField);
        accountCreationDialog.add(emailLabel);
        accountCreationDialog.add(emailTextField);
        accountCreationDialog.add(nameLabel);
        accountCreationDialog.add(nameTextField);
        accountCreationDialog.add(addressLabel);
        accountCreationDialog.add(addressTextField);
        accountCreationDialog.add(createAccountButton);
        accountCreationDialog.add(cancelButton);
    
        createAccountButton.addActionListener(e -> {
            String username = userTextField.getText();
            String password = passwordField.getText();
            String email = emailTextField.getText();
            String name = nameTextField.getText();
            String address = addressTextField.getText();
            
            if (username != null && password != null && email != null && name != null && address != null && userManagement.usernameExists(username) == false && userManagement.isValidEmail(email) == true){
                Customer newCustomer = new Customer(username, password, name, email, address);
                userManagement.addUser(newCustomer);
                userManagement.saveUsersToDB();
                JOptionPane.showMessageDialog(accountCreationDialog, "Account Created Successfully");
                accountCreationDialog.dispose();
            }
            //check if any field is empty
            else if (username == null || password == null || email == null || name == null || address == null) {
                JOptionPane.showMessageDialog(accountCreationDialog, "Please fill all fields", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
                //clear the fields or take other actions
            }
            // Check if username already exists
            else if (userManagement.usernameExists(username)){
                JOptionPane.showMessageDialog(accountCreationDialog, "That Username is Already Taken", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
                //clear the fields or take other actions
            }
            else if (userManagement.isValidEmail(email) == false){
                JOptionPane.showMessageDialog(accountCreationDialog, "Invalid Email Format", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
                //clear the fields or take other actions
            }
            else {
                JOptionPane.showMessageDialog(accountCreationDialog, "Account Creation Failed", "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
                //clear the fields or take other actions
            }
            
        });
    
        cancelButton.addActionListener(e -> accountCreationDialog.dispose());
    
        accountCreationDialog.setLocationRelativeTo(this); // Center on parent (the JFrame)
        accountCreationDialog.setVisible(true);
    }
    
    private void showLandingPage() {
        JPanel landingPagePanel = new JPanel();
        landingPagePanel.add(new JLabel("Welcome to the Online Shopping System"));
        cardPanel.add(landingPagePanel, "LandingPage");
    
        //show the landing page
        cardLayout.show(cardPanel, "LandingPage");
    }

    public void initializeCustomerMenuBar() {
        JMenuBar menuBar = getJMenuBar();
        if (menuBar == null) {
            menuBar = new JMenuBar();
        } else {
            menuBar.removeAll();
        }
    
        JMenu productsMenu = new JMenu("Products");
        JMenu ordersMenu = new JMenu("Orders");
        JMenu checkoutMenu = new JMenu("Checkout");
        JMenu accountMenu = new JMenu("Account Settings");
    
        JMenuItem viewProductsItem = new JMenuItem("View Products");
        viewProductsItem.addActionListener(e -> viewProducts());
        productsMenu.add(viewProductsItem);
    
        JMenuItem viewOrdersItem = new JMenuItem("View Orders");
        viewOrdersItem.addActionListener(e -> viewOrders());
        ordersMenu.add(viewOrdersItem);
    
        JMenuItem viewCheckoutItem = new JMenuItem("View Cart");
        // Ensure the panel identifier matches the expected value in the switchToPanel method
        viewCheckoutItem.addActionListener(e -> viewCart());
        checkoutMenu.add(viewCheckoutItem);
    
        JMenuItem checkoutNowItem = new JMenuItem("Checkout Now");
        // Ensure the panel identifier matches the expected value in the switchToPanel method
        checkoutNowItem.addActionListener(e -> checkOut());
        checkoutMenu.add(checkoutNowItem);
    
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        accountMenu.add(logoutItem);

        JMenuItem viewAndAddFundsItem = new JMenuItem("View/Add Funds");
        viewAndAddFundsItem.addActionListener(e -> viewAndAddFunds());
        accountMenu.add(viewAndAddFundsItem);
    
        menuBar.add(productsMenu);
        menuBar.add(ordersMenu);
        menuBar.add(checkoutMenu);
        menuBar.add(accountMenu);
    
        setJMenuBar(menuBar);
    
        invalidate();
        repaint();
    }

    private void initializeAdminMenuBar() {

        JMenuBar menuBar = getJMenuBar();
        if (menuBar == null) {
            menuBar = new JMenuBar();
        }
    
        JMenu adminMenu = new JMenu("Admin");
    
        JMenuItem editProductsItem = new JMenuItem("Edit Products");
        JMenuItem manageDiscountsItem = new JMenuItem("Manage Discounts");
        JMenuItem viewOrdersItem = new JMenuItem("View Orders");
        JMenuItem viewProductsItem = new JMenuItem("View Products");
    
        editProductsItem.addActionListener(e -> editProducts());
        manageDiscountsItem.addActionListener(e -> manageDiscounts());
        viewOrdersItem.addActionListener(e -> viewOrders());
        viewProductsItem.addActionListener(e -> viewProducts());

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        adminMenu.add(logoutItem);
    
        adminMenu.add(editProductsItem);
        adminMenu.add(manageDiscountsItem);
        adminMenu.add(viewOrdersItem);
        adminMenu.add(viewProductsItem);
    
        menuBar.add(adminMenu);
    
        setJMenuBar(menuBar);
    
        invalidate();
        repaint();
    }

    private void viewProducts() {
        InventoryManagement inventoryManagement = new InventoryManagement();
        List<Product> products = inventoryManagement.getAllProducts();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Product product : products) {
            listModel.addElement(product.getName() + " - $" + product.getPrice());
        }
    
        JList<String> productList = new JList<>(listModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setVisibleRowCount(10);
        JScrollPane productListScrollPane = new JScrollPane(productList);
    
        productList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    int index = productList.locationToIndex(e.getPoint());
                    Product selectedProduct = products.get(index);
                    shoppingCart.addProduct(selectedProduct);
                    JOptionPane.showMessageDialog(null, selectedProduct.getName() + " added to cart.");
                }
            }
        });
    
        cardPanel.add(productListScrollPane, "ViewProducts");
        switchToPanel("ViewProducts");
    }

    private void editProducts() {
        InventoryManagement inventoryManagement = new InventoryManagement();
        List<Product> products = inventoryManagement.getAllProducts();
        DefaultListModel<String> productsModel = new DefaultListModel<>();
        for (Product product : products) {
            productsModel.addElement(product.getName() + " - $" + product.getPrice() + " - Stock: " + inventoryManagement.getProductQuantity(product));
        }
        JPanel editProductsPanel = new JPanel();
        editProductsPanel.setLayout(new BorderLayout());

        // Model for the products list
        JList<String> productsList = new JList<>(productsModel);
        JScrollPane scrollPane = new JScrollPane(productsList);
        editProductsPanel.add(scrollPane, BorderLayout.CENTER);

        //menu for deleting a product
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        contextMenu.add(deleteItem);

        //listener for right-click
        productsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && !productsList.isSelectionEmpty() && productsList.locationToIndex(e.getPoint()) == productsList.getSelectedIndex()) {
                    contextMenu.show(productsList, e.getX(), e.getY());
                }
            }
        });

        //delete action
        deleteItem.addActionListener(e -> {
            String selectedProduct = productsList.getSelectedValue();
            if (selectedProduct != null) {
                int selectedIndex = productsList.getSelectedIndex();
                if (selectedIndex != -1) {
                    productsModel.remove(selectedIndex);
                    inventoryManagement.removeProduct(products.get(selectedIndex));
                }
            }
        });

    JPanel addProductPanel = new JPanel();
    JTextField nameField = new JTextField(10);
    JTextField priceField = new JTextField(5);
    JTextField stockField = new JTextField(5);
    JButton createButton = new JButton("Create");

    addProductPanel.add(new JLabel("Name:"));
    addProductPanel.add(nameField);
    addProductPanel.add(new JLabel("Price:"));
    addProductPanel.add(priceField);
    addProductPanel.add(new JLabel("Stock:"));
    addProductPanel.add(stockField);
    addProductPanel.add(createButton);

    //create action
    createButton.addActionListener(e -> {
        InventoryManagement im = new InventoryManagement();
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        int stock = Integer.parseInt(stockField.getText()); 
        Product product = new Product(name, price);
        im.addProduct(product, stock);
        productsModel.addElement(name + " - $" + price + " - Stock: " + stock);
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
    });

    editProductsPanel.add(addProductPanel, BorderLayout.SOUTH);

    // Add the editProductsPanel to the cardPanel and switch to it
    cardPanel.add(editProductsPanel, "EditProducts");
    switchToPanel("EditProducts");
}
    
    private void manageDiscounts() {

        Discount discount = new Discount();
    
        String message = "Current Discount Code: " + discount.getCode() + " (" + discount.getPercentage()*100 + "%)\n" +
                         discount.retrieveStatus() + "\n" +
                         "Do you want to toggle the discount status?";
        
        //confirm dialog to ask if the admin wants to toggle the discount status
        int response = JOptionPane.showConfirmDialog(null, message, "Manage Discounts", JOptionPane.YES_NO_OPTION);
        
        //toggle the discount status
        if (response == JOptionPane.YES_OPTION) {
            discount.changeActive();

            JOptionPane.showMessageDialog(null, "Discount status changed.\n" + discount.retrieveStatus());
        }
    }
    
    
    private void viewOrders() {
        String username = currentUser;
        
        // Retrieve all orders for the current user
        List<Order> orders = Order.retrieveUsersOrders(username);
        System.out.println(orders);
        System.out.println(username);
        // Model to hold the orders in a format suitable for display
        DefaultListModel<String> ordersModel = new DefaultListModel<>();
        for (Order order : orders) {
            // Formatting each order's information. Adjust as necessary.
            String orderInfo = "Order ID: " + order.getOrderId() +
                            ", Products: " + order.getProducts().stream().map(Product::getName).collect(Collectors.joining(", ")) +
                            ", Total: $" + order.getPayment().getAmount() +
                            ", Status: " + order.getStatus();
            ordersModel.addElement(orderInfo);
        }
        
        // Creating the JList to display orders
        JList<String> ordersList = new JList<>(ordersModel);
        JScrollPane scrollPane = new JScrollPane(ordersList);
        
        // Panel to hold the list
        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Assuming there's a method to add this panel to the main GUI and switch to it
        cardPanel.add(ordersPanel, "ViewOrders");
        switchToPanel("ViewOrders");
    }

    private void viewAndAddFunds() {
        JPanel fundsPanel = new JPanel(new BorderLayout());
        
        // Display current balance
        JLabel balanceLabel = new JLabel("Current Balance: $" + authenticatedUser.getBalance());
        fundsPanel.add(balanceLabel, BorderLayout.NORTH);
        
        // Panel for adding funds
        JPanel addFundsPanel = new JPanel();
        JTextField fundsField = new JTextField(10);
        JButton addFundsButton = new JButton("Add Funds");
        
        addFundsPanel.add(new JLabel("Amount:"));
        addFundsPanel.add(fundsField);
        addFundsPanel.add(addFundsButton);
        
        fundsPanel.add(addFundsPanel, BorderLayout.CENTER);
        
        // Action to add funds
        addFundsButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(fundsField.getText());
                if (amount > 0) {
                    authenticatedUser.changeBalance(-amount); // Since changeBalance subtracts, pass negative to add
                    balanceLabel.setText("Current Balance: $" + authenticatedUser.getBalance());
                    // Update the balance in the database
                    userManagement.updateUser(authenticatedUser);
                    authenticatedUser.updateBalance();
                    JOptionPane.showMessageDialog(this, "Funds added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a positive amount.", "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
            fundsField.setText(""); // Clear the input field
        });
        
        // Add the fundsPanel to the cardPanel and switch to it
        cardPanel.add(fundsPanel, "ViewAndAddFunds");
        switchToPanel("ViewAndAddFunds");
    }

    private void viewCart() {
        List<Product> cartProducts = shoppingCart.getProducts();
        DefaultListModel<String> cartModel = new DefaultListModel<>();
        for (Product product : cartProducts) {
            cartModel.addElement(product.getName() + " - $" + product.getPrice());
        }
    
        JList<String> cartList = new JList<>(cartModel);
        JScrollPane scrollPane = new JScrollPane(cartList);
    
        // Create a panel for buttons with FlowLayout
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    
        // Clear Cart button
        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.addActionListener(e -> {
            shoppingCart.clearCart(); // Assuming there's a method to clear the cart
            cartModel.clear(); // Clear the list model to update the UI
        });
        buttonsPanel.add(clearCartButton);
    
        // Checkout button
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> switchToPanel("Checkout"));
        buttonsPanel.add(checkoutButton);
    
        // Panel to hold both the list and buttons, using BorderLayout
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        cartPanel.add(buttonsPanel, BorderLayout.SOUTH);
    
        cardPanel.add(cartPanel, "ViewCart");
        switchToPanel("ViewCart");
    }

    private void checkOut() {
        // Step 1: Retrieve the products from the shopping cart
        List<Product> cartProducts = shoppingCart.getProducts();
        
        if (cartProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Step 2: Calculate the total amount
        double totalAmount = cartProducts.stream().mapToDouble(Product::getPrice).sum();
        
        // Step 3: Process the payment
        Payment payment = new Payment(totalAmount);
        // Assuming there's a method in Payment.java to process the payment
        boolean paymentSuccess = payment.processPayment(authenticatedUser);
        
        if (!paymentSuccess) {
            JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Payment Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Step 4: Create an Order object with the products and payment details
        Order order = new Order(authenticatedUser, payment);
        order.setProducts(new ArrayList<>(cartProducts)); // Assuming there's a setter for products
        order.setStatus();
        // Assuming there's a method in Order.java to save the order
        order.saveToDatabase();
        
        // Step 5: Clear the shopping cart
        shoppingCart.clearCart();
        
        // Step 6: Display a confirmation message
        JOptionPane.showMessageDialog(this, "Your order has been placed successfully!", "Order Placed", JOptionPane.INFORMATION_MESSAGE);
        
        // Optionally, switch to a different panel or update the UI as needed
        switchToPanel("OrderConfirmation");
    }

    //return to login and hide the menu bar
    public void logout() {
        this.setJMenuBar(null);
        this.invalidate();
        this.repaint();
        currentUser = null;
        switchToPanel("Login");
    }
    
    //switch panels
    private void switchToPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OnlineShoppingSystemGUI frame = new OnlineShoppingSystemGUI();
            frame.setVisible(true);
        });
    }
}