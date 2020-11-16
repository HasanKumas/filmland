package com.sogeti.filmland.repositories;

import com.sogeti.filmland.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserUserName(String userName);
    Boolean existsByUserUserNameAndCategoryName(String userName, String categoryName);
    Subscription findOneByCategoryNameIgnoreCase(String subscribedCategoryName);
}