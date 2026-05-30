package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Terminal;
import com.balmis.proyecto.model.dtos.TerminalCreateRequestDTO;
import com.balmis.proyecto.model.dtos.TerminalCreateResponseDTO;
import com.balmis.proyecto.model.dtos.TerminalEditResponseDTO;
import com.balmis.proyecto.model.dtos.TerminalMarcaModeloDTO;
import com.balmis.proyecto.model.dtos.TerminalUpdateRequestDTO;
import com.balmis.proyecto.service.TerminalService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Terminales", description = "API para gestión de Terminales")
@RestController
@RequestMapping("/api/terminales")
public class TerminalController {

    @Autowired
    private TerminalService terminalService;

    // ***************************************************************************
    // CONSULTAS
    // ***************************************************************************
    // http://localhost:8080/apirest/terminales
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todos los terminales",
            description = "Retorna una lista con todos los terminals disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminales obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("")
    public ResponseEntity<List<Terminal>> showterminals() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(terminalService.findAll());
    }

    // http://localhost:8080/apirest/terminales/2
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener terminal por ID",
            description = "Retorna un Terminal específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal encontrado"),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/{id}")
    public ResponseEntity<Terminal> detailsterminal(@PathVariable int id) {
        Terminal terminal = terminalService.findById(id);

        if (terminal == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(terminal);
        }
    }

    // http://localhost:8080/bdproyecto/api/terminales/sn/SN10001
    // ***************************************************************************
    // SWAGGER
    @Operation(summary = "Obtener terminal por número de serie",
            description = "Retorna un Terminal específico basado en su número de serie (SN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal encontrado"),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************
    @GetMapping("/sn/{numeroSerie}")
    public ResponseEntity<Terminal> detailsterminalByNumeroSerie(@PathVariable String numeroSerie) {
        Terminal terminal = terminalService.findByNumeroSerie(numeroSerie);

        if (terminal == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(terminal);
        }
    }

    // SWAGGER
    @Operation(summary = "Obtener terminal por número de serie para editar",
            description = "Retorna un Terminal específico basado en su número de serie (SN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal encontrado"),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************
    @GetMapping("/{numeroSerie}/edit")
    public ResponseEntity<Map<String, Object>> getTerminalForEdit(
            @PathVariable String numeroSerie
    ) {
        ResponseEntity<Map<String, Object>> response;

        if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El número de serie no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            TerminalEditResponseDTO dto = terminalService.findTerminalForEditByNumeroSerie(numeroSerie);

            if (dto == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Terminal no encontrado");

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Terminal obtenido correctamente");
                map.put("terminal", dto);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }

        return response;
    }

    // http://localhost:8080/apirest/terminales/mayor/7
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener las marcas y modelos",
            description = "Retorna una lista con todos las marcas y modelos de los terminales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marcas y modelos obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("/marcas-modelos")
    public ResponseEntity<Map<String, Object>> getMarcasModelos() {
        List<TerminalMarcaModeloDTO> marcasModelos = terminalService.findDistinctMarcasModelos();

        Map<String, Object> map = new HashMap<>();
        map.put("mensaje", "Marcas y modelos obtenidos correctamente");
        map.put("marcasModelos", marcasModelos);

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // http://localhost:8080/apirest/terminales/count
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener el número de terminales existentes",
            description = "Retorna la cantidad de terminales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de terminales obtenidos con éxito", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countTerminales() {

        ResponseEntity<Map<String, Object>> response = null;

        Map<String, Object> map = new HashMap<>();
        map.put("terminales", terminalService.count());

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
    // http://localhost:8080/apirest/terminales
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Crear un nuevo terminal",
            description = "Registra un nuevo Terminal en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Terminal creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    // ***************************************************************************    

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createTerminal(
            @Valid @RequestBody TerminalCreateRequestDTO request) {

        ResponseEntity<Map<String, Object>> response;

        if (request == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (request.getModelo() == null
                    || request.getMarca() == null || request.getEstado() == null) {

                Map<String, Object> map = new HashMap<>();
                map.put("error", "Los campos 'modelo' , 'marca', 'estado' son obligatorios");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {

                TerminalCreateResponseDTO dto = terminalService.createTerminal(request);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", dto.getMensaje());
                map.put("numeroSerie", dto.getNumeroSerie());

                return ResponseEntity.status(HttpStatus.CREATED).body(map);

            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/bdproyecto/api/terminales/sn/SN10001
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar un terminal existente",
            description = "Reemplaza completamente los datos de un Terminal identificado por su número de serie (SN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Terminal actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @PutMapping("/{numeroSerie}")
    public ResponseEntity<Map<String, Object>> updateterminalByNumeroSerie(
            @PathVariable String numeroSerie,
            @Valid @RequestBody TerminalUpdateRequestDTO request) {

        ResponseEntity<Map<String, Object>> response;

        if (numeroSerie == null || numeroSerie.trim().isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El número de serie no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            Terminal terminal = terminalService.updateTerminal(numeroSerie, request);

            if (terminal == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Terminal no encontrado");

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Terminal actualizado correctamente");
                map.put("numeroSerie", terminal.getNumeroSerie());

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // PATCH
    // http://localhost:8080/bdproyecto/api/terminales/terminales/1
    // ***************************************************************************
    // SWAGGER
    @Operation(summary = "Actualizar parcialmente un terminal existente",
            description = "Actualiza solo los campos enviados de un Terminal identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal actualizado parcialmente con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************
    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patchTerminal(
            @PathVariable int id, @RequestBody Terminal terminalPatch) {

        ResponseEntity<Map<String, Object>> response;

        if (terminalPatch == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {
            Terminal existingTerminal = terminalService.findById(id);

            if (existingTerminal == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Terminal no encontrado");
                map.put("id", id);

                response = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(map);
            } else {
                Terminal terminalPatched = terminalService.patch(id, terminalPatch);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Terminal actualizado parcialmente con éxito");
                map.put("updatedterminal", terminalPatched);

                response = ResponseEntity
                        .status(HttpStatus.OK)
                        .body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // PATCH POR SN
    // http://localhost:8080/bdproyecto/api/terminales/terminales/sn/SN10001
    // ***************************************************************************
    // SWAGGER
    @Operation(summary = "Actualizar parcialmente un terminal por número de serie",
            description = "Actualiza solo los campos enviados de un Terminal identificado por su número de serie (SN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal actualizado parcialmente con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************
    @PatchMapping("/sn/{numeroSerie}")
    public ResponseEntity<Map<String, Object>> patchTerminalByNumeroSerie(
            @PathVariable String numeroSerie, @RequestBody Terminal terminalPatch) {

        ResponseEntity<Map<String, Object>> response;

        if (terminalPatch == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {
            Terminal existingTerminal = terminalService.findByNumeroSerie(numeroSerie);

            if (existingTerminal == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Terminal no encontrado");
                map.put("numeroSerie", numeroSerie);

                response = ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(map);
            } else {
                Terminal terminalPatched = terminalService.patchByNumeroSerie(numeroSerie, terminalPatch);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Terminal actualizado parcialmente con éxito");
                map.put("updatedterminal", terminalPatched);

                response = ResponseEntity
                        .status(HttpStatus.OK)
                        .body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // DELETE
    // http://localhost:8080/apirest/terminales/16
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Eliminar Terminal por ID",
            description = "Elimina un Terminal específico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Terminal eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteterminal(@PathVariable int id) {

        ResponseEntity<Map<String, Object>> response;

        Terminal existingTerminal = terminalService.findById(id);
        if (existingTerminal == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Terminal no encontrado");
            map.put("id", id);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            terminalService.deleteById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Terminal eliminado con éxito");
            map.put("deletedterminal", existingTerminal);

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

    // ****************************************************************************
    // DELETE POR SN
    // http://localhost:8080/bdproyecto/api/terminales/terminales/sn/SN10001
    // ***************************************************************************
    // SWAGGER
    @Operation(summary = "Eliminar Terminal por número de serie",
            description = "Elimina un Terminal específico del sistema por su número de serie (SN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Terminal eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************
    @DeleteMapping("/sn/{numeroSerie}")
    public ResponseEntity<Map<String, Object>> deleteTerminalByNumeroSerie(@PathVariable String numeroSerie) {

        ResponseEntity<Map<String, Object>> response;

        Terminal existingTerminal = terminalService.findByNumeroSerie(numeroSerie);
        if (existingTerminal == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Terminal no encontrado");
            map.put("numeroSerie", numeroSerie);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            terminalService.deleteByNumeroSerie(numeroSerie);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Terminal eliminado con éxito");
            map.put("deletedterminal", existingTerminal);

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

}
