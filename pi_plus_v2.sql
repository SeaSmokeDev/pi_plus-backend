CREATE TABLE IF NOT EXISTS `users` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`first_name` VARCHAR(255),
	`last_name` VARCHAR(255),
	`username` VARCHAR(255),
	`password` VARCHAR(255),
	`role` ENUM('Mozo de almacen', 'Tecnico', 'logisticca', 'Admin'),
	`workplace` VARCHAR(255),
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `payment_terminals` (
	`id` INTEGER NOT NULL UNIQUE,
	`model` VARCHAR(255) NOT NULL,
	`brand` VARCHAR(255) NOT NULL,
	`status` ENUM('disponible', 'enTransito', 'noDisonible') NOT NULL COMMENT 'IAN revisa esto, lo he puesto tipo ENUM para limitar los estados, cambiados conforme veas mejor, pero igual es buena idea así luego se puede filtra por unos estados en concreto
',
	`notes` VARCHAR(255),
	`entry_date` DATE NOT NULL,
	`created_at` DATE NOT NULL,
	`box_id` INTEGER DEFAULT NULL,
	`warehouse_slot_id` INTEGER DEFAULT NULL,
	PRIMARY KEY(`id`)
);


CREATE INDEX `Datafotono_index_0`
ON `payment_terminals` ();
CREATE TABLE IF NOT EXISTS `expeditions` (
	`id` INTEGER NOT NULL UNIQUE,
	`created_at` DATETIME NOT NULL,
	`received_at` DATETIME NOT NULL,
	`modified_at` DATETIME,
	`destination_address` VARCHAR(255) NOT NULL,
	`user_id` INTEGER NOT NULL,
	`packages` INTEGER,
	`weight` INTEGER,
	`notes` VARCHAR(255),
	`box_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `pallets` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`description` VARCHAR(255) NOT NULL,
	`material` ENUM('plastico', 'madera') NOT NULL,
	`type` ENUM('americano', 'europeo') NOT NULL,
	`max_boxes_capacity` INTEGER NOT NULL,
	`warehouse_slot_id` INTEGER,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `shelves` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`code` CHAR(1) NOT NULL COMMENT 'Tipo de descripción: A',
	`levels` ENUM('1', '2', '3', '4') NOT NULL UNIQUE,
	`level_capacity` INTEGER NOT NULL DEFAULT 8 COMMENT 'numero maximo de pales? o de cajas?',
	`aisle_id` INTEGER NOT NULL,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `aisles` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`aisle_number` INTEGER NOT NULL UNIQUE,
	PRIMARY KEY(`id`)
);


CREATE TABLE IF NOT EXISTS `boxes` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`label` VARCHAR(255) NOT NULL UNIQUE COMMENT 'será una referencia en función de la ubicación en el almacén y el contenido?? (REVISAR CON IAN)',
	`pallet_id` INTEGER,
	PRIMARY KEY(`id`, `label`)
);


CREATE TABLE IF NOT EXISTS `warehouse_slots` (
	`id` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`reference` VARCHAR(255) NOT NULL COMMENT 'pasillo-estantería-nivel
1A3',
	`aisle_id` INTEGER NOT NULL,
	`shelf_id` INTEGER NOT NULL UNIQUE,
	`level` ENUM('1', '2', '3', '4') NOT NULL COMMENT 'numero maximo de pales? o de cajas?',
	PRIMARY KEY(`id`)
);


