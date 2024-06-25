package com.reservatec.backendreservatec.services;

import com.reservatec.backendreservatec.entities.Carrera;
import java.util.List;
import java.util.Optional;

public interface CarreraService {
    List<Carrera> findAllCarreras();
    Optional<Carrera> findById(Long id);
}
