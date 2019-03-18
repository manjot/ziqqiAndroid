package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.repository.Repository;

public class MyAddressViewModel extends AndroidViewModel {
    Repository repository;
    private MutableLiveData<ShippingAddressModel> shippingAddressResponse;

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

}
