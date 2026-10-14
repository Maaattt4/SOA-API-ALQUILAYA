package com.api.alquilaya.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; 
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vehiculo")
public class VehiculoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "La marca no puede estar vacía")
    private String marca;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "El modelo no puede estar vacío")
    private String modelo;

    @Column(nullable = false)
    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @Column(nullable = false, unique = true, length = 20)
    @NotBlank(message = "La matrícula es obligatoria")
    private String matricula;

    @Column(name = "tarifa_diaria", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "La tarifa diaria es obligatoria")
    @Positive(message = "La tarifa debe ser mayor a cero")
    private BigDecimal tarifaDiaria;

    @Column(nullable = false, length = 20)
    private String estado; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oficina", nullable = true)
    @JsonIgnoreProperties("vehiculos")
    private OficinaModel oficina;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vehiculo")
    private List<MantenimientoModel> mantenimientos;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("vehiculo")
    private List<AlquilerModel> alquileres;

    public VehiculoModel() {
    }

    public VehiculoModel(String marca, String modelo, Integer anio, String matricula, BigDecimal tarifaDiaria, String estado) {
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.matricula = matricula;
        this.tarifaDiaria = tarifaDiaria;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<AlquilerModel> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerModel> alquileres) {
        this.alquileres = alquileres;
    }

    public OficinaModel getOficina() {
        return oficina;
    }

    public void setOficina(OficinaModel oficina) {
        this.oficina = oficina;
    }

    public List<MantenimientoModel> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<MantenimientoModel> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public String obtenerDetalle() {
    return this.marca + " " + this.modelo + " (" + this.anio + ") - Matrícula: " + this.matricula;
    }

    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

}