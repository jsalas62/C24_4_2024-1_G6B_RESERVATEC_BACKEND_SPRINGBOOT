package com.reservatec.backendreservatec.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String clientType = request.getParameter("client_type");

        if ("mobile".equals(clientType)) {
            response.sendRedirect("com.salas.jorge.laboratoriocalificado03://auth");
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
