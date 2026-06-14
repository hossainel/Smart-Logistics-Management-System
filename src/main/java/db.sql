-- 0. Create the database if it doesn't exist yet
CREATE DATABASE IF NOT EXISTS logistics;
USE logistics;

-- 1. Customers Table
CREATE TABLE IF NOT EXISTS customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    address TEXT NOT NULL
);

-- 2. Drivers Table
CREATE TABLE IF NOT EXISTS drivers (
    driver_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL
);

-- 3. Vehicles Table
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    plate_no VARCHAR(20) NOT NULL UNIQUE
);

-- 4. Warehouses Table
-- Note: Fixed to snake_case but handles warehouse_name and location fields
CREATE TABLE IF NOT EXISTS warehouses (
    warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
    warehouse_name VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- 5. Parcels Table
-- Synced with ParcelManager's tracking_id, sender, receiver, and status fields
CREATE TABLE IF NOT EXISTS parcels (
    tracking_id VARCHAR(15) PRIMARY KEY, -- Holds formats like 'TRK-B28C1A9F'
    sender VARCHAR(100) NOT NULL,
    receiver VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'Manifest Created'
);

CREATE TABLE IF NOT EXISTS locations (
    location_id INT AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(100) NOT NULL, -- e.g., "Dhaka Central Hub", "Chittagong Sorting Facility"
    city VARCHAR(50) NOT NULL,
    state_region VARCHAR(50),
    type ENUM('Hub', 'Warehouse', 'Sorting Center', 'Drop-off Point') NOT NULL
);
CREATE TABLE IF NOT EXISTS transit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    tracking_id VARCHAR(15) NOT NULL,
    current_location_id INT,
    status VARCHAR(50) NOT NULL, -- e.g., "Arrived at Hub", "In Transit"
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT, -- e.g., "Delayed due to weather"
    FOREIGN KEY (tracking_id) REFERENCES parcels(tracking_id) ON DELETE CASCADE,
    FOREIGN KEY (current_location_id) REFERENCES locations(location_id) ON UPDATE CASCADE
);
CREATE TABLE IF NOT EXISTS fleet_assignments (
    assignment_id INT AUTO_INCREMENT PRIMARY KEY,
    driver_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    assigned_date DATE NOT NULL,
    status ENUM('Active', 'Completed', 'Maintenance_Issue') DEFAULT 'Active',
    FOREIGN KEY (driver_id) REFERENCES drivers(driver_id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

CREATE TABLE IF NOT EXISTS dispatches (
    dispatch_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT NOT NULL,
    driver_id INT NOT NULL,
    origin_location_id INT NOT NULL,
    destination_location_id INT NOT NULL,
    departure_time DATETIME,
    estimated_arrival DATETIME,
    actual_arrival DATETIME,
    dispatch_status ENUM('Scheduled', 'En Route', 'Delayed', 'Delivered') DEFAULT 'Scheduled',
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    FOREIGN KEY (driver_id) REFERENCES drivers(driver_id),
    FOREIGN KEY (origin_location_id) REFERENCES locations(location_id),
    FOREIGN KEY (destination_location_id) REFERENCES locations(location_id)
);

CREATE TABLE IF NOT EXISTS shipment_billings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tracking_id VARCHAR(50) NOT NULL,
    customer_id INT NOT NULL,
    total_amount DOUBLE NOT NULL,
    status VARCHAR(20) DEFAULT 'Paid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Optional: If you want strict relational safety matching your other entities
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Stored securely
    role VARCHAR(30) DEFAULT 'Admin'
);

-- Insert a default administrator credential
INSERT INTO users (username, password, role)
VALUES ('admin', 'password', 'Admin')
ON DUPLICATE KEY UPDATE username=username;