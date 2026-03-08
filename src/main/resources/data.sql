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

-- =========================
-- INSERT USERS_SECURITY
-- =========================
-- OJO: password debe estar codificada en BCrypt para que Spring Security autentique bien.
-- Puedes generar hashes con BCrypt y sustituirlos aquí.
INSERT INTO users_security (id, username, email, password, rol, activado, usuario_id) VALUES
(1, 'cmartinez', 'cmartinez@empresa.com', '$2a$10$ayw3FCBIkupFt5n9lrmJQe9XZMJhZiNCjaoOkXo/Ba0KZgymO01ce', 'administrador', TRUE, 1), -- password: 1234
(2, 'lgomez',    'lgomez@empresa.com',    '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'logistica',     TRUE, 2), -- password: 5678
(3, 'druiz',     'druiz@empresa.com',     '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'trabajador_almacen', TRUE, 3), -- password: 5678
(4, 'alopez',    'alopez@empresa.com',    '$2a$10$fzcGgF.8xODz7ptkmZC.OeX1Kj5GDI//FhW2sG0vlshW6ZAKJky0e', 'tecnico',       TRUE, 4); -- password: 5678

-- Reiniciar AUTO_INCREMENT
ALTER TABLE users_security
ALTER COLUMN id
RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM users_security);


-- =========================
-- DATOS DE PRUEBA - CAJAS
-- =========================
INSERT INTO cajas (etiqueta, modelo_producto, palet_id) VALUES
('CAJA-A1', 'Verifone V240', 101),
('CAJA-B2', 'Ingenico Move5000', 102),
('CAJA-C3', 'PAX A920', NULL);

-- =========================
-- DATOS DE PRUEBA - TERMINALES
-- =========================
INSERT INTO terminales_pago (numero_serie, modelo, marca, estado, notas, fecha_ingreso, fecha_creacion, caja_id) VALUES
('SN10001', 'V240', 'Verifone', 'operativo', 'Terminal en uso tienda centro', '2025-01-10', '2024-12-15', 1),
('SN10002', 'Move5000', 'Ingenico', 'pendiente_revision', 'Error en lector de tarjetas', '2025-01-20', '2024-11-10', 2),
('SN10003', 'A920', 'PAX', 'en_transito', 'Enviado desde almacén central', '2025-02-01', '2025-01-25', NULL),
('SN10004', 'V240', 'Verifone', 'nivel_1', 'Configuración inicial pendiente', '2025-01-05', '2024-12-01', 1);