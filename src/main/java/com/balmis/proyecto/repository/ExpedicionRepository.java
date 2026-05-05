package com.balmis.proyecto.repository;

import com.balmis.proyecto.model.EstadoExpedicion;
import com.balmis.proyecto.model.Expedicion;
import com.balmis.proyecto.model.dtos.ExpedicionListDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpedicionRepository extends JpaRepository<Expedicion, Integer> {

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
    @Query(value = "SELECT * FROM expediciones", nativeQuery = true)
    List<Expedicion> findSqlAll();

    // Consulta con SQL mapeado
    @Query(value = "SELECT * FROM expediciones WHERE id = :id", nativeQuery = true)
    Expedicion findSqlById(@Param("id") int empleadoId);

    // Consulta con SQL mapeado
    @Query(value = """
    SELECT e.*
    FROM expediciones e
    JOIN usuarios u ON e.usuario_id = u.id
    JOIN users_security s ON s.usuario_id = u.id
    WHERE LOWER(s.username) = LOWER(:username)
    """, nativeQuery = true)
    List<Expedicion> findSqlByNombreUsuario(@Param("username") String username);

    // Consulta con SQL mapeado
    @Query(value = " SELECT * FROM expediciones WHERE LOWER(direccion_destino) LIKE LOWER(CONCAT('%', :contiene, '%'))", nativeQuery = true)
    List<Expedicion> findSqlLikeDireccion(@Param("contiene") String contiene);

    // Consulta con SQL mapeado
    @Query(value = "SELECT COUNT(*) as expediciones FROM expediciones", nativeQuery = true)
    Long countSql();

    @Query(value = "SELECT * FROM expediciones WHERE DATE(fecha_creacion) = CURDATE() OR DATE(fecha_modificacion) = CURDATE()", nativeQuery = true)
    List<Expedicion> findSqlAllToday();

    @Query("""
    SELECT e
    FROM Expedicion e
    LEFT JOIN e.usuario u
    WHERE (:fechaCreacionDesde IS NULL OR e.fechaCreacion >= :fechaCreacionDesde)
      AND (:fechaCreacionHasta IS NULL OR e.fechaCreacion < :fechaCreacionHasta)
      AND (:fechaRecepcionDesde IS NULL OR e.fechaRecepcion >= :fechaRecepcionDesde)
      AND (:fechaRecepcionHasta IS NULL OR e.fechaRecepcion < :fechaRecepcionHasta)
      AND (:usuarioId IS NULL OR u.id = :usuarioId)
      AND (:destino IS NULL OR LOWER(e.direccionDestino) LIKE LOWER(CONCAT('%', :destino, '%')))
      AND (:estado IS NULL OR e.estado = :estado)
    ORDER BY e.fechaCreacion DESC
    """)
    List<Expedicion> search(
            @Param("fechaCreacionDesde") LocalDateTime fechaCreacionDesde,
            @Param("fechaCreacionHasta") LocalDateTime fechaCreacionHasta,
            @Param("fechaRecepcionDesde") LocalDateTime fechaRecepcionDesde,
            @Param("fechaRecepcionHasta") LocalDateTime fechaRecepcionHasta,
            @Param("usuarioId") Integer usuarioId,
            @Param("destino") String destino,
            @Param("estado") EstadoExpedicion estado
    );

    @Query("""
    SELECT new com.balmis.proyecto.model.dtos.ExpedicionListDTO(
        e.id,
        e.fechaCreacion,
        e.direccionDestino,
        us.username,
        e.estado
    )
    FROM Expedicion e
    LEFT JOIN e.usuario u
    LEFT JOIN u.usuarioSecurity us
    ORDER BY e.fechaCreacion DESC
    """)
    List<ExpedicionListDTO> findAllForList();

    @Query("""
    SELECT new com.balmis.proyecto.model.dtos.ExpedicionListDTO(
            e.id,
            e.fechaCreacion,
            e.direccionDestino,
            us.username,
            e.estado
        )
        FROM Expedicion e
        LEFT JOIN e.usuario u
        LEFT JOIN u.usuarioSecurity us
        WHERE DATE(e.fechaCreacion) = CURRENT_DATE
           OR DATE(e.fechaModificacion) = CURRENT_DATE
        ORDER BY e.fechaCreacion DESC
    """)
    List<ExpedicionListDTO> findTodayForList(
//            @Param("inicioDia") LocalDateTime inicioDia,
//            @Param("finDia") LocalDateTime finDia
    );

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
