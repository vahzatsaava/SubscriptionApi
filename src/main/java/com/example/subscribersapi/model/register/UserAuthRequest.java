package com.example.subscribersapi.model.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Email
    @Schema(example = "user@example.com", description = "Email пользователя")
    private String email;

    @NotBlank(message = "name cannot be blank")
    @Size(min = 3, max = 50, message = "name must be between 3 and 50 characters")
    @Schema(example = "User", description = "Имя пользователя")
    private String name;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
