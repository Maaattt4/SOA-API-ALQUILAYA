package com.api.alquilaya.controllers;

import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.repositories.IMantenimientoRepository;
import com.api.alquilaya.services.VehiculoService; // Importar el servicio
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    private final IMantenimientoRepository mantenimientoRepository;
    private final VehiculoService vehiculoService; // Añadir el servicio

    public MantenimientoController(IMantenimientoRepository mantenimientoRepository, VehiculoService vehiculoService) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<MantenimientoModel> obtenerTodos() {
        return mantenimientoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> crearMantenimiento(@Valid @RequestBody MantenimientoModel mantenimiento, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            MantenimientoModel nuevoMantenimiento = mantenimientoRepository.save(mantenimiento);            
            vehiculoService.actualizarEstado(mantenimiento.getVehiculo().getIdVehiculo(), "Mantenimiento");
            return ResponseEntity.ok(nuevoMantenimiento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar el mantenimiento: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarMantenimiento(@PathVariable Long id) {
        try {
            // 1. Buscamos el registro de mantenimiento ANTES de borrarlo 
            // para saber a qué vehículo pertenece
            java.util.Optional<MantenimientoModel> mantenimientoOpt = mantenimientoRepository.findById(id);
            
            if (mantenimientoOpt.isPresent()) {
                // Guardamos el ID del vehículo asociado
                Long idVehiculo = mantenimientoOpt.get().getVehiculo().getIdVehiculo();
                
                // 2. Eliminamos el registro del historial de mantenimientos
                mantenimientoRepository.deleteById(id);
                
                // 3. Actualizamos el estado del vehículo a "Disponible"
                vehiculoService.actualizarEstado(idVehiculo, "Disponible");
                
                return ResponseEntity.ok(java.util.Map.of("mensaje", "Registro eliminado y vehículo liberado"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarMantenimiento(@PathVariable Long id) {
        try {
            MantenimientoModel mantenimiento = mantenimientoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mantenimiento no encontrado"));
                
            vehiculoService.actualizarEstado(mantenimiento.getVehiculo().getIdVehiculo(), "Disponible");
            
            return ResponseEntity.ok(Map.of("mensaje", "Vehículo marcado como Disponible"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}