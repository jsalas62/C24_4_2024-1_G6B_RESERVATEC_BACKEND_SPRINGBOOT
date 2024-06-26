package com.reservatec.backendreservatec.webs;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class WelcomeController {

    @GetMapping
    public ResponseEntity<String> homePage(Authentication authentication) {
        return ResponseEntity.ok("Welcome to reservatec" );
        }

    }



