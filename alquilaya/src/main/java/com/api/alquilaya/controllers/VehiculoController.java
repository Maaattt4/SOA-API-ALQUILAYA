package com.api.alquilaya.controllers;

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.VehiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoModel> obtenerTodos() {
        return vehiculoService.obtenerTodos();
    }

    @PostMapping
    public ResponseEntity<?> crearVehiculo(@Valid @RequestBody VehiculoModel vehiculo, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(vehiculoService.guardarVehiculo(vehiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVehiculo(@PathVariable Long id) {
        boolean eliminado = vehiculoService.eliminarVehiculo(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); // 204 No Content (Éxito al borrar)
        } else {
            return ResponseEntity.badRequest().build(); // 400 Bad Request (Fallo al borrar)
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable Long id, @RequestBody VehiculoModel vehiculoActualizado) {
        return vehiculoService.obtenerPorId(id).map(vehiculo -> {
            vehiculo.setMarca(vehiculoActualizado.getMarca());
            vehiculo.setModelo(vehiculoActualizado.getModelo());
            vehiculo.setAnio(vehiculoActualizado.getAnio());
            vehiculo.setMatricula(vehiculoActualizado.getMatricula());
            vehiculo.setTarifaDiaria(vehiculoActualizado.getTarifaDiaria());
            
            // Si también se actualiza la oficina
            if(vehiculoActualizado.getOficina() != null) {
                vehiculo.setOficina(vehiculoActualizado.getOficina());
            }
            
            return ResponseEntity.ok(vehiculoService.guardarVehiculo(vehiculo));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}