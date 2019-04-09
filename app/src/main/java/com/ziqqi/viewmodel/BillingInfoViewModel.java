package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addbillingaddressmodel.AddBillingAddressModel;
import com.ziqqi.model.citymodel.CityResponse;
import com.ziqqi.model.countrymodel.CountryResponse;
import com.ziqqi.model.getbillingaddressmodel.BillingAddressModel;
import com.ziqqi.repository.Repository;

public class BillingInfoViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<AddBillingAddressModel> addBillingAddressModel;
    MutableLiveData<BillingAddressModel> getBillingAddressModel;
    MutableLiveData<CountryResponse> countryResponse;
    MutableLiveData<CityResponse> cityResponse;

    public BillingInfoViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String authToken, String Name, String mobile, String county, String city, String location, String address) {
        addBillingAddressModel = repository.addBillingAddress(authToken, Name, mobile, county, city, location, address);
    }

    public MutableLiveData<AddBillingAddressModel> addBillingAddressResponse() {
        return addBillingAddressModel;
    }

    public void getBillingAddress(String authToken){
        getBillingAddressModel = repository.getBillingAddress(authToken);
    }

    public MutableLiveData<BillingAddressModel> getBillingAddressResponse() {
        return getBillingAddressModel;
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
