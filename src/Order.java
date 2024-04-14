/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    // save the order to a file in an easy to read format for admins and users to view
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("./src/Resources/Order.txt", true))) {
            writer.println("Order ID: " + orderId);
            writer.println("Customer: " + customer.getName());
            writer.println("Products:");
            for (Product product : products) {
                writer.println("- " + product.getName() + ": " + product.getPrice());
            }
            writer.println("Total: " + payment.getAmount());
            writer.println("Payment status: " + payment.getStatus());
            writer.println();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the order.");
        }
    }
    
}
