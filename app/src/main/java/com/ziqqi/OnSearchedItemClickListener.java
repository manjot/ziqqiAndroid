package com.ziqqi;

import com.ziqqi.model.searchmodel.Payload;

public interface OnSearchedItemClickListener {
    void onItemClick(String id, String type);

    void onItemClick(Payload payload, String type);
}
