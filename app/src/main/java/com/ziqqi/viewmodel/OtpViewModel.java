package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.ziqqi.model.verifyotpmodel.VerifyOtpResponse;
import com.ziqqi.model.resendotpmodel;
import com.ziqqi.repository.SignUpRepository;

public class OtpViewModel extends AndroidViewModel {

    SignUpRepository repository;
    MutableLiveData<VerifyOtpResponse> verifyOtpResponse;
    MutableLiveData<resendotpmodel> resendotpResponse;

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

    public MutableLiveData<resendotpmodel> getResendOtp() {
        if (resendotpResponse != null) {
            return resendotpResponse;
        } else return null;
    }

    public void resendOtp(String customerId) {
        if (resendotpResponse != null) {

        } else {
            resendotpResponse = repository.resendOtpResponse(customerId);
        }
    }
}
