package com.sogeti.filmland.controllers;

import com.sogeti.filmland.dto.*;
import com.sogeti.filmland.exceptions.BadRequestException;
import com.sogeti.filmland.exceptions.NotFoundException;
import com.sogeti.filmland.models.Category;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.CategoryRepository;
import com.sogeti.filmland.repositories.SubscriptionRepository;
import com.sogeti.filmland.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("filmland") // end point
public class SubscriptionController {
    @Autowired // connect to database
    private UserService userService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    /*
        this method gets all the subscribed and available categories
        for the requested user
     */
    @GetMapping("/all-categories")
    public CategoriesOverview getAllCategories(@RequestParam("email") String email) {
        UserAccount currentUser = userService.getExistingUser(email);
        if(currentUser == null){
            throw new BadRequestException("Check email address. User could not be found!");
        }
        CategoriesOverview categoriesOverview = new CategoriesOverview();
        List<AvailableCategory> allAvailableCategories = new ArrayList<>();
        List<AvailableCategory> availableCategories = new ArrayList<>();
        List<SubscribedCategory> subscribedCategories = new ArrayList<>();

        /*
            get categories and subscriptions
         */
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserUserName(email);
        List<Category> categories = categoryRepository.findAll();


        /* map categories to available categories
         */
        for(Category category: categories) {
            AvailableCategory availableCategory = new AvailableCategory();
            availableCategory.setName(category.getName());
            availableCategory.setAvailableContent(category.getAvailableContent());
            availableCategory.setPrice(category.getPrice());
            allAvailableCategories.add((availableCategory));
        }
        /* map subscriptions to subscribed Categories
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
                    remove subscribed categories from allAvailableCategories
                 */
                availableCategories = allAvailableCategories.stream()
                        .filter(item -> !item.getName().equals(subscription.getCategory().getName()))
                        .collect(Collectors.toList());
            }
        }else
            availableCategories = allAvailableCategories;
        /*
            transfer available categories and subscribed categories to
            categories overview
         */
        categoriesOverview.setAvailableCategories(availableCategories);
        categoriesOverview.setSubscribedCategories(subscribedCategories);
        return categoriesOverview;
    }
    /*
        this method subscribe to a specified category
        for the requested user
     */
    @PostMapping("/subscribe-to-category")
    public ResponseEntity<ResponseMessage> subscribe(@RequestBody SubscribeRequest subscribeRequest) {
        ResponseMessage responseMessage = new ResponseMessage();
        Subscription subscription = new Subscription();
        UserAccount currentUser = userService.getExistingUser(subscribeRequest.getEmail());

        if(currentUser == null){
            throw new BadRequestException("Check email address. User could not be found!");
        }
        if (subscriptionRepository.existsByUserUserNameAndCategoryName(subscribeRequest.getEmail(), subscribeRequest.getAvailableCategory())) {
            responseMessage.setStatus("Subscribe failed!");
            responseMessage.setMessage("You are already subscribed to this category!");
        }else
        {
            Category category = categoryRepository.findOneByNameIgnoreCase(subscribeRequest.getAvailableCategory());
            if(category == null){
                throw new NotFoundException("This category is not available!");
            }
            subscription.setCategory(category);
            subscription.setRemainingContent(category.getAvailableContent());
            subscription.setUser(currentUser);
            subscription.setStartDate(LocalDate.now());

            subscriptionRepository.save(subscription);
            responseMessage.setStatus("Subscribe successful!");
            responseMessage.setMessage("You are successfully subscribed to category: " + subscription.getCategory().getName());
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
