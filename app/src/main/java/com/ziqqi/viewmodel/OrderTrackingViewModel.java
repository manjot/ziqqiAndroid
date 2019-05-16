package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.ordercancelmodel.OrderCancelResponse;
import com.ziqqi.model.ordertrackingmodel.OrderTrackingResponse;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.repository.Repository;

public class OrderTrackingViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<OrderTrackingResponse> orderTrackingResponse;
    MutableLiveData<OrderCancelResponse> orderCancelResponse;


    public OrderTrackingViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String authToken) {
      /*  if (searchResponse != null) {

        } else {*/
        orderTrackingResponse = repository.getOrderTrack(authToken);
        //  }
    }

    public MutableLiveData<OrderTrackingResponse> getOrderTrackingResponse() {
        if (orderTrackingResponse != null)
            return orderTrackingResponse;
        else return null;
    }

    public void cancelOrder(String authToken, String productId, String orderId, String reason) {
      /*  if (searchResponse != null) {

        } else {*/
        orderCancelResponse = repository.cancelOrder(authToken, productId, orderId, reason);
        //  }
    }

    public MutableLiveData<OrderCancelResponse> cancelOrderResponse() {
        if (orderCancelResponse != null)
            return orderCancelResponse;
        else return null;
    }
}
