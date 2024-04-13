/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Bubbltea
 */

 import java.io.BufferedWriter;
 import java.io.FileWriter;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 
 public class UserManagement {
     private List<Customer> customers;
 
     public UserManagement() {
         this.customers = new ArrayList<>();
     }
 
     public void addUser(Customer customer) {
         customers.add(customer);
     }
 
     public void updateUser(Customer customer) {
         for (Customer c : customers) {
             if (c.getUsername().equals(customer.getUsername())) {
                 c.setPassword(customer.getPassword());
                 c.setName(customer.getName());
                 c.setEmail(customer.getEmail());
                 c.setAddress(customer.getAddress());
                 c.setBalance(customer.getBalance());
                 break;
             }
         }
     }
 
     public void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/Resources/Users.txt"))) {
            for (Customer customer : customers) {
                writer.write(customer.getUsername() + ",gaspoweredwheelchair123908519283908563908asjkfgs9a780as908gh0sd" +
                             customer.getPassword() + ",monkeyCyrus89001884844812394123748127349871d2j3s491k234al712934dn1m28s34" +
                             customer.getName() + "," +
                             customer.getEmail() + "," +
                             customer.getAddress() + "," +
                             customer.getBalance() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}