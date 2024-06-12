/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author letmeplay
 */
public class InventoryManagement {

    private HashMap<Product, Integer> inventory;
    private static final String DB_URL = "jdbc:derby:/Users/letmeplay/NetBeansProjects/PDC_project/OSS_DB;create=true";

    public InventoryManagement() {
        this.inventory = new HashMap<>();
        loadInventory();
    }

// adds the set amount of product to the stock and saves the data in the database.
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
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT * FROM INVENTORY")) {
            while (rs.next()) {
                products.add(new Product(rs.getString("INVENTORY_PRODNAME"), rs.getDouble("INVENTORY_PRICE")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return products;
    }

    // remove the set amount of products from the stock and save the data in the text file.
    public void removeProduct(Product product) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM INVENTORY WHERE INVENTORY_PRODNAME = '" + product.getName() + "'";
            stmt.executeUpdate(sql);
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
// loads the stock from the text file

    public void loadInventory() {
        try (Connection conn = DriverManager.getConnection(DB_URL);  
            Statement stmt = conn.createStatement();  ResultSet rs = stmt.executeQuery("SELECT * FROM INVENTORY")) {
            while (rs.next()) {
                Product product = new Product(rs.getString("INVENTORY_PRODNAME"), rs.getDouble("INVENTORY_PRICE"));
                this.inventory.put(product, rs.getInt("INVENTORY_QUANTITY"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
