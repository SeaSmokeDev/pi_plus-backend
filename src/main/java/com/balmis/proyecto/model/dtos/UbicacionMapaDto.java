package com.balmis.proyecto.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item del mapa de almacen listo para frontend")
public class UbicacionMapaDto {

    private Integer idHueco;
    private Integer ubicacionAlmacenId;
    private String referencia;
    private AlmacenResumenDto almacen;
    private PasilloResumenDto pasillo;
    private EstanteriaResumenDto estanteria;
    private PaletResumenDto pale;
    private Integer ocupacionActual;
    private List<CajaResumenDto> cajas;
}
