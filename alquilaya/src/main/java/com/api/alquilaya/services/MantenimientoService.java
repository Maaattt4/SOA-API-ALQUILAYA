package com.api.alquilaya.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.repositories.IMantenimientoRepository;

@Service
public class MantenimientoService {

    @Autowired
    private IMantenimientoRepository mantenimientoRepository;

    public List<MantenimientoModel> obtenerTodos() {
        return mantenimientoRepository.findAll();
    }

    public Optional<MantenimientoModel> obtenerPorId(Long id) {
        return mantenimientoRepository.findById(id);
    }

    public MantenimientoModel guardarMantenimiento(MantenimientoModel mantenimiento) {
        return mantenimientoRepository.save(mantenimiento);
    }

    public boolean eliminarMantenimiento(Long id) {
        try {
            if (mantenimientoRepository.existsById(id)) {
                mantenimientoRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}