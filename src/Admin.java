/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author letmeplay
 */
public class Admin extends User {

//Constructor for admin class. Contains admin as the value for role.
    public Admin(String username, String password, String name, String email) {
        super(username, password, "admin", name, email);
    }
    
}
