DROP DATABASE IF EXISTS travel_reservation;
CREATE DATABASE travel_reservation;
USE travel_reservation;

CREATE TABLE Airline (
    airline_id VARCHAR(2) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Airport (
    airport_id VARCHAR(3) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);

CREATE TABLE Aircraft (
    aircraft_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(2),
    model VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
);

CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE Employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100) UNIQUE,
    role VARCHAR(50),
    password VARCHAR(100)
);

CREATE TABLE Flight (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(2),
    aircraft_id INT,
    departure_airport VARCHAR(3),
    arrival_airport VARCHAR(3),
    departure_time DATETIME,
    arrival_time DATETIME,
    base_price DECIMAL(10,2),
    available_seats INT,
    number_of_stops INT DEFAULT 0,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id),
    FOREIGN KEY (aircraft_id) REFERENCES Aircraft(aircraft_id),
    FOREIGN KEY (departure_airport) REFERENCES Airport(airport_id),
    FOREIGN KEY (arrival_airport) REFERENCES Airport(airport_id)
);

CREATE TABLE Ticket (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    total_fare DECIMAL(10,2),
    booking_date DATETIME,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
);

CREATE TABLE Ticket_Flight (
    ticket_id INT,
    flight_id INT,
    ticket_type VARCHAR(20),
    ticket_status VARCHAR(20),
    price DECIMAL(10,2),
    PRIMARY KEY (ticket_id, flight_id),
    FOREIGN KEY (ticket_id) REFERENCES Ticket(ticket_id),
    FOREIGN KEY (flight_id) REFERENCES Flight(flight_id)
);

CREATE TABLE Waiting_List (
    waitlist_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    flight_id INT,
    request_time DATETIME,
    notified TINYINT(1) DEFAULT 0,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (flight_id) REFERENCES Flight(flight_id)
);

CREATE TABLE Question (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    employee_id INT,
    question_text TEXT NOT NULL,
    reply_text TEXT,
    question_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    reply_time DATETIME,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);

INSERT INTO Airline (airline_id, name) VALUES
('AA', 'American Airlines'),
('UA', 'United Airlines'),
('DL', 'Delta Airlines');

INSERT INTO Airport (airport_id, name, city) VALUES
('JFK', 'John F Kennedy International Airport', 'New York'),
('EWR', 'Newark Liberty International Airport', 'Newark'),
('LAX', 'Los Angeles International Airport', 'Los Angeles'),
('ORD', 'O Hare International Airport', 'Chicago'),
('ATL', 'Hartsfield Jackson Atlanta International Airport', 'Atlanta');

INSERT INTO Aircraft (airline_id, model, total_seats) VALUES
('AA', 'Boeing 737', 180),
('AA', 'Airbus A321', 190),
('UA', 'Airbus A320', 160),
('DL', 'Boeing 757', 200);

INSERT INTO Customer (first_name, last_name, email, password) VALUES
('Sara', 'Shareef', 'sara@gmail.com', 'pass123'),
('Amogh', 'Daksh', 'amogh@gmail.com', 'pass123'),
('Zara', 'Merchant', 'zara@gmail.com', 'pass123'),
('Sher', 'Weng', 'sher@gmail.com', 'pass123'),
('Bianca', 'Vargas', 'bianca@gmail.com', 'pass123');

INSERT INTO Employee (first_name, last_name, email, role, password) VALUES
('Suhani', 'Admin', 'admin@travel.com', 'admin', 'admin123'),
('Lisa', 'Rep', 'rep1@travel.com', 'customer_rep', 'rep123'),
('Chris', 'Rep', 'rep2@travel.com', 'customer_rep', 'rep123');

INSERT INTO Flight
(airline_id, aircraft_id, departure_airport, arrival_airport, departure_time, arrival_time, base_price, available_seats, number_of_stops)
VALUES
('AA', 1, 'EWR', 'JFK', '2026-05-01 10:00:00', '2026-05-01 11:00:00', 120.00, 20, 0),
('UA', 3, 'JFK', 'EWR', '2026-05-02 14:00:00', '2026-05-02 15:00:00', 100.00, 20, 0),
('AA', 1, 'EWR', 'JFK', '2026-05-03 09:00:00', '2026-05-03 10:00:00', 130.00, 0, 0),
('UA', 3, 'EWR', 'JFK', '2026-05-04 16:30:00', '2026-05-04 17:30:00', 125.00, 15, 0),
('AA', 2, 'JFK', 'LAX', '2026-05-10 08:00:00', '2026-05-10 11:30:00', 320.00, 25, 0),
('UA', 3, 'EWR', 'ORD', '2026-05-11 09:00:00', '2026-05-11 10:45:00', 180.00, 10, 0),
('DL', 4, 'ATL', 'JFK', '2026-05-12 14:00:00', '2026-05-12 16:00:00', 210.00, 0, 1),
('AA', 2, 'LAX', 'ORD', '2026-05-13 12:00:00', '2026-05-13 17:00:00', 280.00, 50, 0),
('UA', 3, 'ORD', 'ATL', '2026-05-14 18:00:00', '2026-05-14 20:15:00', 175.00, 5, 0),
('DL', 4, 'JFK', 'EWR', '2026-05-15 07:00:00', '2026-05-15 08:00:00', 95.00, 40, 0),
('UA', 3, 'LAX', 'JFK', '2026-05-17 10:00:00', '2026-05-17 18:00:00', 340.00, 30, 0),
('DL', 4, 'LAX', 'JFK', '2026-05-18 13:00:00', '2026-05-18 21:30:00', 300.00, 25, 1);

INSERT INTO Ticket (customer_id, total_fare, booking_date) VALUES
(1, 320.00, NOW()),
(2, 270.00, NOW()),
(3, 210.00, NOW()),
(1, 560.00, NOW()),
(4, 262.50, NOW()),
(5, 95.00, NOW());

INSERT INTO Ticket_Flight
(ticket_id, flight_id, ticket_type, ticket_status, price)
VALUES
(1, 5, 'economy', 'confirmed', 320.00),
(2, 6, 'business', 'confirmed', 270.00),
(3, 7, 'economy', 'waitlisted', 210.00),
(4, 8, 'first', 'confirmed', 560.00),
(5, 9, 'business', 'confirmed', 262.50),
(6, 10, 'economy', 'confirmed', 95.00);

INSERT INTO Waiting_List (customer_id, flight_id, request_time, notified) VALUES
(3, 7, NOW(), 0),
(2, 3, NOW(), 0);

INSERT INTO Question
(customer_id, employee_id, question_text, reply_text, reply_time)
VALUES
(1, 2, 'Can I change my flight date?', 'Yes, if seats are available.', NOW()),
(2, NULL, 'Do business class tickets get refunds?', NULL, NULL);