package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.repository.Repository;

public class SearchResultViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<SearchResponse> searchResponse;

    public SearchResultViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(String categoryId, String page) {
     /*   if (searchResponse != null) {

        } else {*/
         searchResponse = repository.getSearch(categoryId, page);
        // }
    }

    public MutableLiveData<SearchResponse> getSearchResponse() {
        if (searchResponse != null)
            return searchResponse;
        else return null;
    }
}
