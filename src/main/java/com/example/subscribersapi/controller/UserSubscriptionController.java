package com.example.subscribersapi.controller;

import com.example.subscribersapi.model.CreateSubscriptionDto;
import com.example.subscribersapi.model.PopularSubscriptionResponse;
import com.example.subscribersapi.model.UserSubscriptionDto;
import com.example.subscribersapi.service.subscription.UserSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/api/users/subscriptions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserSubscriptionController {
    private final UserSubscriptionService userSubscriptionService;

    @Operation(summary = "Добавление подписки", description = "Позволяет пользователю оформить новую подписку.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка успешно добавлена"),
            @ApiResponse(responseCode = "400", description = "Неверный запрос или пользователь уже подписан", content = @Content)
    })
    @PostMapping
    public UserSubscriptionDto addSubscription(Principal principal,@RequestBody CreateSubscriptionDto createSubscription){
        return userSubscriptionService.createSubscription(principal,createSubscription);
    }

    @Operation(summary = "Получение всех подписок пользователя", description = "Возвращает список всех активных подписок пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список подписок получен"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный пользователь", content = @Content)
    })
    @GetMapping
    public List<UserSubscriptionDto> getSubscriptions(Principal principal){
        return userSubscriptionService.getSubscription(principal);
    }

    @Operation(summary = "Удаление подписки", description = "Позволяет пользователю отменить подписку по её ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Подписка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Подписка не найдена или не принадлежит пользователю", content = @Content)
    })
    @DeleteMapping("/{subscriptionId}")
    public UserSubscriptionDto deleteSubscription (Principal principal, @PathVariable Long subscriptionId){
        return userSubscriptionService.deleteSubscription(principal,subscriptionId);
    }

    @Operation(summary = "Получение ТОП-3 подписок", description = "Возвращает три самые популярные подписки по количеству пользователей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ТОП-3 подписки успешно получены"),
            @ApiResponse(responseCode = "500", description = "Ошибка сервера при обработке запроса", content = @Content)
    })
    @GetMapping("/top")
    public List<PopularSubscriptionResponse> getTop(){
        return userSubscriptionService.getTop3Subscription();
    }
}
