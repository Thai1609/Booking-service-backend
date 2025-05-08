package com.ecommerce.bookingservicebackend.service;

import com.ecommerce.bookingservicebackend.dto.request.AuthRequest;
import com.ecommerce.bookingservicebackend.dto.response.UserResponse;
import com.ecommerce.bookingservicebackend.entity.CustomUserDetail;
import com.ecommerce.bookingservicebackend.entity.Role;
import com.ecommerce.bookingservicebackend.entity.User;
import com.ecommerce.bookingservicebackend.repository.UserRepository;
import com.ecommerce.bookingservicebackend.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(user);
    }

    public String authenticate(AuthRequest request) {
        UserDetails user = loadUserByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        return jwtUtil.generateToken(user);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.name()))
                        .collect(Collectors.toList()));
    }

    @Transactional
    public UserResponse getUserInfoFromToken(String token) {
        // Trích xuất username từ token
        String username = jwtUtil.extractUsername(token);

        // Tìm kiếm user trong database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Lấy thông tin chi tiết của user (nếu có)
        CustomUserDetail userDetail = user.getCustomUserDetail();

        // Trả về response với thông tin user
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()),
                userDetail != null ? userDetail.getAvatar() : null,
                userDetail != null ? userDetail.getPhoneNumber() : null,
                user.isActive(),
                userDetail != null ? userDetail.getCreatedAt() : null,
                userDetail != null ? userDetail.getLastUpdated() : null

        );
    }
}