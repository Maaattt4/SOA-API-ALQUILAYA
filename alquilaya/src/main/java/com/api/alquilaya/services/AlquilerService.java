package com.api.alquilaya.services;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IAlquilerRepository;
import com.api.alquilaya.repositories.IClienteRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService {

    private final IAlquilerRepository alquilerRepository;
    private final IVehiculoRepository vehiculoRepository;
    private final IClienteRepository clienteRepository; // Añadimos el repositorio de Cliente

    public AlquilerService(IAlquilerRepository alquilerRepository, IVehiculoRepository vehiculoRepository, IClienteRepository clienteRepository) {
        this.alquilerRepository = alquilerRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<AlquilerModel> obtenerTodos() {
        return alquilerRepository.findAll();
    }

    public Optional<AlquilerModel> obtenerPorId(Long id) {
        return alquilerRepository.findById(id);
    }

    public boolean eliminarAlquiler(Long id) {
        Optional<AlquilerModel> alquilerOpt = alquilerRepository.findById(id);
        
        if (alquilerOpt.isPresent()) {
            AlquilerModel alquiler = alquilerOpt.get();
            
            // Regla de negocio: Solo eliminar si está Completado
            if (!"Completado".equalsIgnoreCase(alquiler.getEstado())) {
                throw new IllegalStateException("Solo se pueden eliminar contratos que ya están en estado Completado.");
            }
            
            alquilerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public AlquilerModel guardarAlquiler(AlquilerModel alquiler) {
        
        if (alquiler.getFechaAlquiler() == null || alquiler.getFechaDevolucion() == null) {
            throw new IllegalArgumentException("Las fechas de alquiler y devolución son obligatorias");
        }

        ClienteModel clienteReal = clienteRepository.findById(alquiler.getCliente().getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("El Cliente con ese ID no existe en la BD"));
        alquiler.setCliente(clienteReal); 

        VehiculoModel vehiculoReal = vehiculoRepository.findById(alquiler.getVehiculo().getIdVehiculo())
                .orElseThrow(() -> new IllegalArgumentException("El Vehículo con ese ID no existe en la BD"));
        alquiler.setVehiculo(vehiculoReal); 

        if (!vehiculoReal.getEstado().equalsIgnoreCase("Disponible")) {
            throw new IllegalStateException("El vehículo no está disponible para alquilar. Estado actual: " + vehiculoReal.getEstado());
        }

        long diasAlquiler = ChronoUnit.DAYS.between(alquiler.getFechaAlquiler(), alquiler.getFechaDevolucion());
        if (diasAlquiler <= 0) {
            throw new IllegalArgumentException("La fecha de devolución debe ser posterior a la fecha de alquiler");
        }

        BigDecimal precioTotal = vehiculoReal.getTarifaDiaria().multiply(BigDecimal.valueOf(diasAlquiler));
        alquiler.setPrecioTotal(precioTotal);

        vehiculoReal.setEstado("Alquilado");
        vehiculoRepository.save(vehiculoReal);

        alquiler.setEstado("Activo"); 

        return alquilerRepository.save(alquiler);
    }

    public void finalizarAlquiler(Long id) {
        AlquilerModel alquiler = alquilerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Alquiler no encontrado"));
        alquiler.setEstado("Completado");
        alquilerRepository.save(alquiler);
    }
}