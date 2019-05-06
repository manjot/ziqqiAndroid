package com.ziqqi.model.verifyotpmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("email")
    @Expose
    private String email;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
