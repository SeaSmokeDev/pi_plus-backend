package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de estanteria para el mapa de ubicaciones")
public class EstanteriaResumenDto {

    private Integer id;
    private String descripcion;
    private Integer nivel;
    private Integer capacidadMaxCajas;
}
