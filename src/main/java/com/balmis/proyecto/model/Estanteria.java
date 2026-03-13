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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@ToString(exclude = {"pasillo", "ubicaciones"})           // Excluir del toString para evitar recursividad
@EqualsAndHashCode(exclude = {"pasillo", "ubicaciones"})

// SWAGGER
@Schema(description = "Modelo Estanteria", name = "Estanteria")

// JPA
@Entity
@Table(name = "estanterias")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Estanteria implements Serializable{
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID unico de la estanteria", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Schema(description = "Codigo de la estanteria", example = "A")
    @NotBlank(message = "El codigo es obligatorio")
    @Size(min = 1, max = 1)
    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Schema(description = "Nivel maximo de la estanteria", example = "4")
    @Min(1)
    @Column(name = "niveles_maximos", nullable = false)
    private byte nivelesMaximos;
    
    @Schema(description = "La capacidad de cada nivel de la estanteria", example = "un palé con un maximo de 8 cajas")
    @Min(1)
    @Column(name = "capacidad_nivel", nullable = false)
    private Integer capacidadNivel;
    
    @Schema(description = "Pasillo al que pertenece la estanteria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasillo_id", nullable = false)
    @JsonIgnoreProperties("estanterias")
    private Pasillo pasillo;
    
    @Schema(description = "Ubicaciones disponibles en la estanteria")
    @OneToMany(mappedBy = "estanteria", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("estanteria")
    private Set<UbicacionAlmacen> ubicaciones = new HashSet<>();
}
