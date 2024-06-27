package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.domains.ReservaTO;
import com.reservatec.backendreservatec.entities.Reserva;
import com.reservatec.backendreservatec.entities.Usuario;
import com.reservatec.backendreservatec.mappers.ReservaMapper;
import com.reservatec.backendreservatec.services.AuthenticationService;
import com.reservatec.backendreservatec.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reserva")

public class ReservaController {

    private final ReservaService reservaService;
    private final AuthenticationService authenticationService;
    private final ReservaMapper reservaMapper;

    @Autowired
    public ReservaController(ReservaService reservaService,
                             AuthenticationService authenticationService,
                             ReservaMapper reservaMapper) {
        this.reservaService = reservaService;
        this.authenticationService = authenticationService;
        this.reservaMapper = reservaMapper;
    }

    @PostMapping("/nueva")
    public ResponseEntity<?> submitReservaForm(@RequestBody ReservaTO reservaTO, OAuth2AuthenticationToken token, Authentication authentication) {
        Optional<Usuario> usuario = authenticationService.getAuthenticatedUser(authentication, token);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autorizado.");
        }

        Reserva reserva = reservaMapper.toEntity(reservaTO);
        reserva.setUsuario(usuario.get());

        try {
            reservaService.crearReserva(reserva);
            ReservaTO savedReservaTO = reservaMapper.toTO(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservaTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/misreservas")
    public ResponseEntity<List<ReservaTO>> getMisReservas(OAuth2AuthenticationToken token, Authentication authentication) {
        Optional<Usuario> usuario = authenticationService.getAuthenticatedUser(authentication, token);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Llamar al método actualizarReservas antes de obtener las reservas
        reservaService.actualizarReservas();

        List<Reserva> reservas = reservaService.findReservasByUsuario(usuario.get().getId());
        List<ReservaTO> reservaTOs = reservas.stream().map(reservaMapper::toTO).collect(Collectors.toList());
        return ResponseEntity.ok(reservaTOs);
    }

}
