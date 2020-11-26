package com.sogeti.filmland.services;

import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.models.UserAccount;
import com.sogeti.filmland.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    @Autowired // connect to database
    private SubscriptionRepository subscriptionRepository;

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
        for (Subscription subscription1 : allUserSubscriptions) {
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
}
