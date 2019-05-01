package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.subcategoriesmodel.SubCategories;
import com.ziqqi.repository.Repository;

public class SubCategoryViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<HomeCategoriesResponse> subCategoriesResponse;
    private MutableLiveData<AddToCart> addToCartResponse;
    MutableLiveData<AddToModel> addToModelResponse;

    public SubCategoryViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String categoryId) {
        if (subCategoriesResponse != null) {
        } else {
            subCategoriesResponse = repository.getSubCategories(categoryId);
        }
    }

    public MutableLiveData<HomeCategoriesResponse> getSubCategoriesResponse() {
        if (subCategoriesResponse != null)
            return subCategoriesResponse;
        else return null;
    }

    public void addToCart(String productId, String authToken,  String quantity, String guest_id) {
        addToCartResponse = repository.addToCart(productId, authToken, quantity, guest_id);
    }

    public void addProductWishlist(String authToken, String productId, String guest_id) {
        addToModelResponse = repository.addToWishlist(authToken, productId, guest_id);
    }
    public MutableLiveData<AddToModel> addWishlistResponse() {
        return addToModelResponse;
    }

    public MutableLiveData<AddToCart> addToCartResponse() {
        return addToCartResponse;
    }

}
