package com.nutech.genggam_api.service;

import com.nutech.genggam_api.dto.request.LoginRequest;
import com.nutech.genggam_api.dto.request.RegistrationRequest;
import com.nutech.genggam_api.dto.response.LoginResponse;
import com.nutech.genggam_api.exception.ApiException;
import com.nutech.genggam_api.model.User;
import com.nutech.genggam_api.repository.UserRepository;
import com.nutech.genggam_api.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {
    // ...existing code...

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void register(RegistrationRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw ApiException.badRequest("Email sudah terdaftar");
        }
        User u = new User();
        u.setEmail(req.getEmail());
        u.setFirstName(req.getFirstName());
        u.setLastName(req.getLastName());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.insert(u);
    }

    public LoginResponse login(LoginRequest req) {
        Optional<User> userOpt = userRepository.findByEmail(req.getEmail());
        if (!userOpt.isPresent() || !passwordEncoder.matches(req.getPassword(), userOpt.get().getPassword())) {
            throw ApiException.invalidCredential("Username atau password salah");
        }
        String token = jwtUtil.generate(userOpt.get().getEmail());
        return new LoginResponse(token);
    }
}
