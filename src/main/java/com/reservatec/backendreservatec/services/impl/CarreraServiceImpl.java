package com.reservatec.backendreservatec.services.impl;

import com.reservatec.backendreservatec.entities.Carrera;
import com.reservatec.backendreservatec.repositories.CarreraRepository;
import com.reservatec.backendreservatec.services.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;

    @Autowired
    public CarreraServiceImpl(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    @Override
    public List<Carrera> findAllCarreras() {
        return carreraRepository.findAll();
    }

    @Override
    public Optional<Carrera> findById(Long id) {
        return carreraRepository.findById(id);
    }
}
