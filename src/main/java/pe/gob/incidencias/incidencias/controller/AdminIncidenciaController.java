package pe.gob.incidencias.incidencias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.EstadoUpdateRequest;
import pe.gob.incidencias.incidencias.dto.IncidenciaResponse;
import pe.gob.incidencias.incidencias.dto.ReporteEstadoDTO;
import pe.gob.incidencias.incidencias.entity.EstadoIncidencia;
import pe.gob.incidencias.incidencias.service.IncidenciaService;

/** Panel administrativo (rol ADMIN). */
@RestController
@RequestMapping("/api/admin/incidencias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminIncidenciaController {

    private final IncidenciaService service;

    @GetMapping
    public List<IncidenciaResponse> listar(@RequestParam(required = false) Long distritoId,
                                           @RequestParam(required = false) EstadoIncidencia estado) {
        return service.listarTodas(distritoId, estado);
    }

    @GetMapping("/reporte")
    public List<ReporteEstadoDTO> reporte() {
        return service.reporte();
    }

    @GetMapping("/{id}")
    public IncidenciaResponse detalle(@PathVariable Long id) {
        return service.obtener(id);
    }

    @PatchMapping("/{id}/estado")
    public IncidenciaResponse actualizarEstado(@PathVariable Long id,
                                               @Valid @RequestBody EstadoUpdateRequest req) {
        return service.actualizarEstado(id, req.estado());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
