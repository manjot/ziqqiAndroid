package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.repository.Repository;

public class MyAddressViewModel extends AndroidViewModel {
    Repository repository;
    private MutableLiveData<ShippingAddressModel> shippingAddressResponse;
    MutableLiveData<AddShippingAddressModel> addShippingAddressModel;

    public MyAddressViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }
    public void fetchShippingAddress(String authToken){
        shippingAddressResponse = repository.getShippingAddress(authToken);
    }

    public MutableLiveData<ShippingAddressModel> getShippingAddressResponse() {
        if (shippingAddressResponse != null)
            return shippingAddressResponse;
        else return null;
    }

    public void fetchData(String authToken, String name, String mobile, String country, String city, String location, String address) {
        addShippingAddressModel = repository.addShippingAddress(authToken, name, mobile, country, city, location, address);
    }

    public MutableLiveData<AddShippingAddressModel> addShippingAddressResponse() {
        return addShippingAddressModel;
    }

}
