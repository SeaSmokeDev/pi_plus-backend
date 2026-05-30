/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balmis.proyecto.model.dtos;

import com.balmis.proyecto.model.EstadoTerminal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Ian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalUpdateRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoTerminal estado;

    @Size(max = 255, message = "Las notas no pueden tener más de 255 caracteres")
    private String notas;
}
