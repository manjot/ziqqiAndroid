package com.ziqqi.model.filtermodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VariantFilter {

    @SerializedName("filter_name")
    @Expose
    private String filterName;
    @SerializedName("filter_value")
    @Expose
    private List<FilterValue__> filterValue = null;

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<FilterValue__> getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(List<FilterValue__> filterValue) {
        this.filterValue = filterValue;
    }

}