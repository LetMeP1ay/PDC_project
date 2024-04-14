/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Bubbltea
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
 public class UserManagement {
     private List<Customer> customers;
 
     public UserManagement() {
         this.customers = new ArrayList<>();
     }
     
     // add user to the list
     public void addUser(Customer customer) {
         customers.add(customer);
     }

     
     //update user information
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
     
     // save users to file
     public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/Resources/Users.txt",true))) {
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + ",gaspoweredwheelchair123908519283908563908asjkfgs9a780as908gh0sd" +
                             customer.getPassword() + ",monkeyCyrus89001884844812394123748127349871d2j3s491k234al712934dn1m28s34" +
                             customer.getRole() + ",funnynamejfasdiasdfk" +
                             customer.getName() + "," +
                             customer.getEmail() + "," +
                             customer.getAddress() + "," +
                             customer.getBalance() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // check if username exists already in file
    public boolean usernameExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/Resources/Users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String storedUsername = line.split(",gaspoweredwheelchair123908519283908563908asjkfgs9a780as908gh0sd")[0];
                if(storedUsername.equals(username)) {
                    return true;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // show all orders for a specific customer
    public void viewOrders(Customer customer) {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/Resources/Order.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Customer: " + customer.getName())) {
                    System.out.println("Order ID: " + reader.readLine());
                    System.out.println(line);
                    System.out.println("Products:");
                    while (!(line = reader.readLine()).isEmpty()) {
                        System.out.println(line);
                    }
                    System.out.println("Total: " + reader.readLine());
                    System.out.println("Payment status: " + reader.readLine());
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the orders.");
        }
    }

    // show all orders
    public void viewAllOrders() {
        try (BufferedReader reader = new BufferedReader(new FileReader("./src/Resources/Order.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Order ID: " + reader.readLine());
                System.out.println(line);
                System.out.println("Products:");
                while (!(line = reader.readLine()).isEmpty()) {
                    System.out.println(line);
                }
                System.out.println("Total: " + reader.readLine());
                System.out.println("Payment status: " + reader.readLine());
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the orders.");
        }
    }
}