package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.VerifyOtpResponse;
import com.ziqqi.repository.Repository;
import com.ziqqi.repository.SignUpRepository;

public class OtpViewModel extends AndroidViewModel {

    SignUpRepository repository;
    MutableLiveData<VerifyOtpResponse> verifyOtpResponse;

    public OtpViewModel(@NonNull Application application) {
        super(application);
        repository = new SignUpRepository();
    }

    public MutableLiveData<VerifyOtpResponse> getVerifyOtp() {
        if (verifyOtpResponse != null) {
            return verifyOtpResponse;
        } else return null;
    }

    public void verifyOtp(String customerId, String otp) {
        if (verifyOtpResponse != null) {

        } else {
            verifyOtpResponse = repository.verifyOtpResponse(customerId, otp);
        }
    }
}
