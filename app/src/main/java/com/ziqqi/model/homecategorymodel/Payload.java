
package com.ziqqi.model.homecategorymodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("parent_category_id")
    @Expose
    private String parentCategoryId;
    @SerializedName("menu_display")
    @Expose
    private String menuDisplay;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("linkhref")
    @Expose
    private String linkhref;
    @SerializedName("home_banner")
    @Expose
    private String homeBanner;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_keyword")
    @Expose
    private String metaKeyword;
    @SerializedName("meta_desc")
    @Expose
    private String metaDesc;
    @SerializedName("external_link")
    @Expose
    private String externalLink;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("display_order")
    @Expose
    private String displayOrder;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    @SerializedName("bestsellerProduct")
    @Expose
    private List<Object> bestsellerProduct = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getMenuDisplay() {
        return menuDisplay;
    }

    public void setMenuDisplay(String menuDisplay) {
        this.menuDisplay = menuDisplay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkhref() {
        return linkhref;
    }

    public void setLinkhref(String linkhref) {
        this.linkhref = linkhref;
    }

    public String getHomeBanner() {
        return homeBanner;
    }

    public void setHomeBanner(String homeBanner) {
        this.homeBanner = homeBanner;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public String getMetaDesc() {
        return metaDesc;
    }

    public void setMetaDesc(String metaDesc) {
        this.metaDesc = metaDesc;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<Object> getBestsellerProduct() {
        return bestsellerProduct;
    }

    public void setBestsellerProduct(List<Object> bestsellerProduct) {
        this.bestsellerProduct = bestsellerProduct;
    }

}
