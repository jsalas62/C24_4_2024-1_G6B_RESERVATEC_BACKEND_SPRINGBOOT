package com.reservatec.backendreservatec.webs;

import com.reservatec.backendreservatec.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final AuthenticationService authenticationService;

    @Autowired
    public HomeController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public ResponseEntity<String> homePage(Authentication authentication) {
        if (!authenticationService.isAuthenticated(authentication)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autorizado para acceder a esta página.");
        }
        return ResponseEntity.ok("Welcome to the Home Page");
    }
}
