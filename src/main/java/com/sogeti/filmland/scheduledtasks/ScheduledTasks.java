package com.sogeti.filmland.scheduledtasks;

import com.sogeti.filmland.models.Subscription;
import com.sogeti.filmland.repositories.SubscriptionRepository;
import com.sogeti.filmland.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    SubscriptionRepository subscriptionRepository;

    /**
     * this scheduled task updates all the subscriptions in database
     * everyday at 03:00 AM
     * updates payment date every month and
     * set remaining content to available content of the category
     * without transferring remaining contents from the previous month
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void updateSubscription(){
        List<Subscription> allSubscriptions = subscriptionService.getAllSubscriptions();

        for(Subscription subscription : allSubscriptions) {
            if(subscription.getPaymentDate().isEqual(LocalDate.now())){
                subscription.setRemainingContent(subscription.getCategory().getAvailableContent());
                subscription.setPaymentDate(LocalDate.now().plusMonths(1));
                subscriptionRepository.save(subscription);
            }
        }

    }
}
