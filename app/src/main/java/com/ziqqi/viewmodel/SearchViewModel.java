package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.repository.Repository;

public class SearchViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<SearchResponse> searchResponse;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String productName) {
     /*   if (searchResponse != null) {

        } else {*/
            searchResponse = repository.getSearch(productName);
       // }
    }

    public MutableLiveData<SearchResponse> getSearchResponse() {
        if (searchResponse != null)
            return searchResponse;
        else return null;
    }
}
