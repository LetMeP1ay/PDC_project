/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
            case "Processed":
                this.status = "Payment received";
                break;
            default:
                this.status = "Not processed";
                break;
        }

    }

    // save the order to the database in an easy to read format for admins to view
    public void saveToDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            StringBuilder productStr = new StringBuilder();
            for (Product product : products) {
                productStr.append(product.getName());
            }
            String sql = "INSERT INTO ORDERS (ORDERS_ID, ORDERS_TOTAL, ORDERS_STATUS, USERS_USERNAME, INVENTORY_PRODNAME) VALUES ('"
                    +
                    orderId.toString() + "', '" +
                    payment.getAmount() + "', '" +
                    payment.getStatus() + "', " +
                    customer.getUsername() + ", '" +
                    productStr.toString() + "')";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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
                    Product product = new Product(productName, 0);
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
