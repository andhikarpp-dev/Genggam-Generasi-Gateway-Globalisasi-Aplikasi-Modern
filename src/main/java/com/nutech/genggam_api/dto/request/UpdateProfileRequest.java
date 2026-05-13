package com.nutech.genggam_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UpdateProfileRequest {

    @JsonProperty("first_name")
    @NotBlank(message = "Parameter first_name tidak boleh kosong")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Parameter last_name tidak boleh kosong")
    private String lastName;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
