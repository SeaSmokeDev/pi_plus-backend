package com.balmis.proyecto.controller;

import com.balmis.proyecto.model.Usuario;
import com.balmis.proyecto.model.UsuarioSecurity;
import com.balmis.proyecto.service.UsuarioSecurityService;
import com.balmis.proyecto.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuarios Security", description = "API para gestión de usuarios security")
@RestController
@RequestMapping("/api/security")
public class UsuarioSecurityController {

    @Autowired
    private UsuarioSecurityService userSecurityService;

    @Autowired
    private UsuarioService userService;

    // ***************************************************************************
    // CONSULTAS
    // ***************************************************************************
    // http://localhost:8080/apirestmergesec/users
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener todos los usuarios",
            description = "Retorna una lista con todos los usuarios disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioSecurity>> showUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userSecurityService.findAll());
    }

    // http://localhost:8080/apirestmergesec/users/2
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener usuario security por ID",
            description = "Retorna un usuario específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioSecurity> detailsUser(@PathVariable int id) {
        UsuarioSecurity usu = userSecurityService.findById(id);

        if (usu == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);  // 404 Not Found
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(usu);
        }
    }

    // http://localhost:8080/apirestmergesec/users/mayor/7
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener usuarios mayores de un ID",
            description = "Retorna una lista con todos los usuarios con ID mayor que un valor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios obtenidos con éxito")
    })
    // ***************************************************************************    
    @GetMapping("/usuarios/mayor/{id}")
    public ResponseEntity<List<UsuarioSecurity>> showUsersMayores(@PathVariable int id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userSecurityService.findByIdGrThan(id));
    }

    // http://localhost:8080/apirestmergesec/users/count
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Obtener el número de usuarios existentes",
            description = "Retorna la cantidad de usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Número de usuarios obtenidos con éxito", content = @Content())
    })
    // ***************************************************************************    
    @GetMapping("/usuarios/count")
    public ResponseEntity<Map<String, Object>> countUsers() {

        ResponseEntity<Map<String, Object>> response = null;

        Map<String, Object> map = new HashMap<>();
        map.put("users", userSecurityService.count());

        response = ResponseEntity
                .status(HttpStatus.OK)
                .body(map);

        return response;
    }

    // ***************************************************************************
    // ACTUALIZACIONES
    // ***************************************************************************
    // ****************************************************************************
    // INSERT (POST)    
    // http://localhost:8080/apirestmergesec/users
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Crear un nuevo usuario security",
            description = "Registra un nuevo usuario en el sistema con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @PostMapping("/usuarios/{idUser}")
    public ResponseEntity<Map<String, Object>> createUser(
            @Valid @RequestBody UsuarioSecurity user, @PathVariable int idUser) {

        ResponseEntity<Map<String, Object>> response;

        if (user == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(map);
        } else {

            if (user.getUsername() == null || user.getUsername().trim().isEmpty()
                    || user.getEmail() == null || user.getEmail().trim().isEmpty()
                    || user.getPassword() == null || user.getPassword().trim().isEmpty()) {

                Map<String, Object> map = new HashMap<>();
                map.put("error", "Los campos 'name', 'email' y 'password' son obligatorios");

                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(map);
            } else {

                Usuario usuario = userService.findById(idUser);

                if (usuario == null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("error", "El id del usuario es invalido o no existe el usuario");

                    response = ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(map);
                } else {

                    if (usuario.getRol() != user.getRol()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("error", "El rol es diferente");

                        response = ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(map);
                    } else {
                        user.setUsuario(usuario);

                        UsuarioSecurity usuPost = userSecurityService.save(user);

                        Map<String, Object> map = new HashMap<>();
                        map.put("mensaje", "Usuario creado con éxito");
                        map.put("insertUser", usuPost);

                        response = ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(map);
                    }

                }
            }
        }

        return response;
    }

    // ****************************************************************************
    // UPDATE (PUT)
    // http://localhost:8080/apirestmergesec/users
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Actualizar un usuario security existente",
            description = "Reemplaza completamente los datos de un usuario identificado por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario actualizado con éxito", content = @Content()),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @PutMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> updateUser(
            @Valid @RequestBody UsuarioSecurity userUpdate) {

        ResponseEntity<Map<String, Object>> response;

        if (userUpdate == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "El cuerpo de la solicitud no puede estar vacío");

            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
        } else {
            int id = userUpdate.getId();
            UsuarioSecurity existingUser = userSecurityService.findById(id);

            if (existingUser == null) {
                Map<String, Object> map = new HashMap<>();
                map.put("error", "Usuario no encontrado");
                map.put("id", id);

                response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
            } else {

                // Actualizar campos si están presentes
                if (userUpdate.getUsername() != null) {
                    existingUser.setUsername(userUpdate.getUsername());
                }
                if (userUpdate.getEmail() != null) {
                    existingUser.setEmail(userUpdate.getEmail());
                }
                if (userUpdate.getPassword() != null) {
                    existingUser.setPassword(userUpdate.getPassword());
                }
                existingUser.setRol(userUpdate.getRol());
//                existingUser.setAdministrador(userUpdate.isAdministrador());
//                existingUser.setUsuario(userUpdate.isUsuario());
//                existingUser.setInvitado(userUpdate.isInvitado());
                existingUser.setActivado(userUpdate.isActivado());

                UsuarioSecurity usuPut = userSecurityService.save(existingUser);

                Map<String, Object> map = new HashMap<>();
                map.put("mensaje", "Usuario actualizado con éxito");
                map.put("updatedUser", usuPut);

                response = ResponseEntity.status(HttpStatus.OK).body(map);
            }
        }

        return response;
    }

    // ****************************************************************************
    // DELETE
    // http://localhost:8080/apirestmergesec/users/16
    // ***************************************************************************    
    // SWAGGER
    @Operation(summary = "Eliminar usuario security por ID",
            description = "Elimina un usuario específico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario eliminado con éxito", content = @Content()),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content())
    })
    // ***************************************************************************    
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id) {

        ResponseEntity<Map<String, Object>> response;

        UsuarioSecurity existingUser = userSecurityService.findById(id);
        if (existingUser == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "Usuario no encontrado");
            map.put("id", id);

            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
        } else {

            userSecurityService.deleteById(id);

            Map<String, Object> map = new HashMap<>();
            map.put("mensaje", "Usuario eliminado con éxito");
            map.put("deletedUser", existingUser);

            response = ResponseEntity.status(HttpStatus.OK).body(map);
        }
        return response;
    }

}
