package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.forgotpasswordmodel.ForgotPasswordResponse;
import com.ziqqi.model.viewcartmodel.ViewCartResponse;
import com.ziqqi.repository.Repository;

public class ChangePasswordViewModel extends AndroidViewModel {
    Repository repository;
    MutableLiveData<ForgotPasswordResponse> forgotPasswordResponse;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public void changePassword(String email, String otp, String newPassword  ) {
      /*  if (searchResponse != null) {

        } else {*/
        forgotPasswordResponse = repository.forgotPassword(email, otp, newPassword);
        //  }
    }

    public MutableLiveData<ForgotPasswordResponse> changePasswordResponse() {
        if (forgotPasswordResponse != null)
            return forgotPasswordResponse;
        else return null;
    }
}
