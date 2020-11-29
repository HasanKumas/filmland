package com.sogeti.filmland.controllers;

import com.sogeti.filmland.dto.*;
import com.sogeti.filmland.exceptions.BadRequestException;
import com.sogeti.filmland.exceptions.NotFoundException;
import com.sogeti.filmland.models.Category;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.services.CategoryService;
import com.sogeti.filmland.services.SubscriptionService;
import com.sogeti.filmland.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("filmland") // end point
public class SubscriptionController {
    // connect to database
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final CategoryService categoryService;

    public SubscriptionController(UserService userService, SubscriptionService subscriptionService, CategoryService categoryService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.categoryService = categoryService;
    }

    /**
     * this method gets all the subscribed and available categories
     * for the requested user
     * @param email user email
     * @return returns available and subscribed categories overview
     */
    @GetMapping("/all-categories")
    public CategoriesOverview getAllCategories(@RequestParam("email") String email) {
        checkAuthentication(email);
        /*
            get categories and user subscriptions from database
         */
        UserAccount currentUser = userService.getExistingUser(email);
        List<Subscription> subscriptions = userService.getAllUserSubscriptions(currentUser);
        List<Category> categories = categoryService.getAllCategories();

        return categoryService.getCategoriesOverview(categories, subscriptions);
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

        UserAccount currentUser = userService.getExistingUser(subscribeRequest.getEmail());
        // checks if user is subscribed to requested category
        boolean isUserSubscribed = subscriptionService.getSubscription(currentUser, subscribeRequest.getAvailableCategory()) != null;
        System.out.println(subscriptionService.getSubscription(currentUser, subscribeRequest.getAvailableCategory()).getCategory().getName());

        if (isUserSubscribed) {
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You are already subscribed to this category!");
        }else
        {
            Category category = categoryService.getCategoryByName(subscribeRequest.getAvailableCategory());
            if(category == null){
                throw new NotFoundException("This category is not available!");
            }
            subscriptionService.addSubscription(category, currentUser);

            // set response
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You are successfully subscribed to category: " + category.getName());
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

        checkAuthentication(shareRequest.getEmail());
        ResponseMessage responseMessage = new ResponseMessage();

        UserAccount currentUser = userService.getExistingUser(shareRequest.getEmail());
        UserAccount customer = userService.getExistingUser(shareRequest.getCustomer());
        Category category = categoryService.getCategoryByName(shareRequest.getSubscribedCategory());

        if(category == null || customer == null){
            throw new BadRequestException("Check customer email address and subscribedCategory name!");
        }
        // checks if user/customer is subscribed to requested category
        Subscription sharedSubscription = subscriptionService.getSubscription(currentUser, shareRequest.getSubscribedCategory());
        boolean isUserSubscribed = sharedSubscription != null;
        boolean isCustomerSubscribed = subscriptionService.getSubscription(customer, shareRequest.getSubscribedCategory()) != null;

        if (!isUserSubscribed || isCustomerSubscribed) {
            responseMessage.setStatus("Login successful!");
            responseMessage.setMessage("You are not subscribed or customer subscribed to this category!");
        }else
        {
            customer.getSubscriptions().add(sharedSubscription);
            userService.updateUser(customer);

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
