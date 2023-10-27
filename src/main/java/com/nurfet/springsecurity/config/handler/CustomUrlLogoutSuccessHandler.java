package com.nurfet.springsecurity.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        // Запишем, чтобы попрощаться
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            request.getSession().setAttribute("Authentication-Name", authentication.getName());
        }

        super.onLogoutSuccess(request, response, authentication);
    }
}
