package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.model.MaterialPalet;
import com.balmis.proyecto.model.TipoPalet;
import com.balmis.proyecto.model.UbicacionAlmacen;
import com.balmis.proyecto.model.dtos.ActualizarDescripcionPaletRequestDto;
import com.balmis.proyecto.model.dtos.ActualizarUbicacionPaletRequestDto;
import com.balmis.proyecto.model.dtos.PaletCreateRequestDto;
import com.balmis.proyecto.service.PaletService;
import com.balmis.proyecto.service.UbicacionAlmacenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Palets", description = "API para gestión de palets")
@RestController
@RequestMapping("/api/palets")
public class PaletController {
    
     @Autowired
    private PaletService paletService;

    @Autowired
    private UbicacionAlmacenService ubicacionAlmacenService;

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

    @Operation(summary = "Obtener palets libres",
            description = "Retorna palets sin ubicación asignada (ubicacion_almacen_id IS NULL)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Palets libres obtenidos con éxito")
    })
    @GetMapping("/free")
    public ResponseEntity<List<Palet>> showPaletsFree() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(paletService.findFree());
    }

    @Operation(summary = "Crear un nuevo palet",
            description = "Registra un nuevo palet en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Palet creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createPalet(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear un palet",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "PaletCreateRequest",
                                    value = """
                                            {
                                              "descripcion": "Palet recepción Verifone",
                                              "material": "madera",
                                              "tipo": "europeo",
                                              "capacidadMaxCajas": 8,
                                              "codigoMarca": "PAL-VER-010",
                                              "ubicacionAlmacenId": 2
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody PaletCreateRequestDto request) {

        if (request == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        if (request.getDescripcion() == null || request.getDescripcion().trim().isEmpty()
                || request.getMaterial() == null || request.getMaterial().trim().isEmpty()
                || request.getTipo() == null || request.getTipo().trim().isEmpty()) {

            Map<String, Object> map = new HashMap<>();
            map.put("error", "Los campos 'descripcion', 'material' y 'tipo' son obligatorios");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        MaterialPalet material;
        TipoPalet tipo;
        try {
            material = MaterialPalet.valueOf(request.getMaterial().trim().toLowerCase());
            tipo = TipoPalet.valueOf(request.getTipo().trim().toLowerCase());
        } catch (IllegalArgumentException ex) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Valores inválidos para 'material' o 'tipo'. Valores permitidos: material[plastico,madera], tipo[americano,europeo]");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        UbicacionAlmacen ubicacionAlmacen = null;
        if (request.getUbicacionAlmacenId() != null) {
            ubicacionAlmacen = ubicacionAlmacenService.findById(request.getUbicacionAlmacenId());
            if (ubicacionAlmacen == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "La 'ubicacionAlmacenId' no existe");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            }
        }

        Palet palet = new Palet();
        palet.setDescripcion(request.getDescripcion());
        palet.setMaterial(material);
        palet.setTipo(tipo);
        palet.setCapacidadMaxCajas(request.getCapacidadMaxCajas() == null ? 8 : request.getCapacidadMaxCajas());
        palet.setCodigoMarca(request.getCodigoMarca());
        palet.setUbicacionAlmacen(ubicacionAlmacen);

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
        map.put("success", true);
        map.put("mensaje", "Palet eliminado con éxito");
        map.put("paletId", id);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Operation(summary = "Desasignar una caja de un palé",
            description = "Elimina la asociación entre una caja concreta y un palé concreto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Caja desasignada del palé con éxito"),
        @ApiResponse(responseCode = "400", description = "Error de validación de negocio", content = @Content())
    })
    @DeleteMapping("/{paletId}/cajas/{cajaId}")
    public ResponseEntity<Map<String, Object>> desasignarCajaDePalet(
            @PathVariable int paletId,
            @PathVariable int cajaId) {
        Map<String, Object> result = paletService.desasignarCajaDePalet(paletId, cajaId);
        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @Operation(summary = "Actualizar solo la ubicación de un palé",
            description = "Permite mover un palé a otra ubicación del almacén usando su id. Si ubicacionAlmacenId es null, desasigna la ubicación actual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ubicación del palé actualizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Error de validación de negocio", content = @Content())
    })
    @PatchMapping("/{id}/ubicacion")
    public ResponseEntity<Map<String, Object>> actualizarUbicacionPalet(
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nueva ubicación del palé",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                @ExampleObject(
                                        name = "MoverPalet",
                                        value = """
                                                {
                                                  "ubicacionAlmacenId": 4
                                                }
                                                """
                                ),
                                @ExampleObject(
                                        name = "DesasignarUbicacion",
                                        value = """
                                                {
                                                  "ubicacionAlmacenId": null
                                                }
                                                """
                                )
                            }
                    )
            )
            @RequestBody ActualizarUbicacionPaletRequestDto request) {
        Map<String, Object> result = paletService.actualizarUbicacionPalet(
                id,
                request != null ? request.getUbicacionAlmacenId() : null
        );

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @Operation(summary = "Actualizar solo la descripción de un palé",
            description = "Permite editar únicamente el campo descripción del palé.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Descripción del palé actualizada con éxito"),
        @ApiResponse(responseCode = "400", description = "Error de validación de negocio", content = @Content())
    })
    @PatchMapping("/{id}/descripcion")
    public ResponseEntity<Map<String, Object>> actualizarDescripcionPalet(
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nueva descripción del palé",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ActualizarDescripcionPalet",
                                    value = """
                                            {
                                              "descripcion": "Palé PAX A920 reservado para expedición urgente"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody ActualizarDescripcionPaletRequestDto request) {

        Map<String, Object> result = paletService.actualizarDescripcionPalet(
                id,
                request != null ? request.getDescripcion() : null
        );

        if (Boolean.TRUE.equals(result.get("success"))) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
}
