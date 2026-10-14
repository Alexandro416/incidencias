package pe.gob.incidencias.incidencias.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import pe.gob.incidencias.incidencias.entity.Usuario;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final long expirationMinutes;

    public JwtService(JwtEncoder encoder,
                      @Value("${app.jwt.expiration-minutes:120}") long expirationMinutes) {
        this.encoder = encoder;
        this.expirationMinutes = expirationMinutes;
    }

    public String generarToken(Usuario usuario) {
        Instant ahora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("incidencias-lima")
                .subject(usuario.getEmail())
                .issuedAt(ahora)
                .expiresAt(ahora.plus(expirationMinutes, ChronoUnit.MINUTES))
                .claim("rol", usuario.getRol().name())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long getExpiracionSegundos() {
        return expirationMinutes * 60;
    }
}
