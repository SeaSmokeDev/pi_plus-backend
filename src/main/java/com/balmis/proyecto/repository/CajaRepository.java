
package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.Caja;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CajaRepository extends JpaRepository<Caja, Integer> {

    // ****************************
    // Métodos HEREDADOS
    // ****************************
    /*
        findAll()
        findById(id)

        count()
        delete(User)
        deleteById(id)
        deleteAll()

        equals(User)
        exist(User)
        existById(id)
     */
    
    // **********************************************************
    // Obtener datos (find y count)
    // **********************************************************

    // Consulta con DQM 
    Optional<Caja> findByEtiqueta(String etiqueta);
    
    // Consulta con SQL 
    @Query(value = "SELECT * FROM cajas", nativeQuery = true)
    List<Caja> findSqlAll();

    @Query(value = "SELECT * FROM cajas WHERE id = :id", nativeQuery = true)
    Caja findSqlById(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM cajas", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM cajas WHERE id > :id", nativeQuery = true)
    List<Caja> findSqlByIdGreaterThan(@Param("id") int id);
    
    
    
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
