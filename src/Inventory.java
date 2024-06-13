/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.HashMap;

/**
 *
 * @author letmeplay
 */
public class Inventory {

    private HashMap<Product, Integer> inventory;

    public Inventory(HashMap<Product, Integer> inventory) {
        this.inventory = inventory;
    }
    // getter

    public HashMap<Product, Integer> getInventory() {
        return new HashMap<>(inventory);
    }
    // check availability method allows us to see whether we have the product in
    // stock at the moment

    public boolean checkAvailability(Product product, int quantity) {
        return inventory.getOrDefault(product, 0) >= quantity;
    }
    // add product adds the amount of the product we input to the stock

    public void addProduct(Product product, int quantity) {
        inventory.put(product, inventory.containsKey(product) ? inventory.get(product) + quantity : quantity);
    }

    /*
     * remove product removes the amount of the product we input from the stock and
     * checks if the new amount is equal to 0. If it is, then it removes the product
     * entirely. Then it checks if the new amount is less than 0.
     * If it is, then it tells you that you can't have negative stock.
     */
    public void removeProduct(Product product, int quantity) {
        if (inventory.containsKey(product)) {
            int newQuantity = inventory.get(product) - quantity;
            if (newQuantity == 0) {
                inventory.remove(product);
            }
            if (newQuantity < 0) {
                System.out.println("You cannot have negative stock.");
            } else {
                inventory.put(product, newQuantity);
            }
        }
    }

}
