package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.citymodel.CityResponse;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.repository.Repository;

public class BillingInfoViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<AddBillingAddressModel> addBillingAddressModel;
    MutableLiveData<CountryResponse> countryResponse;
    MutableLiveData<CityResponse> cityResponse;

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

    public void fetchCountry() {
        countryResponse = repository.getCountries();
    }

    public MutableLiveData<CountryResponse> getCountryResponse() {
        return countryResponse;
    }

    public void fetchCity(String country_id) {
        cityResponse = repository.getCities(country_id);
    }

    public MutableLiveData<CityResponse> getCityResponse() {
        return cityResponse;
    }
}
