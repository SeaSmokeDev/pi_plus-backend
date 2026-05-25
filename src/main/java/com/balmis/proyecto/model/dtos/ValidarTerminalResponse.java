package com.balmis.proyecto.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidarTerminalResponse {
    private boolean valido;
    private MotivoValidacionTerminal motivo;
    private TerminalValidacionDto terminal;
    private CajaValidacionDto caja;
}
