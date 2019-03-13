package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.dealsmodel.DealsResponse;
import com.ziqqi.model.searchmodel.SearchResponse;
import com.ziqqi.repository.Repository;

public class DealsViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<DealsResponse> dealsResponse;

    public DealsViewModel(@NonNull Application application ) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(int page) {
      /*  if (searchResponse != null) {

        } else {*/
        dealsResponse = repository.getDeals(page);
       //  }
    }

    public MutableLiveData<DealsResponse> getDealsResponse() {
        if (dealsResponse != null)
            return dealsResponse;
        else return null;
    }
}
