package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.changequantitymodel.ChangeQuantityResponse;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.deletecartmodel.DeleteCartResponse;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.repository.Repository;

public class CartViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ViewCartResponse> viewCartResponse;
    MutableLiveData<DeleteCartResponse> deleteCartResponse;
    MutableLiveData<AddToCart> addToCartResponse;
    MutableLiveData<ChangeQuantityResponse> changeQuantityResponse;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String authToken) {
      /*  if (searchResponse != null) {

        } else {*/
        viewCartResponse = repository.viewCart(authToken);
        //  }
    }

    public MutableLiveData<ViewCartResponse> getCartResponse() {
        if (viewCartResponse != null)
            return viewCartResponse;
        else return null;
    }

    public void deleteCart(String authToken, String ProductId) {
      /*  if (searchResponse != null) {

        } else {*/
        deleteCartResponse = repository.deleteCart(authToken, ProductId);
        //  }
    }

    public MutableLiveData<DeleteCartResponse> deleteCartResponse() {
        if (deleteCartResponse != null)
            return deleteCartResponse;
        else return null;
    }

    public void addProductToCart(String productId, String authToken, String quantity){
        addToCartResponse = repository.addToCart(productId, authToken, quantity);
    }

    public MutableLiveData<AddToCart> addCartResponse() {
        return addToCartResponse;
    }

    public void changeCart(String authToken, String productId, String type){
        changeQuantityResponse = repository.changeQuantity(authToken, productId, type);
    }

    public MutableLiveData<ChangeQuantityResponse> changeCartResponse() {
        return changeQuantityResponse;
    }
}
