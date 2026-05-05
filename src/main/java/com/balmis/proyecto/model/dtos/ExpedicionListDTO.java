/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balmis.proyecto.model.dtos;

import com.balmis.proyecto.model.EstadoExpedicion;
import java.time.LocalDateTime;
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
public class ExpedicionListDTO {
    private Integer id;
    private LocalDateTime fechaCreacion;
    private String direccionDestino;
    private String username;
    private EstadoExpedicion estado;
}
