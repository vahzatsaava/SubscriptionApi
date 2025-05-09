package com.example.subscribersapi.repository;

import com.example.subscribersapi.entity.SubscriptionStatus;
import com.example.subscribersapi.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {
    List<UserSubscription> findAllByUserEmailAndStatus(String userEmail, SubscriptionStatus status);
    UserSubscription findByUserEmailAndSubscription_IdAndStatus(String userEmail, Long subscriptionId,SubscriptionStatus status);

    @Query(value = """
    SELECT s.id, s.name, COUNT(us.id)
    FROM user_subscription us
    JOIN subscription s ON us.subscription_id = s.id
    WHERE us.status = 'ACTIVE'
    GROUP BY s.id, s.name
    ORDER BY COUNT(us.id) DESC
    LIMIT 3
""", nativeQuery = true)
    List<Object[]> findTop3PopularSubscriptions();


}
