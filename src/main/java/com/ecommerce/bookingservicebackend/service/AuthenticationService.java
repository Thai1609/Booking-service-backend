package com.ecommerce.bookingservicebackend.service;

import com.ecommerce.bookingservicebackend.dto.request.AuthRequest;

public interface AuthenticationService {
    String authenticate(AuthRequest request);

    void register(AuthRequest request);

}
