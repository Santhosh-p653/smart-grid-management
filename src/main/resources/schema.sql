-- MySQL schema for Smart Grid Management System
-- Assuming the database already exists or will be selected
USE smart_grid;

-- 1. Roles Table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 2. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3. Zones Table
CREATE TABLE IF NOT EXISTS zones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    region VARCHAR(100),
    description VARCHAR(255)
) ENGINE=InnoDB;

-- 4. Grid Nodes Table
CREATE TABLE IF NOT EXISTS grid_nodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    capacity DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL,
    zone_id BIGINT NOT NULL,
    CONSTRAINT fk_node_zone FOREIGN KEY (zone_id) REFERENCES zones(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 5. Power Readings Table
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
    CONSTRAINT fk_power_grid_node FOREIGN KEY (grid_node_id) REFERENCES grid_nodes(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Alerts Table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    message VARCHAR(255) NOT NULL,
    severity VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL
) ENGINE=InnoDB;

-- 7. Faults Table
CREATE TABLE IF NOT EXISTS faults (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grid_node_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    severity VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    reported_at DATETIME NOT NULL,
    resolved_at DATETIME,
    CONSTRAINT fk_fault_node FOREIGN KEY (grid_node_id) REFERENCES grid_nodes(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 8. Outages Table
CREATE TABLE IF NOT EXISTS outages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grid_node_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME,
    status VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT fk_outage_node FOREIGN KEY (grid_node_id) REFERENCES grid_nodes(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 9. Consumers Table
CREATE TABLE IF NOT EXISTS consumers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255) NOT NULL,
    contract_capacity DOUBLE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 10. Meter Readings Table
CREATE TABLE IF NOT EXISTS meter_readings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reading_date DATETIME NOT NULL,
    active_power DOUBLE NOT NULL,
    reactive_power DOUBLE NOT NULL,
    billing_amount DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL,
    consumer_id BIGINT NOT NULL,
    CONSTRAINT fk_meter_consumer FOREIGN KEY (consumer_id) REFERENCES consumers(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 11. Reports Table
CREATE TABLE IF NOT EXISTS reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    generated_at DATETIME NOT NULL,
    user_id BIGINT,
    content TEXT NOT NULL,
    CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- Insert default roles (run once)
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_OPERATOR');
