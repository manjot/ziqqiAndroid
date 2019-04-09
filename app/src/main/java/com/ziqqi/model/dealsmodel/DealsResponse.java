package com.ziqqi.model.dealsmodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DealsResponse {

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
    private List<Payload> payload = null;
    @SerializedName("Code")
    @Expose
    private Integer code;
    @SerializedName("category_banner")
    @Expose
    private String category_banner;

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

    public List<Payload> getPayload() {
        return payload;
    }

    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCategory_banner() {
        return category_banner;
    }

    public void setCategory_banner(String category_banner) {
        this.category_banner = category_banner;
    }
}