package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.EstadoTerminal;
import com.balmis.proyecto.model.Terminal;
import com.balmis.proyecto.model.dtos.TerminalMarcaModeloDTO;
import java.util.Collection;
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

    List<Terminal> findByNumeroSerieIn(Collection<String> numerosSerie);

    List<Terminal> findByEstado(EstadoTerminal estado);

    // Consulta con SQL
    @Query(value = "SELECT * FROM terminales_pago", nativeQuery = true)
    List<Terminal> findSqlAll();

    // Consulta con SQL
    @Query(value = "SELECT * FROM terminales_pago WHERE id = :id", nativeQuery = true)
    Terminal findSqlById(@Param("id") int id);

    // Consulta con SQL
    @Query(value = "SELECT * FROM terminales_pago WHERE UPPER(numero_serie) = UPPER(:numeroSerie)", nativeQuery = true)
    Terminal findSqlByNumeroSerie(@Param("numeroSerie") String numeroSerie);

    // Consulta con SQL
    @Query(value = "SELECT COUNT(*) FROM terminales_pago", nativeQuery = true)
    Long countSql();

    @Query(value = """
      SELECT DISTINCT marca, modelo
      FROM terminales_pago
      WHERE marca IS NOT NULL
        AND TRIM(marca) <> ''
        AND modelo IS NOT NULL
        AND TRIM(modelo) <> ''
      ORDER BY marca, modelo
      """, nativeQuery = true)
    List<Object[]> findDistinctMarcaAndModelo();

    @Query(value = """
      SELECT DISTINCT marca
      FROM terminales_pago
      WHERE marca IS NOT NULL
        AND TRIM(marca) <> ''
      ORDER BY marca ASC
      """, nativeQuery = true)
    List<String> findDistinctMarcas();

    @Query(value = """
      SELECT DISTINCT modelo
      FROM terminales_pago
      WHERE UPPER(TRIM(marca)) = UPPER(TRIM(:marca))
        AND modelo IS NOT NULL
        AND TRIM(modelo) <> ''
      ORDER BY modelo ASC
      """, nativeQuery = true)
    List<String> findDistinctModelosByMarca(@Param("marca") String marca);

    @Query(value = """
        SELECT numero_serie
        FROM terminales_pago
        WHERE numero_serie LIKE 'SN%'
        ORDER BY CAST(SUBSTRING(numero_serie, 3) AS UNSIGNED) DESC
        LIMIT 1
    """, nativeQuery = true)
    String findLastNumeroSerie();

    @Query("""
        SELECT DISTINCT new com.balmis.proyecto.model.dtos.TerminalMarcaModeloDTO(
            t.marca,
            t.modelo
        )
        FROM Terminal t
        WHERE t.marca IS NOT NULL
          AND t.modelo IS NOT NULL
        ORDER BY t.marca ASC, t.modelo ASC
    """)
    List<TerminalMarcaModeloDTO> findDistinctMarcasModelos();

    @Query("""
        SELECT t
        FROM Terminal t
        LEFT JOIN FETCH t.caja c
        WHERE t.numeroSerie = :numeroSerie
    """)
    Optional<Terminal> findByNumeroSerieWithCaja(
            @Param("numeroSerie") String numeroSerie
    );

    // **********************************************************
    // Actualizaciones
    // **********************************************************
    // ****************************
    // Métodos HEREDADOS
    // ****************************
    /*
   * delete(User)
   * deleteById(id)
   * deleteAll()
   * 
   * save(User)
     */
}
