package com.nutech.genggam_api.security;

import com.nutech.genggam_api.exception.ApiException;
import com.nutech.genggam_api.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Helper untuk ambil user yang sedang login dari SecurityContext.
 */
public final class AuthUtil {

    private AuthUtil() {}

    public static User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User u)) {
            throw ApiException.unauthorized("Token tidak tidak valid atau kadaluwarsa");
        }
        return u;
    }
}
