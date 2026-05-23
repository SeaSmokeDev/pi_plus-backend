package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Capacidad maxima sugerida para un modelo de caja")
public class CapacidadMaximaModeloResponseDto {

    @Schema(description = "Modelo consultado", example = "Move5000")
    private String modelo;

    @Schema(description = "Capacidad maxima encontrada", example = "100")
    private Integer maxCapacity;
}
