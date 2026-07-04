-- Smart Grid Management System SQL Database Schema
-- Suitable for MySQL / MariaDB (XAMPP default port 3307)

CREATE DATABASE IF NOT EXISTS smart_grid;
USE smart_grid;

-- Roles table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Zones table
CREATE TABLE IF NOT EXISTS zones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    region VARCHAR(100) NOT NULL
);

-- Grid Nodes table
CREATE TABLE IF NOT EXISTS grid_nodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    capacity DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL,
    zone_id BIGINT NOT NULL,
    FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE CASCADE
);

-- Power Readings (live monitoring) table
CREATE TABLE IF NOT EXISTS power_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    voltage DOUBLE NOT NULL,
    current DOUBLE NOT NULL,
    frequency DOUBLE NOT NULL,
    power_factor DOUBLE NOT NULL,
    active_load DOUBLE NOT NULL,
    health_status VARCHAR(30) NOT NULL,
    timestamp DATETIME NOT NULL,
    grid_node_id BIGINT NOT NULL,
    FOREIGN KEY (grid_node_id) REFERENCES grid_nodes(id) ON DELETE CASCADE
);

-- Consumers table
CREATE TABLE IF NOT EXISTS consumers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contract_capacity DOUBLE NOT NULL
);

-- Meter Readings table
CREATE TABLE IF NOT EXISTS meter_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reading_date DATETIME NOT NULL,
    active_power DOUBLE NOT NULL,
    reactive_power DOUBLE NOT NULL,
    billing_amount DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL,
    consumer_id BIGINT NOT NULL,
    FOREIGN KEY (consumer_id) REFERENCES consumers(id) ON DELETE CASCADE
);

-- Faults table
CREATE TABLE IF NOT EXISTS faults (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    severity VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    reported_at DATETIME NOT NULL,
    resolved_at DATETIME,
    grid_node_id BIGINT NOT NULL,
    FOREIGN KEY (grid_node_id) REFERENCES grid_nodes(id) ON DELETE CASCADE
);

-- Alerts table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    message VARCHAR(255) NOT NULL,
    severity VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL
);

-- Outages table
CREATE TABLE IF NOT EXISTS outages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    grid_node_id BIGINT NOT NULL,
    FOREIGN KEY (grid_node_id) REFERENCES grid_nodes(id) ON DELETE CASCADE
);

-- Reports table
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    generated_at DATETIME NOT NULL,
    content TEXT NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
