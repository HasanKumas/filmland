package com.sogeti.filmland.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer remainingContent;

    @NotNull
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @OneToOne
    @NotNull
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JsonIgnore
    @NotNull
    private UserAccount user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRemainingContent() {
        return remainingContent;
    }

    public void setRemainingContent(Integer remainingContent) {
        this.remainingContent = remainingContent;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }
}
