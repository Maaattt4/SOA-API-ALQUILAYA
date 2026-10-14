package com.api.alquilaya.models;

import jakarta.persistence.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "oficina")
public class OficinaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina")
    private Long idOficina;

    @Column(name = "nombre_oficina", nullable = false, length = 100)
    private String nombreOficina;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 100)
    private String region;

    @OneToMany(mappedBy = "oficina", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("oficina")
    private List<VehiculoModel> vehiculos;

    public OficinaModel() {}

    public Long getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Long idOficina) {
        this.idOficina = idOficina;
    }

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<VehiculoModel> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(List<VehiculoModel> vehiculos) {
        this.vehiculos = vehiculos;
    }

    
}