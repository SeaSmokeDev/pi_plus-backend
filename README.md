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
- `http://localhost:8080/bdproyecto/api/cajas/cajas`
- `http://localhost:8080/bdproyecto/api/palets`
- `http://localhost:8080/bdproyecto/api/expediciones`
- `http://localhost:8080/bdproyecto/api/estanterias`
- `http://localhost:8080/bdproyecto/api/terminales/terminales`
- `http://localhost:8080/bdproyecto/api/pasillos`
- `http://localhost:8080/bdproyecto/api/ubicaciones`

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
