package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.service.PaletService;
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

@Tag(name = "palets", description = "API para gestión de palets")
@RestController
@RequestMapping("/api/palets")
public class PaletController {
    
     @Autowired
    private PaletService paletService;

    @Operation(summary = "Obtener todos los palets",
            description = "Retorna una lista con todos los palets disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Palets obtenidos con éxito")
    })
    @GetMapping("")
    public ResponseEntity<List<Palet>> showPalets() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paletService.findAll());
    }

    @Operation(summary = "Obtener palet por ID",
            description = "Retorna un palet específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Palet encontrado"),
        @ApiResponse(responseCode = "404", description = "Palet no encontrado", content = @Content())
    })
    @GetMapping("/{id}")
    public ResponseEntity<Palet> detailsPalet(@PathVariable int id) {
        Palet palet = paletService.findById(id);

        if (palet == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(palet);
    }

    @Operation(summary = "Obtener palets mayores de un ID",
            description = "Retorna una lista con todos los palets con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Palets obtenidos con éxito")
    })
    @GetMapping("/mayor/{id}")
    public ResponseEntity<List<Palet>> showPaletsMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paletService.findByIdGrThan(id));
    }

    @Operation(summary = "Obtener el número de palets existentes",
            description = "Retorna la cantidad de palets")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de palets obtenidos con éxito", content = @Content())
    })
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countPalets() {
        Map<String, Object> map = new HashMap<>();
        map.put("palets", paletService.count());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(map);
    }

    @Operation(summary = "Crear un nuevo palet",
            description = "Registra un nuevo palet en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Palet creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createPalet(@Valid @RequestBody Palet palet) {

        if (palet == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        if (palet.getDescripcion() == null || palet.getDescripcion().trim().isEmpty()
                || palet.getMaterial() == null
                || palet.getTipo() == null) {

            Map<String, Object> map = new HashMap<>();
            map.put("error", "Los campos 'descripcion', 'material' y 'tipo' son obligatorios");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        Palet paletPost = paletService.save(palet);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Palet creado con éxito");
        map.put("insertPalet", paletPost);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(map);
    }

    @Operation(summary = "Actualizar un palet existente",
            description = "Actualiza los datos de un palet identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Palet actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Palet no encontrado", content = @Content())
    })
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> updatePalet(@Valid @RequestBody Palet paletUpdate) {

        if (paletUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        int id = paletUpdate.getId();
        Palet existingPalet = paletService.findById(id);

        if (existingPalet == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Palet no encontrado");
            map.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        if (paletUpdate.getDescripcion() != null) {
            existingPalet.setDescripcion(paletUpdate.getDescripcion());
        }
        if (paletUpdate.getMaterial() != null) {
            existingPalet.setMaterial(paletUpdate.getMaterial());
        }
        if (paletUpdate.getTipo() != null) {
            existingPalet.setTipo(paletUpdate.getTipo());
        }
        if (paletUpdate.getCapacidadMaxCajas() >= 0) {
            existingPalet.setCapacidadMaxCajas(paletUpdate.getCapacidadMaxCajas());
        }
        if (paletUpdate.getCodigoMarca() != null) {
            existingPalet.setCodigoMarca(paletUpdate.getCodigoMarca());
        }

        existingPalet.setUbicacionAlmacen(paletUpdate.getUbicacionAlmacen());

        Palet paletPut = paletService.save(existingPalet);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Palet actualizado con éxito");
        map.put("updatedPalet", paletPut);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Operation(summary = "Eliminar palet por ID",
            description = "Elimina un palet específico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Palet eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Palet no encontrado", content = @Content())
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePalet(@PathVariable int id) {
        Palet existingPalet = paletService.findById(id);

        if (existingPalet == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Palet no encontrado");
            map.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        paletService.deleteById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Palet eliminado con éxito");
        map.put("deletedPalet", existingPalet);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
    
}
