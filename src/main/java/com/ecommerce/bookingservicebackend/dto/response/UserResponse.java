package com.ecommerce.bookingservicebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private String avatar;
    private String phoneNumber;
    private boolean accountStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;


}
