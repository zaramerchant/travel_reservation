package dao;

import db.DBConnection;
import java.sql.*;

public class TicketDAO {

    public static void addTicket(int customerId, double totalFare) {
        String query = "INSERT INTO Ticket (customer_id, total_fare, booking_date) VALUES (?, ?, NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, customerId);
            ps.setDouble(2, totalFare);
            ps.executeUpdate();
            System.out.println("Ticket added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void linkTicketToFlight(int ticketId, int flightId) {
        String query = "INSERT INTO Ticket_Flight (ticket_id, flight_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, ticketId);
            ps.setInt(2, flightId);
            ps.executeUpdate();
            System.out.println("Ticket linked to flight successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllTickets() {
        String query = "SELECT * FROM Ticket";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                System.out.println(
                        rs.getInt("ticket_id") + " | " +
                        rs.getInt("customer_id") + " | " +
                        rs.getDouble("total_fare") + " | " +
                        rs.getTimestamp("booking_date")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}