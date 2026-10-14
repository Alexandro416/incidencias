package pe.gob.incidencias.incidencias.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.IncidenciaRequest;
import pe.gob.incidencias.incidencias.dto.IncidenciaResponse;
import pe.gob.incidencias.incidencias.dto.ReporteEstadoDTO;
import pe.gob.incidencias.incidencias.entity.Distrito;
import pe.gob.incidencias.incidencias.entity.EstadoIncidencia;
import pe.gob.incidencias.incidencias.entity.Incidencia;
import pe.gob.incidencias.incidencias.entity.Usuario;
import pe.gob.incidencias.incidencias.exception.BusinessRuleException;
import pe.gob.incidencias.incidencias.exception.ResourceNotFoundException;
import pe.gob.incidencias.incidencias.repository.DistritoRepository;
import pe.gob.incidencias.incidencias.repository.IncidenciaRepository;
import pe.gob.incidencias.incidencias.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final DistritoRepository distritoRepository;
    private final UsuarioRepository usuarioRepository;

    // ===================== USER =====================

    @Transactional
    public IncidenciaResponse crear(IncidenciaRequest req, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        Incidencia inc = Incidencia.builder()
                .titulo(req.titulo().trim())
                .descripcion(req.descripcion().trim())
                .fecha(LocalDateTime.now())
                .latitud(req.latitud())
                .longitud(req.longitud())
                .urlImagenEvidencia(req.urlImagenEvidencia())
                .estado(EstadoIncidencia.PENDIENTE)
                .usuario(usuario)
                .distrito(buscarDistrito(req.distritoId()))
                .build();
        return toResponse(incidenciaRepository.save(inc));
    }

    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listarMias(String email) {
        return incidenciaRepository.findByUsuarioEmail(email).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public IncidenciaResponse obtenerMia(Long id, String email) {
        return toResponse(buscarMia(id, email));
    }

    @Transactional
    public IncidenciaResponse actualizarMia(Long id, IncidenciaRequest req, String email) {
        Incidencia inc = buscarMia(id, email);
        exigirPendiente(inc, "modificar");
        inc.setTitulo(req.titulo().trim());
        inc.setDescripcion(req.descripcion().trim());
        inc.setLatitud(req.latitud());
        inc.setLongitud(req.longitud());
        inc.setUrlImagenEvidencia(req.urlImagenEvidencia());
        if (!inc.getDistrito().getId().equals(req.distritoId())) {
            inc.setDistrito(buscarDistrito(req.distritoId()));
        }
        return toResponse(inc);
    }

    @Transactional
    public void eliminarMia(Long id, String email) {
        Incidencia inc = buscarMia(id, email);
        exigirPendiente(inc, "eliminar");
        incidenciaRepository.delete(inc);
    }

    // ===================== ADMIN =====================

    @Transactional(readOnly = true)
    public List<IncidenciaResponse> listarTodas(Long distritoId, EstadoIncidencia estado) {
        return incidenciaRepository.buscarPorDistritoYEstado(distritoId, estado)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public IncidenciaResponse obtener(Long id) {
        return toResponse(buscar(id));
    }

    @Transactional
    public IncidenciaResponse actualizarEstado(Long id, EstadoIncidencia nuevoEstado) {
        Incidencia inc = buscar(id);
        inc.setEstado(nuevoEstado);
        return toResponse(inc);
    }

    @Transactional
    public void eliminar(Long id) {
        incidenciaRepository.delete(buscar(id));
    }

    /** Total por estado; incluye los estados sin incidencias con total 0. */
    @Transactional(readOnly = true)
    public List<ReporteEstadoDTO> reporte() {
        Map<EstadoIncidencia, Long> conteo = new EnumMap<>(EstadoIncidencia.class);
        incidenciaRepository.reportePorEstado().forEach(r -> conteo.put(r.estado(), r.total()));
        return Arrays.stream(EstadoIncidencia.values())
                .map(e -> new ReporteEstadoDTO(e, conteo.getOrDefault(e, 0L)))
                .toList();
    }

    // ===================== helpers =====================

    private Incidencia buscar(Long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia no encontrada"));
    }

    /** Si la incidencia no es del usuario responde 404 (no revela que existe). */
    private Incidencia buscarMia(Long id, String email) {
        return incidenciaRepository.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Incidencia no encontrada"));
    }

    private Distrito buscarDistrito(Long id) {
        return distritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));
    }

    private void exigirPendiente(Incidencia inc, String accion) {
        if (inc.getEstado() != EstadoIncidencia.PENDIENTE) {
            throw new BusinessRuleException(
                    "Solo se puede " + accion + " una incidencia en estado PENDIENTE");
        }
    }

    private IncidenciaResponse toResponse(Incidencia i) {
        return new IncidenciaResponse(i.getId(), i.getTitulo(), i.getDescripcion(), i.getFecha(),
                i.getLatitud(), i.getLongitud(), i.getUrlImagenEvidencia(), i.getEstado(),
                i.getDistrito().getId(), i.getDistrito().getNombre(), i.getUsuario().getEmail());
    }
}
