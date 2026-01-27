DROP DATABASE IF EXISTS pi_plus;
CREATE DATABASE pi_plus CHARACTER SET utf8mb4;
USE pi_plus;

-- 1) AISLES
CREATE TABLE IF NOT EXISTS aisles (
  id INT NOT NULL AUTO_INCREMENT,
  aisle_number INT NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

-- 2) SHELVES
CREATE TABLE IF NOT EXISTS shelves (
  id INT NOT NULL AUTO_INCREMENT,
  code CHAR(1) NOT NULL COMMENT 'Example: A',
  max_levels TINYINT NOT NULL,
  level_capacity INT NOT NULL DEFAULT 8 COMMENT 'Max pallets or boxes per level',
  aisle_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_shelves_aisles
    FOREIGN KEY (aisle_id) REFERENCES aisles(id)
);

-- 3) WAREHOUSE LOCATIONS
CREATE TABLE IF NOT EXISTS warehouse_locations (
  id INT NOT NULL AUTO_INCREMENT,
  reference VARCHAR(255) NOT NULL COMMENT 'Example: 1A3 (aisle-shelf-level)',
  shelf_id INT NOT NULL,
  level ENUM('1','2','3','4') NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_locations_shelves
    FOREIGN KEY (shelf_id) REFERENCES shelves(id),
  CONSTRAINT uq_location_shelf_level
    UNIQUE (shelf_id, level)
);

-- 4) PALLETS
CREATE TABLE IF NOT EXISTS pallets (
  id INT NOT NULL AUTO_INCREMENT,
  description VARCHAR(255) NOT NULL,
  material ENUM('plastic', 'wood') NOT NULL,
  type ENUM('american', 'european') NOT NULL,
  max_boxes_capacity INT NOT NULL,
  warehouse_location_id INT DEFAULT NULL,
  brand_code VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT fk_pallets_locations
    FOREIGN KEY (warehouse_location_id) REFERENCES warehouse_locations(id)
);

-- 5) BOXES
CREATE TABLE IF NOT EXISTS boxes (
  id INT NOT NULL AUTO_INCREMENT,
  label VARCHAR(255) NOT NULL UNIQUE COMMENT 'Location/reference label',
  product_model VARCHAR(255),
  pallet_id INT DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_boxes_pallets
    FOREIGN KEY (pallet_id) REFERENCES pallets(id)
);

-- 6) PAYMENT TERMINALS
CREATE TABLE IF NOT EXISTS payment_terminals (
  id INT NOT NULL AUTO_INCREMENT,
  serial_number VARCHAR(255) NOT NULL UNIQUE,
  model VARCHAR(255) NOT NULL,
  brand VARCHAR(255) NOT NULL,
  status ENUM(
    'in_transit',
    'pending_review',
    'operational',
    'pending_laboratory',
    'level_1'
  ) NOT NULL,
  notes VARCHAR(255),
  entry_date DATE NOT NULL,
  created_at DATE NOT NULL,
  box_id INT DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_terminals_boxes
    FOREIGN KEY (box_id) REFERENCES boxes(id)
);

-- 7) USERS
CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  username VARCHAR(255),
  password_hash VARCHAR(255),
  role ENUM(
    'warehouse_worker',
    'technician',
    'logistics',
    'administrator'
  ),
  workplace VARCHAR(255),
  PRIMARY KEY (id)
);

-- 8) EXPEDITIONS
CREATE TABLE IF NOT EXISTS expeditions (
  id INT NOT NULL AUTO_INCREMENT,
  created_at DATETIME NOT NULL,
  received_at DATETIME NULL,
  modified_at DATETIME NULL,
  destination_address VARCHAR(255) NOT NULL,
  packages INT,
  weight INT,
  notes VARCHAR(255),
  box_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_expeditions_users
    FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_expeditions_boxes
    FOREIGN KEY (box_id) REFERENCES boxes(id)
);
