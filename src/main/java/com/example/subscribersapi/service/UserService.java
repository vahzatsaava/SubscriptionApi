package com.example.subscribersapi.service;

import com.example.subscribersapi.model.UserUpdateDto;
import com.example.subscribersapi.model.register.AuthResponse;
import com.example.subscribersapi.model.register.UserAuthRequest;
import com.example.subscribersapi.model.UserDto;

import java.security.Principal;

public interface UserService {
    AuthResponse register(UserAuthRequest request);
    AuthResponse login(UserAuthRequest request);
    AuthResponse refreshAccessToken(String currentToken);
    UserDto getAccount(Principal principal);

    void deleteAccount(Principal principal);

    UserDto updateAccount(Principal principal, UserUpdateDto userUpdateDto);
}
