package com.balmis.proyecto.model;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// LOMBOK
@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor

// SWAGGER
@Schema(description = "Modelo del terminal", name="Terminal")
// JPA
@Entity
@Table(name = "terminales_pago")
public class Terminal implements Serializable {
    
    private static final long serialVersionUID = 1L; 
    
    @Schema(description = "ID único del terminal", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true) 
    private int id;
    
    @Schema(description = "Numero de serie unico del terminal", example = "123456YZ")
    @NotBlank(message = "El numero de serie es obligatorio")
    @Size(min=1, max=255, message = "El numero de serie no puede tener mas de 255 caracteres")
    @Column(name = "numero_serie", nullable = false, unique = true) 
    private String numeroSerie;

    @Schema(description = "Modelo del terminal", example = "MOVE 5000")
    @NotBlank(message = "El modelo es obligatorio")
    @Size(min=1, max=255, message = "El modelo no puede tener más de 255 caracteres")
    @Column(name = "modelo", nullable = false, unique = false) 
    private String modelo;

    @Schema(description = "Marca del terminal", example = "Ingenico")
    @NotBlank(message = "La marca es obligatoria")
    @Column(name = "marca", nullable = false, unique = false) 
    private String marca;

    @Schema(description = "Tipo de estado del terminal", example = "en_transito")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTerminal estado;

    @Schema(description = "Notas terminal", example = "Cable tipo C para cargador")
    @Column(name = "notas", nullable = true, unique = false) 
    private String notas;

    @Schema(description = "Fecha ingreso", example = "12/06/2026")
    @Column(name = "fecha_ingreso", nullable = false, unique = false) 
    private LocalDateTime fechaIngreso;

    @Schema(description = "Fecha creacion", example = "01/01/2001")
    @Column(name = "fecha_creacion", nullable = false, unique = false) 
    private LocalDateTime fechaCreacion;

    // @Schema(description = "ID único caja", example = "1")
    // @Column(name = "id_caja", nullable = true)
    // private Integer idCaja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_id") 
    @JsonIgnoreProperties("terminales_pago")
    private Caja caja;

    // @OneToMany(mappedBy = "terminal", cascade = CascadeType.ALL)
    // private Set<Expedicion> expediciones = new HashSet<>();
       
    
    
}
