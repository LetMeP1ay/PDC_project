/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Bubbltea
 */

public class Payment {
    private double amount;
    private String status;

    public Payment(double amount) {
        this.amount = amount;
        this.status = "Pending";
    }

    // process the users payment, checking if there is enough balance available for
    // the transaction
    public boolean processPayment(Customer customer) {
        if (customer.getBalance() >= amount) {
            customer.changeBalance(amount);
            this.status = "Paid";
            return true;
        } else {
            this.status = "Failed";
            return false;
        }
    }

    // get&set methods
    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
