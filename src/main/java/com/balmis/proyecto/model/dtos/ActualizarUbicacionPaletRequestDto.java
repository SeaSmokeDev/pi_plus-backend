package com.balmis.proyecto.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ActualizarUbicacionPaletRequestDto {

    @JsonAlias({"ubicacion_almacen_id", "ubicacionId"})
    private Integer ubicacionAlmacenId;

    public Integer getUbicacionAlmacenId() {
        return ubicacionAlmacenId;
    }

    public void setUbicacionAlmacenId(Integer ubicacionAlmacenId) {
        this.ubicacionAlmacenId = ubicacionAlmacenId;
    }
}
