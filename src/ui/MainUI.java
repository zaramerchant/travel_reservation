package ui;

import dao.*;
import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Travel Reservation System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Customer", customerPanel());
        tabs.addTab("Admin", adminPanel());
        tabs.addTab("Representative", representativePanel());

        add(tabs);
        setVisible(true);
    }

    private JPanel customerPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton searchFlights = new JButton("Search Flights");
        JButton viewTickets = new JButton("View My Reservations");
        JButton waitlist = new JButton("Join Waiting List");
        JButton askQuestion = new JButton("Post Question");

        searchFlights.addActionListener(e -> openSearchFlightsWindow());
        viewTickets.addActionListener(e -> TicketDAO.getAllTickets());

        panel.add(searchFlights);
        panel.add(viewTickets);
        panel.add(waitlist);
        panel.add(askQuestion);

        return panel;
    }

    private JPanel adminPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton customers = new JButton("View Customers");
        JButton employees = new JButton("View Employees");
        JButton salesReport = new JButton("Monthly Sales Report");
        JButton activeFlights = new JButton("Most Active Flights");

        customers.addActionListener(e -> CustomerDAO.getAllCustomers());
        employees.addActionListener(e -> EmployeeDAO.getAllEmployees());

        panel.add(customers);
        panel.add(employees);
        panel.add(salesReport);
        panel.add(activeFlights);

        return panel;
    }

    private JPanel representativePanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));

        JButton addFlight = new JButton("Add Flight");
        JButton viewFlights = new JButton("View Flights");
        JButton viewWaitlist = new JButton("View Waiting List");
        JButton addAirport = new JButton("Add Airport");
        JButton addAircraft = new JButton("Add Aircraft");

        addFlight.addActionListener(e -> openAddFlightWindow());
        viewFlights.addActionListener(e -> FlightDAO.getAllFlights());

        panel.add(addFlight);
        panel.add(viewFlights);
        panel.add(viewWaitlist);
        panel.add(addAirport);
        panel.add(addAircraft);

        return panel;
    }

    private void openSearchFlightsWindow() {
        JFrame frame = new JFrame("Search Flights");
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField departureField = new JTextField();
        JTextField arrivalField = new JTextField();

        JButton searchButton = new JButton("Search");

        searchButton.addActionListener(e -> {
            String departure = departureField.getText();
            String arrival = arrivalField.getText();

            FlightDAO.searchFlights(departure, arrival);
            JOptionPane.showMessageDialog(frame, "Results printed in console.");
        });

        panel.add(new JLabel("Departure Airport:"));
        panel.add(departureField);

        panel.add(new JLabel("Arrival Airport:"));
        panel.add(arrivalField);

        panel.add(searchButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void openAddFlightWindow() {
        JFrame frame = new JFrame("Add Flight");
        frame.setSize(450, 350);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField airlineField = new JTextField();
        JTextField departureField = new JTextField();
        JTextField arrivalField = new JTextField();
        JTextField depTimeField = new JTextField("2026-05-05 10:00:00");
        JTextField arrTimeField = new JTextField("2026-05-05 12:00:00");

        JButton addButton = new JButton("Add Flight");

        addButton.addActionListener(e -> {
            FlightDAO.addFlight(
                    airlineField.getText(),
                    departureField.getText(),
                    arrivalField.getText(),
                    depTimeField.getText(),
                    arrTimeField.getText()
            );

            JOptionPane.showMessageDialog(frame, "Flight added.");
        });

        panel.add(new JLabel("Airline ID:"));
        panel.add(airlineField);

        panel.add(new JLabel("Departure Airport:"));
        panel.add(departureField);

        panel.add(new JLabel("Arrival Airport:"));
        panel.add(arrivalField);

        panel.add(new JLabel("Departure Time:"));
        panel.add(depTimeField);

        panel.add(new JLabel("Arrival Time:"));
        panel.add(arrTimeField);

        panel.add(addButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}