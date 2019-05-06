package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.repository.Repository;

public class FilterProductViewModel extends AndroidViewModel {

    Repository repository;
    MutableLiveData<FeedbackMaster> feedbackMasterResponse;

    public FilterProductViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData() {
        feedbackMasterResponse = repository.getFeedbackMaster();
    }

    public MutableLiveData<FeedbackMaster> getFeedbackMasterResponse() {
        return feedbackMasterResponse;
    }
}
