package com.sogeti.filmland.services;

import com.sogeti.filmland.dto.AvailableCategory;
import com.sogeti.filmland.dto.CategoriesOverview;
import com.sogeti.filmland.dto.SubscribedCategory;
import com.sogeti.filmland.models.Category;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * @return all categories
     */
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    /**
     * get a category by name
     * @param categoryName category name
     * @return category
     */
    public Category getCategoryByName(String categoryName){
       return categoryRepository.findOneByNameIgnoreCase(categoryName);
    }

    /**
     * this method gets all the subscribed and available categories
     * for the requested user
     * @param subscriptions user subscriptions
     * @param categories all categories
     * @return returns available and subscribed categories overview
     */
    public CategoriesOverview getCategoriesOverview(List<Category> categories, List<Subscription> subscriptions) {

        CategoriesOverview categoriesOverview = new CategoriesOverview();
        List<AvailableCategory> allAvailableCategories = new ArrayList<>();
        List<AvailableCategory> availableCategories;
        List<SubscribedCategory> subscribedCategories = new ArrayList<>();

        /* map categories to Availablecategory dto
         */
        for(Category category: categories) {
            AvailableCategory availableCategory = new AvailableCategory();
            availableCategory.setName(category.getName());
            availableCategory.setAvailableContent(category.getAvailableContent());
            availableCategory.setPrice(category.getPrice());
            allAvailableCategories.add((availableCategory));
        }
        availableCategories = allAvailableCategories;
        /* map subscriptions to SubscribedCategory dto
         */
        if(subscriptions.size() > 0) {
            for (Subscription subscription : subscriptions) {
                SubscribedCategory subscribedCategory = new SubscribedCategory();
                subscribedCategory.setName(subscription.getCategory().getName());
                subscribedCategory.setRemainingContent(subscription.getRemainingContent());
                subscribedCategory.setPrice(subscription.getCategory().getPrice());
                subscribedCategory.setStartDate(subscription.getStartDate());
                subscribedCategories.add(subscribedCategory);

                /*
                    remove subscribed categories from allAvailableCategories list to get remaining categories
                 */
                availableCategories = availableCategories.stream()
                        .filter(item -> !item.getName().equals(subscription.getCategory().getName()))
                        .collect(Collectors.toList());
            }
        }
        /*
            transfer available categories and subscribed categories to
            CategoriesOverview dto
         */
        categoriesOverview.setAvailableCategories(availableCategories);
        categoriesOverview.setSubscribedCategories(subscribedCategories);
        return categoriesOverview;
    }

}
