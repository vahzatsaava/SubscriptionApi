package com.example.subscribersapi.service.subscription;

import com.example.subscribersapi.entity.*;
import com.example.subscribersapi.exceptions.UserAuthException;
import com.example.subscribersapi.mapper.UserMapper;
import com.example.subscribersapi.mapper.UserSubscriptionMapper;
import com.example.subscribersapi.model.CreateSubscriptionDto;
import com.example.subscribersapi.model.PopularSubscriptionResponse;
import com.example.subscribersapi.model.UserDto;
import com.example.subscribersapi.model.UserSubscriptionDto;
import com.example.subscribersapi.repository.UserSubscriptionRepository;
import com.example.subscribersapi.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserSubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    private final UserMapper userMapper;
    private final UserSubscriptionMapper userSubscriptionMapper;


    @Transactional
    @Override
    public UserSubscriptionDto createSubscription(Principal principal, CreateSubscriptionDto dto) {
        UserDto userDto = userService.getAccount(principal);
        checkUserStatus(userDto,principal);
        User user = userMapper.toUser(userDto);

        Subscription subscription = subscriptionService.getSubscription(dto.getSubscriptionId());

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setStartSubscriptionAt(LocalDate.now());
        userSubscription.setEndSubscriptionAt(LocalDate.now().plusDays(dto.getDurationDays()));
        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setId(UUID.randomUUID().toString());
        userSubscription.setStatus(SubscriptionStatus.ACTIVE);

        subscriptionRepository.save(userSubscription);
        log.info("subscription created by user_id :   {} and subscription : {}", userDto.getId(), dto.getSubscriptionId());

        return userSubscriptionMapper.toUserSubscriptionDto(userSubscription);
    }

    @Override
    public List<UserSubscriptionDto> getSubscription(Principal principal) {
        return subscriptionRepository.findAllByUserEmailAndStatus(principal.getName(), SubscriptionStatus.ACTIVE)
                .stream()
                .map(userSubscriptionMapper::toUserSubscriptionDto)
                .toList();
    }

    @Override
    @Transactional
    public UserSubscriptionDto deleteSubscription(Principal principal, Long subscriptionId) {
        UserSubscription userSubscription =
                subscriptionRepository.findByUserEmailAndSubscription_IdAndStatus(principal.getName(),
                        subscriptionId,
                        SubscriptionStatus.ACTIVE);
        userSubscription.setStatus(SubscriptionStatus.STOPPED);
        userSubscription.setEndSubscriptionAt(LocalDate.now());
        log.info("subscription successfully deleted by user email {} and by subscription id {}", principal.getName(), subscriptionId);

        return userSubscriptionMapper.toUserSubscriptionDto(userSubscription);
    }

    @Override
    public List<PopularSubscriptionResponse> getTop3Subscription() {
        return subscriptionRepository.findTop3PopularSubscriptions()
                .stream()
                .map(row -> new PopularSubscriptionResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        ((Number) row[2]).longValue()
                ))
                .toList();
    }


    private void checkUserStatus(UserDto userDto,Principal principal){
        if (userDto.getStatus() == UserStatus.DELETED){
            log.warn("Registration attempt for user with email {} and status {}", principal.getName(), userDto.getStatus());
            throw new UserAuthException("User account is not active");
        }
    }

}
