package com.ziqqi;

import com.ziqqi.model.productcategorymodel.Payload;

public interface OnAllItemClickListener {
    void onItemClick(String id, String type);

    void onItemClick(Payload payload, String type);
}