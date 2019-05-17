package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.addfeedbackmodel.AddFeedbackModel;
import com.ziqqi.model.feedbackmastermodel.FeedbackMaster;
import com.ziqqi.model.productdetailsmodel.ProductDetails;
import com.ziqqi.model.similarproductsmodel.SimilarProduct;
import com.ziqqi.repository.Repository;

public class FeedbackViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<FeedbackMaster> feedbackMasterResponse;
    MutableLiveData<AddFeedbackModel> addFeedbackModel;

    public FeedbackViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void fetchData() {
        feedbackMasterResponse = repository.getFeedbackMaster();
    }

    public MutableLiveData<FeedbackMaster> getFeedbackMasterResponse() {
        return feedbackMasterResponse;
    }

    public void addFeedback(String authToken, String ratting) {
        addFeedbackModel = repository.addFeedback(authToken,ratting);
    }

    public MutableLiveData<AddFeedbackModel> addFeedbackResponse() {
        return addFeedbackModel;
    }
}
