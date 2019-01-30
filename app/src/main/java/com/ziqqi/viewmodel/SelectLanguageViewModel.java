package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.languagemodel.LanguageModel;
import com.ziqqi.repository.LanguageRepository;

public class SelectLanguageViewModel extends AndroidViewModel {
    private MutableLiveData<LanguageModel> data;
    private LanguageRepository repository;

    public SelectLanguageViewModel(@NonNull Application application) {
        super(application);
        repository = new LanguageRepository();
    }

    public void init() {
        if (this.data != null) {

        } else {
            data = repository.getLanguageData();
        }
    }

    public MutableLiveData<LanguageModel> getLanguageData() {
        return this.data;
    }

}
