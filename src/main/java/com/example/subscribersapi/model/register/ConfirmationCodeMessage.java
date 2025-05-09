package com.example.subscribersapi.model.register;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmationCodeMessage {
    private String email;
    private String code;
}
