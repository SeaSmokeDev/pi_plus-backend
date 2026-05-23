package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.dtos.MarcaModelosProductoDto;
import com.balmis.proyecto.model.dtos.CapacidadMaximaModeloResponseDto;
import com.balmis.proyecto.model.dtos.MarcasCatalogoResponseDto;
import com.balmis.proyecto.model.dtos.ModelosPorMarcaResponseDto;
import com.balmis.proyecto.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Catalogo", description = "API para catálogos de datos de formularios")
@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @Operation(summary = "Obtener catálogo de modelos producto por marca",
            description = "Retorna marcas y modelos disponibles en base a terminales existentes, sin duplicados y ordenados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catálogo obtenido con éxito")
    })
    @GetMapping("/modelos-producto")
    public ResponseEntity<List<MarcaModelosProductoDto>> obtenerModelosProducto() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(catalogoService.obtenerModelosProductoAgrupado());
    }

    @Operation(summary = "Obtener marcas de terminales",
            description = "Retorna las marcas disponibles en terminales_pago sin duplicados y ordenadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Marcas obtenidas con éxito")
    })
    @GetMapping("/terminales/marcas")
    public ResponseEntity<MarcasCatalogoResponseDto> obtenerMarcasTerminales() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(catalogoService.obtenerMarcasTerminales());
    }

    @Operation(summary = "Obtener modelos por marca",
            description = "Retorna los modelos disponibles para la marca indicada, sin duplicados y ordenados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modelos obtenidos con éxito"),
        @ApiResponse(responseCode = "400", description = "Marca inválida")
    })
    @GetMapping("/terminales/marcas/{marca}/modelos")
    public ResponseEntity<?> obtenerModelosPorMarca(
            @Parameter(description = "Marca de terminal a consultar", example = "Ingenico")
            @PathVariable String marca) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(catalogoService.obtenerModelosPorMarca(marca));
        } catch (IllegalArgumentException ex) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
    }

    @Operation(summary = "Obtener capacidad maxima sugerida por modelo",
            description = "Retorna la capacidad maxima de caja registrada para el modelo indicado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Capacidad obtenida con éxito"),
        @ApiResponse(responseCode = "400", description = "Modelo inválido")
    })
    @GetMapping("/cajas/modelos/{modelo}/max-capacity")
    public ResponseEntity<?> obtenerCapacidadMaximaPorModelo(
            @Parameter(description = "Modelo de terminal/caja a consultar", example = "Move5000")
            @PathVariable String modelo) {
        try {
            CapacidadMaximaModeloResponseDto response = catalogoService.obtenerCapacidadMaximaPorModelo(modelo);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        }
    }
}
