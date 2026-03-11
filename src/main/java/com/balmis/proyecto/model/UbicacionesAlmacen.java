/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.balmis.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@ToString(exclude = "pasillo")           // Excluir del toString para evitar recursividad
@EqualsAndHashCode(exclude = "pasillo")

// SWAGGER
@Schema(description = "Modelo Ubicaciones en almacen", name = "UbicacionesAlmacen")

// JPA
@Entity
@Table(name = "ubicaciones_almacen")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UbicacionesAlmacen implements Serializable{
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID unico de la ubicacion en almacen", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Schema(description = "Referencia del pasillo-estanteria-nivel", example = "1A3")
    @NotBlank(message = "La referencia es obligatoria")
    @Size(min = 1, max = 255, message = "La referencia no puede tener más de 255 caracteres")
    @Column(name = "referencia", nullable = false)
    private String referencia;
    
    @Schema(description = "El nivel que puede tener una ubicacion", example = "Una ubicacion tiene 4 niveles de altura")
    @Min(1)
    @Max(4)
    @Column(name = "nivel", nullable = false)
    private Integer nivel;
    
    @Schema(description = "Pasillo al que pertenece la estanteria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasillo_id", nullable = false)
    @JsonIgnoreProperties("estanterias")
    private Pasillo pasillo;
}
