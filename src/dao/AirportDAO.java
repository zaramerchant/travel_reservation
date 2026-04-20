package dao;

import db.DBConnection;
import java.sql.*;

public class AirportDAO {

    public static void addAirport(String airportId, String name, String city) {
        String query = "INSERT INTO Airport (airport_id, name, city) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, airportId);
            ps.setString(2, name);
            ps.setString(3, city);
            ps.executeUpdate();
            System.out.println("Airport added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllAirports() {
        String query = "SELECT * FROM Airport";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(
                        rs.getString("airport_id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("city")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}