package com.sogeti.filmland.dto;

public class AvailableCategory {
    private String name;
    private Integer availableContent;
    private Double price;

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
