package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.entities.Carrera;
import com.reservatec.backendreservatec.services.CarreraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/carreras")
public class CarreraController {

    private final CarreraService carreraService;

    @Autowired
    public CarreraController(CarreraService carreraService) {
        this.carreraService = carreraService;
    }

    @GetMapping
    public ResponseEntity<List<Carrera>> getCarreras() {
        List<Carrera> carreras = carreraService.findAllCarreras();
        return ResponseEntity.ok(carreras);
    }
}
