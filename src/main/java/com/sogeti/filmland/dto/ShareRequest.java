package com.sogeti.filmland.dto;

import javax.validation.constraints.NotNull;

/**
 * dto to transfer user inputs to backend
 */
public class ShareRequest {
    @NotNull
    private String email;

    @NotNull
    private String customer;

    @NotNull
    private String subscribedCategory;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSubscribedCategory() {
        return subscribedCategory;
    }

    public void setSubscribedCategory(String subscribedCategory) {
        this.subscribedCategory = subscribedCategory;
    }
}
