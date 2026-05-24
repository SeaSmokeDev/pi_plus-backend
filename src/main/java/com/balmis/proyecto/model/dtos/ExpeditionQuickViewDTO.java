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
public class ExpeditionQuickViewDTO {
    private String referenciaExpedicion;
    private String username;
    private LocalDateTime fechaEnvio;
    private String direccionDestino;
    private Integer paquetes;
    private Integer peso;
    private String notas;
    private Long totalExpediciones;
    private Long totalTerminales;
    private List<ExpeditionQuickViewPaymentDTO> terminales;
}
