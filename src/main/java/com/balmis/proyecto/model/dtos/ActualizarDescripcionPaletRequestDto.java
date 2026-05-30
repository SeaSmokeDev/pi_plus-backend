package com.balmis.proyecto.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ActualizarDescripcionPaletRequestDto {

    @JsonAlias({"description", "descripcion_palet"})
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
