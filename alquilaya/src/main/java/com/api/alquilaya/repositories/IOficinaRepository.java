package com.api.alquilaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.api.alquilaya.models.OficinaModel;

@Repository
public interface IOficinaRepository extends JpaRepository<OficinaModel, Long> {
}