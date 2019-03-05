package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.repository.Repository;

public class ProductDetailsViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ProductDetails> productDetailsResponse;

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

    public MutableLiveData<ProductDetails> getProductDetailsResponse() {
        if (productDetailsResponse != null)
            return productDetailsResponse;
        else return null;
    }
}
