package com.example.subscribersapi.controller;

import com.example.subscribersapi.model.UserDto;
import com.example.subscribersapi.model.UserUpdateDto;
import com.example.subscribersapi.model.register.AuthResponse;
import com.example.subscribersapi.model.register.RefreshTokenDto;
import com.example.subscribersapi.model.register.UserAuthRequest;
import com.example.subscribersapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Регистрация пользователя", description = "Создаёт нового пользователя в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос", content = @Content)
    })
    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody UserAuthRequest request) {
        return userService.register(request);
    }

    @Operation(summary = "Авторизация пользователя", description = "Авторизует пользователя и возвращает токены.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован"),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные", content = @Content)
    })
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody UserAuthRequest request) {
        return userService.login(request);
    }

    @Operation(summary = "Обновление access токена", description = "Обновляет access токен с использованием refresh токена.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access токен успешно обновлён"),
            @ApiResponse(responseCode = "401", description = "Недействительный refresh токен", content = @Content)
    })
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenDto refreshToken) {
        return userService.refreshAccessToken(refreshToken.getRefreshToken());
    }

    @Operation(summary = "Удаление аккаунта", description = "Удаление аккаунта пользователем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удален аккаунт"),
            @ApiResponse(responseCode = "401", description = "Аккаунт не удалился", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping
    public void deleteAccount(Principal principal){
        userService.deleteAccount(principal);
    }

    @Operation(summary = "Получение аккаунта", description = "Получение своего аккаунта пользователем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт получен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не получил аккаунт", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public UserDto getAccount(Principal principal){
        return userService.getAccount(principal);
    }

    @Operation(summary = "Обновление аккаунта пользователем", description = "Обновление своего аккаунта пользователем. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аккаунт обновлен"),
            @ApiResponse(responseCode = "401", description = "Пользователь не получил аккаунт", content = @Content)
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping
    public UserDto updateAccount(Principal principal,
                                 @RequestBody UserUpdateDto userUpdateDto){
        return userService.updateAccount(principal,userUpdateDto);

    }
}
