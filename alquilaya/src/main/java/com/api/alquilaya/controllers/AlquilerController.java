package com.api.alquilaya.controllers;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.services.AlquilerService;
import com.api.alquilaya.services.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping
    public List<AlquilerModel> obtenerTodos() {
        return alquilerService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlquilerModel> obtenerPorId(@PathVariable Long id) {
        Optional<AlquilerModel> alquiler = alquilerService.obtenerPorId(id);
        return alquiler.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> guardarAlquiler(@Valid @RequestBody AlquilerModel alquiler, BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            AlquilerModel nuevoAlquiler = alquilerService.guardarAlquiler(alquiler);
            return ResponseEntity.ok(nuevoAlquiler);
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, String> errorRespuesta = new HashMap<>();
            errorRespuesta.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorRespuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAlquiler(@PathVariable Long id) {
        try {
            boolean eliminado = alquilerService.eliminarAlquiler(id);
            if (eliminado) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            // Captura nuestra regla del estado "Completado"
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{idAlquiler}/devolver")
    public ResponseEntity<?> devolverVehiculo(@PathVariable Long idAlquiler) {
        try {
            AlquilerModel alquiler = alquilerService.obtenerPorId(idAlquiler)
                .orElseThrow(() -> new IllegalArgumentException("Alquiler no encontrado"));
                
            // 1. Liberar el vehículo
            vehiculoService.actualizarEstado(alquiler.getVehiculo().getIdVehiculo(), "Disponible");
            
            // 2. Marcar el contrato como Completado
            alquilerService.finalizarAlquiler(idAlquiler);
            
            return ResponseEntity.ok(Map.of("mensaje", "Vehículo devuelto con éxito y marcado como Disponible"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}