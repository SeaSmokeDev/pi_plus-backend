package com.balmis.proyecto.model.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AsignarPaletCajaRequestDto {

    @JsonAlias({"palet_id", "id_pale", "id_palet"})
    private Integer paletId;

    public Integer getPaletId() {
        return paletId;
    }

    public void setPaletId(Integer paletId) {
        this.paletId = paletId;
    }
}
