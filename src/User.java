/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author letmeplay
 */
public abstract class User {

    private String username;
    private String password;
    private String name;
    private String email;
    private String role;
    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

    public User(String username, String password, String role, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        setEmail(email);
        this.role = role;
    }
    // getters and setters

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = isValidEmail(email) ? email : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // This method allows us to authenticate the user by their username and
    // password.
    public static boolean authenticate(String username, String password) {
        boolean success = false;
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT COUNT(*) FROM USERS WHERE USERS_USERNAME = '" + username + "' AND USERS_PASSWORD = '"
                    + password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next() && rs.getInt(1) > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static Customer confirmAuth(String username, String password) {
        if (authenticate(username, password)) {
            return Customer.retrieveLoggedCustomerData(username, password);
        }
        return null;
    }

    // This method allows us to retrieve the user's role by their username and
    // password.
    // for example, whether they are an admin or a customer.
    public static String getUserRole(String username, String password) {
        String role = "invalid";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String sql = "SELECT USERS_ROLE FROM USERS WHERE USERS_USERNAME = '" + username + "' AND USERS_PASSWORD = '"
                    + password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                role = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

    // This method allows us to validate the user's email by checking the characters
    // in the email against a regular expression.
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}