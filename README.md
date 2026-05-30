# pi_plus-backend

Backend de gestión de almacén y terminales para PI-PLUS.

## Setup
Para preparar entorno y levantar el backend, consulta:

- [Setup Backend.md](./Setup%20Backend.md)

## Documentación TFG
Documentación funcional/técnica del backend:

- [Documentación TFG - Backend.md](./Documentaci%C3%B3n%20TFG%20-%20Backend.md)

## Rutas de entrada al arrancar la aplicación

Configuración actual en `application.properties`:
- `server.servlet.context-path=/bdproyecto`
- `server.port` no está fijado (por defecto Spring Boot usa `8080`)

URL base:
- `http://localhost:8080/bdproyecto`

Entradas principales:
- Inicio (redirige a Swagger UI): `GET http://localhost:8080/bdproyecto/`
- Swagger UI: `http://localhost:8080/bdproyecto/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/bdproyecto/api-docs`
- Consola H2: `http://localhost:8080/bdproyecto/h2-console`

Prefijos principales de la API:
- `http://localhost:8080/bdproyecto/api/auth`
- `http://localhost:8080/bdproyecto/api/usuarios`
- `http://localhost:8080/bdproyecto/api/security/usuarios`
- `http://localhost:8080/bdproyecto/api/cajas`
- `http://localhost:8080/bdproyecto/api/palets`
- `http://localhost:8080/bdproyecto/api/expediciones`
- `http://localhost:8080/bdproyecto/api/estanterias`
- `http://localhost:8080/bdproyecto/api/terminales`
- `http://localhost:8080/bdproyecto/api/pasillos`
- `http://localhost:8080/bdproyecto/api/ubicaciones`
- `http://localhost:8080/bdproyecto/api/catalogo`

## Rutas de terminales (incluye búsqueda por SN)

- Obtener todos: `GET http://localhost:8080/bdproyecto/api/terminales`
- Obtener por id: `GET http://localhost:8080/bdproyecto/api/terminales/{id}`
- Obtener por SN (`numero_serie`): `GET http://localhost:8080/bdproyecto/api/terminales/sn/{numeroSerie}`
- Crear: `POST http://localhost:8080/bdproyecto/api/terminales`
- Reemplazar completo por SN: `PUT http://localhost:8080/bdproyecto/api/terminales/sn/{numeroSerie}`
- Actualizar parcial por id: `PATCH http://localhost:8080/bdproyecto/api/terminales/{id}`
- Actualizar parcial por SN: `PATCH http://localhost:8080/bdproyecto/api/terminales/sn/{numeroSerie}`
- Eliminar por id: `DELETE http://localhost:8080/bdproyecto/api/terminales/{id}`
- Eliminar por SN: `DELETE http://localhost:8080/bdproyecto/api/terminales/sn/{numeroSerie}`

Ejemplo por SN:

```bash
curl "http://localhost:8080/bdproyecto/api/terminales/sn/SN10001"
```

Ejemplo PATCH por SN:

```bash
curl -X PATCH "http://localhost:8080/bdproyecto/api/terminales/sn/SN10001" \
  -H "Content-Type: application/json" \
  -d '{"estado":"operativo","notas":"Actualizado por SN"}'
```

## Usuarios de autenticación (cargados por `data.sql`)

Estos usuarios se usan para `POST /api/auth/login`:

- `cmartinez` | password: `1234` | rol: `administrador`
- `lgomez` | password: `5678` | rol: `logistica`
- `druiz` | password: `5678` | rol: `trabajador_almacen`
- `alopez` | password: `5678` | rol: `tecnico`

Ejemplo de login:

```bash
curl -X POST "http://localhost:8080/bdproyecto/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"cmartinez","password":"1234"}'
```

## Endpoints principales para frontend (actualizados)

### Auth
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/user`

### Catálogo
- `GET /api/catalogo/modelos-producto`
- `GET /api/catalogo/terminales/marcas`
- `GET /api/catalogo/terminales/marcas/{marca}/modelos`
- `GET /api/catalogo/cajas/modelos/{modelo}/max-capacity`

### Cajas
- `GET /api/cajas`
- `GET /api/cajas/free` (cajas sin palé y sin expedición asociada)
- `GET /api/cajas/free/marca/{marca}` (cajas sin palé, sin expedición, filtradas por marca)
- `GET /api/cajas/{id}`
- `GET /api/cajas/{id}/capacidad`
- `GET /api/cajas/expedicion-detail/{etiqueta}`
- `POST /api/cajas`
- `PUT /api/cajas`
- `PATCH /api/cajas/{id}/palet` (asignar caja existente a un palé)
- `POST /api/cajas/{id}/validar-terminal`
- `POST /api/cajas/{id}/terminales`
- `DELETE /api/cajas/{id}/terminales/{sn}` (desasignar terminal de caja)
- `DELETE /api/cajas/{id}`

Body recomendado para crear caja:

```json
{
  "etiqueta": "1-B-2-2",
  "modeloProducto": "A920",
  "maxCapacity": 213,
  "paletId": 4
}
```

Body recomendado para asignar caja existente a palé:

```json
{
  "paletId": 4
}
```

### Palets
- `GET /api/palets`
- `GET /api/palets/free` (palets sin ubicación)
- `GET /api/palets/{id}`
- `POST /api/palets`
- `PUT /api/palets`
- `PATCH /api/palets/{id}/ubicacion` (asignar o desasignar ubicación)
- `PATCH /api/palets/{id}/descripcion` (editar solo descripción)
- `DELETE /api/palets/{paletId}/cajas/{cajaId}` (desasignar caja de palé)
- `DELETE /api/palets/{id}`

Body recomendado para crear palé:

```json
{
  "descripcion": "Palet recepción Verifone",
  "material": "madera",
  "tipo": "europeo",
  "capacidadMaxCajas": 8,
  "codigoMarca": "PAL-VER-010",
  "ubicacionAlmacenId": 2,
  "cajas": []
}
```

Body para mover/desasignar ubicación de palé:

```json
{
  "ubicacionAlmacenId": 4
}
```

Para desasignar:

```json
{
  "ubicacionAlmacenId": null
}
```

Body para editar descripción de palé:

```json
{
  "descripcion": "Palé PAX A920 reservado para expedición urgente"
}
```

### Ubicaciones / Mapa
- `GET /api/ubicaciones`
- `GET /api/ubicaciones/mapa`
- `GET /api/ubicaciones/mapa?pasilloId={id}`
- `DELETE /api/ubicaciones/{ubicacionId}/palets/{paletId}` (desasignar palé de ubicación)

### Terminales (SN)
- `GET /api/terminales/sn/{numeroSerie}`
- `DELETE /api/terminales/sn/{numeroSerie}`

### Expediciones
- `GET /api/expediciones`
- `GET /api/expediciones/today`
- `GET /api/expediciones/{id}`
- `GET /api/expediciones/nombre/usuario/{name}`
- `GET /api/expediciones/direccion?contiene={texto}`
- `GET /api/expediciones/today/list`
- `GET /api/expediciones/search?...`
- `PUT /api/expediciones`
- `DELETE /api/expediciones/{id}`
