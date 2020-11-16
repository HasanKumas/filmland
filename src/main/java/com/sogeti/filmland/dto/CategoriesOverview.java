package com.sogeti.filmland.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoriesOverview {
    private List<AvailableCategory> availableCategories = new ArrayList<>();
    private List<SubscribedCategory> subscribedCategories = new ArrayList<>();

    public List<AvailableCategory> getAvailableCategories() {
        return availableCategories;
    }

    public void setAvailableCategories(List<AvailableCategory> availableCategories) {
        this.availableCategories = availableCategories;
    }

    public List<SubscribedCategory> getSubscribedCategories() {
        return subscribedCategories;
    }

    public void setSubscribedCategories(List<SubscribedCategory> subscribedCategories) {
        this.subscribedCategories = subscribedCategories;
    }
}
