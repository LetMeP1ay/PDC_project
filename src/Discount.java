/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Bubbltea
 */

public class Discount {
    private String code;
    private double percentage;
    private boolean isActive;

    public Discount(String code, double percentage) {
        this.code = "DISCOUNT";
        this.percentage = 0.1;
        this.isActive = true;
    }

    // get&set methods
    public String getCode() {
        return code;
    }

    public double getPercentage() {
        return percentage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(User user, boolean active) {
        if (user.isAdmin()) {
            isActive = active;
            System.out.println("Discount changed successfully.");
        }
        else {
            System.out.println("Access denied. Only admin users can change the state of discount.");
        }
    }

    @Override
    public String toString() {
        return code + "," + percentage + "," + isActive;
    }
}
