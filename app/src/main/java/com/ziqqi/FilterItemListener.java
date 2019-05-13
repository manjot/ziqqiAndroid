package com.ziqqi;

import com.ziqqi.model.filterproductmodel.Payload;

public interface FilterItemListener {
    void onFilterCategoryClick(int position, boolean isChecked);
    void onFilterBrandClick(int position, boolean isChecked);
    void onFilterVariantClick( int position, boolean isChecked);
    void onFilterFeatureClick( int position, boolean isChecked);
}
