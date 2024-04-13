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

    private static boolean appRunning;

        private static void menuHandler(User user) {
            if (user.isAdmin()) {
                System.out.println("1. Add Product");
                System.out.println("2. Remove Product");
                System.out.println("4. Manage Discounts");
                System.out.println("5. View Orders");
                System.out.println("6. View Products");
                System.out.println("8. Logout");
            }
            else {
                System.out.println("1. View Products");
                System.out.println("2. View Cart");
                System.out.println("3. Checkout");
                System.out.println("4. View Orders");
                System.out.println("5. Logout");
            }
        }
        
    public static void main(String[] args) {
        appRunning = true;
        Scanner scanner = new Scanner(System.in);
        UserManagement userManagement = new UserManagement();

        while(appRunning) {
            System.out.println("Welcome to the Online Shopping System!");
            System.out.println("Are you an existing user or do you want to create an account?");
            System.out.println("1. Existing User");
            System.out.println("2. Create Account");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    
                    break;
                case 2:
                    System.out.println("Please enter the following information to create an account:");
                    while(true) {
                    
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        
                        Customer newCustomer = new Customer(username, password, name, email, address);
                        
                        if (newCustomer.getEmail() != null) {
                            userManagement.addUser(newCustomer);
                            userManagement.saveUsersToFile();
                            System.out.println("Account created successfully!");
                            break;
                        }
                        else {
                            System.out.println("Invalid email format. Please enter a valid email address.");
                            email = scanner.nextLine();
                            newCustomer.setEmail(email);
                            userManagement.addUser(newCustomer);
                            userManagement.saveUsersToFile();   
                            System.out.println("Account created successfully!");
                            break;
                        }
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 for Existing User or 2 for Create Account.");
                    break;
            }

            scanner.close();
        }
    }
}
