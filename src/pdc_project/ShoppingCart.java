/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_project;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Bubbltea
 */

public class ShoppingCart {
    private List<Product> products;
    private Discount discount;

    public ShoppingCart() {
        this.products = new ArrayList<>();
        this.discount = null;
    }

    // add a product to the cart
    public void addProduct(Product product) {
        products.add(product);
    }

    // remove a product from the cart
    public void removeProduct(Product product) {
        products.remove(product);
    }

    // clear the cart
    public void clearCart() {
        products.clear();
    }

    // apply a discount
    public void applyDiscount(Discount discount) {
        if (discount.isActive()) {
            this.discount = discount;
        }
    }

    // get the total price of the products in the cart
    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        if (discount != null) {
            total = total - total * discount.getPercentage(); // apply discount
        }
        return total;
    }

    // get&set methods
    public List<Product> getProducts() {
        return products;
    }
}
