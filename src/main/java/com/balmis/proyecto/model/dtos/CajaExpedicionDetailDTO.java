/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balmis.proyecto.model.dtos;

import java.util.List;
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
public class CajaExpedicionDetailDTO {
    private Integer id;
    private String etiqueta;
    private String modeloProducto;
    private Integer maxCapacity;
    private Long cantidadTerminales;
    private List<TerminalCajaDTO> terminales;
}
