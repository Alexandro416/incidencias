package pe.gob.incidencias.incidencias.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pe.gob.incidencias.incidencias.dto.DistritoResponse;
import pe.gob.incidencias.incidencias.service.DistritoService;

/** Lista de distritos para llenar el combo al reportar (cualquier usuario autenticado). */
@RestController
@RequestMapping("/api/distritos")
@RequiredArgsConstructor
public class DistritoController {

    private final DistritoService service;

    @GetMapping
    public List<DistritoResponse> listar() {
        return service.listar();
    }
}
