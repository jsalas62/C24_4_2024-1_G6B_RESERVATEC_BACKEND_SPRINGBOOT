package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.domains.UsuarioTO;
import com.reservatec.backendreservatec.entities.Usuario;
import com.reservatec.backendreservatec.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SecurityContextHolderStrategy contextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final HttpSessionSecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UsuarioTO> getProfileForm(OAuth2AuthenticationToken token, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = token.getPrincipal().getAttribute("email");
        Optional<Usuario> usuarioOpt = usuarioService.findByEmail(email);

        if (usuarioOpt.isEmpty()) {
            UsuarioTO newUserTO = new UsuarioTO();
            newUserTO.setNombres(token.getPrincipal().getAttribute("name"));
            newUserTO.setEmail(email);
            return ResponseEntity.ok(newUserTO);
        }

        Usuario usuario = usuarioOpt.get();
        UsuarioTO usuarioTO = new UsuarioTO();
        usuarioTO.setNombres(usuario.getNombres());
        usuarioTO.setEmail(usuario.getEmail());
        usuarioTO.setCodigoTecsup(usuario.getCodigoTecsup());
        usuarioTO.setCarrera(usuario.getCarrera());
        return ResponseEntity.ok(usuarioTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioTO> submitUserForm(@Valid @RequestBody UsuarioTO usuarioTO,
                                                    HttpServletRequest request, HttpServletResponse response,
                                                    Authentication authentication) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombres(usuarioTO.getNombres());
            usuario.setEmail(usuarioTO.getEmail());
            usuario.setCodigoTecsup(usuarioTO.getCodigoTecsup());
            usuario.setCarrera(usuarioTO.getCarrera());
            // Set other default fields (estado, rol, etc.) as necessary
            usuarioService.saveUsuario(usuario);

            // Autenticar al usuario y guardar el contexto de seguridad en la sesión
            var securityContext = this.contextHolderStrategy.createEmptyContext();
            securityContext.setAuthentication(authentication);
            this.securityContextRepository.saveContext(securityContext, (jakarta.servlet.http.HttpServletRequest) request, (jakarta.servlet.http.HttpServletResponse) response);

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
            Optional<Usuario> usuarioOpt = usuarioService.findByEmail(usuarioTO.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Usuario usuario = usuarioOpt.get();
            usuario.setNombres(usuarioTO.getNombres());
            usuario.setCodigoTecsup(usuarioTO.getCodigoTecsup());
            usuario.setCarrera(usuarioTO.getCarrera());
            // Update other fields as necessary
            usuarioService.updateUsuario(usuario);
            return ResponseEntity.ok(usuarioTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
