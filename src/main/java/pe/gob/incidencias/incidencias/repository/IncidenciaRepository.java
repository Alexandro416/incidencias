package pe.gob.incidencias.incidencias.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.gob.incidencias.incidencias.dto.ReporteEstadoDTO;
import pe.gob.incidencias.incidencias.entity.EstadoIncidencia;
import pe.gob.incidencias.incidencias.entity.Incidencia;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    @Query("""
           SELECT i FROM Incidencia i
           JOIN FETCH i.distrito
           JOIN FETCH i.usuario u
           WHERE u.email = :email
           ORDER BY i.fecha DESC
           """)
    List<Incidencia> findByUsuarioEmail(@Param("email") String email);

    @Query("""
           SELECT i FROM Incidencia i
           JOIN FETCH i.distrito
           JOIN FETCH i.usuario u
           WHERE i.id = :id AND u.email = :email
           """)
    Optional<Incidencia> findByIdAndUsuarioEmail(@Param("id") Long id, @Param("email") String email);

    // Consulta JPQL personalizada: filtros opcionales por distrito y/o estado
    @Query("""
           SELECT i FROM Incidencia i
           JOIN FETCH i.distrito d
           JOIN FETCH i.usuario
           WHERE (:distritoId IS NULL OR d.id = :distritoId)
             AND (:estado IS NULL OR i.estado = :estado)
           ORDER BY i.fecha DESC
           """)
    List<Incidencia> buscarPorDistritoYEstado(@Param("distritoId") Long distritoId,
                                              @Param("estado") EstadoIncidencia estado);

    // Reporte: total de incidencias por estado
    @Query("""
           SELECT new pe.gob.incidencias.incidencias.dto.ReporteEstadoDTO(i.estado, COUNT(i))
           FROM Incidencia i
           GROUP BY i.estado
           """)
    List<ReporteEstadoDTO> reportePorEstado();
}
