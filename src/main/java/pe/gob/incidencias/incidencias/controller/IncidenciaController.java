package pe.gob.incidencias.incidencias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.IncidenciaRequest;
import pe.gob.incidencias.incidencias.dto.IncidenciaResponse;
import pe.gob.incidencias.incidencias.service.IncidenciaService;

/** Endpoints del ciudadano (rol USER). El usuario siempre sale del token, nunca del body. */
@RestController
@RequestMapping("/api/incidencias")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class IncidenciaController {

    private final IncidenciaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IncidenciaResponse crear(@Valid @RequestBody IncidenciaRequest req, Authentication auth) {
        return service.crear(req, auth.getName());
    }

    @GetMapping("/mias")
    public List<IncidenciaResponse> misIncidencias(Authentication auth) {
        return service.listarMias(auth.getName());
    }

    @GetMapping("/mias/{id}")
    public IncidenciaResponse miIncidencia(@PathVariable Long id, Authentication auth) {
        return service.obtenerMia(id, auth.getName());
    }

    @PutMapping("/mias/{id}")
    public IncidenciaResponse actualizar(@PathVariable Long id,
                                         @Valid @RequestBody IncidenciaRequest req,
                                         Authentication auth) {
        return service.actualizarMia(id, req, auth.getName());
    }

    @DeleteMapping("/mias/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, Authentication auth) {
        service.eliminarMia(id, auth.getName());
    }
}
