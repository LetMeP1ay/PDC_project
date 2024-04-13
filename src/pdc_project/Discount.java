/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdc_project;

/**
 *
 * @author Bubbltea
 */
public class Discount {
    private String code;
    private double percentage;
    private boolean isActive;

    public Discount(String code, double percentage) {
        this.code = code;
        this.percentage = percentage;
        this.isActive = false;
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

    public void setActive(boolean active) {
        isActive = active;
    }
}
