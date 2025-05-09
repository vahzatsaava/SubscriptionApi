package com.example.subscribersapi.model;


import com.example.subscribersapi.entity.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDto {
    private String id;

    private LocalDate startSubscriptionAt;

    private LocalDate endSubscriptionAt;

    private SubscriptionStatus status;

    private UserDto user;

    private SubscriptionDto subscription;
}
