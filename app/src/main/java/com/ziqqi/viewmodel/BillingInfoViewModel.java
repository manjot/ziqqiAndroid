package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.repository.Repository;

public class BillingInfoViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<AddBillingAddressModel> addBillingAddressModel;

    public BillingInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String authToken, String firstName, String lastName, String mobile, String address, String county) {
        addBillingAddressModel = repository.addBillingAddress(authToken, firstName, lastName, mobile, address, county);
    }

    public MutableLiveData<AddBillingAddressModel> addBillingAddressResponse() {
        return addBillingAddressModel;
    }
}
