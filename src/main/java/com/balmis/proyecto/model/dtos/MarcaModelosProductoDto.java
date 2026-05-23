package com.balmis.proyecto.model.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaModelosProductoDto {
    private String marca;
    private List<String> modelos;
}
