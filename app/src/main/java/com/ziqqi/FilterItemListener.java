package com.ziqqi;

import com.ziqqi.model.filterproductmodel.Payload;

public interface FilterItemListener {
    void onFilterCategoryClick(int position);
    void onFilterBrandClick(int position);
    void onFilterVariantClick( int position);
    void onFilterFeatureClick( int position);
}
