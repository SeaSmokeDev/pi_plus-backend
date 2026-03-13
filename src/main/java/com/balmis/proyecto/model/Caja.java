package com.balmis.proyecto.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// LOMBOK
@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor

// SWAGGER
@Schema(description = "Modelo de cajas de Almacén", name="Cajas")

// JPA
@Entity
@Table(name = "cajas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Caja implements Serializable {

    private static final long serialVersionUID = 1L; 
    
    @Schema(description = "ID único de la caja", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true) 
    private Integer id;
    
    @Schema(description = "Descripción de la etiqueta", example = "P2E4N4C3")
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min=1, max=100, message = "La descripción no puede tener más de 100 caracteres")
    @Column(name = "etiqueta", nullable = false, unique = true) 
    private String etiqueta;

    @Schema(description = "Descripción del producto", example = "Terminales Samsung - Link/2500 LE")
    @NotBlank(message = "La descripción es obligatoria")
    @Size(min=1, max=100, message = "La descripción no puede tener más de 100 caracteres")
    @Column(name = "modelo_producto", nullable = false, unique = false) 
    private String modeloProducto;

    @Schema(description = "ID único del palé que está asignado una caja", example = "PALE-1234N")
    @Column(name = "palet_id", nullable = true, unique = true) 
    private Integer idPale;
    
    @Schema(description = "Palé asignado a la caja")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "palet_id")
    @JsonIgnoreProperties({"cajas", "ubicacionAlmacen"})
    private Palet palet;
    
    @OneToMany(mappedBy = "caja", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("caja")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Terminal> terminales = new HashSet<>();

    
}
