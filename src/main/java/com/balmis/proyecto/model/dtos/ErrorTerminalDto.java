package com.balmis.proyecto.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTerminalDto {
    private String sn;
    private MotivoValidacionTerminal motivo;
}
