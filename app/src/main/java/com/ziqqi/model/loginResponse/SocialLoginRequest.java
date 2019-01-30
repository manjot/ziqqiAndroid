
package com.ziqqi.model.loginResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialLoginRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("device_type")
    @Expose
    private int deviceType;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("social_login_type")
    @Expose
    private String socialLoginType;
    @SerializedName("social_id")
    @Expose
    private String socialId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSocialLoginType() {
        return socialLoginType;
    }

    public void setSocialLoginType(String socialLoginType) {
        this.socialLoginType = socialLoginType;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

}
