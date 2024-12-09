# Restaurant Management System - Setup & Run TL;DR

This guide provides quick instructions to set up and run the RMS project.

---

## Prerequisites

1. **PostgreSQL**:
   - Install PostgreSQL ([Download here](https://www.postgresql.org/download/)).
   - Ensure it's accessible via terminal/command prompt.

2. **Java**:
   - Install Java JDK ([Download here](https://www.oracle.com/java/technologies/javase-jdk-downloads.html)).
   - Ensure `javac` and `java` commands work.

3. **JDBC Driver**:
   - Add the PostgreSQL JDBC driver ([Download here](https://jdbc.postgresql.org/)).

4. **IDE** (Optional):
   - Use IntelliJ, Eclipse, or VSCode for easier development.

---

## Steps to Setup

### 1. Import the Database

1. Navigate to the `dev` folder:
   ```bash
   cd dev

	2.	Restore the database:

psql -U your_username -d postgres < restaurantDb.sql


	3.	Verify the database:

psql -U your_username -d restaurantDb
\dt

2. Run the Application

	1.	Compile the project:

javac -cp .:lib/postgresql-<version>.jar ServiceClients/*.java *.java


	2.	Run the main application:

java -cp .:lib/postgresql-<version>.jar Main


	3.	Replace <version> with the version of the JDBC driver in lib.

Features

Employee Management

	•	Add, edit, delete employees.
	•	Manage schedules for employees.

Orders Management

	•	Add, update, or delete items for table orders.
	•	Calculate totals and generate receipts.

Reservations Management

	•	Create, edit, or cancel reservations.
	•	Prevent overlapping bookings.

Menu

	•	View and print allergen-specific or full menus.

Notes

	•	Default database connection:
	•	Host: localhost
	•	Port: 5432
	•	Database: restaurantDb
	•	User: SuperAdmin
	•	Password: Admin123@

clear && javac -cp .:lib/postgresql-42.5.6.jar $(find . -name "*.java") && java -cp .:lib/postgresql-42.5.6.jar Main

