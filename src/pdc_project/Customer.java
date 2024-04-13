/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_project;

/**
 *
 * @author letmeplay
 */
public class Customer extends User{
    private String address;
    private double balance;
 //   private List orders;
 //   private List reviews;
 //   private Cart cart;

    public Customer(String username, String password, String name, String email, String address) {
        super(username, password, name, email);
        this.address = address;
        this.balance = 100;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        if (balance > 0) {
            this.balance += balance;
        }
        else {
            System.out.println("Please, enter a value greater than 0.")
        }
    }

  /*  public String getCart() {
        return this.cart;
    }

    public String getOrders() {
        return this.orders;
    }

    public String getReviews() {
        return this.reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    } */
    
    @Override
    public boolean isAdmin() {
        return false;
    }
}
