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
INSERT INTO ubicaciones_almacen (id, referencia, estanteria_id, nivel) VALUES
(1, '1A1', 1, 1),
(2, '1A2', 1, 2),
(3, '1B1', 2, 1),
(4, '2C1', 3, 1),
(5, '2D1', 4, 1);

ALTER TABLE ubicaciones_almacen AUTO_INCREMENT = 6;

-- =========================
-- PALETS
-- =========================
INSERT INTO palets (id, material, tipo, capacidad_max_cajas, ubicacion_almacen_id, codigo_marca, descripcion) VALUES
(1, 'madera', 'europeo', 8, 1, 'PAL-VER-001', ''),
(2, 'plastico', 'americano', 8, 2, 'PAL-ING-001', ''),
(3, 'madera', 'europeo', 8, 3, 'PAL-PAX-001', ''),
(4, 'madera', 'europeo', 6, NULL, 'PAL-MIX-001', '');

ALTER TABLE palets AUTO_INCREMENT = 5;

-- =========================
-- CAJAS
-- =========================
-- Criterio:
-- - Las cajas usadas por expediciones en tránsito/recibidas NO tienen palet_id.
-- - Las cajas que siguen en almacén sí tienen palet_id.
-- - Cada caja contiene terminales de un único estado.
INSERT INTO cajas (id, etiqueta, modelo_producto, max_capacity, palet_id) VALUES
(1,  'CAJA-EXP-VER-001',  'Verifone V240 - En tránsito',              120, NULL),
(2,  'CAJA-EXP-ING-001',  'Ingenico Move5000 - En tránsito',          100, NULL),
(3,  'CAJA-EXP-PAX-001',  'PAX A920 - En tránsito',                    90, NULL),
(4,  'CAJA-EXP-MOV-002',  'Ingenico Move5000 - En tránsito',          100, NULL),
(5,  'CAJA-REC-VER-001',  'Verifone V240 - Expedición recibida',      120, NULL),
(6,  'CAJA-REC-ING-001',  'Ingenico Move5000 - Expedición recibida',  100, NULL),
(7,  'CAJA-OP-PAX-001',   'PAX A920 - Operativos',                     90, 3),
(8,  'CAJA-OP-MOV-001',   'Ingenico Move5000 - Operativos',           100, 1),
(9,  'CAJA-REV-VER-001',  'Verifone V240 - Pendiente revisión',       120, 2),
(10, 'CAJA-N1-PAX-001',   'PAX A920 - Nivel 1',                        90, 2);

ALTER TABLE cajas AUTO_INCREMENT = 11;

-- =========================
-- TERMINALES DE PAGO
-- =========================
-- Criterio:
-- - Todos los terminales de una misma caja tienen el mismo estado.
-- - Los terminales de cajas expedidas están en_transito y siguen asociados a su caja.
-- - La caja expedida queda fuera del almacén porque su palet_id es NULL.
INSERT INTO terminales_pago (id, numero_serie, modelo, marca, estado, notas, fecha_ingreso, fecha_creacion, caja_id) VALUES
(1,  'SN10001', 'V240',     'Verifone', 'en_transito', 'Terminal incluido en expedición activa', '2025-01-10 00:00:00', '2024-12-15 00:00:00', 1),
(2,  'SN10002', 'V240',     'Verifone', 'en_transito', 'Terminal incluido en expedición activa', '2025-01-11 00:00:00', '2024-12-16 00:00:00', 1),
(3,  'SN10003', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición activa', '2025-01-20 00:00:00', '2024-11-10 00:00:00', 2),
(4,  'SN10004', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición activa', '2025-01-21 00:00:00', '2024-11-11 00:00:00', 2),
(5,  'SN10005', 'A920',     'PAX',      'en_transito', 'Terminal incluido en expedición activa', '2025-02-01 00:00:00', '2025-01-25 00:00:00', 3),
(6,  'SN10006', 'A920',     'PAX',      'en_transito', 'Terminal incluido en expedición activa', '2025-02-02 00:00:00', '2025-01-26 00:00:00', 3),
(7,  'SN10007', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición activa', '2025-02-10 00:00:00', '2025-02-01 00:00:00', 4),
(8,  'SN10008', 'Move5000', 'Ingenico', 'en_transito', 'Terminal incluido en expedición activa', '2025-02-11 00:00:00', '2025-02-02 00:00:00', 4),
(9,  'SN10009', 'V240',     'Verifone', 'en_transito', 'Terminal perteneciente a expedición recibida', '2025-02-15 00:00:00', '2025-02-10 00:00:00', 5),
(10, 'SN10010', 'V240',     'Verifone', 'en_transito', 'Terminal perteneciente a expedición recibida', '2025-02-16 00:00:00', '2025-02-11 00:00:00', 5),
(11, 'SN10011', 'Move5000', 'Ingenico', 'en_transito', 'Terminal perteneciente a expedición recibida', '2025-02-17 00:00:00', '2025-02-12 00:00:00', 6),
(12, 'SN10012', 'Move5000', 'Ingenico', 'en_transito', 'Terminal perteneciente a expedición recibida', '2025-02-18 00:00:00', '2025-02-13 00:00:00', 6),
(13, 'SN10013', 'A920',     'PAX',      'operativo', 'Terminal operativo en almacén', '2025-03-01 00:00:00', '2025-02-20 00:00:00', 7),
(14, 'SN10014', 'A920',     'PAX',      'operativo', 'Terminal operativo en almacén', '2025-03-02 00:00:00', '2025-02-21 00:00:00', 7),
(15, 'SN10015', 'Move5000', 'Ingenico', 'operativo', 'Terminal operativo en almacén', '2025-03-05 00:00:00', '2025-02-25 00:00:00', 8),
(16, 'SN10016', 'Move5000', 'Ingenico', 'operativo', 'Terminal operativo en almacén', '2025-03-06 00:00:00', '2025-02-26 00:00:00', 8),
(17, 'SN10017', 'V240',     'Verifone', 'pendiente_revision', 'Error en lector de tarjetas', '2025-03-10 00:00:00', '2025-03-01 00:00:00', 9),
(18, 'SN10018', 'V240',     'Verifone', 'pendiente_revision', 'Pantalla con fallo intermitente', '2025-03-11 00:00:00', '2025-03-02 00:00:00', 9),
(19, 'SN10019', 'A920',     'PAX',      'nivel_1', 'Configuración inicial pendiente', '2025-03-15 00:00:00', '2025-03-05 00:00:00', 10),
(20, 'SN10020', 'A920',     'PAX',      'nivel_1', 'Configuración inicial pendiente', '2025-03-16 00:00:00', '2025-03-06 00:00:00', 10);

ALTER TABLE terminales_pago AUTO_INCREMENT = 21;

-- =========================
-- EXPEDICIONES
-- =========================
-- Criterio:
-- - No hay expediciones abiertas.
-- - Las expediciones creadas hoy están en_transito y usan cajas sin palet.
-- - fecha_envio = fecha_creacion para reflejar envío inmediato.
-- - Las expediciones recibidas tienen fecha_recepcion informada.
INSERT INTO expediciones
(id, referencia_expedicion, fecha_creacion, fecha_envio, fecha_recepcion, fecha_modificacion, direccion_destino, paquetes, peso, notas, estado, usuario_id, caja_id)
VALUES
(1, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-001'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 2, 30, 'Salida Verifone V240 para cliente Madrid', 'en_transito', 2, 1),
(2, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-001'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 1, 18, 'Salida Ingenico Move5000 para cliente Madrid', 'en_transito', 2, 2),
(3, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-002'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Av. Maisonnave 20, Alicante', 1, 15, 'Salida PAX A920 para cliente Alicante', 'en_transito', 2, 3),
(4, CONCAT('EXP-', DATE_FORMAT(CURDATE(), '%Y%m%d'), '-002'), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'Av. Maisonnave 20, Alicante', 1, 16, 'Salida Ingenico Move5000 para cliente Alicante', 'en_transito', 2, 4),
(5, 'EXP-20260420-001', '2026-04-20 10:15:00', '2026-04-20 10:15:00', '2026-04-22 17:45:00', '2026-04-22 17:45:00', 'Av. Andalucía 25, Sevilla', 2, 28, 'Entrega recibida correctamente', 'recibida', 3, 5),
(6, 'EXP-20260420-001', '2026-04-20 10:15:00', '2026-04-20 10:15:00', '2026-04-22 17:45:00', '2026-04-22 17:45:00', 'Av. Andalucía 25, Sevilla', 1, 16, 'Entrega recibida correctamente', 'recibida', 3, 6);

ALTER TABLE expediciones AUTO_INCREMENT = 7;
