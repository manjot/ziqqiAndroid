
package com.ziqqi.model.productdetailsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("rate_star")
    @Expose
    private String rateStar;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("rate_review")
    @Expose
    private String rateReview;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("rate_datetime")
    @Expose
    private String rateDatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRateStar() {
        return rateStar;
    }

    public void setRateStar(String rateStar) {
        this.rateStar = rateStar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRateReview() {
        return rateReview;
    }

    public void setRateReview(String rateReview) {
        this.rateReview = rateReview;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getRateDatetime() {
        return rateDatetime;
    }

    public void setRateDatetime(String rateDatetime) {
        this.rateDatetime = rateDatetime;
    }

}
