package pe.gob.incidencias.incidencias.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.gob.incidencias.incidencias.entity.Distrito;

public interface DistritoRepository extends JpaRepository<Distrito, Long> {

    List<Distrito> findAllByOrderByNombreAsc();

    boolean existsByNombreIgnoreCase(String nombre);
}
