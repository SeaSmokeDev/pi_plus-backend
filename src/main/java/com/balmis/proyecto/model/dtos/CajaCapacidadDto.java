package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de capacidad y ocupación de una caja")
public class CajaCapacidadDto {
    private Integer cajaId;
    private Integer terminalesActuales;
    private Integer capacidadMaxima;
}
