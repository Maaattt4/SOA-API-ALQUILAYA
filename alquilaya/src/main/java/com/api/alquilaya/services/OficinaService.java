package com.api.alquilaya.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.alquilaya.models.OficinaModel;
import com.api.alquilaya.repositories.IOficinaRepository;

@Service
public class OficinaService {

    @Autowired
    private IOficinaRepository oficinaRepository;

    public List<OficinaModel> obtenerTodas() {
        return oficinaRepository.findAll();
    }

    public Optional<OficinaModel> obtenerPorId(Long id) {
        return oficinaRepository.findById(id);
    }

    public OficinaModel guardarOficina(OficinaModel oficina) {
        return oficinaRepository.save(oficina);
    }

    public boolean eliminarOficina(Long id) {
        try {
            if (oficinaRepository.existsById(id)) {
                oficinaRepository.deleteById(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}