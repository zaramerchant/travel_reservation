package dao;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirlineDAO {

    public static void addAirline(String airlineId, String name) {
        String query = "INSERT INTO Airline (airline_id, name) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, airlineId);
            ps.setString(2, name);
            ps.executeUpdate();
            System.out.println("Airline added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllAirlines() {
        String query = "SELECT * FROM Airline";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(rs.getString("airline_id") + " | " + rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}