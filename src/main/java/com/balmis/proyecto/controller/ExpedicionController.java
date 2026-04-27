package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Expedicion;
import com.balmis.proyecto.model.Usuario;
import com.balmis.proyecto.service.ExpedicionService;
import com.balmis.proyecto.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Expediciones", description = "API para gestión de expediciones")
@RestController
@RequestMapping("/api/expediciones")
public class ExpedicionController {

    @Autowired
    private ExpedicionService expedicionService;

    @Autowired
    private UsuarioService userService;

    // http://localhost:8080/bdproyecto/h2-console          = Consola de H2
    // http://localhost:8080/bdproyecto                     = /static/index.html
    // http://localhost:8080/bdproyecto/static-noexiste     = gestionado por GlobalExceptionHandler
    // http://localhost:8080/bdproyecto/api/api-noexiste   = gestionado por GlobalExceptionHandler
    // ***************************************************************************
    // CONSULTAS
    // ***************************************************************************
    // http://localhost:8080/bdproyecto/api/expediciones
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todas las expediciones",
            description = "Retorna una lista con todas las expediciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expediciones obtenidas con éxito")
    })
    // ***************************************************************************
    @GetMapping("")
    public ResponseEntity<List<Expedicion>> showAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedicionService.findAll());
    }

    // http://localhost:8080/bdproyecto/api/expediciones/2
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener expedicion por ID",
            description = "Retorna una expedicion específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expedicion encontrada"),
        @ApiResponse(responseCode = "404", description = "Expedicion no encontrada", content = @Content())
    })
    // ***************************************************************************
    @GetMapping("/{id}")
    public ResponseEntity<Expedicion> showById(@PathVariable int id) {
        Expedicion usu = expedicionService.findById(id);

        if (usu == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(usu);
        }
    }

    // http://localhost:8080/bdproyecto/api/expediciones/dep/Ventas
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener expediciones por nombre de usuario",
            description = "Retorna una lista de expediciones específicas basado en el nombre de usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expediciones encontradas"),})
    // ***************************************************************************
    @GetMapping("/nombre/usuario/{name}")
    public ResponseEntity<List<Expedicion>> showByNombreUsuario(@PathVariable String name) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedicionService.findLikeNombre(name));
    }

    // http://localhost:8080/bdproyecto/api/expediciones/nombre?contiene=pe
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener expediciones por direccion",
            description = "Retorna una lista de expediciones específicas basado en la direccion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expediciones encontradas"),})
    // ***************************************************************************
    @GetMapping("/direccion")
    public ResponseEntity<List<Expedicion>> showBydireccion(@RequestParam("contiene") String direccion) {
        return ResponseEntity.ok(expedicionService.findLikeDireccion(direccion));
    }

    // http://localhost:8080/bdproyecto/api/expediciones/count
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener el número de expediciones existentes",
            description = "Retorna la cantidad de expediciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de expediciones obtenidas con éxito", content = @Content())
    })
    // ***************************************************************************
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> count() {

        ResponseEntity<Map<String, Object>> response = null;

        Map<String, Object> map = new HashMap<>();
        map.put("count", expedicionService.count());

        response = ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(map);

        return response;
    }
    
    // http://localhost:8080/bdproyecto/api/expediciones/today
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todas las expediciones del dia de hoy",
            description = "Retorna una lista con todas las expediciones con el dia de hoy")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Expediciones obtenidas con éxito")
    })
    // ***************************************************************************
    @GetMapping("/today")
    public ResponseEntity<List<Expedicion>> showAllToday() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedicionService.findAllToday());
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************
    // ****************************************************************************
    // INSERT (POST)    
    // http://localhost:8080/bdproyecto/api/empleados
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Crear una nueva expedicion",
            description = "Registra una nueva expedicion en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expedicion creada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    // ***************************************************************************
    @PostMapping("/{idUser}")
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody Expedicion expedicion, @PathVariable int idUser) {

        //System.out.println(expedicion);
        ResponseEntity<Map<String, Object>> response;

        if (expedicion == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            Usuario user = userService.findById(idUser);

            if (expedicion.getDireccionDestino() == null || expedicion.getDireccionDestino().trim().isEmpty()
                    || expedicion.getPaquetes() < 0
                    || expedicion.getPeso() < 0) {

                Map<String, Object> map = new HashMap<>();
                String error = "";
                if (expedicion.getDireccionDestino() == null || expedicion.getDireccionDestino().trim().isEmpty()) {
                    if (!error.equals("")) {
                        error += " - ";
                    }
                    error += "El campo 'direccion destino' es obligatorio";
                }
                if (expedicion.getPaquetes() < 0) {
                    if (!error.equals("")) {
                        error += " - ";
                    }
                    error += "El campo 'paquetes' debe ser positivo";
                }
                if (expedicion.getPeso() < 0) {
                    if (!error.equals("")) {
                        error += " - ";
                    }
                    error += "El campo 'peso' debe ser positivo";
                }
                map.put("error", error);

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {

                if (user == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("error", "El id del usuario es invalido o no existe el usuario");

                    response = ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(map);
                } else {
                    expedicion.setUsuario(user);
                    
                    Expedicion objPost = expedicionService.save(expedicion);

                    Map<String, Object> map = new HashMap<>();
                    map.put("mensaje", "Expedicion creado con éxito");
                    map.put("insertRealizado", objPost);

                    response = ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(map);
                }
            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/bdproyecto/api/expediciones
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar una expedicion existente",
            description = "Reemplaza completamente los datos de una expedicion identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expedicion actualizada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Expedicion no encontrada", content = @Content())
    })
    // ***************************************************************************
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> update(
            @Valid @RequestBody Expedicion exp) {

        ResponseEntity<Map<String, Object>> response;

        if (exp == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            int id = exp.getId();
            Expedicion existingObj = expedicionService.findById(id);

            if (existingObj == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Expedicion no encontrada");
                map.put("id", id);

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {

                // Actualizar campos si están presentes
                if (exp.getFechaRecepcion() != null) {
                    existingObj.setFechaRecepcion(exp.getFechaRecepcion());
                }

                if (exp.getDireccionDestino() != null) {
                    existingObj.setDireccionDestino(exp.getDireccionDestino());
                }

                if (exp.getPaquetes() >= 0) {
                    existingObj.setPaquetes(exp.getPaquetes());
                }

                if (exp.getPeso() >= 0) {
                    existingObj.setPeso(exp.getPeso());
                }

                if (exp.getNotas() != null) {
                    existingObj.setNotas(exp.getNotas());
                }
                //existingObj.setFechaModificacion(LocalDateTime.now());
                Expedicion objPut = expedicionService.save(existingObj);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Expedicion actualizada con éxito");
                map.put("updateRealizado", objPut);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT) - ESPECIALES
    //   
    // SWAGGER
    @Operation(summary = "Actualizar el estado a 'en-transito' de una expedicion existente",
            description = "Reemplaza el estado de una expedicion identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expedicion actualizada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Expedicion no encontrada", content = @Content())
    })
    // ***************************************************************************
    @PutMapping("/{id}/en-transito")
    public ResponseEntity<Expedicion> pasarAEnTransito(@PathVariable int id) {
        return ResponseEntity.ok(expedicionService.marcarEnTransito(id));
    }

    // SWAGGER
    @Operation(summary = "Actualizar el estado a 'recbida' de una expedicion existente",
            description = "Reemplaza el estado de una expedicion identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expedicion actualizada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Expedicion no encontrada", content = @Content())
    })
    // ***************************************************************************
    @PutMapping("/{id}/recibida")
    public ResponseEntity<Expedicion> pasarARecibida(@PathVariable int id) {
        return ResponseEntity.ok(expedicionService.marcarRecibida(id));
    }

    // ****************************************************************************
    // DELETE
    // http://localhost:8080/bdproyecto/api/expediciones/16
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Eliminar expedicion por ID",
            description = "Elimina una expedicion específica del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expedicion eliminada con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Expedicion no encontrada", content = @Content())
    })
    // ***************************************************************************
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {

        ResponseEntity<Map<String, Object>> response;

        Expedicion existingObj = expedicionService.findById(id);
        if (existingObj == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Expedicion no encontrada");
            map.put("id", id);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            expedicionService.deleteById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Expedicion eliminado con éxito");
            map.put("deletedRealizado", existingObj);

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

    // ***************************************************************************
    // OPERACIONES ESPECIALES DE ACTUALIZACIÓN
    // ***************************************************************************
    // http://localhost:8080/bdproyecto/api/expediciones/1/reasignar/usuario/3
    // ***************************************************************************
    // SWAGGER
    @Operation(summary = "Reasignar un usuario a la expedicion",
            description = "Reemplaza un usuario por otro identificando la ID de la expedicion y el ID del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Expedicion actualizada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Expedicion no encontrada", content = @Content())
    })
    // ***************************************************************************
    @PutMapping("/{expId}/reasignar/usuario/{usuId}")
    public ResponseEntity<Map<String, Object>> asignarRole(
            @PathVariable int expId,
            @PathVariable int usuId) {

        ResponseEntity<Map<String, Object>> response;

        Expedicion emp = expedicionService.reasignarUsuario(usuId, expId);

        if (emp != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Usuario asignado con éxito");
            map.put("updateRealizado", emp);

            response = ResponseEntity.status(HttpStatus.OK).body(map);

        } else {

            Map<String, Object> map = new HashMap<>();
            map.put("error", "Expedicion o Usuario no existe");
            map.put("empId", expId);
            map.put("depId", usuId);

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        return response;
    }

    /*// http://localhost:8080/bdproyecto/api/empleados/1/desasignar/departamento
    @PutMapping("/empleados/{empId}/desasignar/departamento")
    public ResponseEntity<Map<String, Object>> desasignarRole(@PathVariable int empId) {
        
        ResponseEntity<Map<String, Object>> response;
        
       
        Expedicion emp = expedicionService.desasignarUsuario(empId);
            
        if (emp!=null) {
            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Departamento desasignado con éxito");
            map.put("updateRealizado", emp);
            
            response = ResponseEntity.status(HttpStatus.OK).body(map);
            
        } else {
            
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Empleado no existe");
            map.put("empId", empId);
            
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
        
        return response;
    } */
}
