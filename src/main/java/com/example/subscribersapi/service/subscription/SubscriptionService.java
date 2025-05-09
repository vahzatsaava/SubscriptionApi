package com.example.subscribersapi.service.subscription;

import com.example.subscribersapi.entity.Subscription;
import com.example.subscribersapi.exceptions.SubscriptionNotFoundException;
import com.example.subscribersapi.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription getSubscription(Long id){
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found by id " + id));
    }
}
