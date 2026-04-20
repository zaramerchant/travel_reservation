import db.DBConnection;
import java.sql.Connection;
import dao.CustomerDAO;
import dao.FlightDAO;
import dao.TicketDAO;
import dao.WaitingListDAO;

public class Main {
    public static void main(String[] args) {
        System.out.println("Customers:");
        CustomerDAO.getAllCustomers();

        System.out.println("\nFlights:");
        FlightDAO.getAllFlights();

        System.out.println("\nSearch EWR -> JFK:");
        FlightDAO.searchFlights("EWR", "JFK");

        System.out.println("\nTickets:");
        TicketDAO.getAllTickets();

        System.out.println("\nWaiting List for Flight 1:");
        WaitingListDAO.getWaitingListByFlight(1);
    }
}
