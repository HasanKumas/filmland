package com.sogeti.filmland.dto;

import javax.validation.constraints.NotNull;

/**
 * dto to transfer user inputs to backend
 */
public class SubscribeRequest {
    @NotNull
    private String email;

    @NotNull
    private String availableCategory;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvailableCategory() {
        return availableCategory;
    }

    public void setAvailableCategory(String availableCategory) {
        this.availableCategory = availableCategory;
    }
}
