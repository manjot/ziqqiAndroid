package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.applycouponmodel.ApplyCouponModel;
import com.ziqqi.model.myaddressmodel.ShippingAddressModel;
import com.ziqqi.model.placeordermodel.PlaceOrderResponse;
import com.ziqqi.repository.Repository;

public class PlaceOrderViewModel extends AndroidViewModel {

    Repository repository;
    private MutableLiveData<PlaceOrderResponse> placeOrderResponse;
    private MutableLiveData<ApplyCouponModel> applyCouponResponse;

    public PlaceOrderViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void placeOder(String authToken, String guest_id, String paymentMethod, String orderStatus, String paymentStatus, String transacttionId, String walletNumber, String billingFname, String billingLname, String billingMobile, String pickupName, String pickupMobile, String pickupCountry, String pickup_city, String pickup_location, String pickup_address, String payment_currency){
        placeOrderResponse = repository.placeOrder(authToken, guest_id, paymentMethod, orderStatus, paymentStatus, transacttionId, walletNumber, billingFname, billingLname, billingMobile, pickupName, pickupMobile, pickupCountry, pickup_city, pickup_location,  pickup_address, payment_currency);
    }

    public MutableLiveData<PlaceOrderResponse> getPlaceOrderResponse() {
        if (placeOrderResponse != null)
            return placeOrderResponse;
        else return null;
    }

    public void applyCoupon(String authToken, String couponCode, String guest_id){
        applyCouponResponse = repository.applyCoupon(authToken, couponCode, guest_id);
    }

    public MutableLiveData<ApplyCouponModel> applyCouponResponse() {
        if (applyCouponResponse != null)
            return applyCouponResponse;
        else return null;
    }
}
