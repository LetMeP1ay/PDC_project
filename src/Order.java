/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author letmeplay
 */
public class Order {

    private UUID orderId;
    private Customer customer;
    private List<Product> products;
    private Payment payment;
    private String status;
    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

    // constructor
    public Order(Customer customer, Payment payment) {
        this.customer = customer;
        this.orderId = UUID.randomUUID();
        this.payment = payment;
    }

    // get&set methods
    public UUID getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public String getStatus() {
        return this.status;
    }

    // set the current status of a payment for tracking of orders
    public void setStatus() {
        switch (payment.getStatus()) {
            case "Pending":
                this.status = "Awaiting payment";
                break;
            case "Paid":
                this.status = "Payment received";
                break;
            case "Failed":
                this.status = "Payment failed";
                break;
            default:
                this.status = "Not processed";
                break;
        }
    }

    // save the order to the database in an easy to read format for admins to view
    public void saveToDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO ORDERS (ORDERS_ID, ORDERS_TOTAL, ORDERS_STATUS, USERS_USERNAME, INVENTORY_PRODNAME) VALUES (?, ?, ?, ?, ?)")) {
            StringBuilder productStr = new StringBuilder();
            for (Product product : products) {
                if (productStr.length() > 0) {
                    productStr.append(", ");
                }
                productStr.append(product.getName());
            }

            stmt.setString(1, orderId.toString());
            stmt.setDouble(2, payment.getAmount());
            stmt.setString(3, payment.getStatus());
            stmt.setString(4, customer.getUsername());
            stmt.setString(5, productStr.toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Retrieve all orders for the specific user

    public static List<Order> retrieveUsersOrders(String username) {
        List<Order> orders = new ArrayList<>();
        String ordersQuery = "SELECT * FROM ORDERS WHERE USERS_USERNAME = ?";
        String passwordQuery = "SELECT USERS_PASSWORD FROM USERS WHERE USERS_USERNAME = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement ordersStmt = conn.prepareStatement(ordersQuery);
                PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery)) {

            passwordStmt.setString(1, username);
            ResultSet passwordRs = passwordStmt.executeQuery();
            String password = null;
            if (passwordRs.next()) {
                password = passwordRs.getString("USERS_PASSWORD");
            }

            if (password == null) {
                System.out.println("No password found for the user: " + username);
                return orders;
            }

            ordersStmt.setString(1, username);
            ResultSet rs = ordersStmt.executeQuery();
            while (rs.next()) {
                UUID orderId = UUID.fromString(rs.getString("ORDERS_ID"));
                double total = rs.getDouble("ORDERS_TOTAL");
                String status = rs.getString("ORDERS_STATUS");
                String productNames = rs.getString("INVENTORY_PRODNAME");

                Customer customer = Customer.retrieveLoggedCustomerData(username, password);
                Payment payment = new Payment(total);
                payment.setStatus(status);

                Order order = new Order(customer, payment);
                order.orderId = orderId;

                List<Product> products = new ArrayList<>();
                String[] productArray = productNames.split(",");
                for (String productName : productArray) {
                    Product product = new Product(productName.trim(), 0); // Assuming price is not important here
                    products.add(product);
                }
                order.setProducts(products);
                order.setStatus();

                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    // Retrieve all orders from all customers. Used by Admin users.

    public static List<Order> retrieveAllOrders() {
        List<Order> orders = new ArrayList<>();
        String ordersQuery = "SELECT * FROM ORDERS";

        try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement ordersStmt = conn.prepareStatement(ordersQuery);
                ResultSet rs = ordersStmt.executeQuery()) {

            while (rs.next()) {
                UUID orderId = UUID.fromString(rs.getString("ORDERS_ID"));
                double total = rs.getDouble("ORDERS_TOTAL");
                String status = rs.getString("ORDERS_STATUS");
                String username = rs.getString("USERS_USERNAME");
                String productNames = rs.getString("INVENTORY_PRODNAME");

                // Retrieve the password for the customer
                String passwordQuery = "SELECT USERS_PASSWORD FROM USERS WHERE USERS_USERNAME = ?";
                String password = null;
                try (PreparedStatement passwordStmt = conn.prepareStatement(passwordQuery)) {
                    passwordStmt.setString(1, username);
                    ResultSet passwordRs = passwordStmt.executeQuery();
                    if (passwordRs.next()) {
                        password = passwordRs.getString("USERS_PASSWORD");
                    }
                }

                if (password == null) {
                    System.out.println("No password found for the user: " + username);
                    continue;
                }

                Customer customer = Customer.retrieveLoggedCustomerData(username, password);
                Payment payment = new Payment(total);
                payment.setStatus(status);

                Order order = new Order(customer, payment);
                order.orderId = orderId;

                List<Product> products = new ArrayList<>();
                String[] productArray = productNames.split(",");
                for (String productName : productArray) {
                    Product product = new Product(productName.trim(), 0); // Assuming price is not important here
                    products.add(product);
                }
                order.setProducts(products);
                order.setStatus();

                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

}
