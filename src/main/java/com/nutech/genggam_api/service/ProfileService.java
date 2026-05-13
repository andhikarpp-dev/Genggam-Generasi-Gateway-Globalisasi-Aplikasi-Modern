package com.nutech.genggam_api.service;

import com.nutech.genggam_api.dto.request.UpdateProfileRequest;
import com.nutech.genggam_api.dto.response.ProfileResponse;
import com.nutech.genggam_api.exception.ApiException;
import com.nutech.genggam_api.model.User;
import com.nutech.genggam_api.repository.UserRepository;
import com.nutech.genggam_api.util.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public ProfileService(UserRepository userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public ProfileResponse getProfile(User current) {
        return ProfileResponse.from(current);
    }

    @Transactional
    public ProfileResponse updateProfile(User current, UpdateProfileRequest req) {
        userRepository.updateName(current.getId(), req.getFirstName(), req.getLastName());
        return userRepository.findById(current.getId())
                .map(ProfileResponse::from)
                .orElseThrow(() -> ApiException.unauthorized("Token tidak tidak valid atau kadaluwarsa"));
    }

    @Transactional
    public ProfileResponse updateProfileImage(User current, MultipartFile file) {
        String url = fileStorageService.storeProfileImage(current.getId(), file);
        userRepository.updateProfileImage(current.getId(), url);
        return userRepository.findById(current.getId())
                .map(ProfileResponse::from)
                .orElseThrow(() -> ApiException.unauthorized("Token tidak tidak valid atau kadaluwarsa"));
    }
}
