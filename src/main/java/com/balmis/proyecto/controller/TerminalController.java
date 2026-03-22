package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Terminal;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Terminales", description = "API para gestión de Terminales")
@RestController
@RequestMapping("/api")
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
    @GetMapping("/terminales")
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
    @GetMapping("/terminales/{id}")
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
    @GetMapping("/terminales/sn/{numeroSerie}")
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

    // http://localhost:8080/apirest/terminales/mayor/7
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener terminales mayores de un ID",
            description = "Retorna una lista con todos los terminales con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminales obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("/terminales/mayor/{id}")
    public ResponseEntity<List<Terminal>> showTerminalesMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(terminalService.findByIdGrThan(id));
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
    @GetMapping("/terminales/count")
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

    @PostMapping("/terminales")
    public ResponseEntity<Map<String, Object>> createTerminal(
            @Valid @RequestBody Terminal terminal) {

        ResponseEntity<Map<String, Object>> response;

        if (terminal == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (terminal.getNumeroSerie() == null || terminal.getModelo()== null
                || terminal.getMarca() == null || terminal.getEstado() == null
                || terminal.getFechaCreacion() == null || terminal.getFechaIngreso() == null){

                Map<String, Object> map = new HashMap<>();
                map.put("error", "Los campos 'numero_serie', 'modelo' , 'marca', 'estado', 'fecha_ingreso' y 'fecha_creacion' son obligatorios");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {
                System.out.println(terminal);
                Terminal terminalPost = terminalService.save(terminal);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Terminal creado con éxito");
                map.put("insertterminal", terminalPost);

                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/apirest/terminales
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar un terminal existente",
            description = "Reemplaza completamente los datos de un Terminal identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Terminal actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Terminal no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @PutMapping("/terminales")
    public ResponseEntity<Map<String, Object>> updateterminal(
            @Valid @RequestBody Terminal terminalUpdate) {

        ResponseEntity<Map<String, Object>> response;

        if (terminalUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            int id = terminalUpdate.getId();
            Terminal existingTerminal = terminalService.findById(id);

            if (existingTerminal == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Terminal no encontrado");
                map.put("id", id);

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {

                // Actualizar campos si están presentes
                if (terminalUpdate.getNumeroSerie() != null) {
                    existingTerminal.setNumeroSerie(terminalUpdate.getNumeroSerie());
                }
                if (terminalUpdate.getModelo() != null) {
                    existingTerminal.setModelo(terminalUpdate.getModelo());
                }
                if (terminalUpdate.getMarca() != null) {
                    existingTerminal.setMarca(terminalUpdate.getMarca());
                }
                if (terminalUpdate.getEstado() != null) {
                    existingTerminal.setEstado(terminalUpdate.getEstado());
                }
                if (terminalUpdate.getFechaCreacion() != null) {
                    existingTerminal.setFechaCreacion(terminalUpdate.getFechaCreacion());
                }
                if (terminalUpdate.getFechaIngreso() != null) {
                    existingTerminal.setFechaIngreso(terminalUpdate.getFechaIngreso());
                }
                if (terminalUpdate.getNotas() != null) {
                existingTerminal.setNotas(terminalUpdate.getNotas());
            }
            // if (terminalUpdate.getIdCaja() != null) {
            //     existingTerminal.setIdCaja(terminalUpdate.getIdCaja());
            // }

                            
                Terminal usuPut = terminalService.save(existingTerminal);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Terminal actualizado con éxito");
                map.put("updatedterminal", usuPut);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
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
    @DeleteMapping("/terminales/{id}")
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

}
