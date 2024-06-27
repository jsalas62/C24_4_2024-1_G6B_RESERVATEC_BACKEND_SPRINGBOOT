package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.domains.UsuarioTO;
import com.reservatec.backendreservatec.entities.Usuario;
import com.reservatec.backendreservatec.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UsuarioTO> getProfileForm(OAuth2AuthenticationToken token) {
        UsuarioTO usuarioTO = new UsuarioTO();
        usuarioTO.setNombres(token.getPrincipal().getAttribute("name"));
        usuarioTO.setEmail(token.getPrincipal().getAttribute("email"));
        return ResponseEntity.ok(usuarioTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioTO> submitUserForm(@Valid @RequestBody UsuarioTO usuarioTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombres(usuarioTO.getNombres());
            usuario.setEmail(usuarioTO.getEmail());
            usuarioService.saveUsuario(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<UsuarioTO> updateProfile(@Valid @RequestBody UsuarioTO usuarioTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombres(usuarioTO.getNombres());
            usuario.setEmail(usuarioTO.getEmail());
            usuarioService.updateUsuario(usuario);
            return ResponseEntity.ok(usuarioTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
