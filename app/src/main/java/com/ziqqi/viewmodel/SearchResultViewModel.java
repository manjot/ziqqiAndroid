package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.repository.Repository;

public class SearchResultViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<SearchResponse> searchResponse;
    MutableLiveData<AddToModel> addToModelResponse;
    private MutableLiveData<AddToCart> addToCartResponse;

    public SearchResultViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String categoryId, String page) {
     /*   if (searchResponse != null) {

        } else {*/
         searchResponse = repository.getSearch(categoryId, page);
        // }
    }

    public void addProductWishlist(String authToken, String productId, String guest_id) {
        addToModelResponse = repository.addToWishlist(authToken, productId, guest_id);
    }

    public void addToCart(String productId, String authToken,  String quantity, String guest_id) {
        addToCartResponse = repository.addToCart(productId, authToken, quantity, guest_id);
    }

    public MutableLiveData<AddToModel> addWishlistResponse() {
        return addToModelResponse;
    }

    public MutableLiveData<AddToCart> addToCartResponse() {
        return addToCartResponse;
    }

    public MutableLiveData<SearchResponse> getSearchResponse() {
        if (searchResponse != null)
            return searchResponse;
        else return null;
    }
}
