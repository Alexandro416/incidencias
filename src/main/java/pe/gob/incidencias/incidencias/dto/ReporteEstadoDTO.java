package pe.gob.incidencias.incidencias.dto;

import pe.gob.incidencias.incidencias.entity.EstadoIncidencia;

public record ReporteEstadoDTO(EstadoIncidencia estado, Long total) {
}
