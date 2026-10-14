package pe.gob.incidencias.incidencias.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.DistritoRequest;
import pe.gob.incidencias.incidencias.dto.DistritoResponse;
import pe.gob.incidencias.incidencias.service.DistritoService;

@RestController
@RequestMapping("/api/admin/distritos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminDistritoController {

    private final DistritoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DistritoResponse crear(@Valid @RequestBody DistritoRequest req) {
        return service.crear(req);
    }

    @PutMapping("/{id}")
    public DistritoResponse actualizar(@PathVariable Long id, @Valid @RequestBody DistritoRequest req) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
