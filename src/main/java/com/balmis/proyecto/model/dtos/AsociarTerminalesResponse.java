package com.balmis.proyecto.model.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsociarTerminalesResponse {
    private boolean success;
    private MotivoValidacionTerminal motivo;
    private Integer cajaId;
    private List<TerminalAsociadoDto> terminalesAsociados;
    private List<ErrorTerminalDto> errores;
}
