import javax.swing.*;
import java.awt.*;

public class OnlineShoppingSystemGUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JButton loginButton;
    private JButton createAccountButton;
    private JButton exitButton;
    UserManagement userManagement = new UserManagement();

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
            
            if (isAuthenticated) {
                JOptionPane.showMessageDialog(loginDialog, "Login Successful");
                loginDialog.dispose();
                showLandingPage();
                //opening the next window or dialog based on user role
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                //clear the fields or take other actions
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
                userManagement.saveUsersToFile();
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
        
        JMenuBar menuBar = new JMenuBar();
    
        //menus
        JMenu productsMenu = new JMenu("Products");
        JMenu ordersMenu = new JMenu("Orders");
        JMenu accountMenu = new JMenu("Account Settings");
    
        //products
        JMenuItem viewProductsItem = new JMenuItem("View Products");
        JMenuItem searchProductItem = new JMenuItem("Search for a Product");
    
        //action listeners for switch panels
        viewProductsItem.addActionListener(e -> switchToPanel("ViewProducts"));
        searchProductItem.addActionListener(e -> switchToPanel("SearchProduct"));
    
        //add items to products menu
        productsMenu.add(viewProductsItem);
        productsMenu.add(searchProductItem);
    
        JMenuItem viewOrdersItem = new JMenuItem("View Orders");
        viewOrdersItem.addActionListener(e -> switchToPanel("ViewOrders"));
        ordersMenu.add(viewOrdersItem);

        JMenuItem viewAccountItem = new JMenuItem("View Account");
        viewAccountItem.addActionListener(e -> switchToPanel("ViewAccount"));
        accountMenu.add(viewAccountItem);

        JMenuItem logout = new JMenuItem("Logout");
        accountMenu.add(logout);

    
        // Add menus to menu bar
        menuBar.add(productsMenu);
        menuBar.add(ordersMenu);
        menuBar.add(accountMenu);
    
        //set the JMenuBar on the JFrame
        this.setJMenuBar(menuBar);
    
        //show the landing page
        cardLayout.show(cardPanel, "LandingPage");
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