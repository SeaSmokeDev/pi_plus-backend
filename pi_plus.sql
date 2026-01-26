-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-01-2026 a las 22:38:25
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `pi_plus`
--

DROP DATABASE IF EXISTS pi_plus;
CREATE DATABASE pi_plus CHARACTER SET 'utf8mb4';
USE pi_plus;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `aisles`
--

CREATE TABLE `aisles` (
  `id` int(11) NOT NULL,
  `aisle_number` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `boxes`
--

CREATE TABLE `boxes` (
  `id` int(11) NOT NULL,
  `label` varchar(255) NOT NULL COMMENT 'será una referencia en función de la ubicación en el almacén y el contenido?? (REVISAR CON IAN)',
  `product_model` varchar(255) DEFAULT NULL,
  `pallet_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `expeditions`
--

CREATE TABLE `expeditions` (
  `id` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `received_at` datetime NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `destination_address` varchar(255) NOT NULL,
  `packages` int(11) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `box_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `expedition_terminals`
--

CREATE TABLE `expedition_terminals` (
  `id` int(11) NOT NULL,
  `expedition_id` int(11) NOT NULL,
  `terminal_id` int(11) NOT NULL,
  `added_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pallets`
--

CREATE TABLE `pallets` (
  `id` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `material` enum('plastic','wood') NOT NULL,
  `type` enum('american','european') NOT NULL,
  `max_boxes_capacity` int(11) NOT NULL,
  `warehouse_slot_id` int(11) DEFAULT NULL,
  `brand_code` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `payment_terminals`
--

CREATE TABLE `payment_terminals` (
  `id` int(11) NOT NULL,
  `serial_number` varchar(255) NOT NULL,
  `model` varchar(255) NOT NULL,
  `brand` varchar(255) NOT NULL,
  `status` enum('in_transit','pending_review','operational','pending_lab','level_1') NOT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `entry_date` date NOT NULL,
  `created_at` date NOT NULL,
  `box_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `shelves`
--

CREATE TABLE `shelves` (
  `id` int(11) NOT NULL,
  `code` char(1) NOT NULL COMMENT 'Tipo de descripción: A',
  `max_levels` tinyint(4) NOT NULL,
  `level_capacity` int(11) NOT NULL DEFAULT 8 COMMENT 'numero maximo de pales? o de cajas?',
  `aisle_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `role` enum('warehouse_worker','technician','logistics','admin') DEFAULT NULL,
  `workplace` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `warehouse_slots`
--

CREATE TABLE `warehouse_slots` (
  `id` int(11) NOT NULL,
  `reference` varchar(255) NOT NULL COMMENT 'pasillo-estantería-nivel\r\n1A3',
  `shelf_id` int(11) NOT NULL,
  `level` enum('1','2','3','4') NOT NULL COMMENT 'numero maximo de pales? o de cajas?'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `aisles`
--
ALTER TABLE `aisles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `aisle_number` (`aisle_number`);

--
-- Indices de la tabla `boxes`
--
ALTER TABLE `boxes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `label` (`label`),
  ADD KEY `fk_boxes_pallets` (`pallet_id`);

--
-- Indices de la tabla `expeditions`
--
ALTER TABLE `expeditions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `fk_expeditions_users` (`user_id`);

--
-- Indices de la tabla `expedition_terminals`
--
ALTER TABLE `expedition_terminals`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_et_expedition_terminal` (`expedition_id`,`terminal_id`),
  ADD KEY `fk_et_terminals` (`terminal_id`);

--
-- Indices de la tabla `pallets`
--
ALTER TABLE `pallets`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `fk_pallets_slots` (`warehouse_slot_id`);

--
-- Indices de la tabla `payment_terminals`
--
ALTER TABLE `payment_terminals`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `serial_number` (`serial_number`),
  ADD KEY `fk_terminals_boxes` (`box_id`);

--
-- Indices de la tabla `shelves`
--
ALTER TABLE `shelves`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `fk_shelves_aisles` (`aisle_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indices de la tabla `warehouse_slots`
--
ALTER TABLE `warehouse_slots`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `uq_slot_shelf_level` (`shelf_id`,`level`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `aisles`
--
ALTER TABLE `aisles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `boxes`
--
ALTER TABLE `boxes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `expeditions`
--
ALTER TABLE `expeditions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `expedition_terminals`
--
ALTER TABLE `expedition_terminals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pallets`
--
ALTER TABLE `pallets`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `payment_terminals`
--
ALTER TABLE `payment_terminals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `shelves`
--
ALTER TABLE `shelves`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `warehouse_slots`
--
ALTER TABLE `warehouse_slots`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `boxes`
--
ALTER TABLE `boxes`
  ADD CONSTRAINT `fk_boxes_pallets` FOREIGN KEY (`pallet_id`) REFERENCES `pallets` (`id`);

--
-- Filtros para la tabla `expeditions`
--
ALTER TABLE `expeditions`
  ADD CONSTRAINT `fk_expeditions_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `expedition_terminals`
--
ALTER TABLE `expedition_terminals`
  ADD CONSTRAINT `fk_et_expeditions` FOREIGN KEY (`expedition_id`) REFERENCES `expeditions` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_et_terminals` FOREIGN KEY (`terminal_id`) REFERENCES `payment_terminals` (`id`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `pallets`
--
ALTER TABLE `pallets`
  ADD CONSTRAINT `fk_pallets_slots` FOREIGN KEY (`warehouse_slot_id`) REFERENCES `warehouse_slots` (`id`);

--
-- Filtros para la tabla `payment_terminals`
--
ALTER TABLE `payment_terminals`
  ADD CONSTRAINT `fk_terminals_boxes` FOREIGN KEY (`box_id`) REFERENCES `boxes` (`id`);

--
-- Filtros para la tabla `shelves`
--
ALTER TABLE `shelves`
  ADD CONSTRAINT `fk_shelves_aisles` FOREIGN KEY (`aisle_id`) REFERENCES `aisles` (`id`);

--
-- Filtros para la tabla `warehouse_slots`
--
ALTER TABLE `warehouse_slots`
  ADD CONSTRAINT `fk_slots_shelves` FOREIGN KEY (`shelf_id`) REFERENCES `shelves` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
