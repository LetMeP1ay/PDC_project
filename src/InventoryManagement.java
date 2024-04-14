/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author letmeplay
 */
public class InventoryManagement {

    private HashMap<Product, Integer> inventory;
    private static final String INVENTORY_FILE = "./src/Resources/Inventory.txt";

    public InventoryManagement() {
        this.inventory = new HashMap<>();
        loadInventory();
    }
// adds the set amount of product to the stock and saves the data in the text file.
    public void addProduct(Product product, int quantity) {
    this.inventory.put(product, quantity);
    saveInventory();
}
// retrieve all products 
public List<Product> getAllProducts() {
    return new ArrayList<>(inventory.keySet());
}
// remove the set amount of products from the stock and save the data in the text file.
public void removeProduct(Product product) {
    if (inventory.containsKey(product)) {
        inventory.remove(product);
        saveInventory();
    } else {
        System.out.println("Product not found in inventory.");
    }
}
// find a particular product by name
public Product findProductByName(String name) {
    for (Product product : inventory.keySet()) {
        if (product.getName().equals(name)) {
            return product;
        }
    }
    return null;
}
// loads the stock from the text file
    public void loadInventory() {
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                int quantity = Integer.parseInt(parts[2]);
                Product product = new Product(name, price);
                this.inventory.put(product, quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// saves the stock in the text file
    public void saveInventory() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            for (HashMap.Entry<Product, Integer> entry : this.inventory.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                writer.println(product.getName() + "," + product.getPrice() + "," + quantity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
