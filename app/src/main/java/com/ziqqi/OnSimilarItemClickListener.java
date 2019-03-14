package com.ziqqi;

import com.ziqqi.model.similarproductsmodel.Payload;

public interface OnSimilarItemClickListener {
    void onItemClick(String id, String type);

    void onItemClick(Payload payload, String type);
}
