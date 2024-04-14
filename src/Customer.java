/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author letmeplay
 */
public class Customer extends User{
    private String address;
    private double balance;
    private List<Order> orders;
    private ShoppingCart cart;

// Constructor for logged-in customer.

    public Customer(String username, String password, String role, String name, String email, String address, double balance) {
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
//getters and setters
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
        }
        else {
            System.out.println("Please, enter a value greater than 0.");
        }
    }

    // changeBalance method that allows us to manipulate the user's balance later on.

    public void changeBalance(double sum) {
        this.balance -= sum;
    }

    // This method allows us to retrieve the user's data by their username and password. It allows us to create the instance of a logged-in customer in the shopping system.
    public static Customer retrieveLoggedCustomerData(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/Resources/Users.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                String delimiter = ",gaspoweredwheelchair123908519283908563908asjkfgs9a780as908gh0sd" + password + ",monkeyCyrus89001884844812394123748127349871d2j3s491k234al712934dn1m28s34";
                if (line.contains(delimiter)) {
                    String[] userArr = line.split(delimiter);
                    String logUsername = username;
                    String logPassword = password;
                    String logRole = userArr[1].split(",funnynamejfasdiasdfk")[0];
                    String remainingFields = userArr[1].substring(logRole.length() + ",funnynamejfasdiasdfk".length());
                    String[] remainingFieldsArr = remainingFields.split(",");
                    String logName = remainingFieldsArr[0];
                    String logEmail = remainingFieldsArr[1];
                    String logAddress = remainingFieldsArr[2];
                    double logBalance = Double.parseDouble(remainingFieldsArr[3]);
                    return new Customer(logUsername, logPassword, logRole, logName, logEmail, logAddress, logBalance);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new Customer("","","","","","",0.0);
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
