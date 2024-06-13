/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author letmeplay
 */
public class InventoryManagement {

    private HashMap<Product, Integer> inventory;
    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

    public InventoryManagement() {
        this.inventory = new HashMap<>();
        loadInventory();
    }

    // adds the set amount of product to the stock and saves the data in the
    // database.
    public void addProduct(Product product, int quantity) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "INSERT INTO INVENTORY (INVENTORY_PRODNAME, INVENTORY_PRICE, INVENTORY_QUANTITY) VALUES ('"
                    + product.getName() + "', " + product.getPrice() + ", " + quantity + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // retrieve all products
    public List<Map.Entry<Product, Integer>> getAllProducts() {
        List<Map.Entry<Product, Integer>> productsWithQuantities = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM INVENTORY")) {
            while (rs.next()) {
                Product product = new Product(rs.getString("INVENTORY_PRODNAME"), rs.getDouble("INVENTORY_PRICE"));
                int quantity = rs.getInt("INVENTORY_QUANTITY");
                productsWithQuantities.add(new AbstractMap.SimpleEntry<>(product, quantity));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productsWithQuantities;
    }

    // get the quantity of the selected product
    public int getProductQuantity(Product product) {
        int quantity = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT INVENTORY_QUANTITY FROM INVENTORY WHERE INVENTORY_PRODNAME = '"
                        + product.getName() + "'")) {
            if (rs.next()) {
                quantity = rs.getInt("INVENTORY_QUANTITY");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return quantity;
    }

    // remove the product from the database.
    public void removeProduct(Product product) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM INVENTORY WHERE INVENTORY_PRODNAME = '" + product.getName() + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Move the product from the inventory to user's shopping cart
    public void moveToShoppingCart(Product product, String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);

            try {
                // Check if the product quantity is greater than 0
                String checkQuantitySql = "SELECT INVENTORY_QUANTITY FROM INVENTORY WHERE INVENTORY_PRODNAME = '"
                        + product.getName().replace("'", "''") + "'";
                ResultSet rs = stmt.executeQuery(checkQuantitySql);

                if (rs.next()) {
                    int quantity = rs.getInt("INVENTORY_QUANTITY");
                    if (quantity <= 0) {
                        System.out.println("The product is out of stock and cannot be added to the cart.");
                        conn.rollback();
                        return;
                    }
                } else {
                    System.out.println("The product does not exist in the inventory.");
                    conn.rollback();
                    return;
                }

                // Deduct the product quantity from the inventory
                String deductSql = "UPDATE INVENTORY SET INVENTORY_QUANTITY = INVENTORY_QUANTITY - 1 WHERE INVENTORY_PRODNAME = '"
                        + product.getName().replace("'", "''") + "'";
                stmt.executeUpdate(deductSql);

                // Check if the product is already in the user's cart
                String checkCartSql = "SELECT QUANTITY FROM CART WHERE USERS_USERNAME = '" + username.replace("'", "''")
                        + "' AND PRODUCT_ID = '"
                        + product.getName().replace("'", "''") + "'";
                rs = stmt.executeQuery(checkCartSql);

                if (rs.next()) {
                    // Update the quantity if the product is already in the cart
                    int currentQuantity = rs.getInt("QUANTITY");
                    String updateCartSql = "UPDATE CART SET QUANTITY = ? WHERE USERS_USERNAME = ? AND PRODUCT_ID = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateCartSql)) {
                        pstmt.setInt(1, currentQuantity + 1);
                        pstmt.setString(2, username.replace("'", "''"));
                        pstmt.setString(3, product.getName().replace("'", "''"));
                        pstmt.executeUpdate();
                    }
                } else {
                    // Insert the product into the cart if it's not already there
                    String insertCartSql = "INSERT INTO CART (USERS_USERNAME, PRODUCT_ID, QUANTITY, PRICE) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertCartSql)) {
                        pstmt.setString(1, username.replace("'", "''"));
                        pstmt.setString(2, product.getName().replace("'", "''"));
                        pstmt.setInt(3, 1);
                        pstmt.setDouble(4, product.getPrice());
                        pstmt.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.out.println(e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // find a particular product by name
    public Product findProductByName(String name) {
        Product product = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM INVENTORY WHERE INVENTORY_PRODNAME = '" + name + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                product = new Product(rs.getString("INVENTORY_PRODNAME"), rs.getDouble("INVENTORY_PRICE"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return product;
    }
    // loads the stock from the database

    public void loadInventory() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM INVENTORY")) {
            while (rs.next()) {
                Product product = new Product(rs.getString("INVENTORY_PRODNAME"), rs.getDouble("INVENTORY_PRICE"));
                this.inventory.put(product, rs.getInt("INVENTORY_QUANTITY"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
