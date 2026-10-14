package pe.gob.incidencias.incidencias.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IncidenciaRequest(
        @NotBlank @Size(max = 150) String titulo,
        @NotBlank String descripcion,
        @NotNull @DecimalMin("-90") @DecimalMax("90") BigDecimal latitud,
        @NotNull @DecimalMin("-180") @DecimalMax("180") BigDecimal longitud,
        @Size(max = 500) String urlImagenEvidencia,
        @NotNull Long distritoId) {
}
