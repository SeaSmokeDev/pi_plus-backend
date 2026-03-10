package com.balmis.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// LOMBOK
@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor

@ToString(exclude = "estanterias")
@EqualsAndHashCode(exclude = "estanterias")

// SWAGGER
@Schema(description = "Modelo Pasillo", name="Pasillo")

// JPA
@Entity
@Table(name = "pasillos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pasillo {
    
    @Schema(description = "ID único del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    
    @Schema(description = "Numero pasillo", example = "2")
    @Min(1)
    @Column(name = "numero_pasillo", nullable = false, unique = true)
    private Integer numeroPasillo;
    
    @Schema(description = "Estanterias que pertenecen al pasillo")
    @OneToMany(mappedBy = "pasillo", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("pasillo")
    private Set<Estanteria> estanterias = new HashSet<>();
    
}
