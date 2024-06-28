package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.domains.UsuarioTO;
import com.reservatec.backendreservatec.entities.Usuario;
import com.reservatec.backendreservatec.services.AuthenticationService;
import com.reservatec.backendreservatec.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Validated
public class UsuarioController {

    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(AuthenticationService authenticationService,
                             UsuarioService usuarioService) {
        this.authenticationService = authenticationService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/check")
    public void checkUserStatus(@RequestParam(value = "client_type", required = false) String clientType,
                                OAuth2AuthenticationToken token, Authentication authentication,
                                HttpServletResponse response, HttpServletRequest request) throws IOException {
        if (!authenticationService.isAuthenticated(authentication)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String email = token.getPrincipal().getAttribute("email");
        boolean usuarioRegistrado = usuarioService.existsByEmail(email);

        if ("mobile".equals(clientType)) {
            String sessionId = request.getSession().getId();
            String sessionCookie = request.getHeader("Cookie");
            String redirectUrl = "com.salas.jorge.laboratoriocalificado03://auth?sessionCookie=" + sessionCookie;
            response.sendRedirect(redirectUrl);
        } else {
            String redirectUrl = usuarioRegistrado ? "https://strong-laughter-production.up.railway.app" : "https://strong-laughter-production.up.railway.app/register";
            response.sendRedirect(redirectUrl);
        }
    }


    @GetMapping("/profile")
    public ResponseEntity<UsuarioTO> getProfileForm(OAuth2AuthenticationToken token, Authentication authentication) {
        if (!authenticationService.isAuthenticated(authentication)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<UsuarioTO> usuarioTO = authenticationService.getUserFromToken(token);
        if (usuarioTO.isEmpty()) {
            UsuarioTO newUserTO = new UsuarioTO();
            newUserTO.setNombres(token.getPrincipal().getAttribute("name"));
            newUserTO.setEmail(token.getPrincipal().getAttribute("email"));
            return ResponseEntity.ok(newUserTO);
        }

        return ResponseEntity.ok(usuarioTO.get());
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioTO> submitUserForm(@Valid @RequestBody UsuarioTO usuarioTO) {
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
    public ResponseEntity<UsuarioTO> updateProfile(@Valid @RequestBody UsuarioTO usuarioTO) {
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
