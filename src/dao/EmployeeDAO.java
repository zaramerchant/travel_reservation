package dao;

import db.DBConnection;
import java.sql.*;

public class EmployeeDAO {

    public static void addEmployee(String firstName, String lastName, String email, String role) {
        String query = "INSERT INTO Employee (first_name, last_name, email, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, role);
            ps.executeUpdate();
            System.out.println("Employee added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllEmployees() {
        String query = "SELECT * FROM Employee";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("employee_id") + " | " +
                        rs.getString("first_name") + " | " +
                        rs.getString("last_name") + " | " +
                        rs.getString("role")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}