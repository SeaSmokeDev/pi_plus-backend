package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con marcas disponibles del catalogo de terminales")
public class MarcasCatalogoResponseDto {

    @Schema(description = "Listado de marcas ordenadas alfabeticamente")
    private List<String> items;
}
