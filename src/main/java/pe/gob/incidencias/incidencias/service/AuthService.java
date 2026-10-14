package pe.gob.incidencias.incidencias.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.AuthResponse;
import pe.gob.incidencias.incidencias.dto.LoginRequest;
import pe.gob.incidencias.incidencias.dto.RegistroRequest;
import pe.gob.incidencias.incidencias.entity.Rol;
import pe.gob.incidencias.incidencias.entity.Usuario;
import pe.gob.incidencias.incidencias.exception.BusinessRuleException;
import pe.gob.incidencias.incidencias.repository.UsuarioRepository;
import pe.gob.incidencias.incidencias.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse registrar(RegistroRequest req) {
        String email = req.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessRuleException("El email ya está registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(req.nombre().trim());
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(req.password()));
        usuario.setRol(Rol.USER);   // el rol nunca viene del cliente
        usuarioRepository.save(usuario);
        return respuesta(usuario);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String email = req.email().trim().toLowerCase();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, req.password()));
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));
        return respuesta(usuario);
    }

    private AuthResponse respuesta(Usuario u) {
        return new AuthResponse(jwtService.generarToken(u), "Bearer",
                jwtService.getExpiracionSegundos(), u.getEmail(), u.getRol());
    }
}
