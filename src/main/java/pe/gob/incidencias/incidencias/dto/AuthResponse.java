package pe.gob.incidencias.incidencias.dto;

import pe.gob.incidencias.incidencias.entity.Rol;

public record AuthResponse(
        String token,
        String tipo,
        long expiraEnSegundos,
        String email,
        Rol rol) {
}
