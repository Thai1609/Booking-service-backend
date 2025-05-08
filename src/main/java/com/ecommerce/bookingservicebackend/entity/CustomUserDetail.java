package com.ecommerce.bookingservicebackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CustomUserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String avatar;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
