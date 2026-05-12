package com.educollege.core.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String username;
    private String role;
    private String fullName;
    @Builder.Default
    private String message = "Login successful";
}
