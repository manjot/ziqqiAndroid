
package com.ziqqi.model.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("phone_code")
    @Expose
    private String phoneCode;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("reg_date")
    @Expose
    private String regDate;
    @SerializedName("zoho_contact_id")
    @Expose
    private String zohoContactId;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_facebook_login")
    @Expose
    private String isFacebookLogin;
    @SerializedName("facebook_oauth_uid")
    @Expose
    private String facebookOauthUid;
    @SerializedName("is_google_login")
    @Expose
    private String isGoogleLogin;
    @SerializedName("google_oauth_uid")
    @Expose
    private String googleOauthUid;
    @SerializedName("email_token_id")
    @Expose
    private String emailTokenId;
    @SerializedName("mobile_token_id")
    @Expose
    private String mobileTokenId;
    @SerializedName("password_token_id")
    @Expose
    private String passwordTokenId;
    @SerializedName("password_reset_date")
    @Expose
    private String passwordResetDate;
    @SerializedName("is_email_verify")
    @Expose
    private String isEmailVerify;
    @SerializedName("is_mobile_verify")
    @Expose
    private String isMobileVerify;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getZohoContactId() {
        return zohoContactId;
    }

    public void setZohoContactId(String zohoContactId) {
        this.zohoContactId = zohoContactId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsFacebookLogin() {
        return isFacebookLogin;
    }

    public void setIsFacebookLogin(String isFacebookLogin) {
        this.isFacebookLogin = isFacebookLogin;
    }

    public String getFacebookOauthUid() {
        return facebookOauthUid;
    }

    public void setFacebookOauthUid(String facebookOauthUid) {
        this.facebookOauthUid = facebookOauthUid;
    }

    public String getIsGoogleLogin() {
        return isGoogleLogin;
    }

    public void setIsGoogleLogin(String isGoogleLogin) {
        this.isGoogleLogin = isGoogleLogin;
    }

    public String getGoogleOauthUid() {
        return googleOauthUid;
    }

    public void setGoogleOauthUid(String googleOauthUid) {
        this.googleOauthUid = googleOauthUid;
    }

    public String getEmailTokenId() {
        return emailTokenId;
    }

    public void setEmailTokenId(String emailTokenId) {
        this.emailTokenId = emailTokenId;
    }

    public String getMobileTokenId() {
        return mobileTokenId;
    }

    public void setMobileTokenId(String mobileTokenId) {
        this.mobileTokenId = mobileTokenId;
    }

    public String getPasswordTokenId() {
        return passwordTokenId;
    }

    public void setPasswordTokenId(String passwordTokenId) {
        this.passwordTokenId = passwordTokenId;
    }

    public String getPasswordResetDate() {
        return passwordResetDate;
    }

    public void setPasswordResetDate(String passwordResetDate) {
        this.passwordResetDate = passwordResetDate;
    }

    public String getIsEmailVerify() {
        return isEmailVerify;
    }

    public void setIsEmailVerify(String isEmailVerify) {
        this.isEmailVerify = isEmailVerify;
    }

    public String getIsMobileVerify() {
        return isMobileVerify;
    }

    public void setIsMobileVerify(String isMobileVerify) {
        this.isMobileVerify = isMobileVerify;
    }

}
