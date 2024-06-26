import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author letmeplay
 */

public class ShoppingSystemDB {

    private static final String DB_URL = "jdbc:derby:OSS_DB;create=true";

    // Checks if the database has no tables. If so, creates them and populates with standard data.

    public static void initializeDatabase() {
        if (dbIsEmpty()) {
            createTables();
            populateInitialData();
        }
    }

    // Checks if the database has no tables.

    private static boolean dbIsEmpty() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM SYS.SYSTABLES WHERE TABLETYPE='T'")) {
            if (rs.next()) {
                int tableCount = rs.getInt(1);
                return tableCount == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking if database is empty: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    // Creates the necessary tables.

    private static void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String[] tables = {
                    "CREATE TABLE USERS (USERS_USERNAME VARCHAR(50) PRIMARY KEY, USERS_PASSWORD VARCHAR(50), USERS_ROLE VARCHAR(50), "
                            + "USERS_NAME VARCHAR(50), USERS_EMAIL VARCHAR(50), USERS_ADDRESS VARCHAR(50), USERS_BALANCE DOUBLE)",
                    "CREATE TABLE INVENTORY (INVENTORY_PRODNAME VARCHAR(50) PRIMARY KEY, INVENTORY_PRICE DOUBLE, INVENTORY_QUANTITY INT)",
                    "CREATE TABLE ORDERS (ORDERS_ID VARCHAR(50) PRIMARY KEY, ORDERS_TOTAL DOUBLE, ORDERS_STATUS VARCHAR(50), "
                            + "USERS_USERNAME VARCHAR(50), INVENTORY_PRODNAME VARCHAR(50), FOREIGN KEY (USERS_USERNAME) REFERENCES USERS(USERS_USERNAME))",
                    "CREATE TABLE CART (USERS_USERNAME VARCHAR(50), PRODUCT_ID VARCHAR(50), "
                            + "QUANTITY INT, PRICE DOUBLE, PRIMARY KEY (USERS_USERNAME, PRODUCT_ID), "
                            + "FOREIGN KEY (USERS_USERNAME) REFERENCES USERS(USERS_USERNAME), "
                            + "FOREIGN KEY (PRODUCT_ID) REFERENCES INVENTORY(INVENTORY_PRODNAME))"
            };
    
            for (String sql : tables) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Populates the USERS table with admin user.

    private static void populateInitialData() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String[] createAdmin = {
                    "INSERT INTO USERS (USERS_USERNAME, USERS_PASSWORD, USERS_ROLE, USERS_NAME, USERS_EMAIL, USERS_ADDRESS, USERS_BALANCE) VALUES ('admin', 'admin', 'admin', 'Admin User', 'admin@admin.com', '123 Admin St', 1000000)"
            };

            for (String sql : createAdmin) {
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error populating initial data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Removes the tables. We used this method when we had problems with the DB.

    public static void removeTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            String[] tables = { "ORDERS", "ORDER_PRODUCT", "CART", "INVENTORY", "USERS" };

            for (String table : tables) {
                try {
                    stmt.executeUpdate("DROP TABLE " + table);
                } catch (SQLException e) {
                    System.out.println("Error dropping table " + table + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error removing tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}