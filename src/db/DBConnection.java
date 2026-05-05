package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/travel_reservation";
    private static final String USER = "root";
    private static final String PASSWORD = "sarashareef16";//insert your password here

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}