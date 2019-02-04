package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.homecategorymodel.HomeCategoriesResponse;
import com.ziqqi.model.subcategoriesmodel.SubCategories;
import com.ziqqi.repository.Repository;

public class SubCategoryViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<HomeCategoriesResponse> subCategoriesResponse;

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
}
