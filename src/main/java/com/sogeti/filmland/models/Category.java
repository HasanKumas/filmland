package com.sogeti.filmland.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(unique=true)
    private String name;

    @NotNull
    private Integer availableContent;

    @NotNull
    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvailableContent() {
        return availableContent;
    }

    public void setAvailableContent(Integer availableContent) {
        this.availableContent = availableContent;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
