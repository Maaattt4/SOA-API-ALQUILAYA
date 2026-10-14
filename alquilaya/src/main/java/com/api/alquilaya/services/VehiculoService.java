package com.api.alquilaya.services;

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IVehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final IVehiculoRepository vehiculoRepository;

    public VehiculoService(IVehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<VehiculoModel> obtenerTodos() {
        return vehiculoRepository.findAll();
    }

    public VehiculoModel guardarVehiculo(VehiculoModel vehiculo) {
        // Por defecto, un vehículo nuevo debería estar disponible
        if (vehiculo.getEstado() == null || vehiculo.getEstado().isEmpty()) {
            vehiculo.setEstado("Disponible");
        }
        return vehiculoRepository.save(vehiculo);
    }

    public Optional<VehiculoModel> obtenerPorId(Long id) {
        return vehiculoRepository.findById(id);
    }
    
    public VehiculoModel actualizarEstado(Long id, String nuevoEstado) {
        VehiculoModel vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));
        vehiculo.setEstado(nuevoEstado);
        return vehiculoRepository.save(vehiculo);
    }

    public boolean eliminarVehiculo(Long id) {
            try {
                if (vehiculoRepository.existsById(id)) {
                    // Borrado físico de la base de datos
                    vehiculoRepository.deleteById(id);
                    return true;
                }
                return false;
            } catch (Exception e) {
                // Retorna false si falla por tener alquileres o mantenimientos asociados en la base de datos
                return false; 
            }
        }
}