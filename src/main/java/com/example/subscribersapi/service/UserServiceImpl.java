package com.example.subscribersapi.service;

import com.example.subscribersapi.entity.User;
import com.example.subscribersapi.entity.UserStatus;
import com.example.subscribersapi.exceptions.UserAuthException;
import com.example.subscribersapi.exceptions.UserNotFoundException;
import com.example.subscribersapi.mapper.UserMapper;
import com.example.subscribersapi.model.UserUpdateDto;
import com.example.subscribersapi.model.register.AuthResponse;
import com.example.subscribersapi.model.register.UserAuthRequest;
import com.example.subscribersapi.model.UserDto;
import com.example.subscribersapi.repository.UserRepository;
import com.example.subscribersapi.security.CustomUserDetails;
import com.example.subscribersapi.security.JwtUtil;
import com.example.subscribersapi.service.subscription.UserSubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Transactional
    @Override
    public AuthResponse register(UserAuthRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        checkUserStatusAndExisting(request);

        User user = saveUser(request);

        String token = jwtUtil.generateToken(new CustomUserDetails(user.getId(),
                user.getEmail(), user.getPassword(), new ArrayList<>())
        );
        log.info("JWT token generated for user: {}", user.getEmail());
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(UserAuthRequest request) {
        log.info("User login attempt with email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getStatus() == UserStatus.DELETED) {
            return new AuthResponse("User was deleted please register again");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid credentials for user: {}", user.getEmail());
            throw new UserAuthException("Invalid credentials");
        }
        log.info("User authenticated successfully: {}", user.getEmail());


        String token = jwtUtil.generateToken(new CustomUserDetails(user.getId(),
                user.getEmail(), user.getPassword(), new ArrayList<>())
        );
        log.info("JWT token generated for user: {}", user.getEmail());


        return new AuthResponse(token);
    }

    @Override
    public AuthResponse refreshAccessToken(String refreshToken) {
        log.info("Refreshing access token");
        String username = jwtUtil.extractUsername(refreshToken);
        log.error(username);
        if (username == null || jwtUtil.isTokenExpired(refreshToken)) {
            throw new UserAuthException("Invalid refresh token");
        }

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDetails userDetails = new CustomUserDetails(user.getId(),
                user.getEmail(), user.getPassword(), new ArrayList<>()
        );
        String newAccessToken = jwtUtil.generateToken(userDetails);

        log.info("Access token refreshed successfully for user: {}", user.getEmail());

        return new AuthResponse(newAccessToken);
    }

    @Override
    public UserDto getAccount(Principal principal) {
        log.info("Fetching account for user: {}", principal.getName());
        User user = findUserByEmail(principal.getName());
        return userMapper.toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteAccount(Principal principal) {
        log.info("Deleting account for user: {}", principal.getName());
        User user = findUserByEmail(principal.getName());
        user.setStatus(UserStatus.DELETED);
        log.info("User account deleted: {}", user.getEmail());
    }

    @Transactional
    @Override
    public UserDto updateAccount(Principal principal, UserUpdateDto userUpdateDto) {
        log.info("Updating account for user: {}", principal.getName());
        User user = findUserByEmail(principal.getName());
        user.setEmail(userUpdateDto.getEmail());
        user.setName(userUpdateDto.getName());
        log.info("Updating Successfully for user: {}", principal.getName());
        return userMapper.toUserDto(user);
    }

    private User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found by name " + userEmail));
    }

    private User saveUser(UserAuthRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setName(request.getName());
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());
        return user;
    }

    private void checkUserStatusAndExisting(UserAuthRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (existingUser.getStatus() == UserStatus.DELETED) {
                log.warn("Registration attempt for user with email {} and status {}", request.getEmail(), existingUser.getStatus());
                throw new UserAuthException("User account is not active");
            }
            log.warn("User already exists with email: {}", request.getEmail());
            throw new UserAuthException("User already exists");
        }
    }

}
