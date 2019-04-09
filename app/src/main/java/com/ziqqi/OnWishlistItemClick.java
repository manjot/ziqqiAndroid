package com.ziqqi;

import com.ziqqi.model.viewwishlistmodel.Payload;

public interface OnWishlistItemClick {
    void onItemClick(String id, String type);

    void onItemClick(Payload payload, String type);
}
