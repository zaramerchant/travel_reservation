package ui;

import db.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
            loginCustomer(
                    emailField.getText().trim(),
                    new String(passwordField.getPassword()).trim()
            ));
    
    adminLogin.addActionListener(e ->
            loginEmployee(
                    emailField.getText().trim(),
                    new String(passwordField.getPassword()).trim(),
                    "admin"
            ));
    
    repLogin.addActionListener(e ->
            loginEmployee(
                    emailField.getText().trim(),
                    new String(passwordField.getPassword()).trim(),
                    "customer_rep"
            ));
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
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
    
        JButton viewCustomers = new JButton("View Customers");
        JButton viewEmployees = new JButton("View Representatives");
        JButton addCustomer = new JButton("Add Customer");
        JButton editCustomer = new JButton("Edit Customer");
        JButton deleteCustomer = new JButton("Delete Customer");
        JButton addRep = new JButton("Add Representative");
        JButton editRep = new JButton("Edit Representative");
        JButton deleteRep = new JButton("Delete Representative");
        JButton monthlySales = new JButton("Monthly Sales Report");
        JButton reservationsByFlight = new JButton("Reservations by Flight");
        JButton reservationsByCustomer = new JButton("Reservations by Customer");
        JButton revenueByFlight = new JButton("Revenue by Flight");
        JButton revenueByAirline = new JButton("Revenue by Airline");
        JButton revenueByCustomer = new JButton("Revenue by Customer");
        JButton topCustomer = new JButton("Top Customer by Revenue");
        JButton activeFlights = new JButton("Most Active Flights");
    
        viewCustomers.addActionListener(e -> runSelect("SELECT * FROM Customer"));
        viewEmployees.addActionListener(e -> runSelect("SELECT * FROM Employee WHERE role = 'customer_rep'"));
        addCustomer.addActionListener(e -> addCustomer());
        editCustomer.addActionListener(e -> editCustomer());
        deleteCustomer.addActionListener(e -> deleteCustomer());
        addRep.addActionListener(e -> addRepresentative());
        editRep.addActionListener(e -> editRepresentative());
        deleteRep.addActionListener(e -> deleteRepresentative());
        monthlySales.addActionListener(e -> monthlySalesReport());
        reservationsByFlight.addActionListener(e -> reservationsByFlight());
        reservationsByCustomer.addActionListener(e -> reservationsByCustomer());
        revenueByFlight.addActionListener(e -> revenueByFlight());
        revenueByAirline.addActionListener(e -> revenueByAirline());
        revenueByCustomer.addActionListener(e -> revenueByCustomer());
        topCustomer.addActionListener(e -> topCustomerByRevenue());
        activeFlights.addActionListener(e -> mostActiveFlights());
    
        panel.add(viewCustomers);
        panel.add(viewEmployees);
        panel.add(addCustomer);
        panel.add(editCustomer);
        panel.add(deleteCustomer);
        panel.add(addRep);
        panel.add(editRep);
        panel.add(deleteRep);
        panel.add(monthlySales);
        panel.add(reservationsByFlight);
        panel.add(reservationsByCustomer);
        panel.add(revenueByFlight);
        panel.add(revenueByAirline);
        panel.add(revenueByCustomer);
        panel.add(topCustomer);
        panel.add(activeFlights);
    
        return panel;
    }

    private JPanel representativePanel() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

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
        JButton editFlight = new JButton("Edit Flight");
        JButton editAirport = new JButton("Edit Airport");
        JButton deleteAirport = new JButton("Delete Airport");
        JButton editAircraft = new JButton("Edit Aircraft");
        JButton deleteAircraft = new JButton("Delete Aircraft");
        

        makeReservation.addActionListener(e -> makeReservationForCustomer());

        editReservation.addActionListener(e -> editReservationStatus());
        addFlight.addActionListener(e -> addFlight());
        deleteFlight.addActionListener(e -> deleteFlight());
        addAirport.addActionListener(e -> addAirport());
        addAircraft.addActionListener(e -> addAircraft());
        viewWaitlist.addActionListener(e -> viewWaitlistByFlight());
        flightsByAirport.addActionListener(e -> flightsByAirport());
        viewQuestions.addActionListener(e -> runSelect("SELECT * FROM Question"));
        replyQuestion.addActionListener(e -> replyToQuestion());
        editFlight.addActionListener(e -> editFlight());
        editAirport.addActionListener(e -> editAirport());
        deleteAirport.addActionListener(e -> deleteAirport());
        editAircraft.addActionListener(e -> editAircraft());
        deleteAircraft.addActionListener(e -> deleteAircraft());

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
        panel.add(editFlight);
        panel.add(editAirport);
        panel.add(deleteAirport);
        panel.add(editAircraft);
        panel.add(deleteAircraft);

        return panel;
    }
    private void makeReservationForCustomer() {
        String email = JOptionPane.showInputDialog(this, "Customer email:");
        if (email == null || email.trim().isEmpty()) return;
        email = email.trim();
        
        int flightId = Integer.parseInt(
                JOptionPane.showInputDialog(this, "Flight ID:")
        );
    
        String ticketType = JOptionPane.showInputDialog(
                this,
                "Ticket type (economy/business/first):"
        );
    
        try (Connection conn = DBConnection.getConnection()) {
    
            PreparedStatement findCustomer = conn.prepareStatement(
                "SELECT customer_id FROM Customer WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))"
        );
        findCustomer.setString(1, email.trim());
    
            ResultSet customerRS = findCustomer.executeQuery();
    
            if (!customerRS.next()) {
                JOptionPane.showMessageDialog(this, "Customer not found.");
                return;
            }
    
            int customerId = customerRS.getInt("customer_id");
    
            PreparedStatement flightPS = conn.prepareStatement(
                    "SELECT available_seats, base_price FROM Flight WHERE flight_id = ?"
            );
            flightPS.setInt(1, flightId);
    
            ResultSet flightRS = flightPS.executeQuery();
    
            if (!flightRS.next()) {
                JOptionPane.showMessageDialog(this, "Flight not found.");
                return;
            }
    
            int seats = flightRS.getInt("available_seats");
            double price = flightRS.getDouble("base_price");
    
            // full -> waitlist
            if (seats <= 0) {
                PreparedStatement wait = conn.prepareStatement(
                        "INSERT INTO Waiting_List(customer_id, flight_id, request_time) VALUES (?, ?, NOW())"
                );
                wait.setInt(1, customerId);
                wait.setInt(2, flightId);
                wait.executeUpdate();
    
                JOptionPane.showMessageDialog(this,
                        "Flight full. Added to waiting list.");
                return;
            }
    
            // create ticket
            PreparedStatement ticketPS = conn.prepareStatement(
                    "INSERT INTO Ticket(customer_id, total_fare, booking_date) VALUES (?, ?, NOW())",
                    Statement.RETURN_GENERATED_KEYS
            );
            ticketPS.setInt(1, customerId);
            ticketPS.setDouble(2, price);
            ticketPS.executeUpdate();
    
            ResultSet keys = ticketPS.getGeneratedKeys();
            keys.next();
            int ticketId = keys.getInt(1);
    
            PreparedStatement tf = conn.prepareStatement(
                    "INSERT INTO Ticket_Flight(ticket_id, flight_id, ticket_type, ticket_status, price) VALUES (?, ?, ?, 'confirmed', ?)"
            );
            tf.setInt(1, ticketId);
            tf.setInt(2, flightId);
            tf.setString(3, ticketType);
            tf.setDouble(4, price);
            tf.executeUpdate();
    
            PreparedStatement seatUpdate = conn.prepareStatement(
                    "UPDATE Flight SET available_seats = available_seats - 1 WHERE flight_id = ?"
            );
            seatUpdate.setInt(1, flightId);
            seatUpdate.executeUpdate();
    
            JOptionPane.showMessageDialog(this, "Reservation created.");
    
            runSelect("SELECT * FROM Ticket_Flight");
    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
    private void editFlight() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID to edit:"));
        String airline = JOptionPane.showInputDialog(this, "New airline ID:");
        String dep = JOptionPane.showInputDialog(this, "New departure airport:");
        String arr = JOptionPane.showInputDialog(this, "New arrival airport:");
        String depTime = JOptionPane.showInputDialog(this, "New departure time YYYY-MM-DD HH:MM:SS:");
        String arrTime = JOptionPane.showInputDialog(this, "New arrival time YYYY-MM-DD HH:MM:SS:");
        double price = Double.parseDouble(JOptionPane.showInputDialog(this, "New base price:"));
        int seats = Integer.parseInt(JOptionPane.showInputDialog(this, "New available seats:"));
        int stops = Integer.parseInt(JOptionPane.showInputDialog(this, "New number of stops:"));
    
        runPreparedUpdate("""
            UPDATE Flight
            SET airline_id = ?, departure_airport = ?, arrival_airport = ?,
                departure_time = ?, arrival_time = ?, base_price = ?,
                available_seats = ?, number_of_stops = ?
            WHERE flight_id = ?
            """, airline, dep, arr, depTime, arrTime, price, seats, stops, id);
    
        runSelect("SELECT * FROM Flight");
    }
    private void editAirport() {
        String id = JOptionPane.showInputDialog(this, "Airport ID to edit:");
        String name = JOptionPane.showInputDialog(this, "New airport name:");
        String city = JOptionPane.showInputDialog(this, "New city:");
    
        runPreparedUpdate("""
            UPDATE Airport
            SET name = ?, city = ?
            WHERE airport_id = ?
            """, name, city, id);
    
        runSelect("SELECT * FROM Airport");
    }
    private void deleteAirport() {
        String id = JOptionPane.showInputDialog(this, "Airport ID to delete:");
    
        runPreparedUpdate("DELETE FROM Airport WHERE airport_id = ?", id);
    
        runSelect("SELECT * FROM Airport");
    }
    private void editAircraft() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Aircraft ID to edit:"));
        String airline = JOptionPane.showInputDialog(this, "New airline ID:");
        String model = JOptionPane.showInputDialog(this, "New model:");
        int seats = Integer.parseInt(JOptionPane.showInputDialog(this, "New total seats:"));
    
        runPreparedUpdate("""
            UPDATE Aircraft
            SET airline_id = ?, model = ?, total_seats = ?
            WHERE aircraft_id = ?
            """, airline, model, seats, id);
    
        runSelect("SELECT * FROM Aircraft");
    }
    private void deleteAircraft() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Aircraft ID to delete:"));
    
        runPreparedUpdate("DELETE FROM Aircraft WHERE aircraft_id = ?", id);
    
        runSelect("SELECT * FROM Aircraft");
    }

    private void editCustomer() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Customer ID to edit:"));
        String first = JOptionPane.showInputDialog(this, "New first name:");
        String last = JOptionPane.showInputDialog(this, "New last name:");
        String email = JOptionPane.showInputDialog(this, "New email:");
        String password = JOptionPane.showInputDialog(this, "New password:");
    
        runPreparedUpdate("""
                UPDATE Customer
                SET first_name = ?, last_name = ?, email = ?, password = ?
                WHERE customer_id = ?
                """, first, last, email, password, id);
    
        runSelect("SELECT * FROM Customer");
    }



    private void searchFlights() {
        JPanel panel = new JPanel(new GridLayout(12, 2, 10, 10));

        JTextField depField = new JTextField("JFK");
        JTextField arrField = new JTextField("LAX");
        JTextField departDateField = new JTextField("2026-05-10");
        JTextField returnDateField = new JTextField("2026-05-17");

        JComboBox<String> tripTypeBox = new JComboBox<>(new String[]{
                "One-Way",
                "Round-Trip"
        });

        JCheckBox flexibleBox = new JCheckBox("+/- 3 days");

        JComboBox<String> sortBox = new JComboBox<>(new String[]{
                "None",
                "Price",
                "Take-off Time",
                "Landing Time",
                "Duration"
        });

        JTextField airlineField = new JTextField();
        JTextField maxPriceField = new JTextField();
        JTextField maxStopsField = new JTextField();
        JTextField earliestTakeoffField = new JTextField();
        JTextField latestLandingField = new JTextField();

        panel.add(new JLabel("Departure Airport:"));
        panel.add(depField);

        panel.add(new JLabel("Arrival Airport:"));
        panel.add(arrField);

        panel.add(new JLabel("Departure Date YYYY-MM-DD:"));
        panel.add(departDateField);

        panel.add(new JLabel("Return Date YYYY-MM-DD:"));
        panel.add(returnDateField);

        panel.add(new JLabel("Trip Type:"));
        panel.add(tripTypeBox);

        panel.add(new JLabel("Flexible Dates:"));
        panel.add(flexibleBox);

        panel.add(new JLabel("Sort By:"));
        panel.add(sortBox);

        panel.add(new JLabel("Airline Filter, optional:"));
        panel.add(airlineField);

        panel.add(new JLabel("Max Price, optional:"));
        panel.add(maxPriceField);

        panel.add(new JLabel("Max Stops, optional:"));
        panel.add(maxStopsField);

        panel.add(new JLabel("Earliest Takeoff HH:MM:SS, optional:"));
        panel.add(earliestTakeoffField);

        panel.add(new JLabel("Latest Landing HH:MM:SS, optional:"));
        panel.add(latestLandingField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Search Flights",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String dep = depField.getText().trim();
        String arr = arrField.getText().trim();
        String departDate = departDateField.getText().trim();
        String returnDate = returnDateField.getText().trim();
        String tripType = (String) tripTypeBox.getSelectedItem();
        boolean flexible = flexibleBox.isSelected();
        String sortBy = (String) sortBox.getSelectedItem();

        showFlightSearchResults(
                dep,
                arr,
                departDate,
                flexible,
                sortBy,
                airlineField.getText().trim(),
                maxPriceField.getText().trim(),
                maxStopsField.getText().trim(),
                earliestTakeoffField.getText().trim(),
                latestLandingField.getText().trim(),
                "Outbound Flights"
        );

        if ("Round-Trip".equals(tripType)) {
            JOptionPane.showMessageDialog(this, "Now showing return flights.");

            showFlightSearchResults(
                    arr,
                    dep,
                    returnDate,
                    flexible,
                    sortBy,
                    airlineField.getText().trim(),
                    maxPriceField.getText().trim(),
                    maxStopsField.getText().trim(),
                    earliestTakeoffField.getText().trim(),
                    latestLandingField.getText().trim(),
                    "Return Flights"
            );
        }
    }

    private void showFlightSearchResults(
            String dep,
            String arr,
            String date,
            boolean flexible,
            String sortBy,
            String airline,
            String maxPrice,
            String maxStops,
            String earliestTakeoff,
            String latestLanding,
            String label
    ) {
        StringBuilder sql = new StringBuilder("""
                SELECT flight_id, airline_id, departure_airport, arrival_airport,
                       departure_time, arrival_time, base_price, available_seats,
                       number_of_stops,
                       TIMESTAMPDIFF(MINUTE, departure_time, arrival_time) AS duration_minutes
                FROM Flight
                WHERE departure_airport = ?
                AND arrival_airport = ?
                """);

        boolean hasDate = date != null && !date.trim().isEmpty();

        if (hasDate) {
            if (flexible) {
                sql.append(" AND DATE(departure_time) BETWEEN DATE_SUB(?, INTERVAL 3 DAY) AND DATE_ADD(?, INTERVAL 3 DAY) ");
            } else {
                sql.append(" AND DATE(departure_time) = ? ");
            }
        }

        if (!airline.isEmpty()) {
            sql.append(" AND airline_id = ? ");
        }

        if (!maxPrice.isEmpty()) {
            sql.append(" AND base_price <= ? ");
        }

        if (!maxStops.isEmpty()) {
            sql.append(" AND number_of_stops <= ? ");
        }

        if (!earliestTakeoff.isEmpty()) {
            sql.append(" AND TIME(departure_time) >= ? ");
        }

        if (!latestLanding.isEmpty()) {
            sql.append(" AND TIME(arrival_time) <= ? ");
        }

        if ("Price".equals(sortBy)) {
            sql.append(" ORDER BY base_price ASC ");
        } else if ("Take-off Time".equals(sortBy)) {
            sql.append(" ORDER BY departure_time ASC ");
        } else if ("Landing Time".equals(sortBy)) {
            sql.append(" ORDER BY arrival_time ASC ");
        } else if ("Duration".equals(sortBy)) {
            sql.append(" ORDER BY duration_minutes ASC ");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            ps.setString(index++, dep);
            ps.setString(index++, arr);

            if (hasDate) {
                if (flexible) {
                    ps.setString(index++, date);
                    ps.setString(index++, date);
                } else {
                    ps.setString(index++, date);
                }
            }

            if (!airline.isEmpty()) {
                ps.setString(index++, airline);
            }

            if (!maxPrice.isEmpty()) {
                ps.setDouble(index++, Double.parseDouble(maxPrice));
            }

            if (!maxStops.isEmpty()) {
                ps.setInt(index++, Integer.parseInt(maxStops));
            }

            if (!earliestTakeoff.isEmpty()) {
                ps.setString(index++, earliestTakeoff);
            }

            if (!latestLanding.isEmpty()) {
                ps.setString(index++, latestLanding);
            }

            ResultSet rs = ps.executeQuery();
            fillTable(rs);

            JOptionPane.showMessageDialog(this, label + " loaded.");

        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void bookTicket(int customerId) {
        try {
            int flightId = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID:"));
            String type = JOptionPane.showInputDialog(this, "Ticket class: economy, business, or first");

            double basePrice = 0.0;
            int availableSeats = 0;

            try (Connection conn = DBConnection.getConnection()) {
                conn.setAutoCommit(false);

                PreparedStatement flightPs = conn.prepareStatement(
                        "SELECT base_price, available_seats FROM Flight WHERE flight_id = ?"
                );
                flightPs.setInt(1, flightId);

                ResultSet flightRs = flightPs.executeQuery();

                if (flightRs.next()) {
                    basePrice = flightRs.getDouble("base_price");
                    availableSeats = flightRs.getInt("available_seats");
                } else {
                    JOptionPane.showMessageDialog(this, "Flight not found.");
                    conn.rollback();
                    return;
                }

                if (availableSeats <= 0) {
                    PreparedStatement waitPs = conn.prepareStatement(
                            "INSERT INTO Waiting_List (customer_id, flight_id, request_time, notified) VALUES (?, ?, NOW(), 0)"
                    );
                    waitPs.setInt(1, customerId);
                    waitPs.setInt(2, flightId);
                    waitPs.executeUpdate();

                    conn.commit();

                    JOptionPane.showMessageDialog(this, "Flight is full. You were added to the waiting list.");
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

                PreparedStatement seatPs = conn.prepareStatement(
                        "UPDATE Flight SET available_seats = available_seats - 1 WHERE flight_id = ?"
                );
                seatPs.setInt(1, flightId);
                seatPs.executeUpdate();

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

    try (Connection conn = DBConnection.getConnection()) {

        PreparedStatement checkPs = conn.prepareStatement("""
                SELECT *
                FROM Waiting_List
                WHERE customer_id = ?
                AND flight_id = ?
                """);

        checkPs.setInt(1, customerId);
        checkPs.setInt(2, flightId);

        ResultSet checkRs = checkPs.executeQuery();

        if (checkRs.next()) {
            JOptionPane.showMessageDialog(
                    this,
                    "You are already on the waiting list for flight " + flightId + "."
            );
        } else {
            PreparedStatement insertPs = conn.prepareStatement("""
                    INSERT INTO Waiting_List (customer_id, flight_id, request_time, notified)
                    VALUES (?, ?, NOW(), 0)
                    """);

            insertPs.setInt(1, customerId);
            insertPs.setInt(2, flightId);
            insertPs.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Added to waiting list for flight " + flightId + "."
            );
        }

        PreparedStatement listPs = conn.prepareStatement("""
                SELECT w.waitlist_id, c.first_name, c.last_name,
                       w.customer_id, w.flight_id, w.request_time, w.notified
                FROM Waiting_List w
                JOIN Customer c ON w.customer_id = c.customer_id
                WHERE w.flight_id = ?
                ORDER BY w.request_time
                """);

        listPs.setInt(1, flightId);

        ResultSet listRs = listPs.executeQuery();
        fillTable(listRs);

    } catch (Exception ex) {
        showError(ex);
    }
}

    private void cancelReservation() {
    int ticketId = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Ticket ID to cancel:"));

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        PreparedStatement checkPs = conn.prepareStatement("""
                SELECT tf.ticket_id, tf.flight_id, tf.ticket_type, tf.ticket_status
                FROM Ticket_Flight tf
                JOIN Ticket t ON tf.ticket_id = t.ticket_id
                WHERE tf.ticket_id = ?
                AND t.customer_id = ?
                """);

        checkPs.setInt(1, ticketId);
        checkPs.setInt(2, currentUserId);

        ResultSet rs = checkPs.executeQuery();

        if (!rs.next()) {
            JOptionPane.showMessageDialog(this, "Ticket not found for your account.");
            conn.rollback();
            return;
        }

        String ticketType = rs.getString("ticket_type");
        String status = rs.getString("ticket_status");
        int flightId = rs.getInt("flight_id");

        if (!ticketType.equalsIgnoreCase("business") && !ticketType.equalsIgnoreCase("first")) {
            JOptionPane.showMessageDialog(this, "Only business or first class tickets can be cancelled.");
            conn.rollback();
            return;
        }

        if (status.equalsIgnoreCase("cancelled")) {
            JOptionPane.showMessageDialog(this, "This ticket is already cancelled.");
            conn.rollback();
            return;
        }

        PreparedStatement cancelPs = conn.prepareStatement("""
                UPDATE Ticket_Flight
                SET ticket_status = 'cancelled'
                WHERE ticket_id = ?
                """);

        cancelPs.setInt(1, ticketId);
        cancelPs.executeUpdate();

        PreparedStatement seatPs = conn.prepareStatement("""
                UPDATE Flight
                SET available_seats = available_seats + 1
                WHERE flight_id = ?
                """);

        seatPs.setInt(1, flightId);
        seatPs.executeUpdate();

        PreparedStatement notifyPs = conn.prepareStatement("""
                UPDATE Waiting_List
                SET notified = 1
                WHERE flight_id = ?
                AND notified = 0
                ORDER BY request_time
                LIMIT 1
                """);

        notifyPs.setInt(1, flightId);
        notifyPs.executeUpdate();

        conn.commit();

        JOptionPane.showMessageDialog(this, "Reservation cancelled. One waiting-list customer was notified if available.");
        viewReservations(currentUserId);

    } catch (Exception ex) {
        showError(ex);
    }
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

    private void revenueByFlight() {
        int flightId = Integer.parseInt(JOptionPane.showInputDialog(this, "Flight ID:"));
    
        runPreparedSelect("""
                SELECT f.flight_id, f.airline_id, f.departure_airport, f.arrival_airport,
                       SUM(tf.price) AS revenue
                FROM Flight f
                JOIN Ticket_Flight tf ON f.flight_id = tf.flight_id
                WHERE f.flight_id = ?
                GROUP BY f.flight_id, f.airline_id, f.departure_airport, f.arrival_airport
                """, flightId);
    }

    private void revenueByCustomer() {
        int customerId = Integer.parseInt(JOptionPane.showInputDialog(this, "Customer ID:"));
    
        runPreparedSelect("""
                SELECT c.customer_id, c.first_name, c.last_name,
                       SUM(tf.price) AS revenue
                FROM Customer c
                JOIN Ticket t ON c.customer_id = t.customer_id
                JOIN Ticket_Flight tf ON t.ticket_id = tf.ticket_id
                WHERE c.customer_id = ?
                GROUP BY c.customer_id, c.first_name, c.last_name
                """, customerId);
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
        int aircraftId = Integer.parseInt(JOptionPane.showInputDialog(this, "Aircraft ID:"));
        String dep = JOptionPane.showInputDialog(this, "Departure airport:");
        String arr = JOptionPane.showInputDialog(this, "Arrival airport:");
        String depTime = JOptionPane.showInputDialog(this, "Departure time YYYY-MM-DD HH:MM:SS:");
        String arrTime = JOptionPane.showInputDialog(this, "Arrival time YYYY-MM-DD HH:MM:SS:");
        double price = Double.parseDouble(JOptionPane.showInputDialog(this, "Base price:"));
        int seats = Integer.parseInt(JOptionPane.showInputDialog(this, "Available seats:"));
        int stops = Integer.parseInt(JOptionPane.showInputDialog(this, "Number of stops:"));

        runPreparedUpdate("""
                INSERT INTO Flight
                (airline_id, aircraft_id, departure_airport, arrival_airport, departure_time, arrival_time, base_price, available_seats, number_of_stops)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, airline, aircraftId, dep, arr, depTime, arrTime, price, seats, stops);

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
    try (Connection conn = DBConnection.getConnection()) {

        // show unanswered questions
        String sql = """
            SELECT q.question_id,
                   c.first_name,
                   c.last_name,
                   q.question_text
            FROM Question q
            JOIN Customer c ON q.customer_id = c.customer_id
            WHERE q.reply_text IS NULL
            ORDER BY q.question_time
        """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        StringBuilder choices = new StringBuilder();
        Map<Integer,String> questions = new HashMap<>();

        while (rs.next()) {
            int id = rs.getInt("question_id");
            String customer =
                    rs.getString("first_name") + " " +
                    rs.getString("last_name");
            String question = rs.getString("question_text");

            questions.put(id, question);

            choices.append("ID ")
                   .append(id)
                   .append(" — ")
                   .append(customer)
                   .append(": ")
                   .append(question)
                   .append("\n\n");
        }

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No unanswered questions.");
            return;
        }

        // show all questions first
        JTextArea area = new JTextArea(choices.toString());
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        JScrollPane pane = new JScrollPane(area);
        pane.setPreferredSize(new Dimension(500,300));

        JOptionPane.showMessageDialog(
                this,
                pane,
                "Unanswered Questions",
                JOptionPane.INFORMATION_MESSAGE
        );

        // choose ID
        int questionId = Integer.parseInt(
                JOptionPane.showInputDialog(
                        this,
                        "Enter Question ID to reply to:"
                )
        );

        String selectedQuestion = questions.get(questionId);

        if (selectedQuestion == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid Question ID");
            return;
        }

        // show chosen question clearly
        String reply = JOptionPane.showInputDialog(
                this,
                "Question:\n\n" + selectedQuestion +
                "\n\nType reply:"
        );

        if (reply == null || reply.isBlank()) return;

        // save reply
        PreparedStatement update = conn.prepareStatement("""
            UPDATE Question
            SET employee_id = ?,
                reply_text = ?,
                reply_time = NOW()
            WHERE question_id = ?
        """);

        update.setInt(1, currentUserId);
        update.setString(2, reply);
        update.setInt(3, questionId);
        update.executeUpdate();

        JOptionPane.showMessageDialog(this,
                "Reply saved.");

        runSelect("SELECT * FROM Question");

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
                e.getMessage());
    }
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
    private void editRepresentative() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Representative ID to edit:"));
        String first = JOptionPane.showInputDialog(this, "New first name:");
        String last = JOptionPane.showInputDialog(this, "New last name:");
        String email = JOptionPane.showInputDialog(this, "New email:");
        String password = JOptionPane.showInputDialog(this, "New password:");
    
        runPreparedUpdate("""
                UPDATE Employee
                SET first_name = ?, last_name = ?, email = ?, password = ?
                WHERE employee_id = ? AND role = 'customer_rep'
                """, first, last, email, password, id);
    
        runSelect("SELECT * FROM Employee WHERE role = 'customer_rep'");
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