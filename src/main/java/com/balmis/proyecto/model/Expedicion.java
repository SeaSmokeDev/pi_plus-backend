package com.balmis.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// LOMBOK
@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@ToString(exclude = "usuario")           // Excluir del toString para evitar recursividad
@EqualsAndHashCode(exclude = "usuario")  // Excluir de equals y hashCode para evitar recursividad

// SWAGGER
@Schema(description = "Modelo de Expediciones", name="Expedicion")

// JPA
@Entity
@Table(name = "expediciones")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Expedicion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único de la expedicion", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Schema(description = "Fecha de creacion", example = "10/02/2026")
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de recepcion", example = "12/02/2026")
    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;

    @Schema(description = "Fecha de modificacion", example = "11/02/2026")
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Schema(description = "Direccion del destino", example = "Av. Prueba 123")
    @NotBlank(message = "El direccion destino es obligatorio")
    @Size(min = 1, max = 255, message = "La direccion destino no puede tener más de 255 caracteres")
    @Column(name = "direccion_destino", nullable = false, unique = false)
    private String direccionDestino;

    @Schema(description = "Cantidad de paquetes", example = "10")
    @Min(value = 0, message = "Los paquetes como mínimo es 0")
    @Column(name = "paquetes", unique = false)
    private int paquetes;

    @Schema(description = "Cantidad de peso", example = "5")
    @Min(value = 0, message = "El peso mínimo es 0")
    @Column(name = "peso", unique = false)
    private int peso;

    @Schema(description = "Observaciones de la expedicion", example = "Va con cables sin especificar")
    @Size(min = 1, max = 255, message = "Las notas no puede tener más de 255 caracteres")
    @Column(name = "notas", unique = false)
    private String notas;

    @Schema(description = "Estado de la expedicion", example = "Abierta")
    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable = false)
    private EstadoExpedicion estado;

    @Schema(description = "ID único del usuario que esta asignado a la expedicion", example = "1")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @JsonIgnoreProperties("expediciones")
    private Usuario usuario;

}
