package pe.gob.incidencias.incidencias.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import pe.gob.incidencias.incidencias.entity.Rol;
import pe.gob.incidencias.incidencias.entity.Usuario;
import pe.gob.incidencias.incidencias.repository.UsuarioRepository;

/** Crea el primer ADMIN al arrancar (la contraseña se guarda cifrada con BCrypt). */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner crearAdmin(UsuarioRepository repo,
                                 PasswordEncoder encoder,
                                 @Value("${app.admin.email}") String email,
                                 @Value("${app.admin.password}") String password,
                                 @Value("${app.admin.nombre}") String nombre) {
        return args -> {
            String emailNormalizado = email.toLowerCase();
            if (!repo.existsByEmail(emailNormalizado)) {
                Usuario admin = new Usuario();
                admin.setNombre(nombre);
                admin.setEmail(emailNormalizado);
                admin.setPassword(encoder.encode(password));
                admin.setRol(Rol.ADMIN);
                repo.save(admin);
            }
        };
    }
}
