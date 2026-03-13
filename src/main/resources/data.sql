-- =========================
-- INSERT USUARIOS
-- =========================
INSERT INTO usuarios (id, nombre, apellido, rol, lugar_trabajo) VALUES
(1, 'Carlos', 'Martínez', 'administrador', 'Central'),
(2, 'Lucía', 'Gómez', 'logistica', 'Almacén A'),
(3, 'David', 'Ruiz', 'trabajador_almacen', 'Almacén B'),
(4, 'Ana', 'López', 'tecnico', 'Soporte Técnico');

-- Reiniciar AUTO_INCREMENT
ALTER TABLE usuarios
ALTER COLUMN id
RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM usuarios);
ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 5;

-- =========================
-- INSERT EXPEDICIONES
-- =========================
INSERT INTO expediciones
(id, fecha_creacion, fecha_recepcion, fecha_modificacion, direccion_destino, paquetes, peso, notas, estado, usuario_id)
VALUES
(1, CURRENT_TIMESTAMP, NULL, NULL, 'Calle Mayor 10, Madrid', 5, 120, 'Entrega urgente', 'abierta', 2),
(2, CURRENT_TIMESTAMP, NULL, NULL, 'Av. Andalucía 25, Sevilla', 3, 75, 'Cliente VIP', 'en_transito', 2),
(3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'C/ Valencia 45, Barcelona', 8, 200, 'Recibido correctamente', 'recibida', 3),
(4, CURRENT_TIMESTAMP, NULL, NULL, 'Polígono Industrial Sur, Valencia', 2, 50, 'Pendiente confirmación', 'abierta', 1);

-- Reiniciar AUTO_INCREMENT
ALTER TABLE expediciones
ALTER COLUMN id
RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM expediciones);
ALTER TABLE expediciones ALTER COLUMN id RESTART WITH 5;

-- =========================
-- INSERT USERS SECURITY
-- =========================
-- OJO: password debe estar codificada en BCrypt para que Spring Security autentique bien.
-- Puedes generar hashes con BCrypt y sustituirlos aquí.
INSERT INTO users_security (id, username, email, password, rol, activado, usuario_id) VALUES
(1, 'cmartinez', 'cmartinez@empresa.com', '$2a$10$ayw3FCBIkupFt5n9lrmJQe9XZMJhZiNCjaoOkXo/Ba0KZgymO01ce', 'administrador', TRUE, 1), -- 1234
(2, 'lgomez',    'lgomez@empresa.com',    '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'logistica', TRUE, 2), -- 5678
(3, 'druiz',     'druiz@empresa.com',     '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'trabajador_almacen', TRUE, 3), -- 5678
(4, 'alopez',    'alopez@empresa.com',    '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'tecnico', TRUE, 4); -- 5678

-- Reiniciar AUTO_INCREMENT
ALTER TABLE users_security
ALTER COLUMN id
RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM users_security);
ALTER TABLE users_security ALTER COLUMN id RESTART WITH 5;

-- =========================
-- PASILLOS
-- =========================
INSERT INTO pasillos (id, numero_pasillo) VALUES
(1, 1),
(2, 2);

ALTER TABLE pasillos ALTER COLUMN id RESTART WITH 3;


-- =========================
-- ESTANTERÍAS
-- =========================
INSERT INTO estanterias (id, codigo, niveles_maximos, capacidad_nivel, pasillo_id) VALUES
(1, 'A', 4, 8, 1),
(2, 'B', 4, 8, 1),
(3, 'C', 4, 8, 2),
(4, 'D', 4, 8, 2);

ALTER TABLE estanterias ALTER COLUMN id RESTART WITH 5;


-- =========================
-- UBICACIONES EN ALMACÉN
-- =========================
INSERT INTO ubicaciones_almacen (id, referencia, estanteria_id, nivel) VALUES
(1, '1A1', 1, 1),
(2, '1A2', 1, 2),
(3, '1B1', 2, 1),
(4, '2C1', 3, 1),
(5, '2D1', 4, 1);

ALTER TABLE ubicaciones_almacen ALTER COLUMN id RESTART WITH 6;

-- =========================
-- PALETS
-- =========================
INSERT INTO palets (id, descripcion, material, tipo, capacidad_max_cajas, ubicacion_almacen_id, codigo_marca) VALUES
(1, 'Palet recepción Verifone', 'madera', 'europeo', 8, 1, 'PAL-VER-001'),
(2, 'Palet recepción Ingenico', 'plastico', 'americano', 8, 2, 'PAL-ING-001'),
(3, 'Palet mixto de tránsito', 'madera', 'europeo', 6, NULL, 'PAL-MIX-001');

ALTER TABLE palets ALTER COLUMN id RESTART WITH 4;


-- =========================
-- CAJAS
-- =========================
INSERT INTO cajas (id, etiqueta, modelo_producto, palet_id) VALUES
(1, 'CAJA-A1', 'Verifone V240', 1),
(2, 'CAJA-B2', 'Ingenico Move5000', 2),
(3, 'CAJA-C3', 'PAX A920', NULL);

ALTER TABLE cajas ALTER COLUMN id RESTART WITH 4;


-- =========================
-- TERMINALES DE PAGO
-- =========================
INSERT INTO terminales_pago (id, numero_serie, modelo, marca, estado, notas, fecha_ingreso, fecha_creacion, caja_id) VALUES
(1, 'SN10001', 'V240', 'Verifone', 'operativo', 'Terminal en uso tienda centro', DATE '2025-01-10', DATE '2024-12-15', 1),
(2, 'SN10002', 'Move5000', 'Ingenico', 'pendiente_revision', 'Error en lector de tarjetas', DATE '2025-01-20', DATE '2024-11-10', 2),
(3, 'SN10003', 'A920', 'PAX', 'en_transito', 'Enviado desde almacén central', DATE '2025-02-01', DATE '2025-01-25', NULL),
(4, 'SN10004', 'V240', 'Verifone', 'nivel_1', 'Configuración inicial pendiente', DATE '2025-01-05', DATE '2024-12-01', 1);

ALTER TABLE terminales_pago ALTER COLUMN id RESTART WITH 5;







