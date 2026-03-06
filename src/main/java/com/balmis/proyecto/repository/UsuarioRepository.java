
package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.Usuario;
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
