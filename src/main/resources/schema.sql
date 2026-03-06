
--
-- TABLA usuarios
-- 
CREATE TABLE IF NOT EXISTS usuarios (
    id                INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre            VARCHAR(60),
    apellido          VARCHAR(60),
    lugar_trabajo     VARCHAR(80),
    -- nombre_usuario VARCHAR(60),
    --hash_password VARCHAR(255),
    rol               VARCHAR(30) NOT NULL,
    CONSTRAINT ck_usuarios_rol CHECK (rol IN ('trabajador_almacen','tecnico','logistica','administrador'))
);


--
-- TABLA expediciones
--
CREATE TABLE IF NOT EXISTS expediciones (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_recepcion TIMESTAMP NULL,
    fecha_modificacion TIMESTAMP NULL,
    direccion_destino VARCHAR(255) NOT NULL,
    paquetes INT,
    peso INT,
    notas VARCHAR(255),
    usuario_id INT NOT NULL,
    estado ENUM('abierta', 'en_transito', 'recibida') DEFAULT 'abierta' NOT NULL,
    CONSTRAINT fk_expediciones_usuarios
      FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);


-- TABLAS PARA SPRING SECURIRY
CREATE TABLE IF NOT EXISTS users_security (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    username        VARCHAR(50) NOT NULL UNIQUE,
    email           VARCHAR(100) NOT NULL,
    password        VARCHAR(200) NOT NULL,
    rol         VARCHAR(30) NOT NULL,
    CONSTRAINT ck_security_rol CHECK (rol IN ('trabajador_almacen','tecnico','logistica','administrador')),

    activado        BOOLEAN NOT NULL,
    usuario_id  INT NOT NULL UNIQUE,
    CONSTRAINT fk_security_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);