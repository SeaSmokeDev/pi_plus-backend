# Setup Backend – PI-PLUS

Guía para levantar el backend en local.

## Requisitos
- Java 21 (JDK)
- Maven 3.9+
- MySQL 8+
- IDE opcional (IntelliJ, VS Code, NetBeans)

## 1) Crear base de datos
En MySQL, crear la base de datos:

```sql
CREATE DATABASE pi_plus CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

## 2) Configurar `application.properties`
Editar `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pi_plus?useSSL=false&serverTimezone=UTC
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_PASSWORD
```

Configuración de contexto (si no está ya):

```properties
server.servlet.context-path=/bdproyecto
```

## 3) Instalar dependencias y compilar
```bash
mvn clean install
```

## 4) Arrancar en desarrollo
```bash
mvn spring-boot:run
```

Backend disponible por defecto en:

```text
http://localhost:8080/bdproyecto
```

## 5) Swagger / OpenAPI
- Swagger UI: `http://localhost:8080/bdproyecto/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/bdproyecto/api-docs`

## 6) Carga de datos de prueba
El proyecto usa:
- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`

Verifica en `application.properties` que la inicialización SQL esté habilitada para tu entorno.

## 7) Usuarios de prueba (`data.sql`)
- `cmartinez` / `1234` (administrador)
- `lgomez` / `5678` (logistica)
- `druiz` / `5678` (trabajador_almacen)
- `alopez` / `5678` (tecnico)

## 8) Comprobación rápida
1. Login:
```http
POST /api/auth/login
```
2. Usuario en sesión:
```http
GET /api/auth/user
```
3. Mapa almacén:
```http
GET /api/ubicaciones/mapa
```
