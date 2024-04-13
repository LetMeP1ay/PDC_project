/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author letmeplay
 */
import java.util.Scanner;

public class OnlineShoppingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManagement userManagement = new UserManagement();

        System.out.println("Welcome to the Online Shopping System!");
        System.out.println("Are you an existing user or do you want to create an account?");
        System.out.println("1. Existing User");
        System.out.println("2. Create Account");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Handle existing user login
                break;
            case 2:
                System.out.println("Please enter the following information to create an account:");
                System.out.print("Username: ");
                String username = scanner.next();
                System.out.print("Password: ");
                String password = scanner.next();
                System.out.print("Name: ");
                String name = scanner.next();
                System.out.print("Email: ");
                String email = scanner.next();
                System.out.print("Address: ");
                String address = scanner.next();

                Customer newCustomer = new Customer(username, password, name, email, address);
                userManagement.addUser(newCustomer);
                userManagement.saveUsersToFile();
                System.out.println("Account created successfully!");

                break;
            default:
                System.out.println("Invalid choice. Please enter 1 for Existing User or 2 for Create Account.");
                break;
        }

        scanner.close();
    }
}
