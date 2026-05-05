package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MainUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private int currentUserId = -1;
    private String currentRole = "";
    private String currentName = "";

    public MainUI() {
        setTitle("Travel Reservation System");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showLoginScreen();
        setVisible(true);
    }

    private void showLoginScreen() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(80, 180, 80, 180));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton customerLogin = new JButton("Customer Login");
        JButton adminLogin = new JButton("Admin Login");
        JButton repLogin = new JButton("Representative Login");

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(customerLogin);
        panel.add(adminLogin);
        panel.add(repLogin);

        customerLogin.addActionListener(e ->
                loginCustomer(emailField.getText(), new String(passwordField.getPassword())));

        adminLogin.addActionListener(e ->
                loginEmployee(emailField.getText(), new String(passwordField.getPassword()), "admin"));

        repLogin.addActionListener(e ->
                loginEmployee(emailField.getText(), new String(passwordField.getPassword()), "customer_rep"));

        add(panel);
        revalidate();
        repaint();
    }

    private void loginCustomer(String email, String password) {
        String sql = "SELECT customer_id, first_name, last_name FROM Customer WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                currentUserId = rs.getInt("customer_id");
                currentRole = "customer";
                currentName = rs.getString("first_name") + " " + rs.getString("last_name");
                showMainScreen(customerPanel());
            } else {
                JOptionPane.showMessageDialog(this, "Invalid customer login.");
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void loginEmployee(String email, String password, String role) {
        String sql = "SELECT employee_id, first_name, last_name, role FROM Employee WHERE email = ? AND password = ? AND role = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                currentUserId = rs.getInt("employee_id");
                currentRole = rs.getString("role");
                currentName = rs.getString("first_name") + " " + rs.getString("last_name");

                if (role.equals("admin")) {
                    showMainScreen(adminPanel());
                } else {
                    showMainScreen(representativePanel());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid employee login.");
            }

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showMainScreen(JPanel rolePanel) {
        getContentPane().removeAll();

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Logged in as: " + currentName + " (" + currentRole + ")");
        JButton logout = new JButton("Logout");

        logout.addActionListener(e -> {
            currentUserId = -1;
            currentRole = "";
            currentName = "";
            showLoginScreen();
        });

        topPanel.add(welcome, BorderLayout.WEST);
        topPanel.add(logout, BorderLayout.EAST);

        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(rolePanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, scrollPane);
        splitPane.setDividerLocation(330);

        add(splitPane);
        revalidate();
        repaint();
    }

    private JPanel customerPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JButton searchFlights = new JButton("Search Flights");
        JButton bookTicket = new JButton("Book Ticket");
        JButton viewReservations = new JButton("View My Reservations");
        JButton joinWaitlist = new JButton("Join Waiting List");
        JButton cancelReservation = new JButton("Cancel Business/First Reservation");
        JButton postQuestion = new JButton("Post Question");
        JButton viewPast = new JButton("View Past Flights");
        JButton viewUpcoming = new JButton("View Upcoming Flights");

        searchFlights.addActionListener(e -> searchFlights());
        bookTicket.addActionListener(e -> bookTicket(currentUserId));
        viewReservations.addActionListener(e -> viewReservations(currentUserId));
        joinWaitlist.addActionListener(e -> joinWaitingList(currentUserId));
        cancelReservation.addActionListener(e -> cancelReservation());
        postQuestion.addActionListener(e -> postQuestion(currentUserId));
        viewPast.addActionListener(e -> viewPastFlights(currentUserId));
        viewUpcoming.addActionListener(e -> viewUpcomingFlights(currentUserId));

        panel.add(searchFlights);
        panel.add(bookTicket);
        panel.add(viewReservations);
        panel.add(joinWaitlist);
        panel.add(cancelReservation);
        panel.add(postQuestion);
        panel.add(viewPast);
        panel.add(viewUpcoming);

        return panel;
    }

    private JPanel adminPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JButton viewCustomers = new JButton("View Customers");
        JButton viewEmployees = new JButton("View Representatives");
        JButton addCustomer = new JButton("Add Customer");
        JButton deleteCustomer = new JButton("Delete Customer");
        JButton addRep = new JButton("Add Representative");
        JButton deleteRep = new JButton("Delete Representative");
        JButton monthlySales = new JButton("Monthly Sales Report");
        JButton reservationsByFlight = new JButton("Reservations by Flight");
        JButton reservationsByCustomer = new JButton("Reservations by Customer");
        JButton revenueByAirline = new JButton("Revenue by Airline");
        JButton topCustomer = new JButton("Top Customer by Revenue");
        JButton activeFlights = new JButton("Most Active Flights");

        viewCustomers.addActionListener(e -> runSelect("SELECT * FROM Customer"));
        viewEmployees.addActionListener(e -> runSelect("SELECT * FROM Employee WHERE role = 'customer_rep'"));
        addCustomer.addActionListener(e -> addCustomer());
        deleteCustomer.addActionListener(e -> deleteCustomer());
        addRep.addActionListener(e -> addRepresentative());
        deleteRep.addActionListener(e -> deleteRepresentative());
        monthlySales.addActionListener(e -> monthlySalesReport());
        reservationsByFlight.addActionListener(e -> reservationsByFlight());
        reservationsByCustomer.addActionListener(e -> reservationsByCustomer());
        revenueByAirline.addActionListener(e -> revenueByAirline());
        topCustomer.addActionListener(e -> topCustomerByRevenue());
        activeFlights.addActionListener(e -> mostActiveFlights());

        panel.add(viewCustomers);
        panel.add(viewEmployees);
        panel.add(addCustomer);
        panel.add(deleteCustomer);
        panel.add(addRep);
        panel.add(deleteRep);
        panel.add(monthlySales);
        panel.add(reservationsByFlight);
        panel.add(reservationsByCustomer);
        panel.add(revenueByAirline);
        panel.add(topCustomer);
        panel.add(activeFlights);

        return panel;
    }

    private JPanel representativePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JButton makeReservation = new JButton("Make Reservation for Customer");
        JButton editReservation = new JButton("Edit Reservation Status");
        JButton addFlight = new JButton("Add Flight");
        JButton deleteFlight = new JButton("Delete Flight");
        JButton addAirport = new JButton("Add Airport");
        JButton addAircraft = new JButton("Add Aircraft");
        JButton viewWaitlist = new JButton("View Waiting List by Flight");
        JButton flightsByAirport = new JButton("Flights by Airport");
        JButton viewQuestions = new JButton("View Questions");
        JButton replyQuestion = new JButton("Reply to Question");

        makeReservation.addActionListener(e -> {
            int customerId = Integer.parseInt(JOptionPane.showInputDialog(this, "Customer ID:"));
            bookTicket(customerId);
        });

        editReservation.addActionListener(e -> editReservationStatus());
        addFlight.addActionListener(e -> addFlight());
        deleteFlight.addActionListener(e -> deleteFlight());
        addAirport.addActionListener(e -> addAirport());
        addAircraft.addActionListener(e -> addAircraft());
        viewWaitlist.addActionListener(e -> viewWaitlistByFlight());
        flightsByAirport.addActionListener(e -> flightsByAirport());
        viewQuestions.addActionListener(e -> runSelect("SELECT * FROM Question"));
        replyQuestion.addActionListener(e -> replyToQuestion());

        panel.add(makeReservation);
        panel.add(editReservation);
        panel.add(addFlight);
        panel.add(deleteFlight);
        panel.add(addAirport);
        panel.add(addAircraft);
        panel.add(viewWaitlist);
        panel.add(flightsByAirport);
        panel.add(viewQuestions);
        panel.add(replyQuestion);

        return panel;
    }

    private void searchFlights() {
        String dep = JOptionPane.showInputDialog(this, "Departure airport code, example JFK:");
        String arr = JOptionPane.showInputDialog(this, "Arrival airport code, example LAX:");
        String date = JOptionPane.showInputDialog(this, "Date YYYY-MM-DD, or leave blank for any date:");

        if (date == null || date.trim().isEmpty()) {
            runPreparedSelect("SELECT * FROM Flight WHERE departure_airport = ? AND arrival_airport = ?", dep, arr);
        } else {
            runPreparedSelect("""
                    SELECT * FROM Flight
                    WHERE departure_airport = ?
                    AND arrival_airport = ?
                    AND DATE(departure_time) = ?
                    """, dep, arr, date);
        }
    }

    private void bookTicket(int customerId) {
        try {
            int flightId = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID:"));
            String type = JOptionPane.showInputDialog(this, "Ticket class: economy, business, or first");
    
            double basePrice = 0.0;
    
            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);
    
                PreparedStatement pricePs = conn.prepareStatement(
                        "SELECT base_price FROM Flight WHERE flight_id = ?"
                );
                pricePs.setInt(1, flightId);
    
                ResultSet priceRs = pricePs.executeQuery();
    
                if (priceRs.next()) {
                    basePrice = priceRs.getDouble("base_price");
                } else {
                    JOptionPane.showMessageDialog(this, "Flight not found.");
                    return;
                }
    
                double finalPrice;
    
                if (type.equalsIgnoreCase("business")) {
                    finalPrice = basePrice * 1.5;
                } else if (type.equalsIgnoreCase("first")) {
                    finalPrice = basePrice * 2.0;
                } else {
                    finalPrice = basePrice;
                    type = "economy";
                }
    
                PreparedStatement ticketPs = conn.prepareStatement(
                        "INSERT INTO Ticket (customer_id, total_fare, booking_date) VALUES (?, ?, NOW())",
                        Statement.RETURN_GENERATED_KEYS
                );
    
                ticketPs.setInt(1, customerId);
                ticketPs.setDouble(2, finalPrice);
                ticketPs.executeUpdate();
    
                ResultSet keys = ticketPs.getGeneratedKeys();
                int ticketId = -1;
    
                if (keys.next()) {
                    ticketId = keys.getInt(1);
                }
    
                PreparedStatement tfPs = conn.prepareStatement(
                        "INSERT INTO Ticket_Flight (ticket_id, flight_id, ticket_type, ticket_status, price) VALUES (?, ?, ?, 'confirmed', ?)"
                );
    
                tfPs.setInt(1, ticketId);
                tfPs.setInt(2, flightId);
                tfPs.setString(3, type.toLowerCase());
                tfPs.setDouble(4, finalPrice);
                tfPs.executeUpdate();
    
                conn.commit();
    
                JOptionPane.showMessageDialog(this,
                        "Ticket booked successfully.\nFinal price: $" + finalPrice);
    
                viewReservations(customerId);
            }
    
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void viewReservations(int customerId) {
        runPreparedSelect("""
                SELECT c.first_name, c.last_name, t.ticket_id, f.flight_id,
                       f.departure_airport, f.arrival_airport,
                       f.departure_time, f.arrival_time,
                       tf.ticket_type, tf.ticket_status, tf.price
                FROM Customer c
                JOIN Ticket t ON c.customer_id = t.customer_id
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                JOIN Flight f ON tf.flight_id = f.flight_id
                WHERE c.customer_id = ?
                """, customerId);
    }

    private void joinWaitingList(int customerId) {
        int flightId = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID:"));
        runPreparedUpdate("INSERT INTO Waiting_List (customer_id, flight_id, request_time, notified) VALUES (?, ?, NOW(), 0)",
                customerId, flightId);
        JOptionPane.showMessageDialog(this, "Added to waiting list.");
        runSelect("SELECT * FROM Waiting_List");
    }

    private void cancelReservation() {
        int ticketId = Integer.parseInt(JOptionPane.showInputDialog(this, "Ticket ID:"));

        runPreparedUpdate("""
                UPDATE Ticket_Flight
                SET ticket_status = 'cancelled'
                WHERE ticket_id = ?
                AND ticket_type IN ('business', 'first')
                """, ticketId);

        JOptionPane.showMessageDialog(this, "Cancelled if ticket was business or first class.");
        viewReservations(currentUserId);
    }

    private void postQuestion(int customerId) {
        String question = JOptionPane.showInputDialog(this, "Enter your question:");
        runPreparedUpdate("INSERT INTO Question (customer_id, question_text) VALUES (?, ?)", customerId, question);
        JOptionPane.showMessageDialog(this, "Question posted.");
        runPreparedSelect("SELECT * FROM Question WHERE customer_id = ?", customerId);
    }

    private void viewPastFlights(int customerId) {
        runPreparedSelect("""
                SELECT t.ticket_id, f.flight_id, f.departure_airport, f.arrival_airport,
                       f.departure_time, f.arrival_time, tf.ticket_status
                FROM Ticket t
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                JOIN Flight f ON tf.flight_id = f.flight_id
                WHERE t.customer_id = ? AND f.departure_time < NOW()
                """, customerId);
    }

    private void viewUpcomingFlights(int customerId) {
        runPreparedSelect("""
                SELECT t.ticket_id, f.flight_id, f.departure_airport, f.arrival_airport,
                       f.departure_time, f.arrival_time, tf.ticket_status
                FROM Ticket t
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                JOIN Flight f ON tf.flight_id = f.flight_id
                WHERE t.customer_id = ? AND f.departure_time >= NOW()
                """, customerId);
    }

    private void addCustomer() {
        String first = JOptionPane.showInputDialog(this, "First name:");
        String last = JOptionPane.showInputDialog(this, "Last name:");
        String email = JOptionPane.showInputDialog(this, "Email:");
        String password = JOptionPane.showInputDialog(this, "Password:");

        runPreparedUpdate("INSERT INTO Customer (first_name, last_name, email, password) VALUES (?, ?, ?, ?)",
                first, last, email, password);
        runSelect("SELECT * FROM Customer");
    }

    private void deleteCustomer() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Customer ID to delete:"));
        runPreparedUpdate("DELETE FROM Customer WHERE customer_id = ?", id);
        runSelect("SELECT * FROM Customer");
    }

    private void addRepresentative() {
        String first = JOptionPane.showInputDialog(this, "First name:");
        String last = JOptionPane.showInputDialog(this, "Last name:");
        String email = JOptionPane.showInputDialog(this, "Email:");
        String password = JOptionPane.showInputDialog(this, "Password:");

        runPreparedUpdate("INSERT INTO Employee (first_name, last_name, email, role, password) VALUES (?, ?, ?, 'customer_rep', ?)",
                first, last, email, password);
        runSelect("SELECT * FROM Employee");
    }

    private void deleteRepresentative() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Representative employee ID to delete:"));
        runPreparedUpdate("DELETE FROM Employee WHERE employee_id = ? AND role = 'customer_rep'", id);
        runSelect("SELECT * FROM Employee");
    }

    private void monthlySalesReport() {
        int month = Integer.parseInt(JOptionPane.showInputDialog(this, "Month number, example 5:"));

        runPreparedSelect("""
                SELECT MONTH(t.booking_date) AS month, SUM(tf.price) AS total_sales
                FROM Ticket t
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                WHERE MONTH(t.booking_date) = ?
                GROUP BY MONTH(t.booking_date)
                """, month);
    }

    private void reservationsByFlight() {
        int flightId = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID:"));

        runPreparedSelect("""
                SELECT f.flight_id, c.first_name, c.last_name, t.ticket_id, tf.ticket_status, tf.ticket_type
                FROM Flight f
                JOIN Ticket_Flight tf ON f.flight_id = tf.flight_id
                JOIN Ticket t ON tf.ticket_id = t.ticket_id
                JOIN Customer c ON t.customer_id = c.customer_id
                WHERE f.flight_id = ?
                """, flightId);
    }

    private void reservationsByCustomer() {
        String name = JOptionPane.showInputDialog(this, "Customer last name:");

        runPreparedSelect("""
                SELECT c.first_name, c.last_name, t.ticket_id, f.flight_id,
                       f.departure_airport, f.arrival_airport, tf.ticket_status, tf.price
                FROM Customer c
                JOIN Ticket t ON c.customer_id = t.customer_id
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                JOIN Flight f ON tf.flight_id = f.flight_id
                WHERE c.last_name = ?
                """, name);
    }

    private void revenueByAirline() {
        runSelect("""
                SELECT a.airline_id, a.name, SUM(tf.price) AS revenue
                FROM Airline a
                JOIN Flight f ON a.airline_id = f.airline_id
                JOIN Ticket_Flight tf ON f.flight_id = tf.flight_id
                GROUP BY a.airline_id, a.name
                """);
    }

    private void topCustomerByRevenue() {
        runSelect("""
                SELECT c.customer_id, c.first_name, c.last_name, SUM(tf.price) AS total_revenue
                FROM Customer c
                JOIN Ticket t ON c.customer_id = t.customer_id
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                GROUP BY c.customer_id, c.first_name, c.last_name
                ORDER BY total_revenue DESC
                LIMIT 1
                """);
    }

    private void mostActiveFlights() {
        runSelect("""
                SELECT f.flight_id, f.departure_airport, f.arrival_airport, COUNT(tf.ticket_id) AS tickets_sold
                FROM Flight f
                JOIN Ticket_Flight tf ON f.flight_id = tf.flight_id
                GROUP BY f.flight_id, f.departure_airport, f.arrival_airport
                ORDER BY tickets_sold DESC
                """);
    }

    private void addFlight() {
        String airline = JOptionPane.showInputDialog(this, "Airline ID:");
        String dep = JOptionPane.showInputDialog(this, "Departure airport:");
        String arr = JOptionPane.showInputDialog(this, "Arrival airport:");
        String depTime = JOptionPane.showInputDialog(this, "Departure time YYYY-MM-DD HH:MM:SS:");
        String arrTime = JOptionPane.showInputDialog(this, "Arrival time YYYY-MM-DD HH:MM:SS:");

        runPreparedUpdate("""
                INSERT INTO Flight (airline_id, departure_airport, arrival_airport, departure_time, arrival_time)
                VALUES (?, ?, ?, ?, ?)
                """, airline, dep, arr, depTime, arrTime);

        runSelect("SELECT * FROM Flight");
    }

    private void deleteFlight() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID to delete:"));
        runPreparedUpdate("DELETE FROM Flight WHERE flight_id = ?", id);
        runSelect("SELECT * FROM Flight");
    }

    private void addAirport() {
        String id = JOptionPane.showInputDialog(this, "Airport ID:");
        String name = JOptionPane.showInputDialog(this, "Airport name:");
        String city = JOptionPane.showInputDialog(this, "City:");

        runPreparedUpdate("INSERT INTO Airport (airport_id, name, city) VALUES (?, ?, ?)", id, name, city);
        runSelect("SELECT * FROM Airport");
    }

    private void addAircraft() {
        String airline = JOptionPane.showInputDialog(this, "Airline ID:");
        String aircraftModel = JOptionPane.showInputDialog(this, "Aircraft model:");
        int seats = Integer.parseInt(JOptionPane.showInputDialog(this, "Total seats:"));

        runPreparedUpdate("INSERT INTO Aircraft (airline_id, model, total_seats) VALUES (?, ?, ?)",
                airline, aircraftModel, seats);
        runSelect("SELECT * FROM Aircraft");
    }

    private void viewWaitlistByFlight() {
        int flightId = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID:"));

        runPreparedSelect("""
                SELECT w.waitlist_id, c.first_name, c.last_name, w.flight_id, w.request_time, w.notified
                FROM Waiting_List w
                JOIN Customer c ON w.customer_id = c.customer_id
                WHERE w.flight_id = ?
                """, flightId);
    }

    private void flightsByAirport() {
        String airport = JOptionPane.showInputDialog(this, "Airport code:");

        runPreparedSelect("""
                SELECT * FROM Flight
                WHERE departure_airport = ? OR arrival_airport = ?
                """, airport, airport);
    }

    private void replyToQuestion() {
        int questionId = Integer.parseInt(JOptionPane.showInputDialog(this, "Question ID:"));
        String reply = JOptionPane.showInputDialog(this, "Reply:");

        runPreparedUpdate("""
                UPDATE Question
                SET employee_id = ?, reply_text = ?, reply_time = NOW()
                WHERE question_id = ?
                """, currentUserId, reply, questionId);

        runSelect("SELECT * FROM Question");
    }

    private void editReservationStatus() {
        int ticketId = Integer.parseInt(JOptionPane.showInputDialog(this, "Ticket ID:"));
        String status = JOptionPane.showInputDialog(this, "New status: confirmed, waitlisted, or cancelled");

        runPreparedUpdate("UPDATE Ticket_Flight SET ticket_status = ? WHERE ticket_id = ?", status, ticketId);
        runSelect("SELECT * FROM Ticket_Flight");
    }

    private void runSelect(String sql) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            fillTable(rs);
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void runPreparedSelect(String sql, Object... values) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }

            ResultSet rs = ps.executeQuery();
            fillTable(rs);

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void runPreparedUpdate(String sql, Object... values) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }

            ps.executeUpdate();

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void fillTable(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();

        model.setRowCount(0);
        model.setColumnCount(0);

        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(meta.getColumnName(i));
        }

        while (rs.next()) {
            Object[] row = new Object[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }

            model.addRow(row);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}