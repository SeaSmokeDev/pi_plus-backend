package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resumen de almacen para el mapa de ubicaciones")
public class AlmacenResumenDto {

    private Integer id;
    private String nombre;
}
