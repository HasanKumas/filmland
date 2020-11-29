package com.sogeti.filmland.services;

import com.sogeti.filmland.models.Category;
import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {
    // connect to database
    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
    }

    /**
     * checks if user or customer is already subscribed to requested category
     * if subscribed returns subscription
     * if not subscribed returns null
     * @param user current user or customer
     * @param categoryName category name
     * @return null or subscription
     */
    public Subscription getSubscription(UserAccount user, String categoryName){
        List<Subscription> allUserSubscriptions = user.getSubscriptions();
        Subscription subscription = null;
        for(Subscription subscription1 : allUserSubscriptions){
            if(subscription1.getCategory().getName().equals(categoryName)){
                subscription = subscription1;
                break;
            }
        }
        return subscription;
    }

    /**
     * gets all subscriptions from database
     * @return a list of all subscriptions
     */
    public List<Subscription> getAllSubscriptions(){
        return subscriptionRepository.findAll();
    }

    /**
     * set the fields of new subscription and save it to database
     * @param category category
     */
    public void addSubscription(Category category, UserAccount currentUser){

        Subscription subscription = new Subscription();
        subscription.setCategory(category);
        subscription.setRemainingContent(category.getAvailableContent());
//        List<UserAccount> users = subscription.getUsers();
//        users.add(currentUser);
//        subscription.setUsers(users);
        subscription.setStartDate(LocalDate.now());
        subscription.setPaymentDate(LocalDate.now().plusMonths(1));
        subscriptionRepository.save(subscription);
        // get saved subscription by its id from database and connect it to current user
        currentUser.getSubscriptions().add(subscriptionRepository.getOne(subscription.getId()));
        userService.updateUser(currentUser);
    }
}
