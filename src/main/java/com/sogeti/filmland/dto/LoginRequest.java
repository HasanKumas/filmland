package com.sogeti.filmland.dto;

import javax.validation.constraints.NotNull;

/**
 * dto to transfer user inputs to backend
 */
public class LoginRequest {
    @NotNull
    private String email;

    @NotNull
    private String password;

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
}
