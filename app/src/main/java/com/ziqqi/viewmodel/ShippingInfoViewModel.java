package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.repository.Repository;

public class ShippingInfoViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<AddShippingAddressModel> addShippingAddressModel;

    public ShippingInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String authToken, String name, String mobile, String country, String city, String location, String address) {
        addShippingAddressModel = repository.addBillingAddress(authToken, name, mobile, country, city, location, address);
    }

    public MutableLiveData<AddShippingAddressModel> addShippingAddressResponse() {
        return addShippingAddressModel;
    }
}
