package com.sogeti.filmland.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserAccount {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 60)
    @Column(unique=true)
    private String userName;

    @NotNull
    @Size(min = 8, max = 60)
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Subscription> subscriptions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}

