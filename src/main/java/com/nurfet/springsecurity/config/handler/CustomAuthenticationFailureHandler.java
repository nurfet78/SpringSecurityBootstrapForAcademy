package com.nurfet.springsecurity.config.handler;

import com.nurfet.springsecurity.config.exception.LoginException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public CustomAuthenticationFailureHandler() {
        super("/?error");
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // Запишем, чтобы не вводить заново ошибочные данные формы
        if (isAllowSessionCreation()) {
            LoginException loginException = new LoginException(exception.getMessage());
            request.getParameterMap().forEach((key, value) -> {
                if (key.equals("email")) {
                    loginException.setEmail(value[0]);
                } else if (key.equals("password")) {
                    loginException.setPassword(value[0]);
                }
            });

            request.getSession().setAttribute("Authentication-Exception", loginException);
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
