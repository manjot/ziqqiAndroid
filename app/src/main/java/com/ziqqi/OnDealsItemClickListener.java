package com.ziqqi;

import com.ziqqi.model.dealsmodel.Payload;

public interface OnDealsItemClickListener {
    void onItemClick(String id, String type);

    void onItemClick(Payload payload, String type);
}