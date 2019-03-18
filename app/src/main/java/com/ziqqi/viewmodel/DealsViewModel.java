package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.repository.Repository;

public class DealsViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<DealsResponse> dealsResponse;
    MutableLiveData<AddToModel> addToModelResponse;
    private MutableLiveData<AddToCart> addToCartResponse;

    public DealsViewModel(@NonNull Application application ) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(int page) {
      /*  if (searchResponse != null) {

        } else {*/
        dealsResponse = repository.getDeals(page);
       //  }
    }

    public void addProductWishlist(String authToken, String productId) {
        addToModelResponse = repository.addToWishlist(authToken, productId);
    }

    public void addToCart(String productId, String authToken,  String quantity) {
        addToCartResponse = repository.addToCart(productId, authToken, quantity);
    }

    public MutableLiveData<AddToModel> addWishlistResponse() {
        return addToModelResponse;
    }

    public MutableLiveData<AddToCart> addToCartResponse() {
        return addToCartResponse;
    }

    public MutableLiveData<DealsResponse> getDealsResponse() {
        if (dealsResponse != null)
            return dealsResponse;
        else return null;
    }
}
