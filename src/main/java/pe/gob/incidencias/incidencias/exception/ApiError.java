package pe.gob.incidencias.incidencias.exception;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> errores) {
}
