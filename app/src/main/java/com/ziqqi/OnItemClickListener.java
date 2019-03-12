package com.ziqqi;

import com.ziqqi.model.homecategorymodel.BestsellerProduct;

public interface OnItemClickListener {
    void onItemClick(String id, String type);

    void onItemClick(BestsellerProduct bestsellerProduct, String type);
}
