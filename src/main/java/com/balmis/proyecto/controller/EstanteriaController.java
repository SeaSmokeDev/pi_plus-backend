package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Estanteria;
import com.balmis.proyecto.service.EstanteriaService;
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

@Tag(name = "Estanterias", description = "API para gestión de estanterias")
@RestController
@RequestMapping("/api/estanterias")
public class EstanteriaController {
    
    @Autowired
    private EstanteriaService estanteriaService;

    @Operation(summary = "Obtener todas las estanterías",
            description = "Retorna una lista con todas las estanterías del almacén")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estanterías obtenidas con éxito")
    })
    @GetMapping("")
    public ResponseEntity<List<Estanteria>> showEstanterias() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estanteriaService.findAll());
    }

    @Operation(summary = "Obtener estantería por ID",
            description = "Retorna una estantería específica basada en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estantería encontrada"),
        @ApiResponse(responseCode = "404", description = "Estantería no encontrada", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<Estanteria> detailsEstanteria(@PathVariable int id) {
        Estanteria estanteria = estanteriaService.findById(id);

        if (estanteria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(estanteria);
    }

    @Operation(summary = "Obtener estanterías mayores de un ID",
            description = "Retorna una lista con todas las estanterías con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estanterías obtenidas con éxito")
    })
    @GetMapping("/mayor/{id}")
    public ResponseEntity<List<Estanteria>> showEstanteriasMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(estanteriaService.findByIdGrThan(id));
    }

    @Operation(summary = "Obtener el número de estanterías",
            description = "Retorna la cantidad de estanterías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de estanterías obtenido con éxito", content = @Content())
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countEstanterias() {
        Map<String, Object> map = new HashMap<>();
        map.put("estanterias", estanteriaService.count());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(map);
    }

    @Operation(summary = "Crear una nueva estantería",
            description = "Registra una nueva estantería en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Estantería creada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createEstanteria(@Valid @RequestBody Estanteria estanteria) {

        if (estanteria == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        if (estanteria.getCodigo() == null || estanteria.getCodigo().trim().isEmpty()
                || estanteria.getNivelesMaximos() <= 0
                || estanteria.getCapacidadNivel() == null || estanteria.getCapacidadNivel() <= 0
                || estanteria.getPasillo() == null) {

            Map<String, Object> map = new HashMap<>();
            map.put("error", "Los campos 'codigo', 'niveles_maximos', 'capacidad_nivel' y 'pasillo' son obligatorios");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        Estanteria estanteriaPost = estanteriaService.save(estanteria);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Estantería creada con éxito");
        map.put("insertEstanteria", estanteriaPost);

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @Operation(summary = "Actualizar una estantería existente",
            description = "Actualiza los datos de una estantería identificada por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estantería actualizada con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Estantería no encontrada", content = @Content())
    })
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> updateEstanteria(@Valid @RequestBody Estanteria estanteriaUpdate) {

        if (estanteriaUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        int id = estanteriaUpdate.getId();
        Estanteria existingEstanteria = estanteriaService.findById(id);

        if (existingEstanteria == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Estantería no encontrada");
            map.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        if (estanteriaUpdate.getCodigo() != null) {
            existingEstanteria.setCodigo(estanteriaUpdate.getCodigo());
        }
        if (estanteriaUpdate.getNivelesMaximos() > 0) {
            existingEstanteria.setNivelesMaximos(estanteriaUpdate.getNivelesMaximos());
        }
        if (estanteriaUpdate.getCapacidadNivel() != null && estanteriaUpdate.getCapacidadNivel() > 0) {
            existingEstanteria.setCapacidadNivel(estanteriaUpdate.getCapacidadNivel());
        }
        if (estanteriaUpdate.getPasillo() != null) {
            existingEstanteria.setPasillo(estanteriaUpdate.getPasillo());
        }

        Estanteria estanteriaPut = estanteriaService.save(existingEstanteria);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Estantería actualizada con éxito");
        map.put("updatedEstanteria", estanteriaPut);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Operation(summary = "Eliminar estantería por ID",
            description = "Elimina una estantería específica del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estantería eliminada con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Estantería no encontrada", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteEstanteria(@PathVariable int id) {
        Estanteria existingEstanteria = estanteriaService.findById(id);

        if (existingEstanteria == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Estantería no encontrada");
            map.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        estanteriaService.deleteById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Estantería eliminada con éxito");
        map.put("deletedEstanteria", existingEstanteria);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    
}
