package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.repository.Repository;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<BannerImageModel> data;
    private MutableLiveData<HomeCategoriesResponse> homeCategoryResponse;
    private MutableLiveData<AddToCart> addToCartResponse;
    private Repository repository;
    MutableLiveData<AddToModel> addToModelResponse;
    /*public final ObservableField<Integer> progressVisibility = new ObservableField<>(View.GONE);
    public final ObservableField<Integer> mainLayoutVisibility = new ObservableField<>(View.VISIBLE);*/

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void init() {
        if (this.data != null) {

        } else {
            data = repository.getHomeBanners();
        }
    }

    public void initHomeCategories() {
        if (homeCategoryResponse != null) {
        } else {
            homeCategoryResponse = repository.getHomeCategories();
        }
    }

    public void addToCart(String productId, String authToken,  String quantity, String guest_id) {
        addToCartResponse = repository.addToCart(productId, authToken, quantity, guest_id);
    }

    public void addProductWishlist(String authToken, String productId, String guest_id) {
        addToModelResponse = repository.addToWishlist(authToken, productId, guest_id);
    }

    public MutableLiveData<BannerImageModel> getHomeBanners() {
        return this.data;
    }

    public MutableLiveData<HomeCategoriesResponse> getHomeCategoryResponse() {
        return homeCategoryResponse;
    }

    public MutableLiveData<AddToModel> addWishlistResponse() {
        return addToModelResponse;
    }

    public MutableLiveData<AddToCart> addToCartResponse() {
        return addToCartResponse;
    }

}
