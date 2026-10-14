package pe.gob.incidencias.incidencias.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DistritoRequest(@NotBlank @Size(max = 100) String nombre) {
}
