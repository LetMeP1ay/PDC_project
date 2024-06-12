/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    //set the current status of a payment for tracking of orders
    public void setStatus() {
        switch(payment.getStatus()) {
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
        try (Connection conn = DriverManager.getConnection("jdbc:derby:OSS_DB;create=true");
             Statement stmt = conn.createStatement()) {
            StringBuilder productStr = new StringBuilder();
            for (Product product : products) {
                productStr.append(product.getName());
            }
            String sql = "INSERT INTO ORDERS (ORDERS_ID, ORDERS_TOTAL, ORDERS_STATUS, USERS_USERNAME, INVENTORY_PRODNAME) VALUES ('" +
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
    
}
