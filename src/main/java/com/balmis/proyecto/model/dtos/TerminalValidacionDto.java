package com.balmis.proyecto.model.dtos;

import com.balmis.proyecto.model.EstadoTerminal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalValidacionDto {
    private String sn;
    private String marca;
    private String modelo;
    private EstadoTerminal estado;
}
