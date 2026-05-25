package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request para crear un palet")
public class PaletCreateRequestDto {

    private String descripcion;
    private String material;
    private String tipo;
    private Integer capacidadMaxCajas;
    private String codigoMarca;
    private Integer ubicacionAlmacenId;
}
