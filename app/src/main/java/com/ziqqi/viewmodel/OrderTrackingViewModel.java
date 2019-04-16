package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.ordertrackingmodel.OrderTrackingResponse;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.repository.Repository;

public class OrderTrackingViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<OrderTrackingResponse> orderTrackingResponse;


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
}
