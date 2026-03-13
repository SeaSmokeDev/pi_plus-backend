package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.Palet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaletRepository extends JpaRepository<Palet, Integer> {

    Optional<Palet> findByCodigoMarca(String codigoMarca);

    @Query(value = "SELECT * FROM palets", nativeQuery = true)
    List<Palet> findSqlAll();

    @Query(value = "SELECT * FROM palets WHERE id = :id", nativeQuery = true)
    Palet findSqlById(@Param("id") int id);

    @Query(value = "SELECT COUNT(*) FROM palets", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM palets WHERE id > :id", nativeQuery = true)
    List<Palet> findSqlByIdGreaterThan(@Param("id") int id);
}
