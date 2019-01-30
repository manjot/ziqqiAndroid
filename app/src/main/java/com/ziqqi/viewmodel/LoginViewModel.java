package com.ziqqi.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;

import com.ziqqi.model.loginResponse.LoginResponse;
import com.ziqqi.repository.SocialLoginRepository;

import java.util.HashMap;

public class LoginViewModel extends AndroidViewModel {
    SocialLoginRepository repository;
    MutableLiveData<LoginResponse> loginResponse;
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();

    public LoginViewModel(Application application) {
        super(application);
        repository = new SocialLoginRepository();
    }

    public void init(HashMap<String, Object> socialLoginRequest) {
        if (loginResponse != null) {
        } else {
            loginResponse = repository.getLoginData(socialLoginRequest);
        }
    }

    public MutableLiveData<LoginResponse> getLoginResponse() {
        if (loginResponse != null) {
            return loginResponse;
        } else return null;
    }
}


