package com.nurfet.springsecurity.config.exception;

import java.io.Serial;
import java.io.Serializable;

public class LoginException implements Serializable {

    @Serial
    private static final long serialVersionUID = 8842760896219563389L;

    private final String message;

    private String email;

    private String password;

    public LoginException(String message) {
        this.message = message;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }
}
