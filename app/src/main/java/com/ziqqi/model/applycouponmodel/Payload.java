package com.ziqqi.model.applycouponmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("sub_total")
    @Expose
    private Double subTotal;
    @SerializedName("shipping")
    @Expose
    private Integer shipping;
    @SerializedName("discount")
    @Expose
    private Double discount;

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}