package com.balmis.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
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
@ToString(exclude = "expediciones")           // Excluir del toString para evitar recursividad
@EqualsAndHashCode(exclude = "expediciones")  // Excluir de equals y hashCode para evitar recursividad

// SWAGGER
@Schema(description = "Modelo de Usuario", name = "Usuario")

// JPA
@Entity
@Table(name = "usuarios")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID unico del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Schema(description = "Nombre del usuario/empleado", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 1, max = 60, message = "El nombre no puede tener más de 60 caracteres")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Schema(description = "Apellido del usuario/empleado", example = "Perez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 1, max = 60, message = "El apellido no puede tener más de 60 caracteres")
    @Column(name = "apellido", nullable = false)
    private String apellido;

//    @NotBlank(message = "El nombre de usuario es obligatorio")
//    @Size(min=1, max=60, message = "El nombre de usuario no puede tener más de 60 caracteres")
//    @Column(name = "nombre_usuario", nullable = false) 
//    private String nombreUsuario;
//    
//    @NotBlank(message = "La contraseña es obligatorio")
//    @Size(min=1, max=255, message = "La contraseña no puede tener más de 255 caracteres")
//    @Column(name = "hash_password", nullable = false) 
//    private String hashPassword;
    @Schema(description = "Rol del usuario", example = "Logistica")
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    @Schema(description = "Lugar de trabajo del usuario", example = "Almacen")
    @Size(min = 1, max = 80, message = "El lugar de trabajo no puede tener más de 80 caracteres")
    @Column(name = "lugar_trabajo", nullable = false)
    private String lugarTrabajo;

    @Schema(description = "Numero de expediciones", example = "2")
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    private Set<Expedicion> expediciones = new HashSet<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    private UsuarioSecurity usuarioSecurity;

}
