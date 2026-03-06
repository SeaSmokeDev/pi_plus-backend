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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

// LOMBOK
@AllArgsConstructor         // => Constructor con todos los argumentos
@NoArgsConstructor          // => Constructor sin argumentos
@Data                       // => @Getter + @Setter + @ToString + @EqualsAndHashCode + @RequiredArgsConstructor
@ToString(exclude = "usuario")
@EqualsAndHashCode(exclude = "usuario")

// SWAGGER
@Schema(description = "Modelo de Usuario Security", name = "UsuarioSecurity")

// JPA
@Entity
@Table(name = "users_security")
public class UsuarioSecurity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único del usuario", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 1, max = 100, message = "El nombre de usuario no puede tener más de 100 caracteres")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Schema(description = "Email del usuario", example = "juan@balmis.com")
    @NotBlank(message = "El email es obligatorio")
    @Size(min = 1, max = 150, message = "El email no puede tener más de 150 caracteres")
    @Column(name = "email", nullable = false, unique = false)
    private String email;

    @Schema(description = "Password del usuario encriptada", example = "$2a$10$fzcGgF.8xODz7ptkmZC")
    @NotBlank(message = "El password es obligatorio")
    @Column(name = "password", nullable = false, unique = false)
    private String password;

    @Schema(description = "Rol del usuario", example = "trabajador_almacen")
    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;
//    @Schema(description = "Activar permisos de Administrador", example = "false")
//    @Column(name = "administrador", nullable = false, unique = false) 
//    private boolean administrador;
//
//    @Schema(description = "Activar permisos de Usuario", example = "true")
//    @Column(name = "usuario", nullable = false, unique = false) 
//    private boolean usuario;
//
//    @Schema(description = "Activar permisos de Invitado", example = "true")
//    @Column(name = "invitado", nullable = false, unique = false) 
//    private boolean invitado;

    @Schema(description = "Activar el usuario", example = "true")
    @Column(name = "activado", nullable = false, unique = false)
    private boolean activado;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"expediciones", "usuarioSecurity"})
    private Usuario usuario;

}
