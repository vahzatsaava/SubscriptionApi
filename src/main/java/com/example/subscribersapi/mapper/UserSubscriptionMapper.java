package com.example.subscribersapi.mapper;

import com.example.subscribersapi.entity.UserSubscription;
import com.example.subscribersapi.model.UserSubscriptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, SubscriptionMapper.class})
public interface UserSubscriptionMapper {
    UserSubscriptionDto toUserSubscriptionDto(UserSubscription subscription);
}
