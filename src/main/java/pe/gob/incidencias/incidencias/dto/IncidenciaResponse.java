package pe.gob.incidencias.incidencias.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import pe.gob.incidencias.incidencias.entity.EstadoIncidencia;

public record IncidenciaResponse(
        Long id,
        String titulo,
        String descripcion,
        LocalDateTime fecha,
        BigDecimal latitud,
        BigDecimal longitud,
        String urlImagenEvidencia,
        EstadoIncidencia estado,
        Long distritoId,
        String distrito,
        String usuarioEmail) {
}
