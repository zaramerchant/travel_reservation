package dao;

import db.DBConnection;
import java.sql.*;

public class CustomerDAO {

    public static void addCustomer(String firstName, String lastName, String email, String password) {
        String query = "INSERT INTO Customer (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.executeUpdate();
            System.out.println("Customer added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllCustomers() {
        String query = "SELECT * FROM Customer";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("customer_id") + " | " +
                        rs.getString("first_name") + " | " +
                        rs.getString("last_name") + " | " +
                        rs.getString("email")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}