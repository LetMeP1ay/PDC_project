/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_project;

/**
 *
 * @author Bubbltea
 */
public class Payment {
    private double amount;
    private String Status;

    public Payment(double amount) {
        this.amount = amount;
        this.Status = "Pending";
    }

    // process the payment
    public void processPayment() {
        // add the code to process the payment.
    }

    // get&set methods
    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return Status;
    }
}
