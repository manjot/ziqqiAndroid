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

    public void addToCart(String productId, String customerId, String productVariantId, String quantity) {
        addToCartResponse = repository.addToCart(productId, customerId, productVariantId, quantity);
    }

    public void addProductWishlist(String authToken, String productId) {
        addToModelResponse = repository.addToWishlist(authToken, productId);
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
