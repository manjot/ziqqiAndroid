package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addtocart.AddToCart;
import com.ziqqi.model.addtowishlistmodel.AddToModel;
import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.removewislistmodel.DeleteWishlistModel;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.repository.Repository;

public class ProductDetailsViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ProductDetails> productDetailsResponse;
    MutableLiveData<SimilarProduct> similarProductResponse;
    MutableLiveData<AddToModel> addToModelResponse;
    MutableLiveData<DeleteWishlistModel> deleteWishlistResponse;
    MutableLiveData<AddToCart> addToCartResponse;

    public ProductDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(int productId, String authToken) {
     /*   if (searchResponse != null) {

        } else {*/
        productDetailsResponse = repository.getProductDetails(productId, authToken);
        // }
    }

    public void fetchSimilarProducts(int productId){
        similarProductResponse = repository.getSimilarProduct(productId);
    }

    public void addProductWishlist(String authToken, int productId){
        addToModelResponse = repository.addToWishlist(authToken, String.valueOf(productId));
    }

    public void addProductToCart(String productId, String authToken, String quantity){
        addToCartResponse = repository.addToCart(productId, authToken, quantity);
    }

    public void removeWishlist(String authToken, int productId){
        deleteWishlistResponse = repository.removeWishlist(authToken, String.valueOf(productId));
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

    public MutableLiveData<DeleteWishlistModel> deleteWishlistResponse() {
        return deleteWishlistResponse;
    }

    public MutableLiveData<AddToCart> addCartResponse() {
        return addToCartResponse;
    }
}
