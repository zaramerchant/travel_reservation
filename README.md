# Travel Reservation System

## Setup Guide

This project uses Java, MySQL, JDBC, and MySQL Workbench.

---

# 1. Open MySQL Workbench

Open MySQL Workbench and connect to:

```text
Local instance 3306
```

---

# 2. Create the Database

Run:

```sql
DROP DATABASE IF EXISTS travel_reservation;
CREATE DATABASE travel_reservation;
USE travel_reservation;
```

---

# 3. Load schema.sql

In MySQL Workbench:

```text
File → Open SQL Script
```

Open:

```text
travel_reservation/sql/schema.sql
```

Then click the lightning bolt (⚡) to run the file.

This creates all tables and inserts the sample data.

---

# 4. Refresh the Database

1. Click the “Schemas” tab
2. Right click “Schemas”
3. Click “Refresh All”

You should now see:

```text
travel_reservation
```

with tables including:

```text
Customer
Employee
Flight
Ticket
Ticket_Flight
Waiting_List
Question
Airport
Aircraft
Airline
```

---

# 5. Configure DBConnection.java

Open:

```text
src/db/DBConnection.java
```

Make sure it looks like:

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/travel_reservation";

private static final String USER = "root";

private static final String PASSWORD = "YOUR_PASSWORD";
```

Replace:

```java
"YOUR_PASSWORD"
```

with your actual MySQL password.

Example:

```java
private static final String PASSWORD = "pass123";
```

If the password is wrong, the application will not connect to MySQL.

---

# 6. Run the Application

Run:

```text
Main.java
```

or:

```bash
javac Main.java
java Main
```

---

# Sample Login Credentials

## Customer

```text
Email: sara@gmail.com
Password: pass123
```

## Admin

```text
Email: admin@travel.com
Password: admin123
```

## Customer Representative

```text
Email: rep1@travel.com
Password: rep123
```

---

# Sample Flights

| Flight ID | Route     | Departure        | Arrival          |
| --------- | --------- | ---------------- | ---------------- |
| 5         | JFK → LAX | 2026-05-10 08:00 | 2026-05-10 11:30 |
| 6         | EWR → ORD | 2026-05-11 09:00 | 2026-05-11 10:45 |
| 8         | LAX → ORD | 2026-05-13 12:00 | 2026-05-13 17:00 |

---

# Features

## Customer

* Search Flights
* Book Ticket
* View Reservations
* Cancel Reservation
* Join Waiting List
* View Past Flights
* View Upcoming Flights
* Post Questions

## Admin

* Add/Edit/Delete Customers
* Add/Edit/Delete Representatives
* Revenue Reports
* Reservations by Flight
* Reservations by Customer
* Most Active Flights
* Top Customer by Revenue

## Customer Representative

* Make Reservation for Customer

* Edit Reservation Status

* Add/Edit/Delete Flights

* Add/Edit/Delete Airports

* Add/Edit/Delete Aircraft

* View Waiting Lists

* View Questions

* Reply to Questions

* View

* Always rerun `schema.sql` if your database becomes inconsistent.

* Make sure MySQL server is running before starting the application.

* Refresh schemas after executing SQL scripts.

* The application depends entirely on the MySQL database connection.
