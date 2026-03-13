package com.balmis.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// LOMBOK
@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@ToString(exclude = "expediciones")           // Excluir del toString para evitar recursividad
@EqualsAndHashCode(exclude = "expediciones")  // Excluir de equals y hashCode para evitar recursividad

// SWAGGER
@Schema(description = "Modelo de Palet", name = "Palet")

@Entity
@Table(name = "palets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Palet {

    @Schema(description = "ID único del palet", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Schema(description = "Descripcion del pale", example = "")
    @Size(min = 1, max = 255, message = "La descripcion no puede tener más de 255 caracteres")
    @Column(name = "descripcion", unique = false)
    private String descripcion;
    
    @Schema(description = "Material del palet", example = "Madera")
    @Enumerated(EnumType.STRING)
    @Column(name="material", nullable = false)
    private MaterialPalet material;
    
    @Schema(description = "Tipo del palet", example = "Europeo")
    @Enumerated(EnumType.STRING)
    @Column(name="tipo", nullable = false)
    private TipoPalet tipo;
    
    @Schema(description = "Capacidad maxima de cajas en el palet", example = "8")
    @Min(value = 0, message = "La capacidad maxima no puede ser un numero negativo")
    @Column(name = "capacidad_max_cajas",nullable = false, unique = false)
    private int capacidadMaxCajas;
    
    @Schema(description = "Codigo del palet", example = "")
    @Size(min = 1, max = 255, message = "El codigo no puede tener más de 255 caracteres")
    @Column(name = "codigo_marca", unique = false)
    private String codigoMarca;
    
    
}
