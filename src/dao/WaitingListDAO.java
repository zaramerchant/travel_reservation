package dao;

import db.DBConnection;
import java.sql.*;

public class WaitingListDAO {

    public static void addToWaitingList(int customerId, int flightId) {
        String query = "INSERT INTO Waiting_List (customer_id, flight_id, request_time) VALUES (?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ps.setInt(2, flightId);
            ps.executeUpdate();
            System.out.println("Customer added to waiting list.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getWaitingListByFlight(int flightId) {
        String query = "SELECT * FROM Waiting_List WHERE flight_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, flightId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(
                        rs.getInt("waitlist_id") + " | " +
                        rs.getInt("customer_id") + " | " +
                        rs.getInt("flight_id") + " | " +
                        rs.getTimestamp("request_time")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}