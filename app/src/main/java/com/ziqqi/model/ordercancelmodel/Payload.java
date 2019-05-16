package com.ziqqi.model.ordercancelmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_datetime")
    @Expose
    private String orderDatetime;
    @SerializedName("customers_id")
    @Expose
    private String customersId;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("shipping_amount")
    @Expose
    private String shippingAmount;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("sls_grand_total")
    @Expose
    private String slsGrandTotal;
    @SerializedName("baddress1")
    @Expose
    private String baddress1;
    @SerializedName("bcity")
    @Expose
    private String bcity;
    @SerializedName("payment_gateway")
    @Expose
    private String paymentGateway;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orders_id")
    @Expose
    private String ordersId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("qty")
    @Expose
    private String qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderDatetime() {
        return orderDatetime;
    }

    public void setOrderDatetime(String orderDatetime) {
        this.orderDatetime = orderDatetime;
    }

    public String getCustomersId() {
        return customersId;
    }

    public void setCustomersId(String customersId) {
        this.customersId = customersId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(String shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getSlsGrandTotal() {
        return slsGrandTotal;
    }

    public void setSlsGrandTotal(String slsGrandTotal) {
        this.slsGrandTotal = slsGrandTotal;
    }

    public String getBaddress1() {
        return baddress1;
    }

    public void setBaddress1(String baddress1) {
        this.baddress1 = baddress1;
    }

    public String getBcity() {
        return bcity;
    }

    public void setBcity(String bcity) {
        this.bcity = bcity;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

}
