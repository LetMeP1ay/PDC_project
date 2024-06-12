/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author letmeplay & Bubbltea
 */
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * the OnlineShoppingSystem class represents an online shopping system
 * it provides functionality for user authentication, account creation, managing products, and orders
 * the system can be accessed by both administrators and regular customers, with different menus available to each
 */
public class OnlineShoppingSystem {

    private static boolean appRunning;
    private static InventoryManagement inventoryManagement = new InventoryManagement();
    private static Discount discount = new Discount();
    private static ShoppingCart shoppingCart = new ShoppingCart();
    private static UserManagement userManagement = new UserManagement();
    private static Customer loggedCustomer;

        // allow users to log in or create an account
        public static void main(String[] args) {
        appRunning = true;
        Scanner scanner = new Scanner(System.in);
        UserManagement userManagement = new UserManagement();

        while(appRunning) {
            try {
                System.out.println("Welcome to the Online Shopping System!");
                System.out.println("Are you an existing user or do you want to create an account?");
                System.out.println("1. Existing User");
                System.out.println("2. Create Account");
                System.out.println("3. Quit");

                int choice = scanner.nextInt();
                scanner.nextLine();


                switch (choice) {
                case 1:
                    boolean authenticated = false;
                    while(!authenticated) {
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        if (User.authenticate(username, password)) {
                            System.out.println("Logging in... Welcome " + username + "!");
                            menuHandler(User.getUserRole(username, password));
                            }
                            else {
                                System.out.println("Invalid username or password. Please try again.");
                            }                        
                    }
                    
                  break;
                case 2:
                    System.out.println("Please enter the following information to create an account:");
                    while(true) {
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        while (userManagement.usernameExists(username)) {
                            System.out.println("This username is already taken. Please choose a different username.");
                            username = scanner.nextLine();
                        }
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
                            System.out.println("Invalid email format. Please try again.");
                        }
                    }
                    break;
                case 3:
                    appRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter 1 for Existing User or 2 for Create Account.");
                    break;
            }}
            catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 for Existing User or 2 for Create Account.");
            }
            catch(NoSuchElementException e) {
                System.out.println("Thank you for using our program.");
            }
            break;
        }
            scanner.close();
    }
    // provides separate menus for administrators and customers, with different options available to each
    private static void menuHandler(String role) {
        Scanner scanner = new Scanner(System.in);
        int option;
        boolean menuRunning = true;

        while(menuRunning) {
            try{
                if (role.equals("admin")) {
                System.out.println("1. Add Product");
                System.out.println("2. Remove Product");
                System.out.println("3. Manage Discounts");
                System.out.println("4. View Orders");
                System.out.println("5. View Products");
                System.out.println("6. Quit");
        
                System.out.print("Enter option: ");
                option = scanner.nextInt();
                scanner.nextLine();
        
                switch (option) {
                    case 1:
                        System.out.println("If you have changed your mind, press 'x' to go back");
                        String back = scanner.nextLine();
                        if (back.equals("x")) {
                            break;
                        }
                        System.out.print("Enter product name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter product price: ");
                        double price = scanner.nextDouble();
                        if(price <= 0) {
                            System.out.println("Input a price that is greater than 0.");
                            continue;
                        }
                        System.out.print("Enter product quantity: ");
                        int quantity = scanner.nextInt();
                        if(quantity <= 0) {
                            System.out.println("You need to have a positive amount of products.");
                            continue;
                        }
                        Product product = new Product(name, price);
                        inventoryManagement.addProduct(product, quantity);
                        System.out.println(name + " added successfully.");
                        break;
                    case 2:
                        System.out.print("Enter product name to remove or enter 'x' to go back: ");
                        name = scanner.nextLine();
                        if (name.equals('x')) {
                            break;
                            }
                        Product productToRemove = inventoryManagement.findProductByName(name);
                        if (productToRemove != null) {
                            inventoryManagement.removeProduct(productToRemove);
                            System.out.println(name + " removed successfully.");
                        } 
                        else {
                            System.out.println("Product not found in inventory.");
                        }
                        break;
                    case 3:
                        System.out.println(discount.retrieveStatus());
                        System.out.println("1. Toggle discount");
                        System.out.println("2. Go back");
                        System.out.println("Enter Option:");
                        option = scanner.nextInt();
                        switch(option) {
                            case 1:
                                discount.changeActive();
                                break;
                            case 2:
                                break;
                        }
                        break;
                    case 4:
                        userManagement.viewAllOrders();
                        break;
                    case 5:
                    List<Product> products = inventoryManagement.getAllProducts();
                    if (products.isEmpty()) {
                        System.out.println("No products available.");
                    } else {
                        int i = 1;
                        for (Product producta : products) {
                            System.out.println("Product Name: " + producta.getName() + ", Price: " + producta.getPrice());
                            i++;
                        }
                        System.out.println(i + ". Go back");
                    }
                    break;
                    case 6:
                        menuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 6.");
                        break;
                }
            }
            else {
                System.out.println("1. View Products");
                System.out.println("2. View Cart");
                System.out.println("3. Checkout");
                System.out.println("4. View Orders");
                System.out.println("5. Quit");
        
                System.out.print("Enter option: ");
                option = scanner.nextInt();
        
                switch (option) {
                    case 1:
                    List<Product> products = inventoryManagement.getAllProducts();
                    if (products.isEmpty()) {
                        System.out.println("No products available.");
                    } else {
                        int i = 1;
                        for (Product product : products) {
                            System.out.println(i + ". Product Name: " + product.getName() + ", Price: " + product.getPrice());
                            i++;
                        }
                        System.out.println(i + ". Go back");
                        System.out.println("Enter the number of the product you want to add to your cart");
                        int productNumber = scanner.nextInt();
                        if (productNumber > 0 && productNumber <= products.size()) {
                            Product productToAdd = products.get(productNumber - 1);
                            shoppingCart.addProduct(productToAdd);
                            System.out.println(productToAdd.getName() + " added to your cart.");
                        }
                        else if(productNumber == i) {
                            break;
                        }
                         else {
                            System.out.println("Invalid product number.");
                        }
                    }
                    break;
                    case 2:
                    List<Product> cartProducts = shoppingCart.getProducts();
                    if (cartProducts.isEmpty()) {
                        System.out.println("Your cart is empty.");
                    } else {
                        int i = 1;
                        System.out.println("Your cart has the following products:");
                        for (Product product : cartProducts) {
                            System.out.println(i + ". Product Name: " + product.getName() + ", Price: " + product.getPrice());
                            i++;
                        }
                        System.out.println("Total cost: " + shoppingCart.calculateTotal());
                            System.out.println("Do you have a discount code? (yes/no)");
                            System.out.println("Enter your choice:");
                            String discountChoice = scanner.next();
                            if (discountChoice.equalsIgnoreCase("yes")) {
                                System.out.println("Please enter your discount code:");
                                String discountCode = scanner.next();
                                if (discountCode.equals(discount.getCode())) {
                                    shoppingCart.applyDiscount(discount);
                                    System.out.println("Discount applied. Your new total is: " + shoppingCart.calculateTotal());
                                }
                                else {
                                    System.out.println("Invalid discount code.");
                                }
                            }
                    }
                    break;
                    case 3:
                    double total = shoppingCart.calculateTotal();
                    System.out.println("Your total is: " + total);
                    System.out.println("Do you want to proceed with the payment? (yes/no)");
                    String paymentChoice = scanner.next();
                
                    if (paymentChoice.equalsIgnoreCase("yes")) {
                        System.out.println("For security, please re-enter your username and password.");
                        System.out.print("Username: ");
                        String username = scanner.next();
                        System.out.print("Password: ");
                        String password = scanner.next();
                
                        if (User.authenticate(username, password)) {
                            loggedCustomer = Customer.retrieveLoggedCustomerData(username, password);
                            Payment payment = new Payment(total);
                            System.out.println(total);
                            Order order = new Order(loggedCustomer, payment);
                            System.out.println(loggedCustomer.getBalance());
                            boolean paymentSuccess = payment.processPayment(loggedCustomer);
                            System.out.println(loggedCustomer.getBalance());
                            if (paymentSuccess) {
                                shoppingCart.clearCart();
                                userManagement.updateUser(loggedCustomer);
                                userManagement.saveUsersToFile();
                                order.setProducts(shoppingCart.getProducts());
                                order.setStatus();
                                order.saveToFile();
                                System.out.println("Payment successful. You can check your orders for more information.");
                            } else {
                                System.out.println("Payment failed. Do you want to add money to your balance? (yes/no)");
                                String balanceChoice = scanner.next();
                                if (balanceChoice.equalsIgnoreCase("yes")) {
                                    System.out.println("Enter the amount you want to add:");
                                    double amountToAdd = scanner.nextDouble();
                                    loggedCustomer.setBalance(amountToAdd);
                                    userManagement.updateUser(loggedCustomer);
                                    System.out.println("Balance updated successfully.");
                                    System.out.println("Payment successful. You can check your orders for more information.");
                                    payment.processPayment(loggedCustomer);
                                    order.setProducts(shoppingCart.getProducts());
                                    order.setStatus();
                                    order.saveToFile();
                                    userManagement.updateUser(loggedCustomer);
                                    userManagement.saveUsersToFile();
                                }
                            }
                        } else {
                            System.out.println("Authentication failed. Payment cancelled.");
                        }
                    }
                    break;
                    case 4:
                    System.out.println("For security, please re-enter your username and password.");
                    System.out.print("Username: ");
                    String username = scanner.next();
                    System.out.print("Password: ");
                    String password = scanner.next();
            
                    if (User.authenticate(username, password)) {
                    loggedCustomer = Customer.retrieveLoggedCustomerData(username, password);
                    System.out.println(loggedCustomer.getName() + "'s Orders:");
                    userManagement.viewOrders(loggedCustomer);
                    } else {
                        System.out.println("Authentication failed. Please try again.");
                    }
                    break;
                    case 5:
                        menuRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please enter a number between 1 and 5.");
                        break;
                }
            }
        } catch(InputMismatchException e) {
            System.out.println("Please input a numerical value in a usable range.");
            scanner.next();
        }
        catch(NoSuchElementException e) {
            System.out.println("Input not found. Thank you for using our program.");
        }
            }
            
            scanner.close();
        }

}
