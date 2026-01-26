DROP DATABASE IF EXISTS pi_plus;
CREATE DATABASE pi_plus CHARACTER SET 'utf8mb4';
USE pi_plus;

-- 1) USERS
CREATE TABLE IF NOT EXISTS users (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  username VARCHAR(255),
  password_hash VARCHAR(255),
  role ENUM('warehouse_worker', 'technician', 'logistics', 'admin'),
  workplace VARCHAR(255),
  PRIMARY KEY (id)
);

-- 2) AISLES
CREATE TABLE IF NOT EXISTS aisles (
  id INT NOT NULL AUTO_INCREMENT,
  aisle_number INT NOT NULL UNIQUE,
  PRIMARY KEY (id)
);

-- 3) SHELVES
CREATE TABLE IF NOT EXISTS shelves (
  id INT NOT NULL AUTO_INCREMENT,
  code CHAR(1) NOT NULL COMMENT 'Example: A',
  max_levels TINYINT NOT NULL,
  level_capacity INT NOT NULL DEFAULT 8 COMMENT 'Define if this is max pallets or max boxes per level',
  aisle_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_shelves_aisles
    FOREIGN KEY (aisle_id) REFERENCES aisles(id)
);

-- 4) WAREHOUSE SLOTS
CREATE TABLE IF NOT EXISTS warehouse_slots (
  id INT NOT NULL AUTO_INCREMENT,
  reference VARCHAR(255) NOT NULL COMMENT 'Example: 1A3 (aisle-shelf-level)',
  shelf_id INT NOT NULL,
  level ENUM('1','2','3','4') NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_slots_shelves
    FOREIGN KEY (shelf_id) REFERENCES shelves(id),
  CONSTRAINT uq_slot_shelf_level
    UNIQUE (shelf_id, level)
);

-- 5) PALLETS
CREATE TABLE IF NOT EXISTS pallets (
  id INT NOT NULL AUTO_INCREMENT,
  description VARCHAR(255) NOT NULL,
  material ENUM('plastic', 'wood') NOT NULL,
  type ENUM('american', 'european') NOT NULL,
  max_boxes_capacity INT NOT NULL,
  warehouse_slot_id INT DEFAULT NULL,
  brand_code VARCHAR(255),
  PRIMARY KEY (id),
  CONSTRAINT fk_pallets_slots
    FOREIGN KEY (warehouse_slot_id) REFERENCES warehouse_slots(id)
);

-- 6) BOXES
CREATE TABLE IF NOT EXISTS boxes (
  id INT NOT NULL AUTO_INCREMENT,
  label VARCHAR(255) NOT NULL UNIQUE COMMENT 'Location/reference label',
  product_model VARCHAR(255),
  pallet_id INT DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_boxes_pallets
    FOREIGN KEY (pallet_id) REFERENCES pallets(id)
);

-- 7) PAYMENT TERMINALS
CREATE TABLE IF NOT EXISTS payment_terminals (
  id INT NOT NULL AUTO_INCREMENT,
  serial_number VARCHAR(255) NOT NULL UNIQUE,
  model VARCHAR(255) NOT NULL,
  brand VARCHAR(255) NOT NULL,
  status ENUM('in_transit', 'pending_review', 'operational', 'pending_lab', 'level_1') NOT NULL,
  notes VARCHAR(255),
  entry_date DATE NOT NULL,
  created_at DATE NOT NULL,
  box_id INT DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_terminals_boxes
    FOREIGN KEY (box_id) REFERENCES boxes(id)
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
  `box_id` int(11) DEFAULT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_expeditions_users
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 9) EXPEDITION TERMINALS (bridge table)
CREATE TABLE IF NOT EXISTS expedition_terminals (
  id INT NOT NULL AUTO_INCREMENT,
  expedition_id INT NOT NULL,
  terminal_id INT NOT NULL,
  added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_et_expeditions
    FOREIGN KEY (expedition_id) REFERENCES expeditions(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_et_terminals
    FOREIGN KEY (terminal_id) REFERENCES payment_terminals(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT uq_et_expedition_terminal
    UNIQUE (expedition_id, terminal_id)
);
