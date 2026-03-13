package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Pasillo;
import com.balmis.proyecto.service.PasilloService;
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

@Tag(name = "Pasillos", description = "API para gestión de pasillos")
@RestController
@RequestMapping("/api/pasillos")
public class PasilloController {

    @Autowired
    private PasilloService pasilloService;

    // ***************************************************************************
    // CONSULTAS
    // ***************************************************************************
    // http://localhost:8080/apirest/pasillos
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todos los pasillos",
            description = "Retorna una lista con todos los pasillos del almacén")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "pasillos obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("")
    public ResponseEntity<List<Pasillo>> showPasillos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pasilloService.findAll());
    }

    // http://localhost:8080/apirest/pasillos/2
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener pasillo por ID",
            description = "Retorna un pasillo específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pasillo encontrado"),
        @ApiResponse(responseCode = "404", description = "Pasillo no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/{id}")
    public ResponseEntity<Pasillo> detailsPasillo(@PathVariable int id) {
        Pasillo pasillo = pasilloService.findById(id);

        if (pasillo == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(pasillo);
        }
    }

    // http://localhost:8080/apirest/pasillos/mayor/7
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener pasillos mayores de un ID",
            description = "Retorna una lista con todos los pasillos con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "pasillos obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("/mayor/{id}")
    public ResponseEntity<List<Pasillo>> showPasillosMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pasilloService.findByIdGrThan(id));
    }

    // http://localhost:8080/apirest/pasillos/count
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener el número de pasillos del almacén",
            description = "Retorna la cantidad de pasillos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de pasillos obtenidos con éxito", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> countPasillos() {

        ResponseEntity<Map<String, Object>> response = null;

        Map<String, Object> map = new HashMap<>();
        map.put("pasillos", pasilloService.count());

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
    // http://localhost:8080/apirest/pasillos
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Crear un nuevo pasillo",
            description = "Registra un nuevo pasillo en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pasillo creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content())
    })
    // ***************************************************************************    

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> createPasillo(
            @Valid @RequestBody Pasillo pasillo) {

        ResponseEntity<Map<String, Object>> response;

        if (pasillo == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (pasillo.getNumeroPasillo() == null){

                Map<String, Object> map = new HashMap<>();
                map.put("error", "El campo 'numero_pasillo' es obligatorio");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else if(pasillo.getNumeroPasillo()<=0 ){
                 Map<String, Object> map = new HashMap<>();
                map.put("error", "El numero del pasillo no puede ser menor o 0" );
                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);

            }else {
                System.out.println(pasillo);
                Pasillo pasilloPost = pasilloService.save(pasillo);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Pasillo creado con éxito");
                map.put("insertPasillo", pasilloPost);

                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/apirest/pasillos
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar un pasillo existente",
            description = "Reemplaza completamente los datos de un Pasillo identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pasillo actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Pasillo no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @PutMapping("")
    public ResponseEntity<Map<String, Object>> updatePasillo(
            @Valid @RequestBody Pasillo pasilloUpdate) {

        ResponseEntity<Map<String, Object>> response;

        if (pasilloUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            int id = pasilloUpdate.getId();
            Pasillo existingPasillo = pasilloService.findById(id);

            if (existingPasillo == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Pasillo no encontrado");
                map.put("id", id);

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {

                // Actualizar campos si están presentes
                if (pasilloUpdate.getNumeroPasillo() != null) {
                    existingPasillo.setNumeroPasillo(pasilloUpdate.getNumeroPasillo());
                }
               
            }       
                Pasillo usuPut = pasilloService.save(existingPasillo);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Pasillo actualizado con éxito");
                map.put("updatedPasillo", usuPut);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            
        }

        return response;
    }

    // ****************************************************************************
    // DELETE
    // http://localhost:8080/apirest/pasillos/16
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Eliminar Pasillo por ID",
            description = "Elimina un Pasillo específico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pasillo eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Pasillo no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePasillo(@PathVariable int id) {

        ResponseEntity<Map<String, Object>> response;

        Pasillo existingPasillo = pasilloService.findById(id);
        if (existingPasillo == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Pasillo no encontrado");
            map.put("id", id);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            pasilloService.deleteById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Pasillo eliminado con éxito");
            map.put("deletedPasillo", existingPasillo);

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

}


