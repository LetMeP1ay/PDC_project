/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Bubbltea
 */

public class Product {
    private String name;
    private double price;
    private List<Review> reviews;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.reviews = new ArrayList<>();
    }

    // get&set methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }
}
