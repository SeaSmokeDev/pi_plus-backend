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
(2, 2);

ALTER TABLE pasillos AUTO_INCREMENT = 3;

-- =========================
-- ESTANTERÍAS
-- =========================
INSERT INTO estanterias (id, codigo, niveles_maximos, capacidad_nivel, pasillo_id) VALUES
(1, 'A', 4, 8, 1),
(2, 'B', 4, 8, 1),
(3, 'C', 4, 8, 2),
(4, 'D', 4, 8, 2);

ALTER TABLE estanterias AUTO_INCREMENT = 5;

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
INSERT INTO palets (id, descripcion, material, tipo, capacidad_max_cajas, ubicacion_almacen_id, codigo_marca) VALUES
(1, 'Palet recepción Verifone', 'madera', 'europeo', 8, 1, 'PAL-VER-001'),
(2, 'Palet recepción Ingenico', 'plastico', 'americano', 8, 2, 'PAL-ING-001'),
(3, 'Palet recepción PAX', 'madera', 'europeo', 8, 3, 'PAL-PAX-001'),
(4, 'Palet mixto de tránsito', 'madera', 'europeo', 6, NULL, 'PAL-MIX-001');

ALTER TABLE palets AUTO_INCREMENT = 5;

-- =========================
-- CAJAS
-- =========================
INSERT INTO cajas (id, etiqueta, modelo_producto, palet_id) VALUES
(1, 'CAJA-A1', 'Verifone V240', 1),
(2, 'CAJA-B2', 'Ingenico Move5000', 2),
(3, 'CAJA-C3', 'PAX A920', 3),
(4, 'CAJA-D4', 'Verifone V240', 4),
(5, 'CAJA-E5', 'Ingenico Move5000', 4);

ALTER TABLE cajas AUTO_INCREMENT = 6;

-- =========================
-- TERMINALES DE PAGO
-- =========================
INSERT INTO terminales_pago (id, numero_serie, modelo, marca, estado, notas, fecha_ingreso, fecha_creacion, caja_id) VALUES
(1, 'SN10001', 'V240', 'Verifone', 'operativo', 'Terminal en caja para salida Madrid', '2025-01-10 00:00:00', '2024-12-15 00:00:00', 1),
(2, 'SN10002', 'V240', 'Verifone', 'operativo', 'Terminal en caja para salida Madrid', '2025-01-11 00:00:00', '2024-12-16 00:00:00', 1),
(3, 'SN10003', 'Move5000', 'Ingenico', 'operativo', 'Terminal en caja para salida Madrid', '2025-01-20 00:00:00', '2024-11-10 00:00:00', 2),
(4, 'SN10004', 'A920', 'PAX', 'operativo', 'Terminal en caja para salida Madrid', '2025-02-01 00:00:00', '2025-01-25 00:00:00', 3),
(5, 'SN10005', 'V240', 'Verifone', 'en_transito', 'Terminal enviado a Sevilla', '2025-02-15 00:00:00', '2025-02-10 00:00:00', 4),
(6, 'SN10006', 'Move5000', 'Ingenico', 'en_transito', 'Terminal enviado a Sevilla', '2025-02-16 00:00:00', '2025-02-11 00:00:00', 5),
(7, 'SN10007', 'A920', 'PAX', 'nivel_1', 'Configuración inicial pendiente', '2025-03-01 00:00:00', '2025-02-20 00:00:00', 3),
(8, 'SN10008', 'V240', 'Verifone', 'pendiente_revision', 'Error en lector de tarjetas', '2025-03-05 00:00:00', '2025-02-25 00:00:00', 1);

ALTER TABLE terminales_pago AUTO_INCREMENT = 9;

-- =========================
-- EXPEDICIONES
-- Cada referencia agrupa varias líneas de expedición.
-- Una línea de expedición está asociada a una caja concreta.
-- =========================
INSERT INTO expediciones
(id, referencia_expedicion, fecha_creacion, fecha_envio, fecha_recepcion, fecha_modificacion, direccion_destino, paquetes, peso, notas, estado, usuario_id, caja_id)
VALUES
-- Grupo abierto creado hoy: mismo destino, varias cajas/modelos
(1, 'EXP-20260505-001', CURRENT_TIMESTAMP, '2026-05-06 09:30:00', NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 2, 30, 'Salida Verifone V240 para cliente Madrid', 'abierta', 2, 1),
(2, 'EXP-20260505-001', CURRENT_TIMESTAMP, '2026-05-06 09:30:00', NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 1, 18, 'Salida Ingenico Move5000 para cliente Madrid', 'abierta', 2, 2),
(3, 'EXP-20260505-001', CURRENT_TIMESTAMP, '2026-05-06 09:30:00', NULL, CURRENT_TIMESTAMP, 'Calle Mayor 10, Madrid', 1, 15, 'Salida PAX A920 para cliente Madrid', 'abierta', 2, 3),

-- Grupo en tránsito: mismo envío, varias líneas
(4, 'EXP-20260420-001', '2026-04-20 10:15:00', '2026-04-21 08:00:00', NULL, '2026-04-21 08:05:00', 'Av. Andalucía 25, Sevilla', 2, 28, 'Envío Verifone V240 en tránsito', 'en_transito', 2, 4),
(5, 'EXP-20260420-001', '2026-04-20 10:15:00', '2026-04-21 08:00:00', NULL, '2026-04-21 08:05:00', 'Av. Andalucía 25, Sevilla', 1, 16, 'Envío Ingenico Move5000 en tránsito', 'en_transito', 2, 5),

-- Grupo recibido histórico
(6, 'EXP-20260310-001', '2026-03-10 12:30:00', '2026-03-11 09:00:00', '2026-03-12 17:45:00', '2026-03-12 17:45:00', 'C/ Valencia 45, Barcelona', 2, 32, 'Entrega recibida correctamente', 'recibida', 3, 1),
(7, 'EXP-20260310-001', '2026-03-10 12:30:00', '2026-03-11 09:00:00', '2026-03-12 17:45:00', '2026-03-12 17:45:00', 'C/ Valencia 45, Barcelona', 1, 14, 'Entrega recibida correctamente', 'recibida', 3, 3),

-- Expedición individual sin grupo múltiple
(8, 'EXP-20260215-001', '2026-02-15 09:00:00', '2026-02-16 11:30:00', NULL, '2026-02-15 09:00:00', 'Polígono Industrial Sur, Valencia', 1, 12, 'Expedición individual pendiente de recepción', 'en_transito', 1, 2),

-- Expedición abierta histórica para probar filtros por estado y fecha
(9, 'EXP-20260125-001', '2026-01-25 16:20:00', '2026-01-27 10:00:00', NULL, '2026-01-25 16:20:00', 'Ronda Norte 8, Alicante', 1, 10, 'Pendiente de preparar documentación', 'abierta', 4, 5);

ALTER TABLE expediciones AUTO_INCREMENT = 10;
