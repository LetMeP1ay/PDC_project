/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author letmeplay
 */
public class Customer extends User{
    private String address;
    private double balance;
  /*  private List<Order> orders;
    private List<Review> reviews; */
    private ShoppingCart cart;

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
            System.out.println("Please, enter a value greater than 0.");
        }
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    public ShoppingCart getCart() {
        return this.cart;
    }

    /* public List<Order> getOrders() {
        return this.orders;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }
    
     public void setReviews(String newReview) {
        this.reviews.getReview() = reviews;
    } */
    
    @Override
    public boolean isAdmin() {
        return false;
    }
}
