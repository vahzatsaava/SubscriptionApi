package com.example.subscribersapi.model;

public record PopularSubscriptionResponse(
        Long id,
        String name,
        Long count
) {
}
