package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.UbicacionAlmacen;
import com.balmis.proyecto.service.UbicacionAlmacenService;
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

@Tag(name = "Ubicaciones", description = "API para gestión de ubicaciones en almacen")
@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionesAlmacenController {
    
    @Autowired
    private UbicacionAlmacenService ubicacionAlmacenService;

    @Operation(summary = "Obtener todas las ubicaciones",
            description = "Retorna una lista con todas las ubicaciones de almacén")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicaciones obtenidas con éxito")
    })
    @GetMapping("")
    public ResponseEntity<List<UbicacionAlmacen>> showUbicaciones() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ubicacionAlmacenService.findAll());
    }

    @Operation(summary = "Obtener ubicación por ID",
            description = "Retorna una ubicación específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación encontrada"),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionAlmacen> detailsUbicacion(@PathVariable int id) {
        UbicacionAlmacen ubicacion = ubicacionAlmacenService.findById(id);

        if (ubicacion == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(ubicacion);
    }

    @Operation(summary = "Obtener ubicaciones mayores de un ID",
            description = "Retorna una lista con todas las ubicaciones con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicaciones obtenidas con éxito")
    })
    @GetMapping("/mayor/{id}")
    public ResponseEntity<List<UbicacionAlmacen>> showUbicacionesMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ubicacionAlmacenService.findByIdGrThan(id));
    }

    @Operation(summary = "Obtener el número de ubicaciones",
            description = "Retorna la cantidad de ubicaciones de almacén")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de ubicaciones obtenido con éxito", content = @Content())
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countUbicaciones() {
        Map<String, Object> map = new HashMap<>();
        map.put("ubicaciones", ubicacionAlmacenService.count());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(map);
    }

    @Operation(summary = "Crear una nueva ubicación",
            description = "Registra una nueva ubicación en el almacén")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ubicación creada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createUbicacion(
            @Valid @RequestBody UbicacionAlmacen ubicacion) {

        if (ubicacion == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        if (ubicacion.getReferencia() == null || ubicacion.getReferencia().trim().isEmpty()
                || ubicacion.getEstanteria() == null
                || ubicacion.getNivel() == null || ubicacion.getNivel() <= 0) {

            Map<String, Object> map = new HashMap<>();
            map.put("error", "Los campos 'referencia', 'estanteria' y 'nivel' son obligatorios");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        UbicacionAlmacen ubicacionPost = ubicacionAlmacenService.save(ubicacion);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Ubicación creada con éxito");
        map.put("insertUbicacion", ubicacionPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @Operation(summary = "Actualizar una ubicación existente",
            description = "Actualiza los datos de una ubicación identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación actualizada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content())
    })
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> updateUbicacion(
            @Valid @RequestBody UbicacionAlmacen ubicacionUpdate) {

        if (ubicacionUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        int id = ubicacionUpdate.getId();
        UbicacionAlmacen existingUbicacion = ubicacionAlmacenService.findById(id);

        if (existingUbicacion == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Ubicación no encontrada");
            map.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        if (ubicacionUpdate.getReferencia() != null) {
            existingUbicacion.setReferencia(ubicacionUpdate.getReferencia());
        }
        if (ubicacionUpdate.getEstanteria() != null) {
            existingUbicacion.setEstanteria(ubicacionUpdate.getEstanteria());
        }
        if (ubicacionUpdate.getNivel() != null && ubicacionUpdate.getNivel() > 0) {
            existingUbicacion.setNivel(ubicacionUpdate.getNivel());
        }

        UbicacionAlmacen ubicacionPut = ubicacionAlmacenService.save(existingUbicacion);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Ubicación actualizada con éxito");
        map.put("updatedUbicacion", ubicacionPut);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Operation(summary = "Eliminar ubicación por ID",
            description = "Elimina una ubicación específica del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación eliminada con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Ubicación no encontrada", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUbicacion(@PathVariable int id) {
        UbicacionAlmacen existingUbicacion = ubicacionAlmacenService.findById(id);

        if (existingUbicacion == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Ubicación no encontrada");
            map.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        ubicacionAlmacenService.deleteById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Ubicación eliminada con éxito");
        map.put("deletedUbicacion", existingUbicacion);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
