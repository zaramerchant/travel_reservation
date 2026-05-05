DROP DATABASE IF EXISTS travel_reservation;
CREATE DATABASE travel_reservation;
USE travel_reservation;

CREATE TABLE Airline (
    airline_id VARCHAR(2) PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE Airport (
    airport_id VARCHAR(3) PRIMARY KEY,
    name VARCHAR(100),
    city VARCHAR(100)
);

CREATE TABLE Aircraft (
    aircraft_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(2),
    model VARCHAR(100),
    total_seats INT,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id)
);

CREATE TABLE Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE Employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100),
    role VARCHAR(50)
);

CREATE TABLE Flight (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id VARCHAR(2),
    departure_airport VARCHAR(3),
    arrival_airport VARCHAR(3),
    departure_time DATETIME,
    arrival_time DATETIME,
    FOREIGN KEY (airline_id) REFERENCES Airline(airline_id),
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