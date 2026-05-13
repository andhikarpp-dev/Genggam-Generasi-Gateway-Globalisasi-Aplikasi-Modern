package com.nutech.genggam_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nutech.genggam_api.model.User;

public class ProfileResponse {

    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("profile_image")
    private String profileImage;

    public static ProfileResponse from(User u) {
        ProfileResponse r = new ProfileResponse();
        r.email = u.getEmail();
        r.firstName = u.getFirstName();
        r.lastName = u.getLastName();
        r.profileImage = u.getProfileImage();
        return r;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
}
