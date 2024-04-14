/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public User(String username, String password, String role, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        setEmail(email);
        this.role = "customer";
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

    // This method allows us to authenticate the user by their username and password.
    public static boolean authenticate(String username, String password) {
        boolean success = false;
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/Resources/Users.txt"))) {
            String line;
            while (((line = reader.readLine()) != null)) {
                String[] userArr = line.split(",gaspoweredwheelchair123908519283908563908asjkfgs9a780as908gh0sd");
                String storedUsername = userArr[0];
                String storedPassword = userArr[1].split(",monkeyCyrus89001884844812394123748127349871d2j3s491k234al712934dn1m28s34")[0];
                if(storedUsername.equals(username) && storedPassword.equals(password)) {
                    success = true;
                    break;
                }
            }
        }
            catch(IOException e) {
                e.printStackTrace();
            }
            return success;
        }

        // This method allows us to retrieve the user's role by their username and password. 
        // for example, whether they are an admin or a customer.
    public static String getUserRole(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/Resources/Users.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] userArr = line.split(",gaspoweredwheelchair123908519283908563908asjkfgs9a780as908gh0sd"+password+",monkeyCyrus89001884844812394123748127349871d2j3s491k234al712934dn1m28s34");
                String storedUsername = userArr[0];
                if (storedUsername.equals(username)) {
                    return userArr[1].split(",funnynamejfasdiasdfk")[0];
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return "invalid";
    }

    // This method allows us to validate the user's email by checking the characters in the email against a regular expression.
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
