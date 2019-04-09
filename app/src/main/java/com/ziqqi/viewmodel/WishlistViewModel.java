package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.viewwishlistmodel.ViewWishlist;
import com.ziqqi.repository.Repository;

public class WishlistViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ViewWishlist> viewWishlistResponse;
    private MutableLiveData<AddToCart> addToCartResponse;


    public WishlistViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchWishlist(String authToken){
        viewWishlistResponse = repository.fetchWishlist(authToken);
    }

    public MutableLiveData<ViewWishlist> getWishlistResponse() {
        if (viewWishlistResponse != null)
            return viewWishlistResponse;
        else return null;
    }

    public void addToCart(String productId, String authToken,  String quantity) {
        addToCartResponse = repository.addToCart(productId, authToken, quantity);
    }

    public MutableLiveData<AddToCart> addToCartResponse() {
        return addToCartResponse;
    }
}
