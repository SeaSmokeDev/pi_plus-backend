
package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.EstadoTerminal;
import com.balmis.proyecto.model.Terminal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TerminalRepository extends JpaRepository<Terminal, Integer> {

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
    Optional<Terminal> findByNumeroSerie(String numeroSerie);
    
    List<Terminal> findByEstado(EstadoTerminal estado);

    // Consulta con SQL 
    @Query(value = "SELECT * FROM terminales_pago", nativeQuery = true)
    List<Terminal> findSqlAll();
    
    // Consulta con SQL 
    @Query(value = "SELECT * FROM terminales_pago WHERE id = :id", nativeQuery = true)
    Terminal findSqlById(@Param("id") int id);

    // Consulta con SQL 
      @Query(value = "SELECT COUNT(*) FROM terminales_pago", nativeQuery = true)
    Long countSql(); 

    // Consulta con SQL 
   @Query(value = "SELECT * FROM terminales_pago WHERE id > :id", nativeQuery = true)
    List<Terminal> findSqlByIdGreaterThan(@Param("id") int id);
    
    
    
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
