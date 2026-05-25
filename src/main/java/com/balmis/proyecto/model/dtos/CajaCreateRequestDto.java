package com.balmis.proyecto.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CajaCreateRequestDto {
    private String etiqueta;
    private String modeloProducto;
    private Integer maxCapacity;

    @JsonAlias({"palet_id", "id_pale", "id_palet"})
    private Integer paletId;
}
