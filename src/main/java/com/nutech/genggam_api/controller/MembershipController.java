package com.nutech.genggam_api.controller;

import com.nutech.genggam_api.dto.request.LoginRequest;
import com.nutech.genggam_api.dto.request.RegistrationRequest;
import com.nutech.genggam_api.dto.request.UpdateProfileRequest;
import com.nutech.genggam_api.dto.response.ApiResponse;
import com.nutech.genggam_api.dto.response.LoginResponse;
import com.nutech.genggam_api.dto.response.ProfileResponse;
import com.nutech.genggam_api.security.AuthUtil;
import com.nutech.genggam_api.service.AuthService;
import com.nutech.genggam_api.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MembershipController {

    private final AuthService authService;
    private final ProfileService profileService;

    public MembershipController(AuthService authService, ProfileService profileService) {
        this.authService = authService;
        this.profileService = profileService;
    }

    @PostMapping("/registration")
    public ApiResponse<Object> register(@Valid @RequestBody RegistrationRequest req) {
        authService.register(req);
        return ApiResponse.success("Registrasi berhasil silahkan login", null);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.success("Login Sukses", authService.login(req));
    }

    @GetMapping("/profile")
    public ApiResponse<ProfileResponse> profile() {
        return ApiResponse.success("Sukses", profileService.getProfile(AuthUtil.currentUser()));
    }

    @PutMapping("/profile/update")
    public ApiResponse<ProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest req) {
        return ApiResponse.success(
                "Update Profile Berhasil",
                profileService.updateProfile(AuthUtil.currentUser(), req));
    }

    @PutMapping(value = "/profile/image", consumes = "multipart/form-data")
    public ApiResponse<ProfileResponse> updateProfileImage(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(
                "Update Profile Image berhasil",
                profileService.updateProfileImage(AuthUtil.currentUser(), file));
    }
}
