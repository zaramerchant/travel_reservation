CREATE DATABASE travel_reservation;

USE travel_reservation;

CREATE TABLE customer(
    customer_id INT,
    first_name VARCHAR(20),
    last_name VARCHAR(30),
    email VARCHAR(50),
    phone VARCHAR(20)
    
    PRIMARY KEY (customer_id)
);

CREATE TABLE ticket(
    ticket_num INT,
    customer_id INT NOT NULL,
    ticket_type VARCHAR(20),
    purchase_date DATE,
    ticket_status VARCHAR(20),
    CHECK (ticket_status IN ('confirmed', 'waitlisted', 'cancelled')),

    PRIMARY KEY (ticket_num),
    FOREIGN KEY (customer_id) references customer(customer_id)
);

CREATE TABLE employee(
    employee_id INT,
    first_name VARCHAR(30),
    last_name VARCHAR(30),
    position VARCHAR(30),
    airline_id CHAR(2) NOT NULL,
    
    PRIMARY KEY (employee_id),
    FOREIGN KEY (airline_id) references airline(airline_id)
);

CREATE TABLE airline(
    airline_id CHAR(2),
    airline_name VARCHAR(50),

    PRIMARY KEY (airline_id)
);

