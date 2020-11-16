package com.sogeti.filmland.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

/**
 * dto for mapping Subscription entity to overview
 */
public class SubscribedCategory {
    private String name;
    private Integer remainingContent;
    private Double price;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRemainingContent() {
        return remainingContent;
    }

    public void setRemainingContent(Integer remainingContent) {
        this.remainingContent = remainingContent;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
