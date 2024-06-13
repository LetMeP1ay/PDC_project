/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Bubbltea
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManagement {

    private List<Customer> customers;
    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

    public UserManagement() {
        this.customers = new ArrayList<>();
    }

    // add user to the list
    public void addUser(Customer customer) {
        customers.add(customer);
    }

    // update user information
    public void updateUser(Customer customer) {
        for (Customer c : customers) {
            if (c.getUsername().equals(customer.getUsername())) {
                c.setPassword(customer.getPassword());
                c.setRole(customer.getRole());
                c.setName(customer.getName());
                c.setEmail(customer.getEmail());
                c.setAddress(customer.getAddress());
                c.setBalance(customer.getBalance());
                break;
            }
        }
    }

    // save users to the database
    public void saveUsersToDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            for (Customer customer : customers) {
                String sql = "INSERT INTO USERS (USERS_USERNAME, USERS_PASSWORD, USERS_ROLE, USERS_NAME, USERS_EMAIL, USERS_ADDRESS, USERS_BALANCE) VALUES ('"
                        + customer.getUsername() + "', '"
                        + customer.getPassword() + "', '"
                        + customer.getRole() + "', '"
                        + customer.getName() + "', '"
                        + customer.getEmail() + "', '"
                        + customer.getAddress() + "', "
                        + customer.getBalance() + ")";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // check if username exists already in file
    public boolean usernameExists(String username) {
        boolean exists = false;
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT COUNT(*) FROM USERS WHERE USERS_USERNAME = '" + username + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next() && rs.getInt(1) > 0) {
                exists = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }

    // This method allows us to validate the user's email by checking the characters
    // in the email against a regular expression.
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // show all orders for a specific customer
    public void viewOrders(Customer customer) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT * FROM ORDERS WHERE USERS_USERNAME = '" + customer.getUsername() + "'")) {
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getString("ORDERS_ID"));
                System.out.println("Customer: " + rs.getString("USERS_USERNAME"));
                System.out.println("Products: " + rs.getString("INVENTORY_PRODNAME"));
                System.out.println("Total: " + rs.getDouble("ORDERS_TOTAL"));
                System.out.println("Payment status: " + rs.getString("ORDERS_STATUS"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // show all orders
    public void viewAllOrders() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM ORDERS")) {
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getString("ORDERS_ID"));
                System.out.println("Customer: " + rs.getString("USERS_USERNAME"));
                System.out.println("Products: " + rs.getString("INVENTORY_PRODNAME"));
                System.out.println("Total: " + rs.getDouble("ORDERS_TOTAL"));
                System.out.println("Payment status: " + rs.getString("ORDERS_STATUS"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
