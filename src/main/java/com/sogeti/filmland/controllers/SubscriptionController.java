package com.sogeti.filmland.controllers;

import com.sogeti.filmland.dto.*;
import com.sogeti.filmland.exceptions.BadRequestException;
import com.sogeti.filmland.exceptions.NotFoundException;
import com.sogeti.filmland.models.Category;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.CategoryRepository;
import com.sogeti.filmland.repositories.SubscriptionRepository;
import com.sogeti.filmland.repositories.UserAccountRepository;
import com.sogeti.filmland.services.SubscriptionService;
import com.sogeti.filmland.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    private SubscriptionService subscriptionService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    /**
     * this method gets all the subscribed and available categories
     * for the requested user
     * @param email user email
     * @return returns available and subscribed categories overview
     */
    @GetMapping("/all-categories")
    public CategoriesOverview getAllCategories(@RequestParam("email") String email) {
        checkAuthentication(email);

        UserAccount currentUser = userService.getExistingUser(email);
        CategoriesOverview categoriesOverview = new CategoriesOverview();
        List<AvailableCategory> allAvailableCategories = new ArrayList<>();
        List<AvailableCategory> availableCategories = new ArrayList<>();
        List<SubscribedCategory> subscribedCategories = new ArrayList<>();

        /*
            get categories and user subscriptions from database
         */
        List<Subscription> subscriptions = userService.getAllUserSubscriptions(currentUser);
        List<Category> categories = categoryRepository.findAll();


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
    /**
     * this method subscribe to a specified category
     * for the requested user
     * @param subscribeRequest request for subscription
     * @return status and message
     */
    @PostMapping("/subscribe-to-category")
    public ResponseEntity<ResponseMessage> subscribe(@RequestBody SubscribeRequest subscribeRequest) {
        checkAuthentication(subscribeRequest.getEmail());

        ResponseMessage responseMessage = new ResponseMessage();
        Subscription subscription = new Subscription();
        UserAccount currentUser = userService.getExistingUser(subscribeRequest.getEmail());

        // checks if user is subscribed to requested category
        Boolean isUserSubscribed = subscriptionService.getSubscription(currentUser, subscribeRequest.getAvailableCategory()) != null;
        if (isUserSubscribed) {
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You are already subscribed to this category!");
        }else
        {
            Category category = categoryRepository.findOneByNameIgnoreCase(subscribeRequest.getAvailableCategory());
            if(category == null){
                throw new NotFoundException("This category is not available!");
            }
            // set the fields of new subscription and save it to database
            subscription.setCategory(category);
            subscription.setRemainingContent(category.getAvailableContent());
            List<UserAccount> users = subscription.getUsers();
            users.add(currentUser);
            subscription.setUsers(users);
            subscription.setStartDate(LocalDate.now());
            subscription.setPaymentDate(LocalDate.now().plusMonths(1));
            subscriptionRepository.save(subscription);
            // get saved subscription by its id from database and connect it to current user
//            currentUser.getSubscriptions().add(subscriptionRepository.findOneByCategoryNameIgnoreCase(subscription.getCategory().getName()));
            currentUser.getSubscriptions().add(subscriptionRepository.getOne(subscription.getId()));
            userAccountRepository.save(currentUser);
            // set response
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You are successfully subscribed to category: " + subscription.getCategory().getName());
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    /**
     * shares a subscription with another existing customer
     * @param shareRequest request for sharing a subscription
     * @return status and message
     */
    @PostMapping("/share-category")
    public ResponseMessage share(@RequestBody ShareRequest shareRequest) {
        if(!userService.isAuthenticated(shareRequest.getEmail())){
            throw new BadCredentialsException("Not authorized user! Check email address.");
        }
        ResponseMessage responseMessage = new ResponseMessage();

        UserAccount currentUser = userService.getExistingUser(shareRequest.getEmail());
        UserAccount customer = userService.getExistingUser(shareRequest.getCustomer());
        Category category = categoryRepository.findOneByNameIgnoreCase(shareRequest.getSubscribedCategory());

        if(category == null || customer == null){
            throw new BadRequestException("Check customer email address and subscribedCategory name!");
        }
        // checks if user is subscribed to requested category check for customer?
        Subscription sharedSubscription = subscriptionService.getSubscription(currentUser, shareRequest.getSubscribedCategory());
        Boolean isUserSubscribed = sharedSubscription != null ? true : false;
        Boolean isCustomerSubscribed = subscriptionService.getSubscription(customer, shareRequest.getSubscribedCategory()) != null;

        if (!isUserSubscribed || isCustomerSubscribed) {
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You are not subscribed or customer subscribed to this category!");
        }else
        {
            customer.getSubscriptions().add(sharedSubscription);
            userAccountRepository.save(customer);

            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You shared successfully a subscribed category: "
                    + sharedSubscription.getCategory().getName() + " with " + customer.getUserName());
        }
        return responseMessage;
    }

    private void checkAuthentication(String email) {
        if(!userService.isAuthenticated(email)){
            throw new BadCredentialsException("Not authorized user! Check email address.");
        }
    }
}
