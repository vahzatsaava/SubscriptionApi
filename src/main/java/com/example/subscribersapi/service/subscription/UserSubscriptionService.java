package com.example.subscribersapi.service.subscription;

import com.example.subscribersapi.model.CreateSubscriptionDto;
import com.example.subscribersapi.model.PopularSubscriptionResponse;
import com.example.subscribersapi.model.UserSubscriptionDto;
import jakarta.transaction.Transactional;

import java.security.Principal;
import java.util.List;

public interface UserSubscriptionService {
    @Transactional
    UserSubscriptionDto createSubscription(Principal principal, CreateSubscriptionDto createSubscription);

    List<UserSubscriptionDto> getSubscription(Principal principal);

    UserSubscriptionDto deleteSubscription(Principal principal, Long subscriptionId);

    List<PopularSubscriptionResponse> getTop3Subscription();
}
