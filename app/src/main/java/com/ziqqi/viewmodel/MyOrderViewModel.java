package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.myordersmodel.MyOrdersResponse;
import com.ziqqi.repository.Repository;

public class MyOrderViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<MyOrdersResponse> myOrdersResponse;

    public MyOrderViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String authToken) {
        myOrdersResponse = repository.getOrder(authToken);
    }

    public MutableLiveData<MyOrdersResponse> getMyOrdersResponse() {
        return myOrdersResponse;
    }
}
