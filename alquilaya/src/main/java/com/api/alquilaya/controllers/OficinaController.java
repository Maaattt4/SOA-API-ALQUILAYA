package com.api.alquilaya.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.alquilaya.models.OficinaModel;
import com.api.alquilaya.services.OficinaService;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaController {

    @Autowired
    private OficinaService oficinaService;

    @GetMapping
    public List<OficinaModel> obtenerTodas() {
        return oficinaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OficinaModel> obtenerPorId(@PathVariable Long id) {
        Optional<OficinaModel> oficina = oficinaService.obtenerPorId(id);
        return oficina.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public OficinaModel guardarOficina(@RequestBody OficinaModel oficina) {
        return oficinaService.guardarOficina(oficina);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOficina(@PathVariable Long id) {
        boolean eliminado = oficinaService.eliminarOficina(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}