package com.nutech.genggam_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistrationRequest {

    @NotBlank(message = "Parameter email tidak boleh kosong")
    @Email(message = "Paramter email tidak sesuai format")
    private String email;

    @JsonProperty("first_name")
    @NotBlank(message = "Parameter first_name tidak boleh kosong")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Parameter last_name tidak boleh kosong")
    private String lastName;

    @NotBlank(message = "Parameter password tidak boleh kosong")
    @Size(min = 8, message = "Parameter password length minimal 8 karakter")
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

