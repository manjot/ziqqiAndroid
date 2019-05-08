package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.filterproductmodel.FilterCategoriesResponse;
import com.ziqqi.repository.Repository;

public class FilterProductViewModel extends AndroidViewModel {

    Repository repository;
    MutableLiveData<FilterCategoriesResponse> filterCategoriesResponse;

    public FilterProductViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String menuId) {
        filterCategoriesResponse = repository.getFilterCategories(menuId);
    }

    public MutableLiveData<FilterCategoriesResponse> getFilterResponse() {
        return filterCategoriesResponse;
    }
}
