
package com.ziqqi.model.productdetailsmodel;

import java.util.List;
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
    @SerializedName("ref_no")
    @Expose
    private String refNo;
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
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("specifications")
    @Expose
    private String specifications;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("image")
    @Expose
    private List<String> image = null;
    @SerializedName("variant_image")
    @Expose
    private List<String> variant_image = null;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("is_wishlist")
    @Expose
    private Integer is_wishlist;
    @SerializedName("total_review")
    @Expose
    private Integer totalReview;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;

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

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(Integer totalReview) {
        this.totalReview = totalReview;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getIs_wishlist() {
        return is_wishlist;
    }

    public void setIs_wishlist(Integer is_wishlist) {
        this.is_wishlist = is_wishlist;
    }

    public List<String> getVariant_image() {
        return variant_image;
    }

    public void setVariant_image(List<String> variant_image) {
        this.variant_image = variant_image;
    }
}