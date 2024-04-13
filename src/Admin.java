/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author letmeplay
 */
public class Admin extends User {

    public Admin(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
    
}
