package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.Pasillo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PasilloRepository extends JpaRepository<Pasillo, Integer> {

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
    Optional<Pasillo> findByNumeroPasillo(Integer numeroPasillo);
    
    // Consulta con SQL 
    @Query(value = "SELECT * FROM pasillos", nativeQuery = true)
    List<Pasillo> findSqlAll();

    @Query(value = "SELECT * FROM pasillos WHERE id = :id", nativeQuery = true)
    Pasillo findSqlById(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM pasillos", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM pasillos WHERE id > :id", nativeQuery = true)
    List<Pasillo> findSqlByIdGreaterThan(@Param("id") int id);
    
    
    
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
