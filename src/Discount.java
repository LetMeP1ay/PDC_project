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

    public Discount() {
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

    public boolean getIsActive() {
        return isActive;
    }
// Retrieve the status of the current discount based on the isActive variable.
    public String retrieveStatus() {
        if (isActive == true) {
            return "Your discount is currently active.";
        }
        else {
            return "Your discount is currently disabled.";
        }
    }
// Toggle the discount
    public void changeActive() {

        if (isActive == true) {
            this.isActive = false;
        } else {
            this.isActive = true;
        }
    }
    @Override
    public String toString() {
        return code + "," + percentage + "," + isActive;
    }
}
