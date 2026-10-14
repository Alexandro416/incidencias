package pe.gob.incidencias.incidencias.dto;

import jakarta.validation.constraints.NotNull;
import pe.gob.incidencias.incidencias.entity.EstadoIncidencia;

public record EstadoUpdateRequest(@NotNull EstadoIncidencia estado) {
}
