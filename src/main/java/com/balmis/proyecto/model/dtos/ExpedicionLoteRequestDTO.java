/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balmis.proyecto.model.dtos;

import java.time.LocalDateTime;
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
public class ExpedicionLoteRequestDTO {
    private String direccionDestino;
    private int paquetes;
    private int peso;
    private String notas;
    private Integer usuarioId;
    private List<Integer> cajaIds;
}
