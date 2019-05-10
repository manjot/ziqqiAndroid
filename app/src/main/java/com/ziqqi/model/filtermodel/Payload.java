package com.ziqqi.model.filtermodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payload {

    @SerializedName("categoryFilter")
    @Expose
    private CategoryFilter categoryFilter;
    @SerializedName("brandFilter")
    @Expose
    private BrandFilter brandFilter;
    @SerializedName("variantFilter")
    @Expose
    private List<VariantFilter> variantFilter = null;
    @SerializedName("featureFilter")
    @Expose
    private List<FeatureFilter> featureFilter = null;

    public CategoryFilter getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(CategoryFilter categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public BrandFilter getBrandFilter() {
        return brandFilter;
    }

    public void setBrandFilter(BrandFilter brandFilter) {
        this.brandFilter = brandFilter;
    }

    public List<VariantFilter> getVariantFilter() {
        return variantFilter;
    }

    public void setVariantFilter(List<VariantFilter> variantFilter) {
        this.variantFilter = variantFilter;
    }

    public List<FeatureFilter> getFeatureFilter() {
        return featureFilter;
    }

    public void setFeatureFilter(List<FeatureFilter> featureFilter) {
        this.featureFilter = featureFilter;
    }

}
