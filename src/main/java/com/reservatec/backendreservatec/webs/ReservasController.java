package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.domains.ReservaTO;
import com.reservatec.backendreservatec.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservasController {

    private final ReservaService reservaService;

    @Autowired
    public ReservasController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaTO>> getAllReservas() {
        List<ReservaTO> reservas = reservaService.getAllReservas();
        return ResponseEntity.ok(reservas);
    }
}
