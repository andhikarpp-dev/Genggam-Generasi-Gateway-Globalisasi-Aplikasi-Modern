package com.nutech.genggam_api.util;

import com.nutech.genggam_api.exception.ApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class FileStorageService {

    private static final long MAX_SIZE = 100 * 1024L; // 100 KB
    private static final Set<String> ALLOWED_TYPES = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("image/jpeg", "image/png")));
    private static final Set<String> ALLOWED_EXT = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("jpg", "jpeg", "png")));

    private final String uploadDir;
    private final String publicBaseUrl;
    private Path uploadPath;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir,
                              @Value("${app.upload.public-base-url}") String publicBaseUrl) {
        this.uploadDir = uploadDir;
        this.publicBaseUrl = publicBaseUrl.endsWith("/")
                ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                : publicBaseUrl;
    }

    @PostConstruct
    void init() throws IOException {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);
    }

    public String getUploadDir() {
        return uploadPath.toString();
    }

    public String storeProfileImage(Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw ApiException.badRequest("Field file tidak boleh kosong");
        }
        if (file.getSize() > MAX_SIZE) {
            throw ApiException.badRequest("Format Image tidak sesuai");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw ApiException.badRequest("Format Image tidak sesuai");
        }
        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String ext = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) ext = originalName.substring(dot + 1).toLowerCase();
        if (!ALLOWED_EXT.contains(ext)) {
            // fallback dari content-type
            ext = contentType.equalsIgnoreCase("image/png") ? "png" : "jpg";
        }

        String filename = userId + "_" + System.currentTimeMillis() + "." + ext;
        Path target = uploadPath.resolve(filename);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan file: " + e.getMessage(), e);
        }

        return publicBaseUrl + "/" + filename;
    }
}
