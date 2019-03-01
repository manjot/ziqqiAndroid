package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.ziqqi.repository.Repository;

public class ProfileViewModel extends AndroidViewModel {

    Repository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }
}
