-- ============================================================
-- DATA MYSQL - PI PLUS
-- Datos de prueba actualizados para expediciones agrupadas
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- Limpieza de datos en orden seguro
DELETE FROM terminales_pago;
DELETE FROM expediciones;
DELETE FROM cajas;
DELETE FROM palets;
DELETE FROM ubicaciones_almacen;
DELETE FROM estanterias;
DELETE FROM pasillos;
DELETE FROM users_security;
DELETE FROM usuarios;

ALTER TABLE terminales_pago AUTO_INCREMENT = 1;
ALTER TABLE expediciones AUTO_INCREMENT = 1;
ALTER TABLE cajas AUTO_INCREMENT = 1;
ALTER TABLE palets AUTO_INCREMENT = 1;
ALTER TABLE ubicaciones_almacen AUTO_INCREMENT = 1;
ALTER TABLE estanterias AUTO_INCREMENT = 1;
ALTER TABLE pasillos AUTO_INCREMENT = 1;
ALTER TABLE users_security AUTO_INCREMENT = 1;
ALTER TABLE usuarios AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuarios (id, nombre, apellido, rol, lugar_trabajo) VALUES
(1, 'Carlos', 'Martínez', 'administrador', 'Central'),
(2, 'Lucía', 'Gómez', 'logistica', 'Almacén A'),
(3, 'David', 'Ruiz', 'trabajador_almacen', 'Almacén B'),
(4, 'Ana', 'López', 'tecnico', 'Soporte Técnico');

ALTER TABLE usuarios AUTO_INCREMENT = 5;

-- =========================
-- USERS SECURITY
-- =========================
INSERT INTO users_security (id, username, email, password, rol, activado, usuario_id) VALUES
(1, 'cmartinez', 'cmartinez@empresa.com', '$2a$10$ayw3FCBIkupFt5n9lrmJQe9XZMJhZiNCjaoOkXo/Ba0KZgymO01ce', 'administrador', TRUE, 1),
(2, 'lgomez',    'lgomez@empresa.com',    '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'logistica', TRUE, 2),
(3, 'druiz',     'druiz@empresa.com',     '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'trabajador_almacen', TRUE, 3),
(4, 'alopez',    'alopez@empresa.com',    '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'tecnico', TRUE, 4);

ALTER TABLE users_security AUTO_INCREMENT = 5;

-- =========================
-- PASILLOS
-- =========================
INSERT INTO pasillos (id, numero_pasillo) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

ALTER TABLE pasillos AUTO_INCREMENT = 11;

-- =========================
-- ESTANTERÍAS
-- =========================
INSERT INTO estanterias (id, codigo, niveles_maximos, capacidad_nivel, pasillo_id) VALUES
(1, 'A', 4, 8, 1),
(2, 'B', 3, 8, 1),
(3, 'C', 2, 8, 1),
(4, 'D', 4, 8, 1),
(5, 'E', 1, 8, 1),
(6, 'F', 3, 8, 1),
(7, 'G', 2, 8, 1),
(8, 'H', 4, 8, 1),
(9, 'I', 3, 8, 1),
(10, 'J', 2, 8, 1),

(11, 'A', 3, 8, 2),
(12, 'B', 4, 8, 2),
(13, 'C', 2, 8, 2),
(14, 'D', 1, 8, 2),
(15, 'E', 4, 8, 2),
(16, 'F', 3, 8, 2),
(17, 'G', 2, 8, 2),
(18, 'H', 4, 8, 2),
(19, 'I', 1, 8, 2),
(20, 'J', 3, 8, 2),

(21, 'A', 2, 8, 3),
(22, 'B', 3, 8, 3),
(23, 'C', 4, 8, 3),
(24, 'D', 2, 8, 3),
(25, 'E', 1, 8, 3),
(26, 'F', 4, 8, 3),
(27, 'G', 3, 8, 3),
(28, 'H', 2, 8, 3),
(29, 'I', 4, 8, 3),
(30, 'J', 1, 8, 3),

(31, 'A', 4, 8, 4),
(32, 'B', 2, 8, 4),
(33, 'C', 3, 8, 4),
(34, 'D', 1, 8, 4),
(35, 'E', 4, 8, 4),
(36, 'F', 2, 8, 4),
(37, 'G', 3, 8, 4),
(38, 'H', 4, 8, 4),
(39, 'I', 1, 8, 4),
(40, 'J', 2, 8, 4),

(41, 'A', 1, 8, 5),
(42, 'B', 4, 8, 5),
(43, 'C', 3, 8, 5),
(44, 'D', 2, 8, 5),
(45, 'E', 4, 8, 5),
(46, 'F', 1, 8, 5),
(47, 'G', 3, 8, 5),
(48, 'H', 2, 8, 5),
(49, 'I', 4, 8, 5),
(50, 'J', 3, 8, 5),

(51, 'A', 3, 8, 6),
(52, 'B', 2, 8, 6),
(53, 'C', 4, 8, 6),
(54, 'D', 1, 8, 6),
(55, 'E', 3, 8, 6),
(56, 'F', 4, 8, 6),
(57, 'G', 2, 8, 6),
(58, 'H', 1, 8, 6),
(59, 'I', 4, 8, 6),
(60, 'J', 3, 8, 6),

(61, 'A', 4, 8, 7),
(62, 'B', 1, 8, 7),
(63, 'C', 2, 8, 7),
(64, 'D', 3, 8, 7),
(65, 'E', 4, 8, 7),
(66, 'F', 2, 8, 7),
(67, 'G', 1, 8, 7),
(68, 'H', 3, 8, 7),
(69, 'I', 4, 8, 7),
(70, 'J', 2, 8, 7),

(71, 'A', 2, 8, 8),
(72, 'B', 4, 8, 8),
(73, 'C', 1, 8, 8),
(74, 'D', 3, 8, 8),
(75, 'E', 2, 8, 8),
(76, 'F', 4, 8, 8),
(77, 'G', 1, 8, 8),
(78, 'H', 3, 8, 8),
(79, 'I', 2, 8, 8),
(80, 'J', 4, 8, 8),

(81, 'A', 3, 8, 9),
(82, 'B', 1, 8, 9),
(83, 'C', 4, 8, 9),
(84, 'D', 2, 8, 9),
(85, 'E', 3, 8, 9),
(86, 'F', 1, 8, 9),
(87, 'G', 4, 8, 9),
(88, 'H', 2, 8, 9),
(89, 'I', 3, 8, 9),
(90, 'J', 4, 8, 9),

(91, 'A', 4, 8, 10),
(92, 'B', 3, 8, 10),
(93, 'C', 2, 8, 10),
(94, 'D', 1, 8, 10),
(95, 'E', 4, 8, 10),
(96, 'F', 3, 8, 10),
(97, 'G', 2, 8, 10),
(98, 'H', 1, 8, 10),
(99, 'I', 4, 8, 10),
(100, 'J', 3, 8, 10);

ALTER TABLE estanterias AUTO_INCREMENT = 101;

-- =========================
-- UBICACIONES EN ALMACÉN
-- =========================
-- Referencia: pasillo + estantería + nivel.
-- Las etiquetas de cajas usan el mismo criterio, pero con guiones:
-- Ejemplo: ubicación 1C1 -> caja 1-C-1-1.
INSERT INTO ubicaciones_almacen (id, referencia, estanteria_id, nivel) VALUES

(1,  '1C1', 3, 1),
(2,  '1C2', 3, 2),
(3,  '1D1', 4, 1),
(4,  '1D2', 4, 2),
(5,  '2A1', 11, 1),
(6,  '2A2', 11, 2),
(7,  '2B1', 12, 1),
(8,  '2B2', 12, 2),
(9,  '3C1', 23, 1),
(10, '3C2', 23, 2),
(11, '3C3', 23, 3),
(12, '3D1', 24, 1);


ALTER TABLE ubicaciones_almacen AUTO_INCREMENT = 13;

-- =========================
-- PALETS
-- =========================
-- Cada palé se mantiene con una única familia de producto.
-- Las cajas en tránsito/recibidas tienen palet_id NULL.
-- Las cajas de expediciones abiertas siguen físicamente en almacén, por eso mantienen palet_id.
INSERT INTO palets (id, material, tipo, capacidad_max_cajas, ubicacion_almacen_id, codigo_marca, descripcion) VALUES

(1,  'madera',   'europeo',   8, 1,  'Verifone', 'Palé Verifone V240 operativos'),
(2,  'madera',   'europeo',   8, 2,  'Verifone', 'Palé Verifone V240 pendiente revisión'),
(3,  'madera',   'europeo',   8, 3,  'Verifone', 'Palé Verifone VX680 pendiente laboratorio'),
(4,  'plastico', 'americano', 8, 5,  'Ingenico', 'Palé Ingenico Move5000 operativos'),
(5,  'plastico', 'americano', 8, 6,  'Ingenico', 'Palé Ingenico Desk3500 pendiente laboratorio'),
(6,  'madera',   'europeo',   8, 7,  'PAX',      'Palé PAX A920 operativos'),
(7,  'madera',   'europeo',   8, 8,  'PAX',      'Palé PAX A80 operativos'),
(8,  'madera',   'europeo',   8, 9,  'PAX',      'Palé PAX A920 pendiente tránsito'),
(9,  'plastico', 'americano', 8, 10, 'Ingenico', 'Palé Ingenico Move5000 pendiente tránsito');

ALTER TABLE palets AUTO_INCREMENT = 10;

-- =========================
-- CAJAS
-- =========================
-- Reglas de etiquetas:
-- - Formato: pasillo-estantería-nivel-númeroCaja.
-- - Ejemplos válidos: 1-C-1-1, 3-C-3-2.
-- - No se repiten etiquetas.
-- - El modelo_producto contiene solo el modelo/familia, sin estado.
INSERT INTO cajas (id, etiqueta, modelo_producto, max_capacity, palet_id) VALUES
-- Cajas en tránsito: fuera del almacén
(1,  '1-C-1-1', 'Verifone V240',        120, NULL),
(2,  '2-A-1-1', 'Ingenico Move5000',    100, NULL),
(3,  '2-B-1-1', 'PAX A920',              90, NULL),
(4,  '2-A-1-2', 'Ingenico Move5000',    100, NULL),

-- Cajas de expedición recibida: fuera del almacén
(5,  '1-D-1-1', 'Verifone V240',        120, NULL),
(6,  '2-A-2-1', 'Ingenico Move5000',    100, NULL),

-- Cajas de expedición abierta: siguen ubicadas en almacén, terminales pendiente_transito
(7,  '3-C-1-1', 'PAX A920',              90, 8),
(8,  '3-C-2-1', 'Ingenico Move5000',    100, 9),

-- Cajas disponibles o en otros estados dentro de almacén
(9,  '1-C-2-1', 'Verifone V240',        120, 2),
(10, '3-D-1-1', 'PAX A920',              90, 6),
(11, '1-C-1-2', 'Verifone V240',        120, 1),
(12, '1-D-1-2', 'Verifone VX680',       100, 3),
(13, '2-A-1-3', 'Ingenico Move5000',    100, 4),
(14, '2-A-2-2', 'Ingenico Desk3500',    100, 5),
(15, '2-B-1-2', 'PAX A920',              90, 6),
(16, '2-B-2-1', 'PAX A80',               90, 7);

ALTER TABLE cajas AUTO_INCREMENT = 17;

-- =========================
-- TERMINALES DE PAGO
-- =========================
-- Reglas:
-- - Se respetan números de serie, modelos y marcas del data.sql original.
-- - Todos los terminales de una misma caja mantienen modelo/marca coherente.
-- - Las cajas en tránsito tienen terminales en_transito y palet_id NULL.
-- - Las cajas de expedición abierta tienen terminales pendiente_transito y conservan palet_id.
INSERT INTO terminales_pago (id, numero_serie, modelo, marca, estado, notas, fecha_ingreso, fecha_creacion, caja_id) VALUES
-- Expediciones en tránsito
(1,  'SN10001', 'V240',     'Verifone', 'en_transito', 'Terminal incluido en expedición en tránsito', '2025-01-10 00:00:00', '2024-12-15 00:00:00', 1),
(2,  'SN10002', 'V240',     'Verifone', 'en_transito', 'Terminal incluido en expedición en tránsito', '2025-01-11 00:00:00', '2024-12-16 00:00:00', 1),
(3,  'SN10003', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición en tránsito', '2025-01-20 00:00:00', '2024-11-10 00:00:00', 2),
(4,  'SN10004', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición en tránsito', '2025-01-21 00:00:00', '2024-11-11 00:00:00', 2),
(5,  'SN10005', 'A920',     'PAX',      'en_transito', 'Terminal incluido en expedición en tránsito', '2025-02-01 00:00:00', '2025-01-25 00:00:00', 3),
(6,  'SN10006', 'A920',     'PAX',      'en_transito', 'Terminal incluido en expedición en tránsito', '2025-02-02 00:00:00', '2025-01-26 00:00:00', 3),
(7,  'SN10007', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición en tránsito', '2025-02-10 00:00:00', '2025-02-01 00:00:00', 4),
(8,  'SN10008', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición en tránsito', '2025-02-11 00:00:00', '2025-02-02 00:00:00', 4),

-- Expedición recibida
(9,  'SN10009', 'V240',     'Verifone', 'operativo', 'Terminal perteneciente a expedición recibida', '2025-02-15 00:00:00', '2025-02-10 00:00:00', 5),
(10, 'SN10010', 'V240',     'Verifone', 'operativo', 'Terminal perteneciente a expedición recibida', '2025-02-16 00:00:00', '2025-02-11 00:00:00', 5),
(11, 'SN10011', 'Move5000', 'Ingenico', 'operativo', 'Terminal perteneciente a expedición recibida', '2025-02-17 00:00:00', '2025-02-12 00:00:00', 6),
(12, 'SN10012', 'Move5000', 'Ingenico', 'operativo', 'Terminal perteneciente a expedición recibida', '2025-02-18 00:00:00', '2025-02-13 00:00:00', 6),

-- Expediciones abiertas: cajas reservadas, todavía en almacén
(13, 'SN10013', 'A920',     'PAX',      'pendiente_transito', 'Terminal reservado en expedición abierta', '2025-03-01 00:00:00', '2025-02-20 00:00:00', 7),
(14, 'SN10014', 'A920',     'PAX',      'pendiente_transito', 'Terminal reservado en expedición abierta', '2025-03-02 00:00:00', '2025-02-21 00:00:00', 7),
(15, 'SN10015', 'Move5000', 'Ingenico', 'pendiente_transito', 'Terminal reservado en expedición abierta', '2025-03-05 00:00:00', '2025-02-25 00:00:00', 8),
(16, 'SN10016', 'Move5000', 'Ingenico', 'pendiente_transito', 'Terminal reservado en expedición abierta', '2025-03-06 00:00:00', '2025-02-26 00:00:00', 8),

-- Cajas en almacén con otros estados
(17, 'SN10017', 'V240',     'Verifone', 'pendiente_revision', 'Error en lector de tarjetas', '2025-03-10 00:00:00', '2025-03-01 00:00:00', 9),
(18, 'SN10018', 'V240',     'Verifone', 'pendiente_revision', 'Pantalla con fallo intermitente', '2025-03-11 00:00:00', '2025-03-02 00:00:00', 9),
(19, 'SN10019', 'A920',     'PAX',      'nivel_1', 'Configuración inicial pendiente', '2025-03-15 00:00:00', '2025-03-05 00:00:00', 10),
(20, 'SN10020', 'A920',     'PAX',      'nivel_1', 'Configuración inicial pendiente', '2025-03-16 00:00:00', '2025-03-06 00:00:00', 10),


-- Cajas disponibles para pruebas de agregar/desasignar
(21, 'SN10021', 'V240',     'Verifone', 'operativo', 'Terminal operativo en almacén', '2025-03-20 00:00:00', '2025-03-10 00:00:00', 11),
(22, 'SN10022', 'V240',     'Verifone', 'operativo', 'Terminal operativo en almacén', '2025-03-21 00:00:00', '2025-03-11 00:00:00', 11),
(23, 'SN10023', 'VX680',    'Verifone', 'pendiente_laboratorio', 'Terminal pendiente laboratorio en almacén', '2025-03-22 00:00:00', '2025-03-12 00:00:00', 12),
(24, 'SN10024', 'VX680',    'Verifone', 'pendiente_laboratorio', 'Terminal pendiente laboratorio en almacén', '2025-03-23 00:00:00', '2025-03-13 00:00:00', 12),
(25, 'SN10025', 'Move5000', 'Ingenico', 'operativo', 'Terminal operativo en almacén', '2025-03-24 00:00:00', '2025-03-14 00:00:00', 13),
(26, 'SN10026', 'Move5000', 'Ingenico', 'operativo', 'Terminal operativo en almacén', '2025-03-25 00:00:00', '2025-03-15 00:00:00', 13),
(27, 'SN10027', 'Desk3500', 'Ingenico', 'pendiente_laboratorio', 'Terminal pendiente laboratorio en almacén', '2025-03-26 00:00:00', '2025-03-16 00:00:00', 14),
(28, 'SN10028', 'Desk3500', 'Ingenico', 'pendiente_laboratorio', 'Terminal pendiente laboratorio en almacén', '2025-03-27 00:00:00', '2025-03-17 00:00:00', 14),
(29, 'SN10029', 'A920',     'PAX',      'operativo', 'Terminal operativo en almacén', '2025-03-28 00:00:00', '2025-03-18 00:00:00', 15),
(30, 'SN10030', 'A920',     'PAX',      'operativo', 'Terminal operativo en almacén', '2025-03-29 00:00:00', '2025-03-19 00:00:00', 15),
(31, 'SN10031', 'A80',      'PAX',      'operativo', 'Terminal operativo en almacén', '2025-03-30 00:00:00', '2025-03-20 00:00:00', 16),
(32, 'SN10032', 'A80',      'PAX',      'operativo', 'Terminal operativo en almacén', '2025-03-31 00:00:00', '2025-03-21 00:00:00', 16);


ALTER TABLE terminales_pago AUTO_INCREMENT = 33;

-- =========================
-- EXPEDICIONES
-- =========================
-- Criterios:
-- - Hay expediciones en_transito, recibidas y abiertas.
-- - Las expediciones abiertas tienen fecha_envio NULL.
-- - Las cajas de expediciones abiertas siguen en almacén y sus terminales están pendiente_transito.
-- - Las cajas de expediciones en_transito/recibidas tienen palet_id NULL.
INSERT INTO expediciones
(id, referencia_expedicion, fecha_creacion, fecha_envio, fecha_recepcion, fecha_modificacion, direccion_destino, paquetes, peso, notas, estado, usuario_id, caja_id)
VALUES
-- Expedición en tránsito creada hoy
(1, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-001'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 2, 30, 'Salida Verifone V240 para cliente Madrid', 'en_transito', 2, 1),
(2, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-001'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 1, 18, 'Salida Ingenico Move5000 para cliente Madrid', 'en_transito', 2, 2),

-- Segunda expedición en tránsito creada hoy
(3, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-002'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Av. Maisonnave 20, Alicante', 1, 15, 'Salida PAX A920 para cliente Alicante', 'en_transito', 2, 3),
(4, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-002'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Av. Maisonnave 20, Alicante', 1, 16, 'Salida Ingenico Move5000 para cliente Alicante', 'en_transito', 2, 4),

-- Expedición recibida histórica
(5, 'EXP-20260420-001', '2026-04-20 10:15:00', '2026-04-20 10:15:00', '2026-04-22 17:45:00', '2026-04-22 17:45:00', 'Av. Andalucía 25, Sevilla', 2, 28, 'Entrega recibida correctamente', 'recibida', 3, 5),
(6, 'EXP-20260420-001', '2026-04-20 10:15:00', '2026-04-20 10:15:00', '2026-04-22 17:45:00', '2026-04-22 17:45:00', 'Av. Andalucía 25, Sevilla', 1, 16, 'Entrega recibida correctamente', 'recibida', 3, 6),

-- Expediciones abiertas para pruebas de edición/confirmación
(7, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-003'), CURRENT_TIMESTAMP, NULL, NULL, CURRENT_TIMESTAMP, 'C/ Colón 5, Valencia', 1, 12, 'Expedición abierta pendiente de confirmar', 'abierta', 2, 7),
(8, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-004'), CURRENT_TIMESTAMP, NULL, NULL, CURRENT_TIMESTAMP, 'Av. Diagonal 100, Barcelona', 1, 14, 'Expedición abierta con Ingenico pendiente de confirmar', 'abierta', 2, 8);

ALTER TABLE expediciones AUTO_INCREMENT = 9;
