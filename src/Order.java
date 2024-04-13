/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.UUID;

/**
 *
 * @author letmeplay
 */
public class Order {

    private UUID orderId;
    private Customer customer;
   // private List<Product> products;
    private Payment payment;
    private String status;

    public Order(Customer customer) {
        this.customer = customer;
        this.orderId = UUID.randomUUID();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /* public List<Proudct> getProducts() {
        return this.products;
    }*/

    public Payment getPayment() {
        return this.payment;
    }

    public String getStatus() {
        return this.status;
    }

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
    
}
