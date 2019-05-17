package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.bannerimagemodel.BannerImageModel;
import com.ziqqi.model.helpcentermodel.HelpCenterModel;
import com.ziqqi.model.searchcategorymodel.SearchCategory;
import com.ziqqi.repository.Repository;

public class HelpCenterViewModel extends AndroidViewModel {

    private MutableLiveData<HelpCenterModel> helpCenterResponse;
    Repository repository;

    public HelpCenterViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchHelpCenter(String lang){
        helpCenterResponse = repository.getHelpCenter(lang);
    }

    public MutableLiveData<HelpCenterModel> getHelpResponse() {
        if (helpCenterResponse != null)
            return helpCenterResponse;
        else return null;
    }
}
