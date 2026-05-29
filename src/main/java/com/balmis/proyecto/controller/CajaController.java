package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Caja;
import com.balmis.proyecto.model.Palet;
import com.balmis.proyecto.model.dtos.AsignarPaletCajaRequestDto;
import com.balmis.proyecto.model.dtos.AsociarTerminalesRequest;
import com.balmis.proyecto.model.dtos.AsociarTerminalesResponse;
import com.balmis.proyecto.model.dtos.CajaCapacidadDto;
import com.balmis.proyecto.model.dtos.CajaCreateRequestDto;
import com.balmis.proyecto.model.dtos.CajaExpedicionDetailDTO;
import com.balmis.proyecto.model.dtos.MotivoValidacionTerminal;
import com.balmis.proyecto.model.dtos.ValidarTerminalRequest;
import com.balmis.proyecto.model.dtos.ValidarTerminalResponse;
import com.balmis.proyecto.service.CajaTerminalService;
import com.balmis.proyecto.service.CajaService;
import com.balmis.proyecto.service.CatalogoService;
import com.balmis.proyecto.service.PaletService;
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
import java.util.Optional;
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

@Tag(name = "Cajas", description = "API para gestión de cajas")
@RestController
@RequestMapping("/api/cajas")
public class CajaController {

    @Autowired
    private CajaService cajaService;
    
    @Autowired
    private CatalogoService catalogoService;
    
    @Autowired
    private CajaTerminalService cajaTerminalService;

    @Autowired
    private PaletService paletService;

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

    @Operation(summary = "Obtener cajas sin palé asignado",
            description = "Retorna únicamente las cajas cuyo palet_id es NULL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cajas libres obtenidas con éxito")
    })
    @GetMapping("/free")
    public ResponseEntity<List<Caja>> showCajasFree() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cajaService.findSinPalet());
    }

    @Operation(summary = "Obtener cajas sin palé por marca",
            description = "Retorna cajas con palet_id NULL filtradas por marca (prefijo de modeloProducto, ej: 'Verifone V240')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cajas libres por marca obtenidas con éxito"),
        @ApiResponse(responseCode = "400", description = "Marca inválida", content = @Content())
    })
    @GetMapping("/free/marca/{marca}")
    public ResponseEntity<?> showCajasFreeByMarca(@PathVariable String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El parámetro 'marca' es obligatorio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cajaService.findSinPaletByMarca(marca.trim()));
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

    @Operation(summary = "Obtener ocupación y capacidad máxima de una caja",
            description = "Retorna la cantidad de terminales actuales y la capacidad máxima para mostrar formato tipo 3/90")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumen de capacidad obtenido con éxito"),
        @ApiResponse(responseCode = "404", description = "Caja no encontrada", content = @Content())
    })
    @GetMapping("/{id}/capacidad")
    public ResponseEntity<CajaCapacidadDto> getCapacidadCaja(@PathVariable int id) {
        CajaCapacidadDto dto = cajaService.getCapacidadCajaById(id);
        if (dto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener caja para detalle de expediciones",
            description = "Retorna una caja con detalle de la misma junto con detalle de los terminales que lleva dentro ")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Caja obtenida con éxito", content = @Content())
    })
    // ***************************************************************************
    @GetMapping("/expedicion-detail/{etiqueta}")
    public ResponseEntity<CajaExpedicionDetailDTO> getCajaForExpedicionDetail(
            @PathVariable String etiqueta
    ) {
        CajaExpedicionDetailDTO dto = cajaService.findCajaExpedicionDetailByEtiqueta(etiqueta);

        if (dto == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos para crear una caja",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CajaCreateRequest",
                                    value = """
                                            {
                                              "etiqueta": "P2E4N4C3",
                                              "modeloProducto": "V240",
                                              "maxCapacity": 300,
                                              "paletId": 5
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody CajaCreateRequestDto request) {

        ResponseEntity<Map<String, Object>> response;

        if (request == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (request.getEtiqueta() == null || request.getEtiqueta().trim().isEmpty()
                    || request.getModeloProducto() == null
                    || request.getMaxCapacity() == null) {

                Map<String, Object> map = new HashMap<>();
                map.put("error", "Los campos 'etiqueta', 'modelo producto' y 'max_capacity' son obligatorios");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else if (request.getMaxCapacity() <= 0) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "El campo 'max_capacity' debe ser mayor que 0");
                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else if (!catalogoService.existeModeloProducto(request.getModeloProducto())) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "El 'modelo_producto' no existe en el catálogo real de terminales");
                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {
                Palet palet = null;
                if (request.getPaletId() != null) {
                    palet = paletService.findById(request.getPaletId());
                    if (palet == null) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("error", "El 'paletId' no existe");
                        response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                        return response;
                    }
                }

                Caja caja = new Caja();
                caja.setEtiqueta(request.getEtiqueta());
                caja.setMaxCapacity(request.getMaxCapacity());
                caja.setPalet(palet);
                Optional<String> modeloCanonico = catalogoService.resolverModeloProductoCanonico(request.getModeloProducto());
                modeloCanonico.ifPresent(caja::setModeloProducto);

                Caja cajaPost = cajaService.save(caja);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Caja creado con éxito");
                map.put("insertCaja", cajaPost);
                map.put("paletId", cajaPost.getPalet() != null ? cajaPost.getPalet().getId() : null);

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
                    Optional<String> modeloCanonico = catalogoService.resolverModeloProductoCanonico(caja.getModeloProducto());
                    if (modeloCanonico.isEmpty()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("error", "El 'modelo_producto' no existe en el catálogo real de terminales");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                    }
                    existingCaja.setModeloProducto(modeloCanonico.get());
                }
                if (caja.getMaxCapacity() != null) {
                    if (caja.getMaxCapacity() <= 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("error", "El campo 'max_capacity' debe ser mayor que 0");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
                    }
                    existingCaja.setMaxCapacity(caja.getMaxCapacity());
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
    
    @Operation(summary = "Validar un terminal para asociarlo a una caja",
            description = "Valida SN, existencia de caja/terminal, compatibilidad de modelo, estado y asociación previa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validación ejecutada con éxito"),
        @ApiResponse(responseCode = "400", description = "JSON inválido", content = @Content())
    })
    @PostMapping("/{id}/validar-terminal")
    public ResponseEntity<ValidarTerminalResponse> validarTerminalParaCaja(
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SN del terminal a validar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "sn": "SN10001"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody ValidarTerminalRequest request) {
        ValidarTerminalResponse response = cajaTerminalService.validarTerminalParaCaja(id, request != null ? request.getSn() : null);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Asociar terminales a una caja (operación final)",
            description = "Revalida todos los SN y asocia en una operación transaccional de todo o nada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminales asociados con éxito"),
        @ApiResponse(responseCode = "400", description = "Errores de validación de negocio")
    })
    @PostMapping("/{id}/terminales")
    public ResponseEntity<AsociarTerminalesResponse> asociarTerminalesACaja(
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Lista de números de serie a asociar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "sns": ["SN10001", "SN10002", "SN10003"]
                                            }
                                            """
                            )
                    )
            )
            @RequestBody AsociarTerminalesRequest request) {
        AsociarTerminalesResponse response = cajaTerminalService.asociarTerminalesACaja(id, request != null ? request.getSns() : null);
        if (!response.isSuccess() && response.getMotivo() == MotivoValidacionTerminal.ALGUNOS_TERMINALES_INVALIDOS) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (!response.isSuccess() && response.getMotivo() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desasignar terminal de una caja",
            description = "Elimina la asociación entre una caja y un terminal por su número de serie")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal desasignado con éxito"),
        @ApiResponse(responseCode = "400", description = "Error de validación de negocio")
    })
    @DeleteMapping("/{id}/terminales/{sn}")
    public ResponseEntity<Map<String, Object>> desasignarTerminalDeCaja(
            @PathVariable int id,
            @PathVariable String sn) {
        Map<String, Object> result = cajaTerminalService.desasignarTerminalDeCaja(id, sn);
        Object success = result.get("success");
        if (Boolean.TRUE.equals(success)) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @Operation(summary = "Asignar una caja existente a un palé",
            description = "Asigna palet_id a una caja ya creada, evitando recrear la caja")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Caja asignada al palé con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Caja o palé no encontrado")
    })
    @PatchMapping("/{id}/palet")
    public ResponseEntity<Map<String, Object>> asignarPaletACaja(
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID del palé a asignar",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "paletId": 4
                                            }
                                            """
                            )
                    )
            )
            @RequestBody AsignarPaletCajaRequestDto request) {

        Map<String, Object> map = new HashMap<>();

        if (request == null || request.getPaletId() == null) {
            map.put("error", "El campo 'paletId' es obligatorio");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }

        Caja caja = cajaService.findById(id);
        if (caja == null) {
            map.put("error", "Caja no encontrada");
            map.put("cajaId", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        Palet palet = paletService.findById(request.getPaletId());
        if (palet == null) {
            map.put("error", "El 'paletId' no existe");
            map.put("paletId", request.getPaletId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        }

        Caja updated = cajaService.asignarPalet(id, palet);
        map.put("mensaje", "Caja asignada al palé con éxito");
        map.put("cajaId", updated.getId());
        map.put("paletId", updated.getPalet() != null ? updated.getPalet().getId() : null);
        map.put("updatedCaja", updated);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}
