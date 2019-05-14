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
import com.ziqqi.model.productvariantmodel.ProductVariantModel;
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
    MutableLiveData<ProductVariantModel> productVariantResponse;

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

    public void addProductWishlist(String authToken, int productId, String guest_id){
        addToModelResponse = repository.addToWishlist(authToken, String.valueOf(productId), guest_id);
    }

    public void addProductToCart(String productId, String authToken, String quantity, String guest_id){
        addToCartResponse = repository.addToCart(productId, authToken, quantity, guest_id);
    }

    public void removeWishlist(String authToken, int productId, String guest_id){
        deleteWishlistResponse = repository.removeWishlist(authToken, String.valueOf(productId), guest_id);
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

    public void getProductVariant(String productId){
        productVariantResponse = repository.getProductVariants(productId);
    }

    public MutableLiveData<ProductVariantModel> getProductVariantResponse() {
        return productVariantResponse;
    }
}
