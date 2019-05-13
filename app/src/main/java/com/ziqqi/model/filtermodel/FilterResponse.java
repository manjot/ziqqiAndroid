package com.ziqqi.model.filtermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterResponse {

    @SerializedName("Error")
    @Expose
    private Boolean error;
    @SerializedName("Code")
    @Expose
    private Integer code;
    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Min_price")
    @Expose
    private String minPrice;
    @SerializedName("Max_price")
    @Expose
    private String maxPrice;
    @SerializedName("Payload")
    @Expose
    private Payload payload;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }
}
