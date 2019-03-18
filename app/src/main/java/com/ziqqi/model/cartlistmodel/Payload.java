
package com.ziqqi.model.cartlistmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("mrp_price")
    @Expose
    private String mrpPrice;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("supplier_id")
    @Expose
    private String supplierId;
    @SerializedName("linkhref")
    @Expose
    private String linkhref;
    @SerializedName("small_desc")
    @Expose
    private String smallDesc;
    @SerializedName("display_order")
    @Expose
    private String displayOrder;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("is_bestseller")
    @Expose
    private String isBestseller;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("guest_id")
    @Expose
    private String guestId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("product_variant_id")
    @Expose
    private String productVariantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getMrpPrice() {
        return mrpPrice;
    }

    public void setMrpPrice(String mrpPrice) {
        this.mrpPrice = mrpPrice;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getLinkhref() {
        return linkhref;
    }

    public void setLinkhref(String linkhref) {
        this.linkhref = linkhref;
    }

    public String getSmallDesc() {
        return smallDesc;
    }

    public void setSmallDesc(String smallDesc) {
        this.smallDesc = smallDesc;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsBestseller() {
        return isBestseller;
    }

    public void setIsBestseller(String isBestseller) {
        this.isBestseller = isBestseller;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductVariantId() {
        return productVariantId;
    }

    public void setProductVariantId(String productVariantId) {
        this.productVariantId = productVariantId;
    }

}