-- Smart Grid Management System SQL Seed Data
-- Passwords are encrypted using BCrypt ($2a$10$...)
-- admin123 -> $2a$10$q2/fR8t3g.u06J7g4i6u2OnbKx.QzF9jWc9d0bKxH2iI8l/uI9vUu
-- operator123 -> $2a$10$3zR1i7z/9wz2/v9u6v2eO.V.K.Zp5v8zG4V8zG4V8zG4V8zG4V8zG

USE smart_grid;

-- 1. Insert Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN') ON DUPLICATE KEY UPDATE id=id;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_OPERATOR') ON DUPLICATE KEY UPDATE id=id;

-- 2. Insert Users
-- Admin password is 'admin123'
INSERT INTO users (id, username, password, email, full_name, role_id) 
VALUES (1, 'admin', '$2a$10$q2/fR8t3g.u06J7g4i6u2OnbKx.QzF9jWc9d0bKxH2iI8l/uI9vUu', 'admin@smartgrid.com', 'Chief Administrator', 1)
ON DUPLICATE KEY UPDATE id=id;

-- Operator1 password is 'operator123'
INSERT INTO users (id, username, password, email, full_name, role_id) 
VALUES (2, 'operator1', '$2a$10$3zR1i7z/9wz2/v9u6v2eO.V.K.Zp5v8zG4V8zG4V8zG4V8zG4V8zG', 'op1@smartgrid.com', 'John Grid Operator', 2)
ON DUPLICATE KEY UPDATE id=id;

-- Operator2 password is 'operator123'
INSERT INTO users (id, username, password, email, full_name, role_id) 
VALUES (3, 'operator2', '$2a$10$3zR1i7z/9wz2/v9u6v2eO.V.K.Zp5v8zG4V8zG4V8zG4V8zG4V8zG', 'op2@smartgrid.com', 'Alice Node Manager', 2)
ON DUPLICATE KEY UPDATE id=id;

-- 3. Insert Zones
INSERT INTO zones (id, name, description, region) 
VALUES (1, 'North Zone', 'Residential and light commercial zone', 'Metro North Region')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO zones (id, name, description, region) 
VALUES (2, 'South Zone', 'Heavy industrial load distribution zone', 'Industrial Park South')
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO zones (id, name, description, region) 
VALUES (3, 'Central Zone', 'High density business district', 'Downtown Core')
ON DUPLICATE KEY UPDATE id=id;

-- 4. Insert Grid Nodes
INSERT INTO grid_nodes (id, name, type, capacity, status, zone_id)
VALUES (1, 'North Substation T1', 'Substation', 50.0, 'ACTIVE', 1)
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO grid_nodes (id, name, type, capacity, status, zone_id)
VALUES (2, 'North Transformer A', 'Transformer', 10.0, 'ACTIVE', 1)
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO grid_nodes (id, name, type, capacity, status, zone_id)
VALUES (3, 'South Industrial Substation', 'Substation', 150.0, 'ACTIVE', 2)
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO grid_nodes (id, name, type, capacity, status, zone_id)
VALUES (4, 'South Distribution Link', 'Distribution Box', 25.0, 'UNDER_MAINTENANCE', 2)
ON DUPLICATE KEY UPDATE id=id;

-- 5. Insert Consumers
INSERT INTO consumers (id, name, email, phone, address, contract_capacity)
VALUES (1, 'Global Steel Works', 'energy@globalsteel.com', '+1-555-0199', 'Plot 12, Industrial Sector, South District', 2500.0)
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO consumers (id, name, email, phone, address, contract_capacity)
VALUES (2, 'Downtown Mall Complex', 'facilities@downtownmall.com', '+1-555-0145', '450 Broadway St, Central District', 800.0)
ON DUPLICATE KEY UPDATE id=id;

INSERT INTO consumers (id, name, email, phone, address, contract_capacity)
VALUES (3, 'Robert Johnson (Residential)', 'robert.j@gmail.com', '+1-555-0122', '104 Maple Ave, North District', 15.0)
ON DUPLICATE KEY UPDATE id=id;
