import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

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
        ShoppingSystemDB.initializeDatabase();

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

    // Display login dialog
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
                currentUser = username;
                loginDialog.dispose();
                initializeAdminMenuBar();
                showLandingPage();
            } else if (isAuthenticated && User.getUserRole(username, new String(password)).equals("customer")) {
                JOptionPane.showMessageDialog(loginDialog, "Login Successful");
                currentUser = username;
                loginDialog.dispose();
                initializeCustomerMenuBar();
                showLandingPage();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password", "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
                userTextField.setText("");
                passwordField.setText("");
            }
        });

        cancelButton.addActionListener(e -> loginDialog.dispose());

        loginDialog.setLocationRelativeTo(this);
        loginDialog.setVisible(true);
    }

    // Display account creation dialog
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

            if (username != null && password != null && email != null && name != null && address != null
                    && !userManagement.usernameExists(username) && userManagement.isValidEmail(email)) {
                Customer newCustomer = new Customer(username, password, name, email, address);
                userManagement.addUser(newCustomer);
                userManagement.saveUsersToDB();
                JOptionPane.showMessageDialog(accountCreationDialog, "Account Created Successfully");
                accountCreationDialog.dispose();
            } else if (username == null || password == null || email == null || name == null || address == null) {
                JOptionPane.showMessageDialog(accountCreationDialog, "Please fill all fields",
                        "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
            } else if (userManagement.usernameExists(username)) {
                JOptionPane.showMessageDialog(accountCreationDialog, "That Username is Already Taken",
                        "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
            } else if (!userManagement.isValidEmail(email)) {
                JOptionPane.showMessageDialog(accountCreationDialog, "Invalid Email Format", "Account Creation Failed",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(accountCreationDialog, "Account Creation Failed",
                        "Account Creation Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> accountCreationDialog.dispose());

        accountCreationDialog.setLocationRelativeTo(this);
        accountCreationDialog.setVisible(true);
    }

    // Show landing page
    private void showLandingPage() {
        JPanel landingPagePanel = new JPanel();
        landingPagePanel.add(new JLabel("Welcome to the Online Shopping System"));
        cardPanel.add(landingPagePanel, "LandingPage");
        cardLayout.show(cardPanel, "LandingPage");
    }

    // Initialize customer menu bar
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
        viewCheckoutItem.addActionListener(e -> viewCart());
        checkoutMenu.add(viewCheckoutItem);

        JMenuItem checkoutNowItem = new JMenuItem("Checkout Now");
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

    // Initialize admin menu bar
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

    // Display products available for customers
    private void viewProducts() {
        InventoryManagement inventoryManagement = new InventoryManagement();
        List<Map.Entry<Product, Integer>> productsWithQuantities = inventoryManagement.getAllProducts();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (Map.Entry<Product, Integer> entry : productsWithQuantities) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            listModel.addElement(product.getName() + " - $" + product.getPrice() + " - Quantity: " + quantity);
        }

        JList<String> productList = new JList<>(listModel);
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setVisibleRowCount(10);
        JScrollPane productListScrollPane = new JScrollPane(productList);

        productList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    int index = productList.locationToIndex(e.getPoint());
                    Product selectedProduct = productsWithQuantities.get(index).getKey();
                    inventoryManagement.moveToShoppingCart(selectedProduct, currentUser);
                    JOptionPane.showMessageDialog(null, selectedProduct.getName() + " added to cart.");
                }
            }
        });

        cardPanel.add(productListScrollPane, "ViewProducts");
        switchToPanel("ViewProducts");
    }

    // Edit products for admin
    private void editProducts() {
        InventoryManagement inventoryManagement = new InventoryManagement();
        List<Map.Entry<Product, Integer>> productsWithQuantities = inventoryManagement.getAllProducts();
        DefaultListModel<String> productsModel = new DefaultListModel<>();

        for (Map.Entry<Product, Integer> entry : productsWithQuantities) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            productsModel.addElement(product.getName() + " - $" + product.getPrice() + " - Stock: " + quantity);
        }

        JPanel editProductsPanel = new JPanel();
        editProductsPanel.setLayout(new BorderLayout());

        JList<String> productsList = new JList<>(productsModel);
        JScrollPane scrollPane = new JScrollPane(productsList);
        editProductsPanel.add(scrollPane, BorderLayout.CENTER);

        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        contextMenu.add(deleteItem);

        productsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && !productsList.isSelectionEmpty()
                        && productsList.locationToIndex(e.getPoint()) == productsList.getSelectedIndex()) {
                    contextMenu.show(productsList, e.getX(), e.getY());
                }
            }
        });

        deleteItem.addActionListener(e -> {
            int selectedIndex = productsList.getSelectedIndex();
            if (selectedIndex != -1) {
                Map.Entry<Product, Integer> selectedEntry = productsWithQuantities.get(selectedIndex);
                Product selectedProduct = selectedEntry.getKey();
                productsModel.remove(selectedIndex);
                inventoryManagement.removeProduct(selectedProduct);
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

        createButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            Product product = new Product(name, price);
            inventoryManagement.addProduct(product, stock);
            productsModel.addElement(name + " - $" + price + " - Stock: " + stock);
            nameField.setText("");
            priceField.setText("");
            stockField.setText("");
        });

        editProductsPanel.add(addProductPanel, BorderLayout.SOUTH);

        cardPanel.add(editProductsPanel, "EditProducts");
        switchToPanel("EditProducts");
    }

    // Manage discounts for admin
    private void manageDiscounts() {
        Discount discount = new Discount();

        String message = "Current Discount Code: " + discount.getCode() + " (" + discount.getPercentage() * 100 + "%)\n"
                + discount.retrieveStatus() + "\n" +
                "Do you want to toggle the discount status?";

        int response = JOptionPane.showConfirmDialog(null, message, "Manage Discounts", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            discount.changeActive();
            JOptionPane.showMessageDialog(null, "Discount status changed.\n" + discount.retrieveStatus());
        }
    }

    // Display orders for both customers and admins
    private void viewOrders() {
        DefaultListModel<String> ordersModel = new DefaultListModel<>();

        if (authenticatedUser.getRole().equals("admin")) {
            List<Order> orders = Order.retrieveAllOrders();

            for (Order order : orders) {
                StringBuilder productsDetails = new StringBuilder();
                HashMap<String, Integer> productQuantities = new HashMap<>();
                for (Product product : order.getProducts()) {
                    productQuantities.put(product.getName(), productQuantities.getOrDefault(product.getName(), 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                    productsDetails.append(entry.getKey()).append(" (Quantity: ").append(entry.getValue())
                            .append(")<br>");
                }

                String orderInfo = "<html>Order ID: " + order.getOrderId() +
                        "<br>Customer: " + order.getCustomer().getUsername() +
                        "<br>Products:<br>" +
                        productsDetails.toString() +
                        "Total: $" + order.getPayment().getAmount() +
                        "<br>Status: " + order.getStatus() +
                        "</html>";

                ordersModel.addElement(orderInfo);
            }
        } else {
            List<Order> orders = Order.retrieveUsersOrders(currentUser);

            for (Order order : orders) {
                StringBuilder productsDetails = new StringBuilder();
                HashMap<String, Integer> productQuantities = new HashMap<>();
                for (Product product : order.getProducts()) {
                    productQuantities.put(product.getName(), productQuantities.getOrDefault(product.getName(), 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
                    productsDetails.append(entry.getKey()).append(" (Quantity: ").append(entry.getValue())
                            .append(")<br>");
                }

                String orderInfo = "<html>Order ID: " + order.getOrderId() +
                        "<br>Products:<br>" +
                        productsDetails.toString() +
                        "Total: $" + order.getPayment().getAmount() +
                        "<br>Status: " + order.getStatus() +
                        "</html>";

                ordersModel.addElement(orderInfo);
            }
        }

        JList<String> ordersList = new JList<>(ordersModel);
        JScrollPane scrollPane = new JScrollPane(ordersList);

        JPanel ordersPanel = new JPanel(new BorderLayout());
        ordersPanel.add(scrollPane, BorderLayout.CENTER);

        cardPanel.add(ordersPanel, "ViewOrders");
        switchToPanel("ViewOrders");
    }

    // View and add funds to customer account
    private void viewAndAddFunds() {
        JPanel fundsPanel = new JPanel(new BorderLayout());

        JLabel balanceLabel = new JLabel("Current Balance: $" + authenticatedUser.getBalance());
        fundsPanel.add(balanceLabel, BorderLayout.NORTH);

        JPanel addFundsPanel = new JPanel();
        JTextField fundsField = new JTextField(10);
        JButton addFundsButton = new JButton("Add Funds");

        addFundsPanel.add(new JLabel("Amount:"));
        addFundsPanel.add(fundsField);
        addFundsPanel.add(addFundsButton);

        fundsPanel.add(addFundsPanel, BorderLayout.CENTER);

        addFundsButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(fundsField.getText());
                if (amount > 0) {
                    authenticatedUser.setBalance(authenticatedUser.getBalance() + amount);
                    balanceLabel.setText("Current Balance: $" + authenticatedUser.getBalance());

                    authenticatedUser.updateBalance();
                    JOptionPane.showMessageDialog(this, "Funds added successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter a positive amount.", "Invalid Amount",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
            fundsField.setText("");
        });

        cardPanel.add(fundsPanel, "ViewAndAddFunds");
        switchToPanel("ViewAndAddFunds");
    }

    // View shopping cart for customer
    private void viewCart() {
        List<Map.Entry<Product, Integer>> cartProducts = shoppingCart.getCartDetails(currentUser);
        DefaultListModel<String> cartModel = new DefaultListModel<>();

        for (Map.Entry<Product, Integer> entry : cartProducts) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            cartModel.addElement(product.getName() + " - $" + product.getPrice() + " - Quantity: " + quantity);
        }

        JList<String> cartList = new JList<>(cartModel);
        JScrollPane scrollPane = new JScrollPane(cartList);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.addActionListener(e -> {
            shoppingCart.clearCart(currentUser);
            cartModel.clear();
        });
        buttonsPanel.add(clearCartButton);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(e -> checkOut());
        buttonsPanel.add(checkoutButton);

        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(scrollPane, BorderLayout.CENTER);
        cartPanel.add(buttonsPanel, BorderLayout.SOUTH);

        cardPanel.add(cartPanel, "ViewCart");
        switchToPanel("ViewCart");
    }

    // Checkout process for customer
    private void checkOut() {
        List<Map.Entry<Product, Integer>> cartProducts = shoppingCart.getCartDetails(currentUser);

        if (cartProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double totalAmount = 0;
        List<Product> products = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : cartProducts) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalAmount += product.getPrice() * quantity;

            for (int i = 0; i < quantity; i++) {
                products.add(product);
            }
        }

        Payment payment = new Payment(totalAmount);
        boolean paymentSuccess = payment.processPayment(authenticatedUser);

        if (!paymentSuccess) {
            JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Payment Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = new Order(authenticatedUser, payment);
        order.setProducts(products);
        order.setStatus();
        order.saveToDatabase();

        shoppingCart.clearCart(currentUser);

        JOptionPane.showMessageDialog(this, "Your order has been placed successfully!", "Order Placed",
                JOptionPane.INFORMATION_MESSAGE);

        switchToPanel("OrderConfirmation");
    }

    // Logout user and return to login screen
    public void logout() {
        this.setJMenuBar(null);
        this.invalidate();
        this.repaint();
        currentUser = null;
        switchToPanel("Login");
    }

    // Switch between panels
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