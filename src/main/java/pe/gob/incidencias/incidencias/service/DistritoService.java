package pe.gob.incidencias.incidencias.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.DistritoRequest;
import pe.gob.incidencias.incidencias.dto.DistritoResponse;
import pe.gob.incidencias.incidencias.entity.Distrito;
import pe.gob.incidencias.incidencias.exception.BusinessRuleException;
import pe.gob.incidencias.incidencias.exception.ResourceNotFoundException;
import pe.gob.incidencias.incidencias.repository.DistritoRepository;

@Service
@RequiredArgsConstructor
public class DistritoService {

    private final DistritoRepository distritoRepository;

    @Transactional(readOnly = true)
    public List<DistritoResponse> listar() {
        return distritoRepository.findAllByOrderByNombreAsc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public DistritoResponse crear(DistritoRequest req) {
        String nombre = req.nombre().trim();
        if (distritoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessRuleException("El distrito ya existe");
        }
        Distrito d = new Distrito();
        d.setNombre(nombre);
        return toResponse(distritoRepository.save(d));
    }

    @Transactional
    public DistritoResponse actualizar(Long id, DistritoRequest req) {
        Distrito d = buscar(id);
        String nombre = req.nombre().trim();
        if (!d.getNombre().equalsIgnoreCase(nombre) && distritoRepository.existsByNombreIgnoreCase(nombre)) {
            throw new BusinessRuleException("El distrito ya existe");
        }
        d.setNombre(nombre);
        return toResponse(d);
    }

    @Transactional
    public void eliminar(Long id) {
        Distrito d = buscar(id);
        distritoRepository.delete(d);
        distritoRepository.flush();   // fuerza el error de FK aquí si el distrito tiene incidencias
    }

    private Distrito buscar(Long id) {
        return distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));
    }

    private DistritoResponse toResponse(Distrito d) {
        return new DistritoResponse(d.getId(), d.getNombre());
    }
}
