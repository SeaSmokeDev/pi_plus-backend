/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balmis.proyecto.model.dtos;

import com.balmis.proyecto.model.EstadoTerminal;
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
public class TerminalCajaDTO {
    private String modelo;
    private String marca;
    private EstadoTerminal estado;
    private String numeroSerie;
    
}
