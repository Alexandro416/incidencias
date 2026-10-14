package pe.gob.incidencias.incidencias.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.gob.incidencias.incidencias.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
