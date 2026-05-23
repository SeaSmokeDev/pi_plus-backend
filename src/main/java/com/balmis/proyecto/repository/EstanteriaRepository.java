/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.Estanteria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface EstanteriaRepository extends JpaRepository<Estanteria, Integer>{
    Optional<Estanteria> findByCodigo(String codigo);

    @Query(value = "SELECT * FROM estanterias", nativeQuery = true)
    List<Estanteria> findSqlAll();

    @Query(value = "SELECT * FROM estanterias WHERE id = :id", nativeQuery = true)
    Estanteria findSqlById(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM estanterias", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM estanterias WHERE id > :id", nativeQuery = true)
    List<Estanteria> findSqlByIdGreaterThan(@Param("id") int id);

    @Query("""
        SELECT e
        FROM Estanteria e
        JOIN FETCH e.pasillo p
        ORDER BY p.numeroPasillo ASC, e.codigo ASC
    """)
    List<Estanteria> findAllWithPasilloForMapa();

    @Query("""
        SELECT e
        FROM Estanteria e
        JOIN FETCH e.pasillo p
        WHERE p.id = :pasilloId
        ORDER BY p.numeroPasillo ASC, e.codigo ASC
    """)
    List<Estanteria> findAllWithPasilloForMapaByPasillo(@Param("pasilloId") Integer pasilloId);
    
}
