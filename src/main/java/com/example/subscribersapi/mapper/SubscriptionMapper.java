package com.example.subscribersapi.mapper;

import com.example.subscribersapi.entity.Subscription;
import com.example.subscribersapi.model.SubscriptionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionDto toSubscriptionDto(Subscription subscription);
}
