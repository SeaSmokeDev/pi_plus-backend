package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de palet para el mapa de ubicaciones")
public class PaletResumenDto {

    private Integer id;
    private String descripcion;
    private String material;
    private String tipo;
    private Integer capacidadMaxCajas;
}
