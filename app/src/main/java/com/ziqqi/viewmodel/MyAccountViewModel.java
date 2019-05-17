package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.helpcenterbyidmodel.HelpCenterByIdResponse;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.repository.Repository;

public class MyAccountViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<HelpCenterByIdResponse> helpCenterByIdResponse;

    public MyAccountViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData(int helpId, String lang) {
      /*  if (searchResponse != null) {

        } else {*/
        helpCenterByIdResponse = repository.getHelpById(helpId, lang);
        //  }
    }

    public MutableLiveData<HelpCenterByIdResponse> getHelpByIdResponse() {
        if (helpCenterByIdResponse != null)
            return helpCenterByIdResponse;
        else return null;
    }
}
