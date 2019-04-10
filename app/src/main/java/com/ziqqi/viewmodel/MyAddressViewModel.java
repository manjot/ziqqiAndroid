package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.addshippingaddressmodel.AddShippingAddressModel;
import com.ziqqi.model.getbillingaddressmodel.BillingAddressModel;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.repository.Repository;

public class MyAddressViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<BillingAddressModel> getBillingAddressModel;
    MutableLiveData<AddBillingAddressModel> addBillingAddressModel;

    public MyAddressViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }
    public void getBillingAddress(String authToken){
        getBillingAddressModel = repository.getBillingAddress(authToken);
    }

    public MutableLiveData<BillingAddressModel> getBillingAddressResponse() {
        return getBillingAddressModel;
    }

    public void fetchData(String authToken, String Fname, String Lname, String mobile, String county, String city, String location, String address) {
        addBillingAddressModel = repository.addBillingAddress(authToken, Fname, Lname, mobile, county, city, location, address);
    }

    public MutableLiveData<AddBillingAddressModel> addBillingAddressResponse() {
        return addBillingAddressModel;
    }

}
