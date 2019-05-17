package com.ziqqi.model.ordertrackingmodel;

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
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("product_desc")
    @Expose
    private String productDesc;
    @SerializedName("customer_name")
    @Expose
    private String customer_name;
    @SerializedName("is_cancel_date")
    @Expose
    private Integer is_cancel_date;

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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public Integer getIs_cancel_date() {
        return is_cancel_date;
    }

    public void setIs_cancel_date(Integer is_cancel_date) {
        this.is_cancel_date = is_cancel_date;
    }
}