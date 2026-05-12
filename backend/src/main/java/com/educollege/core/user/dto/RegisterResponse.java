package com.educollege.core.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterResponse {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String status;
    private String idKey;
    private String idType;
    private String message;
    private java.time.LocalDateTime createdAt;
}
