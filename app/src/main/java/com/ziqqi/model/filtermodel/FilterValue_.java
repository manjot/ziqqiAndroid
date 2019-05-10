package com.ziqqi.model.filtermodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterValue_ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("parent_category_id")
    @Expose
    private String parentCategoryId;
    @SerializedName("colorcode")
    @Expose
    private String colorcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

}