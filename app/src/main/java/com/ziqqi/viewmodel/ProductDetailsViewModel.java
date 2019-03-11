package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.repository.Repository;

public class ProductDetailsViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ProductDetails> productDetailsResponse;
    MutableLiveData<SimilarProduct> similarProductResponse;
    MutableLiveData<AddToModel> addToModelResponse;

    public ProductDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(int productId) {
     /*   if (searchResponse != null) {

        } else {*/
        productDetailsResponse = repository.getProductDetails(productId);
        // }
    }

    public void fetchSimilarProducts(int productId){
        similarProductResponse = repository.getSimilarProduct(productId);
    }

    public void addProductWishlist(String authToken, int productId){
        addToModelResponse = repository.addToWishlist(authToken, productId);
    }

    public MutableLiveData<ProductDetails> getProductDetailsResponse() {
        if (productDetailsResponse != null)
            return productDetailsResponse;
        else return null;
    }

    public MutableLiveData<SimilarProduct> getSimilarProductsResponse() {
        return similarProductResponse;
    }

    public MutableLiveData<AddToModel> addWishlistResponse() {
        return addToModelResponse;
    }
}
