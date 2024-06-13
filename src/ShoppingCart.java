/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bubbltea
 */

public class ShoppingCart {
    private List<Product> products;
    private Discount discount;
    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

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
    public void clearCart(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM CART WHERE USERS_USERNAME = '" + username.replace("'", "''") + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }

    // apply a discount
    public void applyDiscount(Discount discount) {
        if (discount.getIsActive()) {
            this.discount = discount;
        }
    }

    // get the total price of the products in the cart and apply the discount if
    // there is one to the total
    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        if (discount != null) {
            total = total - total * discount.getPercentage();
        }
        return total;
    }

    // get&set methods
    public List<Product> getProducts() {
        return products;
    }

    // Retrieve shopping cart details from the database
    public List<Map.Entry<Product, Integer>> getCartDetails(String username) {
        List<Map.Entry<Product, Integer>> cartDetails = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT PRODUCT_ID, QUANTITY, PRICE FROM CART WHERE USERS_USERNAME = '"
                        + username.replace("'", "''") + "'")) {
            while (rs.next()) {
                String productName = rs.getString("PRODUCT_ID");
                int quantity = rs.getInt("QUANTITY");
                double price = rs.getDouble("PRICE");
                Product newProduct = new Product(productName, price);
                cartDetails.add(new AbstractMap.SimpleEntry<>(newProduct, quantity));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving cart details: " + e.getMessage());
        }
        return cartDetails;
    }
}
