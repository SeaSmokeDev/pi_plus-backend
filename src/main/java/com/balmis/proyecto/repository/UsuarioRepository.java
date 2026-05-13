package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.Usuario;
import com.balmis.proyecto.model.dtos.UsuarioIdDTO;
import com.balmis.proyecto.model.dtos.UsuarioListDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // **********************************************************
    // Obtener datos (find y count)
    // **********************************************************
    // ****************************
    // Métodos HEREDADOS
    // ****************************
    /*
        findAll()
        findById(id)

        count()

        equals(User)
        exist(User)
        existById(id)
     */
    // Consulta con SQL mapeado
    @Query(value = "SELECT * FROM usuarios", nativeQuery = true)
    List<Usuario> findSqlAll();

    // Consulta con SQL mapeado
    @Query(value = "SELECT * FROM usuarios WHERE id = :id", nativeQuery = true)
    Usuario findSqlById(@Param("id") int usuarioId);

    // Consulta con SQL mapeado
    @Query(value = "SELECT COUNT(*) as usuarios FROM usuarios", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM usuarios WHERE LOWER(nombre) LIKE LOWER(CONCAT('%',:nombre,'%'))", nativeQuery = true)
    List<Usuario> findSqlLikeNombre(@Param("nombre") String usuarioNombre);

    @Query("""
    SELECT new com.balmis.proyecto.model.dtos.UsuarioListDTO(
        us.username,
        u.nombre,
        u.apellido,
        u.id
    )
    FROM Usuario u
    LEFT JOIN u.usuarioSecurity us
    ORDER BY u.nombre ASC, u.apellido ASC
    """)
    List<UsuarioListDTO> findAllForList();

    @Query("""
    SELECT new com.balmis.proyecto.model.dtos.UsuarioIdDTO(
        u.id
    )
    FROM Usuario u
    JOIN u.usuarioSecurity us
    WHERE LOWER(us.username) = LOWER(:username)
    """)
    UsuarioIdDTO findUsuarioIdByUsername(@Param("username") String username);

    // **********************************************************
    // Actualizaciones
    // **********************************************************
    // ****************************
    // Métodos HEREDADOS
    // ****************************
    /*
        delete(User)
        deleteById(id)
        deleteAll()

        save(User)
     */
}
