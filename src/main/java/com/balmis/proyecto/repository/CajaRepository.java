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

    @Query("""
        SELECT c
        FROM Caja c
        LEFT JOIN FETCH c.terminales
        WHERE LOWER(c.etiqueta) = LOWER(:etiqueta)
    """)
    Caja findByEtiquetaWithTerminales(@Param("etiqueta") String etiqueta);


    @Query("""
        SELECT c
        FROM Caja c
        LEFT JOIN FETCH c.terminales
        WHERE c.id = :id
    """)
    Caja findByIdWithTerminales(@Param("id") Integer id);

    @Query(value = """
        SELECT MAX(max_capacity)
        FROM cajas
        WHERE modelo_producto IS NOT NULL
          AND TRIM(modelo_producto) <> ''
          AND UPPER(TRIM(modelo_producto)) LIKE CONCAT('%', UPPER(TRIM(:marca)), '%')
    """, nativeQuery = true)
    Integer findMaxCapacityByMarca(@Param("marca") String marca);

    @Query(value = "SELECT COUNT(*) FROM terminales_pago WHERE caja_id = :cajaId", nativeQuery = true)
    Long countTerminalesByCajaId(@Param("cajaId") int cajaId);

    @Query(value = "SELECT * FROM cajas WHERE palet_id IS NULL ORDER BY id", nativeQuery = true)
    List<Caja> findCajasSinPalet();

    @Query(value = """
        SELECT *
        FROM cajas
        WHERE palet_id IS NULL
          AND modelo_producto IS NOT NULL
          AND TRIM(modelo_producto) <> ''
          AND UPPER(TRIM(modelo_producto)) LIKE CONCAT(UPPER(TRIM(:marca)), '%')
        ORDER BY id
    """, nativeQuery = true)
    List<Caja> findCajasSinPaletByMarca(@Param("marca") String marca);

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
