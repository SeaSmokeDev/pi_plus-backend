# pi_plus-backend

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
