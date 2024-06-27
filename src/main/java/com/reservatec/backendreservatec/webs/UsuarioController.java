package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.domains.UsuarioTO;
import com.reservatec.backendreservatec.entities.Usuario;
import com.reservatec.backendreservatec.mappers.UsuarioMapper;
import com.reservatec.backendreservatec.services.AuthenticationService;
import com.reservatec.backendreservatec.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")

public class UsuarioController {

    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;
    @Autowired
    public UsuarioController(AuthenticationService authenticationService,
                             UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.authenticationService = authenticationService;
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("/profile")
    public ResponseEntity<UsuarioTO> getProfileForm(OAuth2AuthenticationToken token) {
        if (token == null || !token.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Object> attributes = token.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);
        UsuarioTO usuarioTO;

        if (usuarioOpt.isEmpty()) {
            // Crear un nuevo UsuarioTO si no existe el usuario
            usuarioTO = new UsuarioTO();
            usuarioTO.setNombres(name);
            usuarioTO.setEmail(email);
        } else {
            // Convertir Usuario a UsuarioTO
            Usuario usuario = usuarioOpt.get();
            usuarioTO = usuarioMapper.toTO(usuario);
        }

        return ResponseEntity.ok(usuarioTO);
    }




    @PostMapping("/register")
    public ResponseEntity<UsuarioTO> submitUserForm( @RequestBody UsuarioTO usuarioTO) {
        try {
            Usuario usuario = authenticationService.convertToEntity(usuarioTO);
            usuarioService.saveUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<UsuarioTO> updateProfile( @RequestBody UsuarioTO usuarioTO) {
        try {
            Usuario usuario = authenticationService.convertToEntity(usuarioTO);
            usuarioService.updateUsuario(usuario);
            return ResponseEntity.ok(usuarioTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
