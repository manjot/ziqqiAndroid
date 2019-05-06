package com.ziqqi.model.applycouponmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplyCouponModel {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Payload")
    @Expose
    private Payload payload;
    @SerializedName("Code")
    @Expose
    private Integer code;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}