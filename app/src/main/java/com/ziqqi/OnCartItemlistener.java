package com.ziqqi;

import com.ziqqi.model.viewcartmodel.Payload;

public interface OnCartItemlistener {
    void onCartItemClick(String id, String type);

    void onCartItemClick(Payload payload, String type);
}
