package com.ecommerce.bookingservicebackend.controller;

import com.ecommerce.bookingservicebackend.dto.request.AuthRequest;
import com.ecommerce.bookingservicebackend.dto.response.UserResponse;
import com.ecommerce.bookingservicebackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse> getUserInfo(@RequestHeader("Authorization") String token) {
        // Loại bỏ "Bearer " ở đầu token (nếu có)
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Gọi service để lấy thông tin user
        UserResponse userResponse = authService.getUserInfoFromToken(jwt);
        return ResponseEntity.ok(userResponse);
    }


}

