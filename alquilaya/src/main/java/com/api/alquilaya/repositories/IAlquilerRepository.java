package com.api.alquilaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.alquilaya.models.AlquilerModel;

@Repository
public interface IAlquilerRepository extends JpaRepository<AlquilerModel, Long> {
}
