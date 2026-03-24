package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.service.CajaService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cajas", description = "API para gestión de cajas")
@RestController
@RequestMapping("/api/cajas")
public class CajaController {

    @Autowired
    private CajaService cajaService;

    // ***************************************************************************
    // CONSULTAS
    // ***************************************************************************
    // http://localhost:8080/apirest/cajas
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todos los cajas",
            description = "Retorna una lista con todos los cajas disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cajas obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("")
    public ResponseEntity<List<Caja>> showCajas() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cajaService.findAll());
    }

    // http://localhost:8080/apirest/cajas/2
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener caja por ID",
            description = "Retorna un caja específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Caja encontrado"),
        @ApiResponse(responseCode = "404", description = "Caja no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/{id}")
    public ResponseEntity<Caja> detailsCaja(@PathVariable int id) {
        Caja caja = cajaService.findById(id);

        if (caja == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(caja);
        }
    }

    // http://localhost:8080/apirest/cajas/mayor/7
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener cajas mayores de un ID",
            description = "Retorna una lista con todos los cajas con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cajas obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("/mayor/{id}")
    public ResponseEntity<List<Caja>> showCajasMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cajaService.findByIdGrThan(id));
    }

    // http://localhost:8080/apirest/cajas/count
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener el número de cajas existentes",
            description = "Retorna la cantidad de cajas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de cajas obtenidos con éxito", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countCajas() {

        ResponseEntity<Map<String, Object>> response = null;

        Map<String, Object> map = new HashMap<>();
        map.put("cajas", cajaService.count());

        response = ResponseEntity
                .status(HttpStatus.OK)
                .body(map);

        return response;
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************
    // ****************************************************************************
    // INSERT (POST)    
    // http://localhost:8080/apirest/cajas
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Crear un nuevo Caja",
            description = "Registra un nuevo Caja en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Caja creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    // ***************************************************************************    

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createCaja(
            @Valid @RequestBody Caja caja) {

        ResponseEntity<Map<String, Object>> response;

        if (caja == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (caja.getEtiqueta() == null || caja.getEtiqueta().trim().isEmpty()
                    || ( caja.getModeloProducto() == null) 
                    ) {

                Map<String, Object> map = new HashMap<>();
                map.put("error", "Los campos 'etiqueta' , 'modelo producto' son obligatorios");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {
                System.out.println(caja);
                Caja cajaPost = cajaService.save(caja);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Caja creado con éxito");
                map.put("insertCaja", cajaPost);

                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/apirest/cajas
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar un Caja existente",
            description = "Reemplaza completamente los datos de un Caja identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Caja actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Caja no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> updateProd(
            @Valid @RequestBody Caja caja) {

        ResponseEntity<Map<String, Object>> response;

        if (caja == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            int id = caja.getId();
            Caja existingCaja = cajaService.findById(id);

            if (existingCaja == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Caja no encontrado");
                map.put("id", id);

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {

                // Actualizar campos si están presentes
                if (caja.getEtiqueta() != null) {
                    existingCaja.setEtiqueta(caja.getEtiqueta());
                }
                 if (caja.getModeloProducto() != null) {
                    existingCaja.setModeloProducto(caja.getModeloProducto());
                }
                if (caja.getPalet() != null) {
                    existingCaja.setPalet(caja.getPalet());
                }

                Caja cajaPut = cajaService.save(existingCaja);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Caja actualizado con éxito");
                map.put("updatedCaja", cajaPut);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // DELETE
    // http://localhost:8080/apirest/cajas/16
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Eliminar Caja por ID",
            description = "Elimina un Caja específico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Caja eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Caja no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProd(@PathVariable int id) {

        ResponseEntity<Map<String, Object>> response;

        Caja existingProd = cajaService.findById(id);
        if (existingProd == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Caja no encontrado");
            map.put("id", id);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            cajaService.deleteById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Caja eliminado con éxito");
            map.put("deletedprod", existingProd);

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

}
