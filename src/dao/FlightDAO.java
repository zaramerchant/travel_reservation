package dao;

import db.DBConnection;
import java.sql.*;

public class FlightDAO {

    public static void addFlight(String airlineId, String departureAirport, String arrivalAirport,
                                 String departureTime, String arrivalTime) {
        String query = "INSERT INTO Flight (airline_id, departure_airport, arrival_airport, departure_time, arrival_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, airlineId);
            ps.setString(2, departureAirport);
            ps.setString(3, arrivalAirport);
            ps.setString(4, departureTime);
            ps.setString(5, arrivalTime);
            ps.executeUpdate();
            System.out.println("Flight added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllFlights() {
        String query = "SELECT * FROM Flight";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("flight_id") + " | " +
                        rs.getString("airline_id") + " | " +
                        rs.getString("departure_airport") + " -> " +
                        rs.getString("arrival_airport") + " | " +
                        rs.getTimestamp("departure_time") + " | " +
                        rs.getTimestamp("arrival_time")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void searchFlights(String departureAirport, String arrivalAirport) {
        String query = "SELECT * FROM Flight WHERE departure_airport = ? AND arrival_airport = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, departureAirport);
            ps.setString(2, arrivalAirport);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("flight_id") + " | " +
                        rs.getString("airline_id") + " | " +
                        rs.getString("departure_airport") + " -> " +
                        rs.getString("arrival_airport") + " | " +
                        rs.getTimestamp("departure_time")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}