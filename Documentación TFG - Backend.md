# Documentación TFG - Backend de Gestión de Almacén y Terminales (PI-PLUS)

## 1. Introducción y objetivos

Este backend implementa la lógica de negocio y la API REST para gestionar:

- terminales de pago,
- cajas,
- palés,
- ubicaciones de almacén,
- expediciones,
- autenticación y autorización por sesión.

### Objetivos funcionales

1. Exponer endpoints REST para la operativa logística.
2. Validar reglas de negocio críticas en servidor.
3. Mantener consistencia de relaciones (terminal-caja-palé-ubicación).
4. Soportar sesión segura por cookie HTTP.
5. Servir datos analíticos para dashboard y mapa de almacén.

---

## 2. Arquitectura técnica

### Stack principal

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- Spring Security
- MySQL
- Springdoc OpenAPI (Swagger)

### Capas

- `controller`: endpoints REST
- `service`: reglas de negocio y casos de uso
- `repository`: acceso a datos
- `model`: entidades JPA
- `model/dtos`: contratos de entrada/salida

---

## 3. Seguridad y autenticación

### Modelo de autenticación

- Sesión de servidor (Spring Security + cookie `JSESSIONID`).
- Login: `POST /api/auth/login`
- Usuario autenticado: `GET /api/auth/user`
- Logout: `POST /api/auth/logout`

### Notas

- El frontend debe enviar `credentials: "include"`.
- No se utiliza JWT.
- Los roles se cargan desde `users_security`/`usuarios`.

---

## 4. Módulos y endpoints principales

### 4.1 Catálogo

- `GET /api/catalogo/terminales/marcas`
- `GET /api/catalogo/terminales/marcas/{marca}/modelos`
- `GET /api/catalogo/cajas/modelos/{modelo}/max-capacity`

### 4.2 Ubicaciones y mapa

- `GET /api/ubicaciones/mapa`
- `GET /api/ubicaciones`
- `GET /api/ubicaciones/{id}`

El endpoint de mapa devuelve estructura para UI con:
- hueco/ubicación,
- pasillo,
- estantería,
- palé,
- cajas,
- ocupación.

### 4.3 Palés

- `POST /api/palets`
- `GET /api/palets`
- `GET /api/palets/{id}`
- `GET /api/palets/free`
- `PATCH /api/palets/{id}/ubicacion`
- `PATCH /api/palets/{id}/descripcion`
- `DELETE /api/palets/{id}`
- `DELETE /api/palets/{paletId}/cajas/{cajaId}`

Mover/desasignar ubicación:

```json
{
  "ubicacionAlmacenId": 4
}
```

o

```json
{
  "ubicacionAlmacenId": null
}
```

Editar descripción:

```json
{
  "descripcion": "Nuevo texto"
}
```

### 4.4 Cajas

- `POST /api/cajas`
- `GET /api/cajas`
- `GET /api/cajas/{id}`
- `GET /api/cajas/free`
- `GET /api/cajas/free/marca/{marca}`
- `PATCH /api/cajas/{id}/palet`
- `POST /api/cajas/{id}/validar-terminal`
- `POST /api/cajas/{id}/terminales`
- `DELETE /api/cajas/{id}/terminales/{sn}`
- `GET /api/cajas/{id}/capacidad`

Regla implementada:
- `/api/cajas/free` y `/api/cajas/free/marca/{marca}` devuelven cajas sin palé y sin expedición asociada.

### 4.5 Terminales

- `GET /api/terminales`
- `GET /api/terminales/sn/{numeroSerie}`
- `PUT /api/terminales/sn/{numeroSerie}`
- `DELETE /api/terminales/sn/{numeroSerie}`

### 4.6 Expediciones

- `GET /api/expediciones`
- `GET /api/expediciones/list`
- `GET /api/expediciones/today/list`
- `GET /api/expediciones/search`
- `GET /api/expediciones/grouped/today`
- `GET /api/expediciones/grouped/search`
- `POST /api/expediciones/lote`
- `PUT /api/expediciones`
- `DELETE /api/expediciones/{id}`

---

## 5. Reglas de negocio destacadas

1. Un palé define una marca permitida para cajas asociadas.
2. La validación de terminal contra caja se hace en backend.
3. No se puede borrar palé con cajas asociadas (integridad referencial).
4. Las cajas en expedición no se ofrecen como cajas libres.
5. El backend es la fuente de verdad para validaciones críticas.

---

## 6. Base de datos y datos de prueba

Scripts:
- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`

`data.sql` incluye:
- usuarios de prueba,
- estructura de pasillos/estanterías/ubicaciones,
- palés, cajas, terminales y expediciones en distintos estados.

---

## 7. Manejo de errores

Formato de error con campos:
- `status`
- `error`
- `message`
- `path`
- `timestamp`

Ejemplo:

```json
{
  "status": 409,
  "error": "DataIntegrityViolationException",
  "message": "Cannot delete or update a parent row...",
  "path": "/bdproyecto/api/palets/5"
}
```

---

## 8. Testing recomendado

Casos mínimos:
1. Login correcto/incorrecto.
2. PATCH palé ubicación con id válido y con `null`.
3. PATCH palé descripción válido/inválido.
4. Cajas libres excluyendo expediciones.
5. Asignar/desasignar caja-palé.
6. Validación y asociación batch de terminales.
7. Borrado de palé con y sin cajas asociadas.

---

## 9. Despliegue y ejecución

Para preparar entorno y levantar backend:
- revisar [Setup Backend.md](./Setup%20Backend.md)

---

## 10. Mejoras futuras

1. Cobertura de tests de integración más amplia.
2. Auditoría de operaciones (quién/cuándo).
3. Catálogo completo para material/tipo de palé desde backend.
4. Estandarización final de DTOs y nombres de campos.
5. Endpoints de reporting adicionales para dashboard.
