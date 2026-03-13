/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.UbicacionAlmacen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UbicacionAlmacenRepository extends JpaRepository<UbicacionAlmacen, Integer> {

    Optional<UbicacionAlmacen> findByReferencia(String referencia);

    @Query(value = "SELECT * FROM ubicaciones_almacen", nativeQuery = true)
    List<UbicacionAlmacen> findSqlAll();

    @Query(value = "SELECT * FROM ubicaciones_almacen WHERE id = :id", nativeQuery = true)
    UbicacionAlmacen findSqlById(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM ubicaciones_almacen", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM ubicaciones_almacen WHERE id > :id", nativeQuery = true)
    List<UbicacionAlmacen> findSqlByIdGreaterThan(@Param("id") int id);
}
