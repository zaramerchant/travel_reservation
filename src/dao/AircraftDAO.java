package dao;

import db.DBConnection;
import java.sql.*;

public class AircraftDAO {

    public static void addAircraft(String airlineId, String model, int totalSeats) {
        String query = "INSERT INTO Aircraft (airline_id, model, total_seats) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, airlineId);
            ps.setString(2, model);
            ps.setInt(3, totalSeats);
            ps.executeUpdate();
            System.out.println("Aircraft added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllAircraft() {
        String query = "SELECT * FROM Aircraft";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("aircraft_id") + " | " +
                        rs.getString("airline_id") + " | " +
                        rs.getString("model") + " | " +
                        rs.getInt("total_seats")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}