-- Ejecutar desde terminal (en la raiz del proyecto):
-- mysql -u root -p pi_plus < src/main/resources/schema.sql

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS terminales_pago;
DROP TABLE IF EXISTS expediciones;
DROP TABLE IF EXISTS cajas;
DROP TABLE IF EXISTS palets;
DROP TABLE IF EXISTS ubicaciones_almacen;
DROP TABLE IF EXISTS estanterias;
DROP TABLE IF EXISTS pasillos;
DROP TABLE IF EXISTS users_security;
DROP TABLE IF EXISTS usuarios;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- TABLA usuarios
-- =========================
CREATE TABLE IF NOT EXISTS usuarios (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(60),
    apellido VARCHAR(60),
    lugar_trabajo VARCHAR(80),
    rol VARCHAR(30) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT ck_usuarios_rol
        CHECK (rol IN ('trabajador_almacen','tecnico','logistica','administrador'))
);

-- =========================
-- TABLA expediciones
-- =========================
CREATE TABLE IF NOT EXISTS expediciones (
    id INT NOT NULL AUTO_INCREMENT,
    referencia_expedicion VARCHAR(50) NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_recepcion TIMESTAMP NULL,
    fecha_modificacion TIMESTAMP NULL,
    fecha_envio TIMESTAMP NULL,
    direccion_destino VARCHAR(255) NOT NULL,
    paquetes INT,
    peso INT,
    notas VARCHAR(255),
    usuario_id INT NOT NULL,
    caja_id INT NULL,
    estado ENUM('abierta', 'en_transito', 'recibida') NOT NULL DEFAULT 'abierta',
    PRIMARY KEY (id),
    CONSTRAINT fk_expediciones_usuarios
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- =========================
-- TABLAS PARA SPRING SECURITY
-- =========================
CREATE TABLE IF NOT EXISTS users_security (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(200) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activado BOOLEAN NOT NULL,
    usuario_id INT NOT NULL UNIQUE,
    PRIMARY KEY (id),
    CONSTRAINT ck_security_rol
        CHECK (rol IN ('trabajador_almacen','tecnico','logistica','administrador')),
    CONSTRAINT fk_security_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- =========================
-- PASILLOS
-- =========================
CREATE TABLE IF NOT EXISTS pasillos (
    id INT NOT NULL AUTO_INCREMENT,
    numero_pasillo INT NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- =========================
-- ESTANTERÍAS
-- =========================
CREATE TABLE IF NOT EXISTS estanterias (
    id INT NOT NULL AUTO_INCREMENT,
    codigo CHAR(1) NOT NULL COMMENT 'Ejemplo: A',
    niveles_maximos INT NOT NULL,
    capacidad_nivel INT NOT NULL DEFAULT 8 COMMENT 'En cada nivel cabe solo un palé con un maximo de 8 cajas',
    pasillo_id INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_estanterias_pasillos
        FOREIGN KEY (pasillo_id) REFERENCES pasillos(id)
);

-- =========================
-- UBICACIONES EN ALMACÉN
-- =========================
CREATE TABLE IF NOT EXISTS ubicaciones_almacen (
    id INT NOT NULL AUTO_INCREMENT,
    referencia VARCHAR(255) NOT NULL COMMENT 'Ejemplo: 1A3 (pasillo-estantería-nivel)',
    estanteria_id INT NOT NULL,
    nivel INT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ubicaciones_estanterias
        FOREIGN KEY (estanteria_id) REFERENCES estanterias(id),
    CONSTRAINT uq_ubicacion_estanteria_nivel
        UNIQUE (estanteria_id, nivel),
    CONSTRAINT ck_ubicaciones_nivel
        CHECK (nivel BETWEEN 1 AND 4)
);

-- =========================
-- PALETS
-- =========================
CREATE TABLE IF NOT EXISTS palets (
    id INT NOT NULL AUTO_INCREMENT,
    material ENUM('plastico', 'madera') NOT NULL,
    tipo ENUM('americano', 'europeo') NOT NULL,
    capacidad_max_cajas INT NOT NULL DEFAULT 8,
    ubicacion_almacen_id INT DEFAULT NULL,
    codigo_marca VARCHAR(255),
    descripcion VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_palets_ubicaciones
        FOREIGN KEY (ubicacion_almacen_id) REFERENCES ubicaciones_almacen(id)
);

-- =========================
-- CAJAS
-- =========================
CREATE TABLE IF NOT EXISTS cajas (
    id INT NOT NULL AUTO_INCREMENT,
    etiqueta VARCHAR(255) NOT NULL UNIQUE COMMENT 'Etiqueta de ubicación/referencia',
    modelo_producto VARCHAR(255),
    max_capacity INT NOT NULL DEFAULT 100 COMMENT 'Capacidad maxima de terminales en caja',
    palet_id INT DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_cajas_palets
        FOREIGN KEY (palet_id) REFERENCES palets(id)
);

-- FK diferida para respetar el orden de creacion
ALTER TABLE expediciones
    ADD CONSTRAINT fk_expediciones_cajas
        FOREIGN KEY (caja_id) REFERENCES cajas(id);

-- =========================
-- TERMINALES DE PAGO
-- =========================
CREATE TABLE IF NOT EXISTS terminales_pago (
    id INT NOT NULL AUTO_INCREMENT,
    numero_serie VARCHAR(255) NOT NULL UNIQUE,
    modelo VARCHAR(255) NOT NULL,
    marca VARCHAR(255) NOT NULL,
    estado ENUM('en_transito', 'pendiente_revision', 'operativo', 'pendiente_laboratorio', 'nivel_1') NOT NULL,
    notas VARCHAR(255),
    fecha_ingreso TIMESTAMP NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    caja_id INT DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_terminales_cajas
        FOREIGN KEY (caja_id) REFERENCES cajas(id)
);
