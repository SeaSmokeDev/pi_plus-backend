package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con modelos disponibles para una marca")
public class ModelosPorMarcaResponseDto {

    @Schema(description = "Marca consultada")
    private String marca;

    @Schema(description = "Listado de modelos ordenados alfabeticamente")
    private List<String> items;
}
