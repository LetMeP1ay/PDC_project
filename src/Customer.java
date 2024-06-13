/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author letmeplay
 */
public class Customer extends User {

    private String address;
    private double balance;
    private List<Order> orders;
    private ShoppingCart cart;
    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

    // Constructor for logged-in customer.
    public Customer(String username, String password, String role, String name, String email, String address,
            double balance) {
        super(username, password, role, name, email);
        this.address = address;
        this.balance = balance;
    }

    // General constructor
    public Customer(String username, String password, String name, String email, String address) {
        super(username, password, "customer", name, email);
        this.address = address;
        this.balance = 100;
    }

    // getters and setters
    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        if (balance > 0) {
            this.balance += balance;
        } else {
            System.out.println("Please, enter a value greater than 0.");
        }
    }

    public void updateBalance() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement stmt = conn.prepareStatement("UPDATE USERS SET USERS_BALANCE = ? WHERE USERS_USERNAME = ?")) {
            stmt.setDouble(1, this.balance);
            stmt.setString(2, this.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        };
    }

    // changeBalance method that allows us to manipulate the user's balance later
    // on.
    public void changeBalance(double sum) {
        this.balance -= sum;
    }

    // This method allows us to retrieve the user's data by their username and
    // password. It allows us to create the instance of a logged-in customer in the
    // shopping system.
    public static Customer retrieveLoggedCustomerData(String username, String password) {
        Customer customer = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM USERS WHERE USERS_USERNAME = '" + username + "' AND USERS_PASSWORD = '"
                    + password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                customer = new Customer(
                        rs.getString("USERS_USERNAME"),
                        rs.getString("USERS_PASSWORD"),
                        rs.getString("USERS_ROLE"),
                        rs.getString("USERS_NAME"),
                        rs.getString("USERS_EMAIL"),
                        rs.getString("USERS_ADDRESS"),
                        rs.getDouble("USERS_BALANCE"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    // more getters
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    public ShoppingCart getCart() {
        return this.cart;
    }

    public List<Order> getOrders() {
        return this.orders;
    }
}