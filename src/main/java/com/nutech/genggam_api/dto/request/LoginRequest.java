package com.nutech.genggam_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "Parameter email tidak boleh kosong")
    @Email(message = "Paramter email tidak sesuai format")
    private String email;

    @NotBlank(message = "Parameter password tidak boleh kosong")
    @Size(min = 8, message = "Parameter password length minimal 8 karakter")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
