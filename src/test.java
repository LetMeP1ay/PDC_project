/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author letmeplay
 */
public class test {
    public static void main(String[] args) {
        String databaseURL = "jbdc:derby:OSS_DB";
        try {
            Connection connection = DriverManager.getConnection(databaseURL);
            
            System.out.println("Connection successful");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
