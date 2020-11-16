package com.sogeti.filmland.repositories;

import com.sogeti.filmland.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findOneByCategoryNameIgnoreCase(String subscribedCategoryName);
}