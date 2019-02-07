package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.productcategorymodel.ProductCategory;
import com.ziqqi.repository.Repository;

public class ViewAllViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ProductCategory> productCategory;

    public ViewAllViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String id, String page) {
        productCategory = repository.getCategoryProducts(id, page);
    }

    public MutableLiveData<ProductCategory> getCategoryProduct() {
        if (productCategory != null)
            return productCategory;
        else return null;
    }
}
