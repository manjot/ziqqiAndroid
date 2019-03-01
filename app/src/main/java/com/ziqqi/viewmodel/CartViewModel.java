package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.ziqqi.repository.Repository;

public class CartViewModel extends AndroidViewModel {
    Repository repository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }
}
