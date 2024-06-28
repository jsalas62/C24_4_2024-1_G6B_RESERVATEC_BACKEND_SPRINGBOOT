package com.reservatec.backendreservatec.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/oauth2")
public class UserController {

    @Value("${redirect.uri.web}")
    private String webRedirectUri;

    @Value("${redirect.uri.mobile}")
    private String mobileRedirectUri;

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirectAfterLogin(@RequestParam("platform") String platform, OAuth2AuthenticationToken token, Authentication authentication) {
        String redirectUri;
        if ("mobile".equalsIgnoreCase(platform)) {
            redirectUri = mobileRedirectUri;
        } else {
            redirectUri = webRedirectUri;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUri));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
